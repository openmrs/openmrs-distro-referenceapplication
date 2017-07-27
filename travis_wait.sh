#!/usr/bin/env bash

echo "\nWaiting for Travis-CI build to start...\n";
sleep 300;
while true
do
  response=$(curl -s -X GET \
  -H "Content-Type: application/json" \
  -H "Accept: application/vnd.travis-ci.2+json" \
  https://api.travis-ci.org/repos/openmrs/openmrs-distro-referenceapplication);
  
  if echo "$response" | grep '"last_build_state":"passed"'
  then
    exit 0;
  else
    if echo "$response" | grep '"last_build_state":"errored"'
    then
      exit 1;
    fi
    
    if echo "$response" | grep '"last_build_state":"failed"'
    then
      exit 1;
    fi
    
    if echo "$response" | grep '"last_build_state":"canceled"'
    then
      exit 1;
    fi
    
    if echo "$response" | grep '"last_build_state":"unknown"'
    then
      exit 1;
    fi
    
    echo "Travis-CI build is running...\n";
    build=`echo "$response" | grep -Po '(?<="last_build_id":)[0-9]*'`;
    buildNumber=`echo "$response" | grep -Po '(?<="last_build_number":")[^"]*'`;
    echo "Please visit https://travis-ci.org/openmrs/openmrs-distro-referenceapplication/builds/$build or see build $buildNumber at https://saucelabs.com/u/openmrs for details\n"
  fi
  
  sleep 60;
done
