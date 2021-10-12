iSantePlus OpenMRS Distribution
===================================

[![Publish package to GitHub Packages](https://github.com/IsantePlus/openmrs-distro-isanteplus/actions/workflows/release.yml/badge.svg)](https://github.com/IsantePlus/openmrs-distro-isanteplus/actions/workflows/release.yml)

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

coreapps --> registrationapp, isanteplus, appointmentschedulingui

-----

mpi-client --> registrationcore

xds-sender --> registrationcore, registrationapp, isanteplus

m2sys-biometrics --> registrationapp

-----

registrationcore --> m2sys-biometrics,registrationapp,outgoingmessageexceptions,coreapps

registrationapp --> isanteplus


## Configuration Options (Dev-focused)

1. Create location in the location admin if needed.

2. Set the following in Admin/Settings:
  - **General Settings/Default Locale**: `en_gb` (for english version)
  - **General Settings/Default Location**: `<location from step 1>`
  - **M 2 Sys - Biometrics/Server Const Test Template:** (for dev)
    ```xml
       <Fingers AccessPointInfo=""><Finger POS="3" SOURCE="LEFT" FORMAT="ISO">Rk1SACAyMAAAAADkAAABZQGIAMUAxQEACAAAIUCKAKzpZECMAJ99ZEBxAOK2NUBsAPE1NUA8AIqZZED0AOzJZEDxAPpIZIBWASdEZED5ASbRXUCKAA52ZEBBAWJTXYCmALxjZECWAJNxZECdAHtsZEBOAKujZID6ALfSZEBgAR3FZEBBAGiTZEC5ATrPV0CXAU7TZEAnADAQB0EjADvUZECZAMxsZECAAOTkNUDIAJVZZEBNANizZEAuAOK1ZIAlAM2qUICLAD91ZIEYALxTXUD/ATVQXUA6ACAHSUEqACxSUAAA</Finger><Finger POS="8" SOURCE="RIGHT" FORMAT="ISO">Rk1SACAyMAAAAADqAAABZQGIAMUAxQEAAwAAIkCOAK1uUEB/AI/qZEBiANc3V0DoANZIZEBBAMG0ZEBXAQLHZIBMAQ5EZECOATLUZED3ARZRUEDHAVFZZEDwAWVcZEAdABF7PICdAJ9kZEB+AIGBZEC7AHNeZIDuAJLTZEBAAJOmZEDvAQPRUEC2ASpNXUAiAM22UEAwAFGTZEA4AUtSZEEPABbWXYB4ANHDL0CIAHZyZEDqAMnIZECMAFxwZECyARvNXUEKAJxSZIDAASvVXUAsAHKdZEB3AB55XUEsADBRUEEoABxZQwAA</Finger></Fingers>
    ```
  - **M 2 Sys - Biometrics/National - Service Url:** `<national fingerprint url>`
  
  - **Mpi - Client/Endpoint Pdq Addr:** `http://sedish-haiti.org:5001/CR/fhir`
  - **Mpi - Client/Endpoint Pix Addr:** `http://sedish-haiti.org:5001/CR/`
  - **Mpi - Client/Msg Sending Facility:** `name of your instance's facility`
  - **Mpi - Client/Pid Auto Xref:** `ENTID`
  - **Mpi - Client/Background Threads:** `true`
  - **Mpi - Client/Endpoint Format:** `fhir`
  - **Mpi - Client/Security Auth Type:** `basic`
  - **Mpi - Client/Msg Sending Application:** `<openhim client name>`
  - **Mpi - Client/Security Authtoken:** `<openhim client basic auth password>`  
  - **Mpi - Client/Pid Local:** `<your domain or ip>/ws/fhir2/pid/openmrsid/`
  - **Mpi - Client/Pid Export Identitifer Type:** `Patient ID=iSantePlus ID`
  
  - **Registrationcore/Mpi Implementation:** `registrationcore.mpi.implementation.Fhir`
  
  - **Xdssender/Export Ccd Endpoint:** `http://sedish-haiti.org:5001/SHR/fhir`
  - **Xdssender/Mpi Endpoint:** `http://sedish-haiti.org:5001/CR/fhir`
  - **Xdssender/Openmrs Password:** `<openmrs login password>`
  - **Xdssender/Openmrs Username:** `<openmrs username>`
  - **Xdssender/Oshr Password:** `<openhim client basic auth password>`
  - **Xdssender/Oshr Username:** `<openhim client name>`
  - **Xdssender/Shr Type:** `fhir`
  

