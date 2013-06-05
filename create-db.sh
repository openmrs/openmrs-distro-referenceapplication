#!/bin/bash

MYSQL=`which mysql`
mysqlPw='Admin123'
schemaName='openmrs'
userName="${schemaName}_user"

Q1="CREATE DATABASE IF NOT EXISTS ${schemaName};"
Q2="GRANT USAGE ON ${schemaName}.* TO '${userName}'@'localhost' IDENTIFIED BY '${mysqlPw}'; "
Q3="GRANT ALL PRIVILEGES ON ${schemaName}.* TO ${userName}@localhost;"
Q4="FLUSH PRIVILEGES;"
SQL="${Q1}${Q2}${Q3}${Q4}"


$MYSQL -uroot -p${mysqlPw} -e "${SQL}"