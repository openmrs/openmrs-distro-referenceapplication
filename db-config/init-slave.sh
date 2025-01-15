#!/bin/bash
set -e

# Wait for primary to be ready
until mysql -h mariadb_primary -uroot -p"${MYSQL_ROOT_PASSWORD:-openmrs}" -e "SELECT 1"; do
  echo "Waiting for master database connection..."
  sleep 5
done

mysql -uroot -p "${MYSQL_ROOT_PASSWORD:-openmrs}" <<-EOSQL
  CHANGE MASTER TO
    MASTER_HOST='db',
    MASTER_USER='openmrs_repl',
    MASTER_PASSWORD='openmrs_repl',
    MASTER_AUTO_POSITION=1;
  
  START SLAVE;
EOSQL