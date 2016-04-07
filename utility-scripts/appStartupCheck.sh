#!/bin/bash


##This needs to get refactored so that there is not so much duplication in each uber if statement
Host=$1
startedSignal="INFO: Server startup"

timeout () {
  sleep 1
  count=$((count+1))
  if [ ${count} == 300 ];
    then
    echo "Waited for 5 minutes and Tomcat has not started."
    echo "Check /var/log/tomcat7/catalina.out to see what is happening."
    exit 1
  fi

}

checkForStartup() {

  echo "Waiting for OpenMRS to start.  This could take a few minutes."
  while [ -z "${result}"  ];
    do
      if [ "${Host}" == "int-refapp.openmrs.org" ]
        then
          result=`ssh -o StrictHostKeyChecking=no bamboo@int-refapp.openmrs.org "sudo tail /var/log/tomcat7/catalina.out | grep \"${startedSignal}\""`
        timeout
        else
          result=`tail /var/log/tomcat7/catalina.out | grep "${startedSignal}"`
        timeout
      fi
  done
  echo "Tomcat started in ${count} seconds."
  echo "OpenMRS Reference Application is ready for use."
}

checkForStartup $1