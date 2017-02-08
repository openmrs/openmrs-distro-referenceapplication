#!/usr/bin/env bash

if [ "$TRAVIS_PULL_REQUEST" = "false" ]
then
mvn org.openmrs.maven.plugins:openmrs-sdk-maven-plugin:3.8.0:setup-sdk -DbatchAnswers=n
mvn org.openmrs.maven.plugins:openmrs-sdk-maven-plugin:3.8.0:build-distro -Ddir=distro -e
cd distro
export TOMCAT_PORT=8080
docker-compose -f docker-compose.yml -f docker-compose.prod.yml up -d #detached mode
cd ..
sleep 60
fi
