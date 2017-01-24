#!/usr/bin/env bash

if [ "$TRAVIS_PULL_REQUEST" = "false" ]
then
mvn org.openmrs.maven.plugins:openmrs-sdk-maven-plugin:build-distro -Ddir=distro -e -B
cd distro
docker-compose -f docker-compose.yml -f docker-compose.prod.yml up -d #detached mode
cd ..
sleep 60
fi
