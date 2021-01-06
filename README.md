iSantePlus OpenMRS Distribution
===================================

![Publish package to GitHub Packages](https://github.com/IsantePlus/openmrs-distro-isanteplus/workflows/Publish%20package%20to%20GitHub%20Packages/badge.svg)

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


## iSantePlus-specific module dependency tree:

fhir2 --> xds-sender, mpi-client

labintegration --> xds-sender

coreapps --> registrationapp

-----

mpi-client --> registrationcore

xds-sender --> registrationcore, registrationapp

m2sys-biometrics --> registrationapp

-----

registrationcore --> m2sys-biometrics,registrationapp

registrationapp --> (none)




