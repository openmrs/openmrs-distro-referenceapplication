#!/usr/bin/env bash

# Run UI tests only, if the build was triggered by API from Bamboo, see travis_trigger.sh
if [ "$TRAVIS_EVENT_TYPE" = "api" ]
then
mvn org.openmrs.maven.plugins:openmrs-sdk-maven-plugin:setup-sdk -DbatchAnswers=n
mvn org.openmrs.maven.plugins:openmrs-sdk-maven-plugin:build-distro -Ddir=distro -e
cd distro
docker-compose up -d #detached mode
cd ..
sleep 60
fi
