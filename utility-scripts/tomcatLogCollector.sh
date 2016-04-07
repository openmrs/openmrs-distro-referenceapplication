#!/bin/bash

workingDir=`pwd`
remoteExec='ssh -o StrictHostKeyChecking=no bamboo@int-refapp.openmrs.org'
tomcatLogLocation="/var/log/tomcat7/catalina.out"

cleanTomcatLog(){
  ##Clean out any old logs
  ## In Bamboo's Home Dir on int-refapp first
  if ${remoteExec} test -e "~/catalina.out";
    then
      ${remoteExec} "rm ~/catalina.out";
  fi
  ## Then from Bamboos Plan's working Dir
  if test -e "${workingDir}/catalina.out";
    then
      rm ${workingDir}/catalina.out
  fi
  ## And finally zero out the source log file
  ${remoteExec} "sudo bash -c 'cat /dev/null > ${tomcatLogLocation}'"
}

getTomcatLog() {
  ${remoteExec} "sudo cp ${tomcatLogLocation} ~/"
  scp -o StrictHostKeyChecking=no bamboo@int-refapp.openmrs.org:~/catalina.out ${workingDir}
}

$1