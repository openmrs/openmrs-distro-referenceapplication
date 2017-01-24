#!/usr/bin/env bash

if [ "$TRAVIS_PULL_REQUEST" = "false" ]
then
mvn org.openmrs.maven.plugins:openmrs-sdk-maven-plugin:3.6.1:setup-sdk -DbatchAnswers=n
mvn org.openmrs.maven.plugins:openmrs-sdk-maven-plugin:3.6.1:build-distro -Ddir=distro -e
cd distro
docker-compose up -d #detached mode
cd ..
sleep 60
fi
