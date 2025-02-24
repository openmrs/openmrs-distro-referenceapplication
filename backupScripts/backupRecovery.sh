#!/bin/bash

# Define backup directories
BACKUP_DIR="$BACKUP_DATA"  # Replace with the actual backup directory path
FULL_BACKUP_DIR="$BACKUP_DIR/full"
INCREMENTAL_BACKUP_DIR="$BACKUP_DIR/incremental"

# Docker container name or ID for MariaDB
CONTAINER_NAME="$DB_CONTAINER_ID"  # Replace with your container name or ID

# MariaDB root password
MYSQL_PASSWORD="${MYSQL_ROOT_R_PASSWORD:-openmrs_r}"  # Replace with your MariaDB root password

# Function to restore full backup
restore_full_backup() {
    BACKUP_FILE=$1
    echo "Restoring full backup from $BACKUP_FILE..."
    docker exec -i $CONTAINER_NAME mysql -u root -p$MYSQL_PASSWORD < $BACKUP_FILE
}

# Function to restore incremental backup
restore_incremental_backup() {
    BACKUP_FILE=$1
    echo "Restoring incremental backup from $BACKUP_FILE..."
    docker exec -i $CONTAINER_NAME mysql -u root -p$MYSQL_PASSWORD < $BACKUP_FILE
}

# Check for full backup and restore it first
echo "Restoring from full backup..."
FULL_BACKUP_FILE=$(ls -t $FULL_BACKUP_DIR | head -n 1)  # Get the latest full backup
restore_full_backup "$FULL_BACKUP_DIR/$FULL_BACKUP_FILE"

# Check for incremental backups and apply them
echo "Applying incremental backups..."
for INCREMENTAL_BACKUP_FILE in $(ls -t $INCREMENTAL_BACKUP_DIR); do
    restore_incremental_backup "$INCREMENTAL_BACKUP_DIR/$INCREMENTAL_BACKUP_FILE"
done

echo "Recovery complete."