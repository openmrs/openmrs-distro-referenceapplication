#!/bin/bash

# Log every outout in log.txt
exec > >(tee -a fullBackup_log.txt) 2>&1

#!/bin/bash

# Configuration
NAME_PREFIX="peruhce-distro-referenceapplication_"
CONTAINER_NAME="peruHCE-db-master"                      # Change to your MariaDB container name
FULL_BACKUP_DIR="/home/${USER}/peruHCE-fullBackups"     # Change to your desired backup storage location, make sure its exists
MAX_BACKUPS=10                                          # Maximum number of backups to keep
TIMESTAMP=$(date +"%Y-%m-%d_%H-%M-%S")
BACKUP_NAME="$TIMESTAMP"
TEMP_FULL_BACKUP_PATH="/backup/full"                    # Inside the container 
TEMP_INCR_BACKUP_PATH="/backup/inc"                     # Inside the container 
BUSYBOX_VERSION="1.37.0"

TEMP_BACKUP_PATH="./tmpFull"

BACKUP_DIR="/path/to/backups"                           # Change this to your backup directory
CONTAINER_NAME="mariadb_container"                      # Change this to your MariaDB container name
DB_USER="root"                                          # Change this to your database user
DB_NAME="my_database"                           # Change this to your database name
DB_PASSWORD="password"  # Change this to your database password

# Go to docker compose directory
cd ..

# Stop the backend container
#docker compose stop backend

# Stop database container
docker compose stop db-replic
docker compose stop db

# Verify full backups directory
# Verify directory exists
if [ ! -d "$FULL_BACKUP_DIR" ]; then
    echo "Error: Directory '$FULL_BACKUP_DIR' does not exist."
    exit 1
fi

# List Temporal backups
fullBackups=($(ls -t "$FULL_BACKUP_DIR"))

