#!/bin/bash

MYSQL=`which mysql`
mysqlPw='Admin123'
schemaName='openmrs'
userName="${schemaName}_user"

Q1="CREATE DATABASE IF NOT EXISTS ${schemaName} CHARACTER SET UTF8;"
Q2="GRANT USAGE ON ${schemaName}.* TO '${userName}'@'localhost' IDENTIFIED BY '${mysqlPw}';"
Q3="GRANT ALL PRIVILEGES ON ${schemaName}.* TO ${userName}@'%';"
Q4="GRANT ALL PRIVILEGES ON ${schemaName}.* TO root@'%' IDENTIFIED BY '${mysqlPw}';"
Q5="FLUSH PRIVILEGES;"
SQL="${Q1}${Q2}${Q3}${Q4}${Q5}"

#Create the OpenMRS Schema
$MYSQL -uroot -p${mysqlPw} -e "${SQL}"

#Set the privs for it
#$MYSQL -uroot -p${mysqlPw} -e "GRANT ALL PRIVILEGES ON openmrs.* TO root@'%' IDENTIFIED BY '${mysqlPw}';"