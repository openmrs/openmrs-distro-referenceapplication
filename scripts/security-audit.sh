#!/bin/bash
# Script de auditoría de seguridad para sihsalus
# Verifica configuraciones de seguridad y reporta vulnerabilidades

set -e

# Colores
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

ISSUES_FOUND=0
WARNINGS_FOUND=0

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}sihsalus - Auditoría de Seguridad${NC}"
echo -e "${BLUE}========================================${NC}"
echo ""

# Función para reportar problema crítico
report_issue() {
    echo -e "${RED}✗ CRÍTICO:${NC} $1"
    ISSUES_FOUND=$((ISSUES_FOUND + 1))
}

# Función para reportar advertencia
report_warning() {
    echo -e "${YELLOW}⚠ ADVERTENCIA:${NC} $1"
    WARNINGS_FOUND=$((WARNINGS_FOUND + 1))
}

# Función para reportar éxito
report_ok() {
    echo -e "${GREEN}✓${NC} $1"
}

echo "1. Verificando puertos expuestos públicamente..."
echo "------------------------------------------------"

# Verificar Portainer
PORTAINER_PORTS=$(docker port sihsalus-portainer 2>/dev/null | grep -v "127.0.0.1" | grep -E "9000|9443|8000" || true)
if [ -n "$PORTAINER_PORTS" ]; then
    report_issue "Portainer expone puertos públicamente:"
    echo "$PORTAINER_PORTS" | while read line; do
        echo "  $line"
    done
    echo "  → Solución: Modificar docker-compose.yml para solo exponer en localhost"
    echo "    ports:"
    echo "      - '127.0.0.1:9000:9000'"
else
    report_ok "Portainer no expone puertos públicamente"
fi

# Verificar Grafana
GRAFANA_PORTS=$(docker port sihsalus-grafana 2>/dev/null | grep -v "127.0.0.1" | grep "3001" || true)
if [ -n "$GRAFANA_PORTS" ]; then
    report_warning "Grafana expone puerto 3001 públicamente"
    echo "  → Recomendación: Acceder solo a través del gateway o VPN"
else
    report_ok "Grafana no expone puertos públicamente"
fi

# Verificar Prometheus
PROMETHEUS_PORTS=$(docker port sihsalus-prometheus 2>/dev/null | grep -v "127.0.0.1" | grep "9090" || true)
if [ -n "$PROMETHEUS_PORTS" ]; then
    report_warning "Prometheus expone puerto 9090 públicamente"
    echo "  → Recomendación: Solo para uso interno, no exponer a internet"
else
    report_ok "Prometheus no expone puertos públicamente"
fi

echo ""
echo "2. Verificando secrets de Docker..."
echo "------------------------------------"

# Verificar que secrets existan y no estén vacíos
check_secret() {
    local secret_file=$1
    if [ ! -f "$secret_file" ]; then
        report_issue "Archivo de secreto no existe: $secret_file"
    elif [ ! -s "$secret_file" ]; then
        report_issue "Archivo de secreto está vacío: $secret_file"
    else
        local content=$(cat "$secret_file")
        if [ "$content" = "changeme" ] || [ "$content" = "password" ] || [ "$content" = "admin" ]; then
            report_warning "Secreto usa valor por defecto inseguro: $secret_file"
        else
            report_ok "$(basename $secret_file)"
        fi
    fi
}

if [ -d "secrets" ]; then
    check_secret "secrets/mysql_root_password.txt"
    check_secret "secrets/mysql_openmrs_password.txt"
    check_secret "secrets/keycloak_admin_password.txt"
    check_secret "secrets/keycloak_db_password.txt"
    check_secret "secrets/pihole_password.txt"
else
    report_issue "Directorio secrets/ no existe"
fi

echo ""
echo "3. Verificando permisos de archivos sensibles..."
echo "--------------------------------------------------"

