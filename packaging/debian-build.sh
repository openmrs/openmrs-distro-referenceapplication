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
            applyVersion
        else
            echo "Can't find the distribution dir.  Do you need to run mvn package?"
    fi
}
createDeb() {
    cd target
    dpkg-deb -b openmrs-reference openmrs-${version}.deb
    #dpkg-deb --build openmrs-reference
    #equivs-build openmrs-reference
}

applyVersion() {
    if [ -z ${version} ];
        then
            echo "This looks like it is not being run from CI."
        else
            sed -i 's/99999/'${version}'/g' ${DebCtrlDir}/control

    fi
}

clean() {
    rm -rf target
}
clean
prepareDir
createDeb
