#!/bin/bash

# You need to set previously these variables:
#   BACKUP_DATA :       Location of the mariaDb backups
#   DB_CONTAINER_ID :   Id of the docker container of the database to backup


# Define backup directories
BACKUP_DIR="$BACKUP_DATA"  # Replace with the actual backup directory path
FULL_BACKUP_DIR="$BACKUP_DIR/full"
INCREMENTAL_BACKUP_DIR="$BACKUP_DIR/incremental"

# Get current date and time
DATE=$(date +"%Y-%m-%d_%H-%M-%S")
FULL_BACKUP_FILE="$FULL_BACKUP_DIR/full_backup_$DATE.sql"
INCREMENTAL_BACKUP_FILE="$INCREMENTAL_BACKUP_DIR/incremental_backup_$DATE.sql"

# Docker container name or ID for MariaDB
CONTAINER_NAME="$DB_CONTAINER_ID"  # Replace with your container name or ID

# Check if it's time for a full backup or incremental backup
if [[ $(date +%d) % 3 -eq 0 ]]; then
    # Full backup every 3 days
    echo "Performing full backup..."
    docker exec $CONTAINER_NAME mysqldump -u root -p${MYSQL_ROOT_R_PASSWORD:-openmrs_r} --all-databases > $FULL_BACKUP_FILE
else
    # Incremental backup every 3 hours
    echo "Performing incremental backup..."
    docker exec $CONTAINER_NAME mysqldump -u root -p${MYSQL_ROOT_R_PASSWORD:-openmrs_r} --all-databases --single-transaction --flush-logs > $INCREMENTAL_BACKUP_FILE
fi