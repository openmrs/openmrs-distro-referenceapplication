#!/bin/bash
set -e

echo "Initializing replic db container"
mariadb -u root -p${MYSQL_ROOT_PASSWORD} -e "
  SELECT '=== Connecting using replication user before initialization ===';
  CHANGE MASTER TO 
    MASTER_HOST='db',
    MASTER_USER='${OMRS_DB_REPL_USER}',
    MASTER_PASSWORD='${OMRS_DB_REPL_PASSWORD}',
    MASTER_PORT=3306;
  FLUSH PRIVILEGES;
  SELECT '=== Starting replication process ===';
  START SLAVE;
  SELECT '=== Showing replication status before initialization ===';
  SHOW SLAVE STATUS;
"