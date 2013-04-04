#!/bin/bash

MavenRootDir=$(dirname `pwd`)
DistroDir=${MavenRootDir}/target/distro
DebCtrlDir=target/openmrs-reference/DEBIAN
DebWarDir=target/openmrs-reference/var/lib/tomcat6/webapps/
OmodTemp=target/openmrs-reference/tmp/openmrs-omods

prepareDir() {
    if [ -d ${DistroDir} ];
        then
            #mkdir -p ${DebCtrlDir}
            mkdir -p ${DebWarDir}
            mkdir -p ${OmodTemp}
            cp ${DistroDir}/*.war ${DebWarDir}/openmrs.war
            cp ${DistroDir}/*.omod ${OmodTemp}
            cp -r DEBIAN ${DebCtrlDir}
        else
            echo "Can't find the distribution dir.  Do you need to run mvn package?"
    fi
}
createDeb() {
    cd target
    dpkg-deb -b openmrs-reference openmrs.deb
    #dpkg-deb --build openmrs-reference
    #equivs-build openmrs-reference

}
clean() {
    rm -rf target
}
clean
prepareDir
createDeb
