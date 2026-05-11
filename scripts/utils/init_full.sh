done
#!/bin/bash
# ------------------------------------------------------------------------------
# Script: init_full.sh
# Descripción: Inicializa el entorno completo (producción o desarrollo) usando docker-compose.
# Uso: ./init_full.sh -m [production|development] [-d DIRECTORIO]
# Autor: Equipo SIHSALUS
# Fecha: 2025-10-20
# ------------------------------------------------------------------------------

set -euo pipefail

MODE="development"
COMPOSE_DIR="../"
LOG_FILE="fullInit_log.txt"

usage() {
        echo "Uso: $0 -m [production|development] [-d DIRECTORIO]" >&2
        exit 1
}

# Parseo de argumentos
while [[ $# -gt 0 ]]; do
    case $1 in
        -m)
            if [[ "$2" == "production" || "$2" == "development" ]]; then
                MODE="$2"; shift 2;
            else
                usage
            fi;;
        -d)
            COMPOSE_DIR="$2"; shift 2;;
        *)
            usage;;
    esac
done

exec > >(tee -a "$LOG_FILE") 2>&1

echo "[INFO] Inicializando entorno en modo $MODE usando docker-compose en $COMPOSE_DIR"

cd "$COMPOSE_DIR" || { echo "[ERROR] Directorio $COMPOSE_DIR no encontrado"; exit 1; }

if sudo docker compose ps -q > /dev/null 2>&1; then
        echo "[INFO] Deteniendo y eliminando contenedores..."
    sudo docker compose down
else
    echo "No containers to stop and remove."
fi

# Remove all associated volumes
if sudo docker compose ps -q > /dev/null 2>&1; then
    echo "Removing volumes..."
    sudo docker compose down -v
else
    echo "No volumes to remove."
fi

# Build and start the Docker Compose stack
if [[ "$MODE" == "production" ]]; then
    echo "Starting in production mode..."
    docker compose up -d --build --env-file "$ENV_FILE"
else
    echo "Starting in development mode..."
    echo "WARNING: Using default environment variables. DO NOT DEPLOY IN A PRODUCTION ENVIRONMENT."
    docker compose up -d --build
fi

# Check if the stack started successfully
if [ $? -eq 0 ]; then
    echo "Docker Compose stack started successfully."
else
    echo "ERROR: Failed to start Docker Compose stack."
    exit 1
fi
