echo ""
echo "-----------------------------------------------------"
echo "Starting sihsalus MariaDB replic FULL backup in $TIMESTAMP ..."
echo "Creating backup directory in slave container..."
docker exec --user root $CONTAINER_NAME mkdir -p "$TEMP_BACKUP_PATH"
echo "Erasing backup directory in replica container..."
docker exec --user root $CONTAINER_NAME rm -rf $TEMP_BACKUP_PATH/
docker exec --user root $CONTAINER_NAME mariadb-backup --user=root --password=${OMRS_DB_R_PASSWORD:-openmrs_r} --backup --target-dir=$TEMP_BACKUP_PATH
docker cp "$CONTAINER_NAME:$TEMP_BACKUP_PATH" "$BACKUP_DIR/temp/"
tar -czf "$BACKUP_DIR/$BACKUP_NAME.tar.gz" -C "temp/"

#!/bin/bash
# ------------------------------------------------------------------------------
# Script: backup_full_generate.sh
# Descripción: Genera un backup completo de la base de datos MariaDB del contenedor especificado.
# Uso: ./backup_full_generate.sh [--container NOMBRE] [--dir DIRECTORIO] [--max N]
# Autor: Equipo SIHSALUS
# Fecha: 2025-10-20
# ------------------------------------------------------------------------------

set -euo pipefail

CONTAINER_NAME="${CONTAINER_NAME:-sihsalus-db-master}"
BACKUP_DIR="${BACKUP_DIR:-/home/${USER}/sihsalus-fullBackups}"
MAX_BACKUPS="${MAX_BACKUPS:-15}"
TIMESTAMP=$(date +"%Y-%m-%d_%H-%M-%S")
BACKUP_NAME="sihsalus_backup_$TIMESTAMP"
TEMP_BACKUP_PATH="/backup/full"

 # Leer credenciales sensibles desde Docker secrets si existen
if [ -f /run/secrets/OMRS_DB_R_PASSWORD ]; then
    export OMRS_DB_R_PASSWORD="$(cat /run/secrets/OMRS_DB_R_PASSWORD)"
fi
if [ -f /run/secrets/BACKUP_ENCRYPTION_PASSWORD ]; then
    export BACKUP_ENCRYPTION_PASSWORD="$(cat /run/secrets/BACKUP_ENCRYPTION_PASSWORD)"
fi

# Parseo de argumentos
while [[ $# -gt 0 ]]; do
    case $1 in
        --container)
            CONTAINER_NAME="$2"; shift 2;;
        --dir)
            BACKUP_DIR="$2"; shift 2;;
        --max)
            MAX_BACKUPS="$2"; shift 2;;
        *)
            echo "Uso: $0 [--container NOMBRE] [--dir DIRECTORIO] [--max N]"; exit 1;;
    esac
done


# Rotación de logs: mantener solo los últimos 5 logs
LOG_FILE="$BACKUP_DIR/fullBackup_log.txt"
mkdir -p "$BACKUP_DIR"
LOG_PATTERN="$BACKUP_DIR/fullBackup_log.txt*"
LOG_COUNT=$(ls -1 $LOG_PATTERN 2>/dev/null | wc -l)
if [ "$LOG_COUNT" -ge 5 ]; then
    ls -1t $LOG_PATTERN | tail -n +6 | xargs rm -f
fi
# Renombrar log anterior si existe
if [ -f "$LOG_FILE" ]; then
    mv "$LOG_FILE" "$LOG_FILE.$(date +%Y%m%d%H%M%S)"
fi
exec > >(tee -a "$LOG_FILE") 2>&1

echo "[INFO] Iniciando backup completo $TIMESTAMP para contenedor $CONTAINER_NAME"

if ! docker ps -a --format '{{.Names}}' | grep -q "^$CONTAINER_NAME$"; then
    echo "[ERROR] Contenedor '$CONTAINER_NAME' no encontrado."; exit 1
fi

# Crear directorio de backup en el contenedor
docker exec --user root "$CONTAINER_NAME" mkdir -p "$TEMP_BACKUP_PATH"
docker exec --user root "$CONTAINER_NAME" rm -rf "$TEMP_BACKUP_PATH/"

# Ejecutar backup físico
docker exec --user root "$CONTAINER_NAME" mariadb-backup --user=root --password="${OMRS_DB_R_PASSWORD:-openmrs_r}" --backup --target-dir="$TEMP_BACKUP_PATH"

# Copiar backup al host
TEMP_HOST_DIR="$BACKUP_DIR/temp"
mkdir -p "$TEMP_HOST_DIR"
docker cp "$CONTAINER_NAME:$TEMP_BACKUP_PATH" "$TEMP_HOST_DIR/"

# Comprimir backup
cd "$BACKUP_DIR" || exit 1
# Comprimir backup
tar -czf "$BACKUP_DIR/$BACKUP_NAME.tar.gz" -C "temp/" .
rm -rf "$TEMP_HOST_DIR"

# Cifrar backup con openssl (AES-256)
if [ -z "${BACKUP_ENCRYPTION_PASSWORD:-}" ]; then
    echo "[ERROR] Variable BACKUP_ENCRYPTION_PASSWORD no definida. Abortando cifrado." >&2
    exit 2
fi
openssl enc -aes-256-cbc -salt -pbkdf2 -pass env:BACKUP_ENCRYPTION_PASSWORD \
    -in "$BACKUP_DIR/$BACKUP_NAME.tar.gz" \
    -out "$BACKUP_DIR/$BACKUP_NAME.tar.gz.enc"
if [ $? -eq 0 ]; then
    rm -f "$BACKUP_DIR/$BACKUP_NAME.tar.gz"
    echo "[OK] Backup cifrado exitosamente en '$BACKUP_DIR/$BACKUP_NAME.tar.gz.enc'"
else
    echo "[ERROR] Falló el cifrado del backup. El archivo sin cifrar permanece."
    exit 3
fi

# Rotar backups antiguos
BACKUP_COUNT=$(ls -1 "$BACKUP_DIR"/*.tar.gz 2>/dev/null | wc -l)
if [ "$BACKUP_COUNT" -gt "$MAX_BACKUPS" ]; then
    ls -1t "$BACKUP_DIR"/*.tar.gz | tail -n +$((MAX_BACKUPS+1)) | xargs rm -f
    echo "[INFO] Se eliminaron backups antiguos, manteniendo los últimos $MAX_BACKUPS."
fi

echo "[OK] Backup completo realizado en '$BACKUP_DIR/$BACKUP_NAME.tar.gz'"

# Notice full backup created
echo "Backup created: $BACKUP_DIR/$BACKUP_NAME.tar.gz"

# Rotate old backups: Keep only the latest $MAX_BACKUPS backups
BACKUP_COUNT=$(ls -1 "$BACKUP_DIR"/*.tar.gz 2>/dev/null | wc -l)

if [ "$BACKUP_COUNT" -gt "$MAX_BACKUPS" ]; then
    OLDEST_BACKUP=$(ls -1t "$BACKUP_DIR"/*.tar.gz | tail -1)
    echo "Removing oldest backup: $OLDEST_BACKUP"
    rm -f "$OLDEST_BACKUP"
fi

echo "Backup process completed!"
