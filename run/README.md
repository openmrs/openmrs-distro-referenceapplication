_Runs the OpenMRS distribution_

----

### Run the OpenMRS distribution

Unpack your OpenMRS distro package in the `docker/distro/`.

Then run:
```bash
cd docker/
docker-compose up
```

New OpenMRS UI is accessible at http://localhost/openmrs/spa

OpenMRS Legacy UI is accessible at http://localhost/openmrs

We provide 3 docker-compose configurations for slightly different scenarios:

|File|Purpose|
|----|-------|
|docker-compose.yml|Uses light-weight images and mounts the contents of `./distro`; in this mode, the distribution can be modified without need to re-build the Docker images.
|docker-compose-embedded.yml|Uses images that already have the contents of `./distro` loaded into them. These are the images we use for development and demo.
|docker-compose-distro.yml|Uses the exact same pre-built images that we use to run the development environment, downloading these from Docker Hub.


### Notes

Unpacking the OpenMRS distro package **is not needed** if you've already run `mvn clean package` from the [root folder](../) project

---

## Environment Variables

Using the default package defined in this repository, the following are important environment variables:

### OpenMRS

For the OpenMRS image, the following environment variables may be supplied:

#### OpenMRS Distribution

|Variable|Default Value|Description|
|--------|-------------|-----------|
|`OMRS_HOME`|`/openmrs`|The base path for where the distribution files are stored on the image.|
|`OMRS_WEBAPP_NAME`|`openmrs`|The name OpenMRS WAR should be deployed with. This determines the relative URL and some other environment variables.|
|`OMRS_DISTRO_DIR`|`$OMRS_HOME/distribution`|The folder that the OMRS distribution is mounted in.|
|`OMRS_DISTRO_CORE`|`$OMRS_DISTRO_DIR/openmrs_core`|The folder that contains the core OpenMRS WAR file and any other WAR files that should be deployed alongside it.|
|`OMRS_DISTRO_MODULES`|`$OMRS_DISTRO_DIR/openmrs_modules`|The folder that contains the OMODs (backend modules) that should be deployed on this instance of OpenMRS|
|`OMRS_DISTRO_OWAS`|`$OMRS_DISTRO_DIR/openmrs_owas`|The folder that contains any OWAs (deprecated front-end modules) that should be deployed on this instance of OpenMRS|
|`OMRS_DISTRO_CONFIG`|`$OMRS_DISTRO_DIR/openmrs_config`|The folder that contains the configuration to deploy. This is primarily intended to hold files processed by the [Initializer module](https://github.com/mekomsolutions/openmrs-module-initializer).|

#### OpenMRS Runtime
|Variable|Default Value|Description|
|--------|-------------|-----------|
|`OMRS_DATA_DIR`|`$OMRS_HOME/data`|The folder used to store the running OpenMRS configuration, including log files, attachments, etc.|
|`OMRS_SERVER_PROPERTIES_FILE`|`$OMRS_HOME/$OMRS_WEBAPP_NAME-server.properties`|The location of the server properties file for this server.|
|`OMRS_RUNTIME_PROPERTIES_FILE`|`$OMRS_DATA_DIR/$OMRS_WEBAPP_NAME-runtime.properties`|The location of the runtime properties file for this OpenMRS instance.|

#### Database (JDBC) Properties
|Variable|Default Value|Description|
|--------|-------------|-----------|
|`OMRS_CONFIG_DATABASE`|`mysql`|The database used by this OpenMRS instance. Should be either `mysql` or `postgresql`.|
|`OMRS_CONFIG_CONNECTION_SERVER`|`localhost`|The host that the database is running on.|
|`OMRS_CONFIG_CONNECTION_PORT`|`3306` (MySQL) or `5432` (Postgres)|The port to use to connect to the database
|`OMRS_CONFIG_CONNECTION_DATABASE`|`openmrs`|The name of the openmrs database on the database server.|
|`OMRS_CONFIG_CONNECTION_ARGS`|`?autoReconnect=true&sessionVariables=default_storage_engine=InnoDB&useUnicode=true&characterEncoding=UTF-8` (MySQL)|Any connection arguments to append to the end of the JDBC string|
|`OMRS_CONFIG_CONNECTION_EXTRA_ARGS`| |If you have any additional arguments that should be added after `$OMRS_CONFIG_CONNECTION_ARGS`.|
|`OMRS_CONFIG_CONNECTION_DRIVER_CLASS`|`com.mysql.jdbc.Driver` (MySQL) or `org.postgresql.Driver` (Postgresql)|The JDBC driver class to use.|
|`OMRS_CONFIG_JDBC_URL_PROTOCOL`|`mysql` (MySQL) or `postgresql` (Postgresql)|The JDBC protocol to use for the connection string.|
|`OMRS_CONFIG_CONNECTION_URL`|`jdbc:${OMRS_CONFIG_JDBC_URL_PROTOCOL}://${OMRS_CONFIG_CONNECTION_SERVER}:${OMRS_CONFIG_CONNECTION_PORT}/${OMRS_CONFIG_CONNECTION_DATABASE}${OMRS_CONFIG_CONNECTION_ARGS}${OMRS_CONFIG_CONNECTION_EXTRA_ARGS`}|If the above options do not give you enough control over the JDBC URL, you can specify an entirely custom one here.|

#### Server Initial Configuration
|Variable|Default Value|Description|
|--------|-------------|-----------|
|`OMRS_CONFIG_ADD_DEMO_DATA`|`false`|Whether to load demo data into the database while creating the database.|
|`OMRS_CONFIG_ADMIN_USER_PASSWORD`|`Admin123`|The password for the `admin` user.|
|`OMRS_CONFIG_AUTO_UPDATE_DATABASE`|`true`|Whether to automatically apply database updates on startup.|
|`OMRS_CONFIG_MODULE_WEB_ADMIN`|`true`|Whether to allow modules to be uploaded at runtime.|

### UI

The ui image makes use of the following environment variables:

|Variable|Default Value|Description|
|--------|-------------|-----------|
|`SPA_PATH`| |The URL the SPA resources are served from. Generally should be `/openmrs/spa`, but can vary depending on how the gateway / proxy server is configured.|
|`API_URL`| |The URL to reach the backend server from the SPA.|
|`SPA_CONFIG_URLS`| |A space or comma separated list of URLs to use to load any SPA configurations.|
|`IMPORTMAP_URL`| |To use a remote importmap rather than the bundled import map, specify the URL here.|
