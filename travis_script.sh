#!/usr/bin/env bash

if [ "$TRAVIS_PULL_REQUEST" = "false" ]
then
mvn org.openmrs.maven.plugins:openmrs-sdk-maven-plugin:3.9.0:setup-sdk -DbatchAnswers=n
mvn clean install -U
cd package/target/distro
export TOMCAT_PORT=8080

if [ "$DB" = "mysql" ]
then
docker-compose -f docker-compose.yml -f docker-compose.prod.yml up -d #detached mode
fi

if [ "$DB" = "mariadb" ]
then
docker-compose -f docker-compose.yml -f docker-compose.prod.yml -f ../../../docker/docker-compose.mariadb.yml up -d #detached mode
fi

sleep 60

cd ../../../ui-tests
mvn test -Pci -Dwebapp.url=http://localhost:8080/openmrs -Dsaucelabs.hub.url=localhost:4445 -DbuildNumber=$TRAVIS_BUILD_NUMBER -Dbranch=$TRAVIS_BRANCH -DsaucelabsTunnel=$TRAVIS_JOB_NUMBER -Dmaven.junit.usefile=false
fi

else
mvn clean install -U
fi