# Check if directory is empty
if [ ${#fullBackups[@]} -eq 0 ]; then
    echo "No backups found in directory: $FULL_BACKUP_DIR"
    exit 1
fi

# Display numbered list of files
echo
echo "Available backups in $FULL_BACKUP_DIR (newest first):"
echo "------------------------------------------------------------------------------------------------------------------------"
echo "------------------------------------------------------------------------------------------------------------------------"
for ((i=0; i<${#fullBackups[@]}; i++)); do
    printf "%2d) %s\n" "$((i+1))" "${fullBackups[i]}"
    echo "------------------------------------------------------------------------------------------------------------------------"
done
echo "------------------------------------------------------------------------------------------------------------------------"


# Get user selection
while true; do
    read -p "Enter the number of the file you want to select (1-${#fullBackups[@]}): " selection
    
    # Validate input
    if [[ "$selection" =~ ^[0-9]+$ ]] && [ "$selection" -ge 1 ] && [ "$selection" -le ${#fullBackups[@]} ]; then
        selected_file="${fullBackups[$((selection-1))]}"
        full_path="$FULL_BACKUP_DIR/$selected_file"
        break
    else
        echo "Invalid selection. Please enter a number between 1 and ${#files[@]}."
    fi
done

# selection has the index selected and selected_file  the name of the file selected
#echo $selection
#echo $selected_file

# Erase previous files in back dir in master container
docker run --rm --name mariadb-dummy -v ${NAME_PREFIX}db-backup:/backup --user root busybox:${BUSYBOX_VERSION} sh -c "rm -rf /backup/*"

# Create the directories in master container
docker run --rm --name mariadb-dummy -v ${NAME_PREFIX}db-backup:/backup --user root mariadb:10.11.7 mkdir -p $TEMP_FULL_BACKUP_PATH
docker run --rm --name mariadb-dummy -v ${NAME_PREFIX}db-backup:/backup --user root mariadb:10.11.7 mkdir -p $TEMP_INCR_BACKUP_PATH

# Create temporal directory
rm -rf ${TEMP_BACKUP_PATH}
mkdir -p ${TEMP_BACKUP_PATH}

# Unzip full backup in tempral directory
tar -xzvf ${full_path} -C "${TEMP_BACKUP_PATH}" > /dev/null

# Copy unzipped full backup to container with dummy container
docker create --name dummy_container -v ${NAME_PREFIX}db-backup:/backup --user root mariadb:10.11.7
docker cp $PWD/tmpFull/. dummy_container:${TEMP_FULL_BACKUP_PATH}

# Set backup to use with dummy container
docker exec ${CONTAINER_NAME} mariadb-backup --backup --target-dir=/backup/full --user=root --password=${OMRS_DB_USER:-openmrs}

# Stop the dummy container and erase temporal volumes 
docker rm -v dummy_container 

# Free var/lib/mysql dir in db master
docker run --rm -v ${NAME_PREFIX}db-data:/var/lib/mysql busybox sh -c "rm -rf /var/lib/mysql/*"

# Execute dummy container to fill var/lib/mysql 
docker run --rm --name mariadb-dummy -v ${NAME_PREFIX}:/var/lib/mysql -v ${NAME_PREFIX}db-backup:/backup --user root mariadb:10.11.7 mariadb-backup --copy-back --target-dir=/backup/full

# Erase temporal folder 
rm -rf tempFull/

# Resume docker compose stack
docker compose up -d


exit 1






# Set directory of full backup
docker exec $CONTAINER_NAME mariadb-backup --prepare --target-dir=/backup/full

# Stop and erase volume of database cotainer
docker stop ${CONTAINER_NAME}
docker volume rm peruhce-distro-referenceapplication_db-data

# Restore using full backup in persistent volume



# Restore using full backup
docker run --rm --name mariadb-dummy -v temp_db-data:/var/lib/mysql -v testdockerbackup_db-backup:/backup --user root mariadb:10.11.7 mariadb-backup --copy-back --target-dir=/backup/full

docker cp ${CONTAINER_NAME}:/backup/full ./mariadb_backup/full 

# Copy backup content
docker cp ./mariadb_backup/full test-db-master:/backup/full

docker stop test-db-master 

docker run --rm -v testdockerbackup_db-backup-t:/var/lib/mysql busybox sh -c "rm -rf /backup/*"
docker run --rm -v testdockerbackup_db-backup-t:/var/lib/mysql busybox sh -c "rm -rf /backup/inc/*"
docker run --rm -v testdockerbackup_db-data-t:/var/lib/mysql busybox sh -c "rm -rf /var/lib/mysql/*"

docker run --rm --name mariadb-dummy -v db-backup:/var/lib/mysql -v testdockerbackup_db-backup-t:/backup --user root mariadb:10.11.7 mariadb-backup --copy-back --target-dir=/backup/full
docker start test-db-master


# List available backups
echo "Available backups:"
BACKUPS=("$BACKUP_DIR"/*.sql "$BACKUP_DIR"/*.sql.gz)
if [ ${#BACKUPS[@]} -eq 0 ]; then
    echo "No backup files found in the directory."
    exit 1
fi

for i in "${!BACKUPS[@]}"; do
    echo "$((i+1)). $(basename "${BACKUPS[$i]}")"
done

# Prompt user to select a backup
while true; do
    read -rp "Select the backup number to restore: " CHOICE
    if [[ $CHOICE =~ ^[0-9]+$ ]] && [ "$CHOICE" -ge 1 ] && [ "$CHOICE" -le ${#BACKUPS[@]} ]; then
        SELECTED_BACKUP="${BACKUPS[$((CHOICE-1))]}"
        break
    else
        echo "Invalid selection. Please try again."
    fi
done

# Restore the selected backup
echo "Restoring backup: $(basename "$SELECTED_BACKUP")"
if [[ "$SELECTED_BACKUP" == *.gz ]]; then
    # If the backup is gzipped, use zcat to decompress it on the fly
    zcat "$SELECTED_BACKUP" | docker exec -i "$CONTAINER_NAME" mariadb -u "$DB_USER" -p"$DB_PASSWORD" "$DB_NAME"
else
    # If the backup is not compressed, use cat
    cat "$SELECTED_BACKUP" | docker exec -i "$CONTAINER_NAME" mariadb -u "$DB_USER" -p"$DB_PASSWORD" "$DB_NAME"
fi

# Check if the restore was successful
if [ $? -eq 0 ]; then
    echo "Restore completed successfully."
else
    echo "Restore failed. Please check the backup file and container logs."
fi