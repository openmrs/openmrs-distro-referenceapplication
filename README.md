iSantePlus OpenMRS Distribution
===================================

[master](https://github.com/openmrs/openmrs-distro-referenceapplication/) (platform-2.0.x): [![Build Status](https://travis-ci.org/openmrs/openmrs-distro-referenceapplication.svg?branch=master)](https://travis-ci.org/openmrs/openmrs-distro-referenceapplication/branches)

## Building

To build the distribution, run the following script from the project root directory.
```shell script
 mvn org.openmrs.maven.plugins:openmrs-sdk-maven-plugin:3.8.0:build-distro \ 
     -Ddir=<desired-output-dir> \ 
     -Ddistro=./package/src/main/resources/openmrs-distro.properties \ 
     -U
```
```
mvn clean process-resources

```
## Running with OpenMRS SDK

Please follow the instructions at the [OpenMRS SDK Wiki page](https://wiki.openmrs.org/display/docs/OpenMRS+SDK). Set up a server with the Reference Application distribution.
test cases, please, follow the instructions in the [Code Style paragraph](https://wiki.openmrs.org/display/docs/Java+Conventions) and the [guidelines](https://wiki.openmrs.org/display/docs/Automated+Testing+Guidelines) 