# Verificar permisos de secrets
if [ -d "secrets" ]; then
    SECRET_PERMS=$(stat -c "%a" secrets/)
    if [ "$SECRET_PERMS" = "700" ] || [ "$SECRET_PERMS" = "600" ]; then
        report_ok "Permisos del directorio secrets/ son seguros ($SECRET_PERMS)"
    else
        report_warning "Permisos del directorio secrets/ deberían ser 700 (actual: $SECRET_PERMS)"
        echo "  → Ejecutar: chmod 700 secrets/"
    fi

    # Verificar permisos individuales
    find secrets/ -type f ! -perm 600 -print | while read file; do
        report_warning "Archivo $file debería tener permisos 600 (actual: $(stat -c "%a" "$file"))"
        echo "  → Ejecutar: chmod 600 $file"
    done
fi

echo ""
echo "4. Verificando exposición del Docker socket..."
echo "-----------------------------------------------"

# Verificar contenedores con acceso al socket
SOCKET_CONTAINERS=$(docker ps --format '{{.Names}}' --filter "volume=/var/run/docker.sock" 2>/dev/null || true)
if [ -n "$SOCKET_CONTAINERS" ]; then
    report_warning "Los siguientes contenedores tienen acceso al Docker socket:"
    echo "$SOCKET_CONTAINERS" | while read container; do
        echo "  - $container"
        # Verificar si es read-only
        MOUNT_INFO=$(docker inspect $container --format '{{range .Mounts}}{{if eq .Source "/var/run/docker.sock"}}{{.Mode}}{{end}}{{end}}')
        if [ "$MOUNT_INFO" = "ro" ]; then
            echo "    ✓ Montado en modo read-only (seguro)"
        else
            echo "    ⚠ Montado con permisos de escritura (riesgoso)"
        fi
    done
    echo "  → IMPORTANTE: Solo Portainer y el gateway deberían tener este acceso"
else
    report_ok "No se encontraron contenedores con acceso al Docker socket"
fi

echo ""
echo "5. Verificando configuración de redes..."
echo "-----------------------------------------"

# Verificar que database-network sea internal
DB_NETWORK_INTERNAL=$(docker network inspect sihsalus-distro-referenceapplication_database-network --format '{{.Internal}}' 2>/dev/null || echo "false")
if [ "$DB_NETWORK_INTERNAL" = "true" ]; then
    report_ok "Red database-network está configurada como internal"
else
    report_warning "Red database-network NO está configurada como internal"
    echo "  → Solución: Modificar docker-compose.yml:"
    echo "    database-network:"
    echo "      internal: true"
fi

# Verificar que management-network no esté expuesta
MGMT_CONTAINERS=$(docker network inspect sihsalus-distro-referenceapplication_management-network --format '{{range .Containers}}{{.Name}} {{end}}' 2>/dev/null || true)
if [ -n "$MGMT_CONTAINERS" ]; then
    echo "Contenedores en management-network: $MGMT_CONTAINERS"
    # Verificar que no expongan puertos públicos
fi

echo ""
echo "6. Verificando configuración SSH..."
echo "------------------------------------"

if [ -f "/etc/ssh/sshd_config" ]; then
    # Verificar PasswordAuthentication
    if grep -q "^PasswordAuthentication no" /etc/ssh/sshd_config; then
        report_ok "Autenticación por contraseña SSH deshabilitada"
    else
        report_warning "Autenticación por contraseña SSH está habilitada"
        echo "  → Recomendación: Usar solo autenticación por clave"
        echo "    Editar /etc/ssh/sshd_config:"
        echo "    PasswordAuthentication no"
    fi

    # Verificar PermitRootLogin
    if grep -q "^PermitRootLogin no" /etc/ssh/sshd_config; then
        report_ok "Login SSH como root deshabilitado"
    else
        report_warning "Login SSH como root podría estar habilitado"
        echo "  → Recomendación: Editar /etc/ssh/sshd_config:"
        echo "    PermitRootLogin no"
    fi
else
    echo "No se puede verificar configuración SSH (archivo no encontrado)"
fi

echo ""
echo "7. Verificando firewall (ufw)..."
echo "---------------------------------"

