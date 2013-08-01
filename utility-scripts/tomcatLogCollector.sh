#!/bin/bash

workingDir=`pwd`
remoteExec='ssh -o StrictHostKeyChecking=no devtest01.openmrs.org'
tomcatLogLocation="/var/log/tomcat7/catalina.out"

cleanTomcatLog(){
  ##Clean out any old logs
  ## In Bamboo's Home Dir on Devtest01 first
  if ${remoteExec} test -e "~/catalina.out";
    then
      ${remoteExec} "rm ~/catalina.out";
  fi
  ## Then from Bamboos Plan's working Dir
  if test -e "${workingDir}/catalina.out";
    then
      rm ${workingDir}/catalina.out
  fi
}

getTomcatLog() {
  ${remoteExec} "sudo cp ${tomcatLogLocation} ~/"
  scp -o StrictHostKeyChecking=no devtest01.openmrs.org:~/catalina.out ${workingDir}
}

$1