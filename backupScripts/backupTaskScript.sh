#!/bin/bash

# Configuration
CONTAINER_NAME="peruHCE-db-Hreplic"     # Change to your MariaDB container name
BACKUP_DIR="/home/dev-user/peruHCE-fullBackups"             # Change to your desired backup storage location
MAX_BACKUPS=10                          # Maximum number of backups to keep
TIMESTAMP=$(date +"%Y-%m-%d_%H-%M-%S")
BACKUP_NAME="peruHCE_backup_$TIMESTAMP"
TEMP_BACKUP_PATH="/backup"              # Inside the container

echo "Starting MariaDB replic backup..."

# 1️⃣ Run physical backup inside the MariaDB container
docker exec --user root $CONTAINER_NAME mariadb-backup --user=root --password=${OMRS_DB_R_PASSWORD:-openmrs_r} --backup --target-dir=$TEMP_BACKUP_PATH

# 2️⃣ Copy the backup to the host
docker cp "$CONTAINER_NAME:$TEMP_BACKUP_PATH" "$BACKUP_DIR/$BACKUP_NAME"

# 3️⃣ Zip the backup
cd "$BACKUP_DIR" || exit
tar -czf "$BACKUP_NAME.tar.gz" "$BACKUP_NAME"

# 4️⃣ Remove the uncompressed backup directory
rm -rf "$BACKUP_NAME"

echo "Backup created: $BACKUP_DIR/$BACKUP_NAME.tar.gz"

# 5️⃣ Rotate old backups: Keep only the latest $MAX_BACKUPS backups
BACKUP_COUNT=$(ls -1 "$BACKUP_DIR"/*.tar.gz 2>/dev/null | wc -l)

if [ "$BACKUP_COUNT" -gt "$MAX_BACKUPS" ]; then
    OLDEST_BACKUP=$(ls -1t "$BACKUP_DIR"/*.tar.gz | tail -1)
    echo "Removing oldest backup: $OLDEST_BACKUP"
    rm -f "$OLDEST_BACKUP"
fi

echo "Backup process completed!"
