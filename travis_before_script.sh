#!/usr/bin/env bash

if [ "$TRAVIS_PULL_REQUEST" = "false" ]
then
mvn org.openmrs.maven.plugins:openmrs-sdk-maven-plugin:setup-sdk -DbatchAnswers=n
mvn org.openmrs.maven.plugins:openmrs-sdk-maven-plugin:build-distro -Ddir=distro -e
cd distro
docker-compose up -d #detached mode
cd ..
sleep 60
fi
