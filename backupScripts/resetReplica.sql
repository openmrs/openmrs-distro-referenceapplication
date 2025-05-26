SELECT '=== Stopping replication process ===';
STOP SLAVE;
RESET SLAVE ALL;
SELECT '=== Restarting replication process ===';
CHANGE MASTER TO
  MASTER_HOST='db',
  MASTER_USER='openmrs_repl',
  MASTER_PASSWORD='openmrs_repl',
  MASTER_PORT=3306,
  MASTER_LOG_FILE='${TEMP_MYSQLBIN}',  -- From SHOW MASTER STATUS in master DB
  MASTER_LOG_POS='${TEMP_MYSQL_POS}';  -- From SHOW MASTER STATUS in master DB
START SLAVE;
SELECT '=== Showing replication status after backup process ===';
SHOW SLAVE STATUS;


