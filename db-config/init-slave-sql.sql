CHANGE MASTER TO 
  MASTER_HOST='db',
  MASTER_USER='openmrs_repl',
  MASTER_PASSWORD='openmrs_repl',
  MASTER_AUTO_POSITION=1;  
START SLAVE;
