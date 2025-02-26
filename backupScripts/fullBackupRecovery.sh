#!/bin/bash

# Configuration
MASTER_CONTAINER="peruHCE-db-master"                    # Change this to your MariaDB master container name
BACKUP_DIR="/home/dev-user/peruHCE-fullBackups"         # Change this to where your backup is stored on the host
CONTAINER_BACKUP_PATH="/backup"   
TEMP_BACKUP_PATH="/backup"                              # Temporary backup location inside the container

echo "Available backup files:"
ls -1t "$BACKUP_DIR"/*.tar.gz

echo -e "\nDo you want to use the most recent backup? (yes/no)"
read -r USE_LATEST

if [ "$USE_LATEST" == "yes" ]; then
    BACKUP_FILE=$(ls -1t "$BACKUP_DIR"/*.tar.gz | head -n 1)
    echo "Using most recent backup: $BACKUP_FILE"
else
    echo "Enter the backup file name (with .tar.gz extension):"
    read -r BACKUP_FILE
    BACKUP_FILE="$BACKUP_DIR/$BACKUP_FILE"

    if [ ! -f "$BACKUP_FILE" ]; then
        echo "Error: Backup file not found!"
        exit 1
    fi
fi

# Create the directorie in case doesnt exist
echo "Creating backup directory in master container..."
docker exec --user root $CONTAINER_NAME mkdir -p "$TEMP_BACKUP_PATH"

# Copy that zipped bback to the master container
echo "Copying compressed backup to backup directory in master container..."
docker cp "$BACKUP_DIR/$BACKUP_FILE" "$MASTER_CONTAINER:$CONTAINER_BACKUP_PATH"

# Extract the backup
echo "Extracting backup..."
tar -xzf "$BACKUP_FILE" -C "$BACKUP_DIR"
EXTRACTED_BACKUP_DIR=$(basename "$BACKUP_FILE" .tar.gz)

echo "Starting restoration of MariaDB Master from backup..."



# Clean the backup directorie in the replica container
echo "Erasing backup directory in master container..."
docker exec --user root $CONTAINER_NAME rm -rf "$TEMP_BACKUP_PATH"

# Stop the MariaDB master container
echo "Stopping the MariaDB master container..."
docker stop "$MASTER_CONTAINER"

# Copy the backup from the host to the master container
echo "Copying backup to the master container..."
docker cp "$BACKUP_DIR" "$MASTER_CONTAINER:$CONTAINER_BACKUP_PATH"

# Start the container without running MariaDB (to perform the restore)
echo "Starting the container..."
docker start "$MASTER_CONTAINER"

# Prepare the backup inside the container
echo "Preparing the backup..."
docker exec -it "$MASTER_CONTAINER" mariadb-backup --prepare --target-dir="$CONTAINER_BACKUP_PATH"

# Remove old data and restore the backup
echo "Restoring the backup to /var/lib/mysql..."
docker exec -it "$MASTER_CONTAINER" bash -c "
    rm -rf /var/lib/mysql/*
    mariadb-backup --copy-back --target-dir=$CONTAINER_BACKUP_PATH
    chown -R mysql:mysql /var/lib/mysql
"

# Restart the MariaDB master
echo "Restarting MariaDB..."
docker restart "$MASTER_CONTAINER"

# Reset replication (if needed)
echo "Resetting master replication..."
docker exec -it "$MASTER_CONTAINER" mariadb -u root -p -e "RESET MASTER; SHOW MASTER STATUS;"

echo "MariaDB master restoration completed!"
