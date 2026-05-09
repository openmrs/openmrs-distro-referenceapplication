#!/bin/bash
# Script de verificación post-instalación para sihsalus

set -e

echo "========================================"
echo "sihsalus - Verificación de Instalación"
echo "========================================"
echo ""

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Función para verificar si un contenedor está corriendo
check_container() {
    local container_name=$1
    if docker ps --format '{{.Names}}' | grep -q "^${container_name}$"; then
        echo -e "${GREEN}✓${NC} ${container_name} está corriendo"
        return 0
    else
        echo -e "${RED}✗${NC} ${container_name} NO está corriendo"
        return 1
    fi
}

# Función para verificar el health status
check_health() {
    local container_name=$1
    local health=$(docker inspect --format='{{.State.Health.Status}}' ${container_name} 2>/dev/null || echo "no-healthcheck")

    if [ "$health" = "healthy" ]; then
        echo -e "  ${GREEN}✓${NC} Health: healthy"
        return 0
    elif [ "$health" = "no-healthcheck" ]; then
        echo -e "  ${YELLOW}⚠${NC} Health: sin healthcheck configurado"
        return 0
    else
        echo -e "  ${RED}✗${NC} Health: ${health}"
        return 1
    fi
}

echo "1. Verificando contenedores críticos..."
echo "----------------------------------------"

critical_containers=(
    "sihsalus-db-master"
    "sihsalus-db-replic"
    "sihsalus-backend"
    "sihsalus-frontend"
    "sihsalus-gateway"
    "sihsalus-keycloak"
    "sihsalus-keycloak-db"
)

all_ok=true
for container in "${critical_containers[@]}"; do
    if check_container "$container"; then
        check_health "$container" || all_ok=false
    else
        all_ok=false
    fi
done
echo ""

echo "2. Verificando conectividad a base de datos..."
echo "-----------------------------------------------"

# Verificar que el secreto tiene la contraseña correcta
secret_password=$(cat secrets/mysql_openmrs_password.txt 2>/dev/null || echo "FILE_NOT_FOUND")
if [ "$secret_password" = "openmrs" ]; then
    echo -e "${GREEN}✓${NC} Secreto mysql_openmrs_password configurado correctamente"
else
    echo -e "${RED}✗${NC} ADVERTENCIA: El secreto debería contener 'openmrs', tiene: '$secret_password'"
    all_ok=false
fi

# Verificar conexión desde el backend
if docker exec sihsalus-backend test -f /openmrs/openmrs-server.properties 2>/dev/null; then
    backend_password=$(docker exec sihsalus-backend grep "connection.password=" /openmrs/openmrs-server.properties | cut -d'=' -f2)
    if [ "$backend_password" = "openmrs" ]; then
        echo -e "${GREEN}✓${NC} Configuración del backend correcta"
    else
        echo -e "${RED}✗${NC} Backend tiene contraseña: '$backend_password' (debería ser 'openmrs')"
        all_ok=false
    fi
else
    echo -e "${YELLOW}⚠${NC} Backend aún no ha generado openmrs-server.properties"
fi

# Verificar usuario en MySQL
if docker exec sihsalus-db-master mysql -uroot -p$(cat secrets/mysql_root_password.txt) -e "SELECT User, Host FROM mysql.user WHERE User='openmrs';" 2>/dev/null | grep -q openmrs; then
    echo -e "${GREEN}✓${NC} Usuario 'openmrs' existe en MySQL"

    # Verificar que puede conectar con la contraseña
    if docker exec sihsalus-db-master mysql -uopenmrs -popenmrs -e "SELECT 1;" >/dev/null 2>&1; then
        echo -e "${GREEN}✓${NC} Usuario 'openmrs' puede conectarse a MySQL"
    else
        echo -e "${RED}✗${NC} Usuario 'openmrs' NO puede conectarse (verificar contraseña)"
        all_ok=false
    fi
else
    echo -e "${RED}✗${NC} Usuario 'openmrs' NO existe en MySQL"
    all_ok=false
fi
echo ""

echo "3. Verificando puertos expuestos..."
echo "------------------------------------"
ports_to_check=(
    "80:Gateway HTTP"
    "443:Gateway HTTPS"
    "8080:Backend OpenMRS"
    "8180:Keycloak"
    "3307:MySQL Master"
    "3308:MySQL Replica"
)

for port_info in "${ports_to_check[@]}"; do
    port=$(echo $port_info | cut -d':' -f1)
    service=$(echo $port_info | cut -d':' -f2)

    if netstat -tuln 2>/dev/null | grep -q ":${port} " || ss -tuln 2>/dev/null | grep -q ":${port} "; then
        echo -e "${GREEN}✓${NC} Puerto ${port} (${service}) está escuchando"
    else
        echo -e "${YELLOW}⚠${NC} Puerto ${port} (${service}) no parece estar escuchando"
    fi
done
echo ""

echo "4. Verificando logs recientes de errores..."
echo "--------------------------------------------"
error_count=$(docker logs sihsalus-backend --tail 100 2>&1 | grep -i "error\|exception\|failed" | grep -v "WARN" | wc -l)
if [ $error_count -eq 0 ]; then
    echo -e "${GREEN}✓${NC} No se encontraron errores recientes en el backend"
else
    echo -e "${YELLOW}⚠${NC} Se encontraron ${error_count} líneas con errores en el backend"
    echo "  Ejecuta: docker logs sihsalus-backend --tail 100"
fi
echo ""

echo "========================================"
if [ "$all_ok" = true ]; then
    echo -e "${GREEN}✓ Sistema verificado correctamente${NC}"
    echo ""
    echo "Accede a OpenMRS en:"
    echo "  → http://localhost/openmrs"
    echo ""
    echo "Credenciales por defecto:"
    echo "  Usuario: admin"
    echo "  Contraseña: Admin123"
else
    echo -e "${RED}✗ Se encontraron problemas en el sistema${NC}"
    echo ""
    echo "Revisa los errores arriba y consulta:"
    echo "  → docs/TROUBLESHOOTING.md"
fi
echo "========================================"