if command -v ufw &> /dev/null; then
    UFW_STATUS=$(sudo ufw status 2>/dev/null | grep "Status:" | awk '{print $2}' || echo "unknown")
    if [ "$UFW_STATUS" = "active" ]; then
        report_ok "Firewall UFW está activo"

        # Verificar reglas específicas
        if sudo ufw status | grep -q "9000"; then
            report_warning "Puerto 9000 (Portainer) tiene regla en el firewall"
            echo "  → Si es público, considerar SSH tunneling en su lugar"
        fi
    else
        report_warning "Firewall UFW no está activo"
        echo "  → Recomendación: Habilitar UFW para protección adicional"
        echo "    sudo ufw enable"
    fi
else
    echo "UFW no está instalado en este sistema"
fi

echo ""
echo "8. Verificando actualizaciones de imágenes Docker..."
echo "-----------------------------------------------------"

# Verificar imágenes desactualizadas
OUTDATED=0
while IFS= read -r image; do
    CURRENT_DIGEST=$(docker inspect "$image" --format '{{.RepoDigests}}' 2>/dev/null | grep -oP '\[.*?\]' || echo "")
    if [ -n "$CURRENT_DIGEST" ]; then
        # Intentar pull para verificar si hay actualizaciones
        # (esto es solo informativo, no hace pull)
        echo "  Verificando: $image"
    fi
done < <(docker ps --format '{{.Image}}' | sort -u)

echo "  → Ejecutar regularmente: docker compose pull && docker compose up -d"

echo ""
echo "9. Verificando logs de acceso sospechoso..."
echo "--------------------------------------------"

# Verificar intentos de login fallidos en Portainer
if docker logs sihsalus-portainer 2>&1 | grep -i "failed\|unauthorized\|forbidden" | tail -5 | grep -q .; then
    report_warning "Se encontraron intentos de acceso fallidos recientes en Portainer"
    docker logs sihsalus-portainer 2>&1 | grep -i "failed\|unauthorized\|forbidden" | tail -3
else
    report_ok "No se encontraron intentos de acceso fallidos recientes"
fi

echo ""
echo "10. Verificando .env y archivos de configuración..."
echo "----------------------------------------------------"

if [ -f ".env" ]; then
    if grep -q "changeme\|password123\|admin123" .env 2>/dev/null; then
        report_warning "Archivo .env contiene valores por defecto inseguros"
    else
        report_ok "Archivo .env no contiene valores obviamente inseguros"
    fi

    # Verificar que .env esté en .gitignore
    if [ -f ".gitignore" ]; then
        if grep -q "^\.env$" .gitignore; then
            report_ok "Archivo .env está en .gitignore"
        else
            report_issue "Archivo .env NO está en .gitignore"
            echo "  → URGENTE: Agregar .env a .gitignore"
        fi
    fi
else
    echo "Archivo .env no encontrado"
fi

echo ""
echo "${BLUE}========================================${NC}"
echo "${BLUE}Resumen de Auditoría${NC}"
echo "${BLUE}========================================${NC}"
echo ""

if [ $ISSUES_FOUND -eq 0 ] && [ $WARNINGS_FOUND -eq 0 ]; then
    echo -e "${GREEN}✓ No se encontraron problemas de seguridad${NC}"
    echo ""
    echo "El sistema cumple con las mejores prácticas básicas de seguridad."
else
    echo -e "${RED}Problemas críticos encontrados: $ISSUES_FOUND${NC}"
    echo -e "${YELLOW}Advertencias encontradas: $WARNINGS_FOUND${NC}"
    echo ""
    echo "Consulta la documentación en docs/SECURITY.md para soluciones detalladas."
    echo ""
fi

echo "Documentación adicional:"
echo "  - docs/SECURITY.md - Guía completa de seguridad"
echo "  - docs/TROUBLESHOOTING.md - Solución de problemas"
echo ""

# Retornar código de salida basado en problemas críticos
if [ $ISSUES_FOUND -gt 0 ]; then
    exit 1
else
    exit 0
fi
