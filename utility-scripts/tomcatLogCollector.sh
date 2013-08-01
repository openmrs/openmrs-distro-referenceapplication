#!/bin/bash

workingDir=`pwd`
remoteExec='ssh -o StrictHostKeyChecking=no breeze@devtest01.openmrs.org'

cleanTomcatLog(){
  ##Clean out any old logs
  ## In Bamboo's Home Dir on Devtest01 first
  echo "Hello"
  if ${remoteExec} test -e "~/catalina.out";
    then
      ${remoteExec} "rm ~/catalina.out";
  fi
  ## Then from Bamboos Plan's working Dir
  if ${remoteExec} test -e "${bamboo.build.working.directory}/catalina.out";
    then
      echo "here"
      #${remoteExec} rm "${bamboo.build.working.directory}/catalina.out";
  fi

}
getTomcatLog() {
  ${remoteExec} "sudo cp ${tomcatLogLocation} ~"
  scp -o StrictHostKeyChecking=no breeze@devtest01.openmrs.org:~/catalina.out .

}

$1