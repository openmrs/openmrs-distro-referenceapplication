#!/bin/bash

# Log every outout in log.txt
# Rotación de logs: mantener solo los últimos 5 logs
LOG_FILE="fullBackup_log.txt"
LOG_PATTERN="fullBackup_log.txt*"
LOG_COUNT=$(ls -1 $LOG_PATTERN 2>/dev/null | wc -l)
if [ "$LOG_COUNT" -ge 5 ]; then
    ls -1t $LOG_PATTERN | tail -n +6 | xargs rm -f
fi
# Renombrar log anterior si existe
if [ -f "$LOG_FILE" ]; then
    mv "$LOG_FILE" "$LOG_FILE.$(date +%Y%m%d%H%M%S)"
fi
exec > >(tee -a "$LOG_FILE") 2>&1

 # Leer credenciales sensibles desde Docker secrets si existen
if [ -f /run/secrets/OMRS_DB_BACKUP_USER ]; then
    export OMRS_DB_BACKUP_USER="$(cat /run/secrets/OMRS_DB_BACKUP_USER)"
fi
if [ -f /run/secrets/OMRS_DB_BACKUP_PASSWORD ]; then
    export OMRS_DB_BACKUP_PASSWORD="$(cat /run/secrets/OMRS_DB_BACKUP_PASSWORD)"
fi
if [ -f /run/secrets/BACKUP_ENCRYPTION_PASSWORD ]; then
    export BACKUP_ENCRYPTION_PASSWORD="$(cat /run/secrets/BACKUP_ENCRYPTION_PASSWORD)"
fi

# Configuration
CONTAINER_NAME="sihsalus-db-replic"                      # Change to your MariaDB container name
BACKUP_DIR="/home/${USER}/sihsalus-fullBackups"          # Change to your desired backup storage location
MAX_BACKUPS=15                                          # Maximum number of backups to keep
TIMESTAMP=$(date +"%Y-%m-%d_%H-%M-%S")
BACKUP_NAME="sihsalus_backup_$TIMESTAMP"
TEMP_BACKUP_PATH="/backup/full"                         # Inside the container

echo ""
echo "-----------------------------------------------------"
echo "Starting sihsalus MariaDB replic FULL backup in $TIMESTAMP ..."

#Create directorie in user
mkdir -p "$BACKUP_DIR"

# Create the directorie in case doesnt exist
echo "Creating backup directory in slave container..."
docker exec --user root $CONTAINER_NAME mkdir -p "$TEMP_BACKUP_PATH"

# Clean the backup directorie in the replica container
echo "Erasing backup directory in replica container..."
docker exec --user root $CONTAINER_NAME rm -rf $TEMP_BACKUP_PATH/

# Run physical backup inside the MariaDB container
docker exec --user root $CONTAINER_NAME mariadb-backup --slave-info --safe-slave-backup --user=${OMRS_DB_BACKUP_USER:-openmrs_b} --password=${OMRS_DB_BACKUP_PASSWORD:-openmrs_b} --backup --target-dir=$TEMP_BACKUP_PATH

# Copy the backup to the host
mkdir -p temp
docker cp "$CONTAINER_NAME:$TEMP_BACKUP_PATH" "$BACKUP_DIR/temp"

# Zip the backup
cd "$BACKUP_DIR" || exit

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
