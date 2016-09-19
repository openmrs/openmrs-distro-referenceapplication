#!/usr/bin/env bash

if [ "$TRAVIS_PULL_REQUEST" = "false" ]
then
mvn clean verify -Pci -Dwebapp.url=http://localhost:8080/openmrs -Dsaucelabs.hub.url=localhost:4445 -DbuildNumber=$TRAVIS_BUILD_NUMBER -Dbranch=$TRAVIS_BRANCH -DsaucelabsTunnel=$TRAVIS_JOB_NUMBER
else
mvn clean install -DskipTests=true
fi
