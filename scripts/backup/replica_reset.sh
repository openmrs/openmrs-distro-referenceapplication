#!/bin/bash
# ------------------------------------------------------------------------------
# Script: replica_reset.sh
# Descripción: Reinicia la replicación de MariaDB/MySQL en el contenedor especificado.
# Uso: ./replica_reset.sh [--container NOMBRE] [--user USUARIO] [--password PASSWORD]
# Autor: Equipo SIHSALUS
# Fecha: 2025-10-20
# ------------------------------------------------------------------------------

set -euo pipefail

CONTAINER_NAME="${CONTAINER_NAME:-sihsalus-db-replic}"
MYSQL_USER="${MYSQL_USER:-openmrs_repl}"
MYSQL_PASSWORD="${MYSQL_PASSWORD:-openmrs_repl}"
MYSQL_HOST="${MYSQL_HOST:-db}"
MYSQL_PORT="${MYSQL_PORT:-3306}"

# Parseo de argumentos
while [[ $# -gt 0 ]]; do
  case $1 in
    --container)
      CONTAINER_NAME="$2"; shift 2;;
    --user)
      MYSQL_USER="$2"; shift 2;;
    --password)
      MYSQL_PASSWORD="$2"; shift 2;;
    --host)
      MYSQL_HOST="$2"; shift 2;;
    --port)
      MYSQL_PORT="$2"; shift 2;;
    *)
      echo "Uso: $0 [--container NOMBRE] [--user USUARIO] [--password PASSWORD] [--host HOST] [--port PUERTO]"; exit 1;;
  esac
done

SQL_CMDS="STOP SLAVE; RESET SLAVE ALL; CHANGE MASTER TO MASTER_HOST='${MYSQL_HOST}', MASTER_USER='${MYSQL_USER}', MASTER_PASSWORD='${MYSQL_PASSWORD}', MASTER_PORT=${MYSQL_PORT}, MASTER_LOG_FILE='mysql-bin.000001', MASTER_LOG_POS=4; START SLAVE; SHOW SLAVE STATUS;"

echo "[INFO] Reiniciando replicación en contenedor $CONTAINER_NAME..."

docker exec -i "$CONTAINER_NAME" mysql -u root -p"$MYSQL_PASSWORD" -e "$SQL_CMDS"

echo "[OK] Replicación reiniciada. Estado mostrado arriba."
