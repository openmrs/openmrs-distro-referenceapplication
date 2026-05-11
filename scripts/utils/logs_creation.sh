# ------------------------------------------------------------------------------
# Script: logs_creation.sh
# Descripción: Captura logs del contenedor backend hasta encontrar un texto clave.
# Uso: ./logs_creation.sh [--container NOMBRE] [--logfile ARCHIVO] [--stoptext TEXTO]
# Autor: Equipo SIHSALUS
# Fecha: 2025-10-20
# ------------------------------------------------------------------------------

set -euo pipefail

CONTAINER_NAME="${CONTAINER_NAME:-sihsalus-backend}"
LOG_FILE="${LOG_FILE:-creation-log.txt}"
SEARCH_TEXT="${SEARCH_TEXT:-org.apache.coyote.AbstractProtocol.start Starting ProtocolHandler [\"http-nio-8080\"]}"

# Parseo de argumentos
while [[ $# -gt 0 ]]; do
    case $1 in
        --container)
            CONTAINER_NAME="$2"; shift 2;;
        --logfile)
            LOG_FILE="$2"; shift 2;;
        --stoptext)
            SEARCH_TEXT="$2"; shift 2;;
        *)
            echo "Uso: $0 [--container NOMBRE] [--logfile ARCHIVO] [--stoptext TEXTO]"; exit 1;;
    esac
done

echo "[INFO] Capturando logs de '$CONTAINER_NAME' hasta encontrar '$SEARCH_TEXT'..."

if ! docker ps --format '{{.Names}}' | grep -q "^$CONTAINER_NAME$"; then
        echo "[ERROR] Contenedor '$CONTAINER_NAME' no encontrado."; exit 1
fi

docker logs -f "$CONTAINER_NAME" | tee "$LOG_FILE" | awk "/$SEARCH_TEXT/ {exit}"

echo "[OK] Logs capturados en '$LOG_FILE' hasta '$SEARCH_TEXT'."

#!/bin/bash

# Configuration
CONTAINER_NAME="sihsalus-backend"  # Set your container name
LOG_FILE="logs.txt"
SEARCH_TEXT="STOP_LOGGING_HERE"  # Text that triggers stopping

# Ensure the container exists
if ! docker ps --format '{{.Names}}' | grep -q "^$CONTAINER_NAME$"; then
        echo "Error: Container '$CONTAINER_NAME' not found."
        exit 1
fi

# Capture logs until the search text is found
docker logs -f "$CONTAINER_NAME" | tee "$LOG_FILE" | awk "/$SEARCH_TEXT/ {exit}"

echo "✅ Logs captured in '$LOG_FILE' until '$SEARCH_TEXT' was found."
#!/bin/bash

#Script to get logs of the initializer module and all tasks regarding the creation and preload of data for OpenMRS server.

# Configuration
CONTAINER_NAME="sihsalus-backend"  # Set your container name
LOG_FILE="creation-log.txt"
SEARCH_TEXT="org.apache.coyote.AbstractProtocol.start Starting ProtocolHandler ["http-nio-8080"]"  # Text that triggers stopping

# Ensure the container exists
if ! docker ps --format '{{.Names}}' | grep -q "^$CONTAINER_NAME$"; then
    echo "Error: Container '$CONTAINER_NAME' not found."
    exit 1
fi

# Capture logs until the search text is found
docker logs -f "$CONTAINER_NAME" | tee "$LOG_FILE" | awk "/$SEARCH_TEXT/ {exit}"

echo "✅ Logs captured in '$LOG_FILE' until '$SEARCH_TEXT' was found."
#!/bin/bash

# Configuration
CONTAINER_NAME="sihsalus-backend"  # Set your container name
LOG_FILE="logs.txt"
SEARCH_TEXT="STOP_LOGGING_HERE"  # Text that triggers stopping

# Ensure the container exists
if ! docker ps --format '{{.Names}}' | grep -q "^$CONTAINER_NAME$"; then
    echo "Error: Container '$CONTAINER_NAME' not found."
    exit 1
fi

# Capture logs until the search text is found
docker logs -f "$CONTAINER_NAME" | tee "$LOG_FILE" | awk "/$SEARCH_TEXT/ {exit}"

echo "✅ Logs captured in '$LOG_FILE' until '$SEARCH_TEXT' was found."