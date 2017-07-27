#!/usr/bin/env bash

if [ "$TRAVIS_PULL_REQUEST" = "false" ]
then
mvn org.openmrs.maven.plugins:openmrs-sdk-maven-plugin:3.8.0:setup-sdk -DbatchAnswers=n
mvn org.openmrs.maven.plugins:openmrs-sdk-maven-plugin:3.8.0:build-distro -Ddir=distro -e -U
cd distro
export TOMCAT_PORT=8080

if [ "$DB" = "mysql" ]
then
docker-compose -f docker-compose.yml -f docker-compose.prod.yml up -d #detached mode
fi

if [ "$DB" = "mariadb" ]
then
docker-compose -f docker-compose.yml -f docker-compose.prod.yml -f ../docker/docker-compose.mariadb.yml up -d #detached mode
fi

cd ..
sleep 60
fi
