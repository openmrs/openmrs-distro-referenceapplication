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

DB_USER="root"                                          # Change this to your database user
DB_NAME="my_database"                           # Change this to your database name
DB_PASSWORD="password"  # Change this to your database password

# Go to docker compose directory
cd ..

# Stop the backend container
echo -e "\nStop backend container"
docker compose stop backend

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
echo -e "\nErase backup directory in container"
docker exec ${CONTAINER_NAME} bash -c "rm -rf /backup/*"

# Create the directories in master container
echo -e "\nCreate the backup directories"
docker exec ${CONTAINER_NAME} bash -c "mkdir -p $TEMP_FULL_BACKUP_PATH"
docker exec ${CONTAINER_NAME} bash -c "mkdir -p $TEMP_INCR_BACKUP_PATH"

# Create temporal directory
echo -e "\nCreate temporal directory to unzip backup"
rm -rf ${TEMP_BACKUP_PATH}
mkdir -p ${TEMP_BACKUP_PATH}

# Unzip full backup in temporal directory
echo -e "\nUnzip backup in temporal directory"
tar -xzvf ${full_path} -C "${TEMP_BACKUP_PATH}" > /dev/null

# Copy unzipped full backup to container with dummy container
echo -e "\nCopy unzipped full backup to container"
docker cp $PWD/tmpFull/. ${CONTAINER_NAME}:${TEMP_FULL_BACKUP_PATH}

# Set backup to use with dummy container
echo -e "\nSet backup to use with dummy container"
docker exec ${CONTAINER_NAME} bash -c "mariadb-backup --prepare --target-dir=/backup/full"

# Stop db containers
echo -e "\nStopping database containers"
docker compose stop db-replic
docker compose stop db

# Free var/lib/mysql dir in db master
echo -e "\nFree var/lib/mysql dir in db master"
docker run --rm -v ${NAME_PREFIX}db-data:/var/lib/mysql busybox sh -c "rm -rf /var/lib/mysql/**"


# Execute dummy container to fill var/lib/mysql 
echo -e "\nRestore with backup"
docker run --rm --name mariadb-dummy -v ${NAME_PREFIX}db-data:/var/lib/mysql -v ${NAME_PREFIX}db-backup:/backup --user root mariadb:10.11.7 mariadb-backup --copy-back --target-dir=/backup/full


# Erase temporal folder of unzipped backup
echo -e "\nErase temporal folder of unzipped backup"
rm -rf tempFull/

# Resume docker compose stack
echo -e "\nResume docker compose stack"
docker compose up -d

# Synchronize replic db container
echo -e "\nTo synchronize replic database container ..."

exit 1



