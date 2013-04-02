#!/bin/bash

## Usage debian_build.sh [-v version] [-b motech_trunk_directory] [-d destination_directory]
#
TMP_DIR=/tmp/refapp-base-pkg
REFAPP_BASE=${TMP_DIR}
#WARNAME=motech-platform-server.war
#CURRENT_DIR=`pwd`
#
## exit on non-zero exit code
#set -e
#
## Set motech directory
#MOTECH_BASE=.
#
#while getopts "v:b:d:" opt; do
#	case $opt in
#	v)
#		MOTECH_VERSION=$OPTARG
#		WARNAME=motech-platform-server-$MOTECH_VERSION.war
#	;;
#	b)
#		MOTECH_BASE=$OPTARG
#	;;
#	d)
#	    BUILD_DIR=$OPTARG
#	;;
#	s)
#	    CONTENT_DIR=$OPTARG/main/debian
#    ;;
#	esac
#done
#
#if [ -z $CONTENT_DIR ]; then
#    CONTENT_DIR=$MOTECH_BASE/packaging/deb/src/main/debian
#fi
#
#if [ -z $BUILD_DIR ]; then
#    BUILD_DIR=$MOTECH_BASE/packaging/deb/target
#fi
#
#if [ -z $MOTECH_VERSION ]; then
#    echo "Version not specified"
#    exit 1
#fi

mkdir -p $REFAPP_BASE

#ARTIFACT_DIR=$BUILD_DIR/artifacts
#DEPENDENCY_DIR=$BUILD_DIR/dependencies
#CONFIG_DIR=$MOTECH_BASE/platform/server-config/src/main/config
#
#MOTECH_PACKAGENAME="motech_$MOTECH_VERSION.deb"
#MOTECH_BASE_PACKAGENAME="motech-base_$MOTECH_VERSION.deb"
#
#MOTECH_WAR=$ARTIFACT_DIR/$WARNAME

echo "====================="
echo "Building motech-base"
echo "====================="

#if [ ! -f $MOTECH_WAR ]; then
#    echo $MOTECH_WAR does not exist
#    exit 1
#fi

# Create a temp dir for package building
#mkdir $TMP_DIR
cp target/distro/* $REFAPP_BASE
cp -r packaging/deb/src/main/debian/refapp-base/DEBIAN $REFAPP_BASE
cd $TMP_DIR

## Create empty dirs if missing
#mkdir -p motech-base/var/cache/motech/work/Catalina/localhost
#mkdir -p motech-base/var/cache/motech/temp
#mkdir -p motech-base/var/cache/motech/felix-cache
#mkdir -p motech-base/var/lib/motech/webapps
#mkdir -p motech-base/var/log/motech
#mkdir -p motech-base/usr/share/motech/.motech/bundles
#mkdir -p motech-base/usr/share/motech/.motech/rules
#
## copy motech-base
#cp -r $CONTENT_DIR/motech-base .
#mv $WARNAME ./motech-base/var/lib/motech/webapps/ROOT.war

## handle changelogs
#perl -p -i -e "s/\\$\\{version\\}/$MOTECH_VERSION/g" ./motech-base/usr/share/doc/motech-base/changelog
#perl -p -i -e "s/\\$\\{version\\}/$MOTECH_VERSION/g" ./motech-base/usr/share/doc/motech-base/changelog.Debian
#
#gzip --best ./motech-base/usr/share/doc/motech-base/changelog
#gzip --best ./motech-base/usr/share/doc/motech-base/changelog.Debian
#
## Update version
#perl -p -i -e "s/\\$\\{version\\}/$MOTECH_VERSION/g" ./motech-base/DEBIAN/control
#
##Copy config
#cp -r $CONFIG_DIR ./motech-base/usr/share/motech/.motech
#
#mkdir -p ./motech-base/usr/share/motech/.motech/bundles

## Platofrm bundles
#cp -r $ARTIFACT_DIR/motech-platform-*.jar ./motech-base/usr/share/motech/.motech/bundles
## Include motech-admin
#cp -r $ARTIFACT_DIR/motech-admin-bundle*.jar ./motech-base/usr/share/motech/.motech/bundles
#
## Include dependencies
#cp -r $DEPENDENCY_DIR/* ./motech-base/usr/share/motech/.motech/bundles
#
## set up permissions
#find ./motech-base -type d | xargs chmod 755  # for directories
#find ./motech-base -type f | xargs chmod 644  # for files
## special permissions for executbale files
#chmod 755 ./motech-base/DEBIAN/postinst
#chmod 755 ./motech-base/DEBIAN/prerm
#chmod 755 ./motech-base/DEBIAN/postrm
#chmod 755 ./motech-base/DEBIAN/control
#chmod 755 ./motech-base/etc/init.d/motech

# Build package
echo "Building package"
fakeroot dpkg-deb --build $REFAPP_BASE

#mv motech-base.deb $BUILD_DIR/$MOTECH_BASE_PACKAGENAME
#
## Check package for problems
#echo "Checking package with lintian"
#lintian -i $BUILD_DIR/$MOTECH_BASE_PACKAGENAME
#
#echo "Done! Created $MOTECH_PACKAGENAME"
#
##clean up
#rm -r $TMP_DIR/*
#
#echo "====================="
#echo "Building motech"
#echo "====================="
#
## copy files
#cp -r $CONTENT_DIR/motech .
#
## handle changelogs
#perl -p -i -e "s/\\$\\{version\\}/$MOTECH_VERSION/g" ./motech/usr/share/doc/motech/changelog
#perl -p -i -e "s/\\$\\{version\\}/$MOTECH_VERSION/g" ./motech/usr/share/doc/motech/changelog.Debian
#
#gzip --best ./motech/usr/share/doc/motech/changelog
#gzip --best ./motech/usr/share/doc/motech/changelog.Debian
#
## Update version
#perl -p -i -e "s/\\$\\{version\\}/$MOTECH_VERSION/g" ./motech/DEBIAN/control
#
## set up permissions
#find ./motech -type d | xargs chmod 755  # for directories
#find ./motech -type f | xargs chmod 644  # for files
## special permissions for executbale files
#chmod 755 ./motech/DEBIAN/control
#
#echo "Building package"
#
#fakeroot dpkg-deb --build motech
#mv motech.deb $BUILD_DIR/$MOTECH_PACKAGENAME
#
#echo "Checking package with lintian"
#lintian -i $BUILD_DIR/$MOTECH_PACKAGENAME
#
#echo "Done! Created $MOTECH_PACKAGENAME"
#
## clean up
#cd $CURRENT_DIR
#rm -r $TMP_DIR
#
## build modules
#export MOTECH_BASE
#export MOTECH_VERSION
#export BUILD_DIR
#export CONTENT_DIR
#export ARTIFACT_DIR
#
#$CONTENT_DIR/modules/build-modules.sh