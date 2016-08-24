openmrs-distro-referenceapplication
===================================

[![Build Status](https://travis-ci.org/openmrs/openmrs-distro-referenceapplication.svg?branch=master)](https://travis-ci.org/openmrs/openmrs-distro-referenceapplication) [![Sauce Test Status](https://saucelabs.com/buildstatus/rkorytkowski)](https://saucelabs.com/u/rkorytkowski)

## Running locally with Vagrant 
https://wiki.openmrs.org/x/CIC3Ag

### Running ui-tests locally:

By default tests are run with Firefox 42.0 ([[download here](https://ftp.mozilla.org/pub/firefox/releases/42.0/)]), so please be sure to have it installed.
Also tests are run against http://int02.openmrs.org/openmrs so confirm it is accessible from your machine.
It is also possible that tests start failing due to int02.openmrs.org being redeployed. If it happens, please wait for int02 to be available again and run tests again.

1. Clone the repo
2. Go to the ui-tests directory using command line
3. Run `mvn clean install -Prun-all-tests`
