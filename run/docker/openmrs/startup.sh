#!/bin/bash -eux

DB_CREATE_TABLES=${DB_CREATE_TABLES:-false}
DB_AUTO_UPDATE=${DB_AUTO_UPDATE:-false}
MODULE_WEB_ADMIN=${MODULE_WEB_ADMIN:-true}
DEBUG=${DEBUG:-false}
DEBUG_PORT=${DEBUG_PORT:-8000}
catalina_params=()

cat > /usr/local/tomcat/openmrs-server.properties << EOF
install_method=auto
connection.url=jdbc\:mysql\://${DB_HOST}\:3306/${DB_DATABASE}?autoReconnect\=true&sessionVariables\=default_storage_engine\=InnoDB&useUnicode\=true&characterEncoding\=UTF-8
connection.username=${DB_USERNAME}
connection.password=${DB_PASSWORD}
has_current_openmrs_database=true
create_database_user=false
module_web_admin=${MODULE_WEB_ADMIN}
create_tables=${DB_CREATE_TABLES}
auto_update_database=${DB_AUTO_UPDATE}
EOF

configFiles=`ls -d /etc/properties/*`
for file in $configFiles
do
    name=$(basename "${file}")
    envsubst < ${file} > /usr/local/tomcat/.OpenMRS/${name}
done


# create datbase credentials file to check the existance of data
mkdir -p /etc/mysql/ && touch /etc/mysql/db-credentials.cnf
cat > /etc/mysql/db-credentials.cnf << EOF
[client]
user=${DB_USERNAME}
password=${DB_PASSWORD}
EOF

# wait for mysql to initialise
/usr/local/tomcat/wait-for-it.sh --timeout=3600 ${DB_HOST}:3306

# checking if the database is already available
db_tables_count=`mysql --defaults-extra-file=/etc/mysql/db-credentials.cnf -h${DB_HOST} --skip-column-names -e "SELECT count(*) FROM information_schema.tables WHERE table_schema = '${DB_DATABASE}'"`

# generate encryption keys
encryption_key=`openssl rand -base64 22 | sed 's/=/\\\=/g'`
encryption_vector=`openssl rand -base64 22 | sed 's/=/\\\=/g'`

if [ ${db_tables_count} > 1 ]; then
    cat > /usr/local/tomcat/.OpenMRS/openmrs-runtime.properties << EOF
encryption.vector=${encryption_vector}
connection.url=jdbc\:mysql\://${DB_HOST}\:3306/${DB_DATABASE}?autoReconnect\=true&sessionVariables\=default_storage_engine\=InnoDB&useUnicode\=true&characterEncoding\=UTF-8
module.allow_web_admin=true
connection.username=${DB_USERNAME}
auto_update_database==${DB_AUTO_UPDATE}
encryption.key=${encryption_key}
connection.driver_class=com.mysql.jdbc.Driver
connection.password=${DB_PASSWORD}
EOF
fi

if [ $DEBUG == true ]; then
    export JPDA_ADDRESS=$DEBUG_PORT
    export JPDA_TRANSPORT=dt_socket
    catalina_params+=(jpda)
fi
catalina_params+=(run)

# start tomcat
/usr/local/tomcat/bin/catalina.sh "${catalina_params[@]}" &

# trigger first filter to start database initialization
sleep 15
curl -L http://localhost:8080/openmrs/ > /dev/null
sleep 15

# bring tomcat process to foreground again
wait ${!}
