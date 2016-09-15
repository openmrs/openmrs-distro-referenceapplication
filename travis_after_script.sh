#!/usr/bin/env bash

if [ "$TRAVIS_PULL_REQUEST" = "false" ]
then
docker exec openmrs-referenceapplication cat .OpenMRS/openmrs.log
fi