#!/bin/bash

# Log every outout in log.txt
exec > >(tee -a incBackup_log.txt) 2>&1

# Configuration
CONTAINER_NAME="peruHCE-db-replic"                         # Change to your MariaDB container name
FULL_BACKUP_DIR="/home/${USER}/peruHCE-fullBackups"
BACKUP_DIR="/home/${USER}/peruHCE-incrBackups"         # Change to your desired backup storage location, make sure its exists
MAX_BACKUPS=10                                          # Maximum number of backups to keep
TIMESTAMP=$(date +"%Y-%m-%d_%H-%M-%S")
BACKUP_NAME="$TIMESTAMP"
TEMP_FULL_BACKUP_PATH="/backup/full"                    # Inside the container 
TEMP_BACKUP_PATH="/backup/inc"                          # Inside the container

echo ""
echo "-----------------------------------------------------"
echo "Starting peruHCE MariaDB replic INCREMENTAL backup in $TIMESTAMP ..."

#Create directorie in user
mkdir -p "$BACKUP_DIR"

# Create the directorie in case doesnt exist
echo "Creating backup directory in slave container..."
docker exec --user root $CONTAINER_NAME mkdir -p "$TEMP_BACKUP_PATH"

# Get the most recent file and remove .tar.gz
LASTEST_FULL_BACKUP=$(ls -t "$FULL_BACKUP_DIR"/peruHCE_backup_*.tar.gz 2>/dev/null | head -n 1)
FILENAME=$(basename "$LASTEST_FULL_BACKUP" .tar.gz)

# Check if no files were found
if [ -z "$LASTEST_FULL_BACKUP" ]; then
    echo "No backup files found in $FULL_BACKUP_DIR . Exiting."
    exit 1  # Exit with a non-zero status to indicate failure
fi

# Print the result
echo "Latest full backup: $FILENAME"

# Clean the backup directorie in the replica container
echo "Erasing backup directory in replica container..."
docker exec --user root $CONTAINER_NAME rm -rf $TEMP_BACKUP_PATH/

# Run physical backup inside the MariaDB container
docker exec --user root $CONTAINER_NAME mariadb-backup --slave-info --safe-slave-backup --user=root --password=${OMRS_DB_R_PASSWORD:-openmrs_r} --backup --target-dir=$TEMP_BACKUP_PATH --incremental-basedir=$TEMP_FULL_BACKUP_PATH

# Copy the backup to the host
mkdir -p temp
docker cp "$CONTAINER_NAME:$TEMP_BACKUP_PATH" "$BACKUP_DIR/temp"

# Zip the backup
cd "$BACKUP_DIR" || exit
tar -czf "$BACKUP_DIR/${FILENAME}_${BACKUP_NAME}.tar.gz" -C "temp/"
rm -rf temp

# Notice full backup created
echo "Backup created: ${FILENAME}_${BACKUP_NAME}.tar.gz"

# Rotate old backups: Keep only the latest $MAX_BACKUPS backups
BACKUP_COUNT=$(ls -1 "${BACKUP_DIR}"/*.tar.gz 2>/dev/null | wc -l)

if [ "$BACKUP_COUNT" -gt "$MAX_BACKUPS" ]; then
    OLDEST_BACKUP=$(ls -1t "$BACKUP_DIR"/*.tar.gz | tail -1)
    echo "Removing oldest backup: $OLDEST_BACKUP"
    rm -f "$OLDEST_BACKUP"
fi

echo "Backup process completed!"
