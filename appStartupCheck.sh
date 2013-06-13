#!/bin/bash


##This needs to get refactored so that there is not so much duplication in each uber if statement

checkForStartup() {
Host=$1

  echo "Waiting for OpenMRS to start.  This could take a few minutes."
  while [ -z "${result}"  ];
    do
      if [ "${Host}" == "devtest01.openmrs.org" ]
        then
          result=`ssh -o StrictHostKeyChecking=no devtest01.openmrs.org "sudo tail /var/log/tomcat7/catalina.out | grep \"INFO: Server startup\""`
          sleep 1
          count=$((count+1))
          if [ ${count} == 120 ];
            then
            echo "Waited for 2 minutes and Tomcat has not started."
            echo "Check /var/log/tomcat7/catalina.out to see what is happening."
            exit
          fi
        else
          result=`tail /var/log/tomcat7/catalina.out | grep "INFO: Server startup"`
          sleep 1
          count=$((count+1))
          if [ ${count} == 120 ];
            then
            echo "Waited for 2 minutes and Tomcat has not started."
            echo "Check /var/log/tomcat7/catalina.out to see what is happening."
            exit
          fi
      fi
done
  echo "Tomcat started."
  echo "OpenMRS Reference Application is ready for use."

}
checkForStartup $1