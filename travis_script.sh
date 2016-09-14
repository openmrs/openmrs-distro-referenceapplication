#!/usr/bin/env bash

# Run UI tests only, if the build was triggered by API from Bamboo, see travis_trigger.sh
if [ "$TRAVIS_EVENT_TYPE" = "api" ]
then
mvn clean verify -Pci -Dwebapp.url=http://localhost:8080/openmrs -Dsaucelabs.hub.url=localhost:4445 -DbuildNumber=$TRAVIS_BUILD_NUMBER -Dtunnel.identifier=$TRAVIS_JOB_NUMBER
else
mvn clean install -DskipTests=true
fi
