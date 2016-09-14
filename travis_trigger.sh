#!/bin/sh

# Triggers a build of https://travis-ci.org/openmrs/openmrs-distro-referenceapplication
# In order to use the script you have to set the TRAVIS_ACCESS_TOKEN environment variable.

body='{
"request": {
  "branch":"master"
}}'

curl -s -X POST \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -H "Travis-API-Version: 3" \
  -H "Authorization: token $TRAVIS_ACCESS_TOKEN" \
  -d "$body" \
  https://api.travis-ci.org/repo/openmrs%2openmrs-distro-referenceapplication/requests