#!/bin/bash
set -e

echo "Initializing master db container"
mariadb -u root -p${MYSQL_ROOT_PASSWORD} -e "
    SELECT '=== Creating replication user before initialization ===';
    CREATE USER '${OMRS_DB_REPL_USER}'@'%' IDENTIFIED BY '${OMRS_DB_REPL_PASSWORD}';
    SELECT '=== Granting replication user before initialization ===';
    GRANT RELOAD, LOCK TABLES, PROCESS, REPLICATION SLAVE, BINLOG MONITOR ON *.* TO '${OMRS_DB_REPL_USER}'@'%';
    FLUSH PRIVILEGES;
    SELECT '=== Showing replication status before initialization ===';
    SHOW MASTER STATUS;
    SELECT '=== Create backup user ===';
    CREATE USER '${OMRS_DB_BACKUP_USER}'@'%' IDENTIFIED BY '${OMRS_DB_BACKUP_PASSWORD}';
    SELECT '=== Granting permissions backup user ===';
    GRANT RELOAD, PROCESS, LOCK TABLES, REPLICATION CLIENT, REPLICA MONITOR, REPLICATION SLAVE ADMIN ON *.* TO '${OMRS_DB_BACKUP_USER}'@'%';
    FLUSH PRIVILEGES;
"