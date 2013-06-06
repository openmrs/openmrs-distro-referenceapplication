#!/bin/bash

checkForStartup() {

  echo "Waiting for OpenMRS to start.  This could take a few minutes."
  while [ -z "${result}"  ];
    do
      result=`tail /var/log/tomcat7/catalina.out | grep "INFO: Server startup"`
      sleep 1
      count=$((count+1))
      if [ ${count} == 120 ];
        then
        echo "Waited for 2 minutes and Tomcat has not started."
        echo "Check /var/log/tomcat7/catalina.out to see what is happening."
        break
      fi
done
  echo "Tomcat started."
  echo "OpenMRS Reference Application is ready for use."
}
checkForStartup