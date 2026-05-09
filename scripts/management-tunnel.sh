#!/bin/bash
# Script para crear SSH tunnels a TODOS los servicios administrativos
# Uso: ./scripts/management-tunnel.sh [usuario@servidor]

set -e

# Colores
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configuración
SERVER="${1:-${SIHSALUS_SERVER}}"

if [ -z "$SERVER" ]; then
    echo -e "${YELLOW}Uso:${NC}"
    echo "  $0 usuario@servidor"
    echo ""
    echo -e "${YELLOW}O configurar variable de entorno:${NC}"
    echo "  export SIHSALUS_SERVER=usuario@servidor"
    echo "  $0"
    echo ""
    echo -e "${YELLOW}Ejemplo:${NC}"
    echo "  $0 admin@hii1sc-dev.inf.pucp.edu.pe"
    exit 1
fi

echo -e "${BLUE}==========================================${NC}"
echo -e "${BLUE}SSH Tunnels - Servicios de Management${NC}"
echo -e "${BLUE}==========================================${NC}"
echo ""
echo -e "Servidor: ${YELLOW}$SERVER${NC}"
echo ""
echo -e "${GREEN}Creando túneles SSH...${NC}"
echo ""
echo -e "${YELLOW}Una vez conectado, acceder a:${NC}"
echo "  → Portainer:  http://localhost:9000"
echo "  → Grafana:    http://localhost:3001"
echo "  → Pi-hole:    http://localhost:8081/admin"
echo ""
echo -e "Presiona ${YELLOW}Ctrl+C${NC} para cerrar todos los túneles"
echo ""

# Verificar que los puertos locales estén libres
check_port() {
    local port=$1
    if lsof -Pi :$port -sTCP:LISTEN -t >/dev/null 2>&1 ; then
        echo -e "${YELLOW}Advertencia: Puerto $port ya está en uso. Cerrando proceso...${NC}"
        kill $(lsof -t -i:$port) 2>/dev/null || true
        sleep 1
    fi
}

check_port 9000
check_port 3001
check_port 8081

# Crear múltiples túneles SSH
ssh -L 9000:localhost:9000 \
    -L 3001:localhost:3001 \
    -L 8081:localhost:8081 \
    $SERVER -N
