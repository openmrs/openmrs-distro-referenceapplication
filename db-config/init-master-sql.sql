CREATE USER 'openmrs_repl'@'%' IDENTIFIED BY 'openmrs_repl';
GRANT REPLICATION SLAVE ON *.* TO 'openmrs_repl'@'%';
FLUSH PRIVILEGES;
