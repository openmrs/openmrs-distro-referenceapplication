# UI Tests

[Bamboo CI](https://ci.openmrs.org/browse/REFAPP-UI) (Chrome): ![Build Status](https://ci.openmrs.org/plugins/servlet/wittified/build-status/REFAPP-UI)

[Travis CI](https://travis-ci.org/github/openmrs/openmrs-distro-referenceapplication) (Firefox): [![Build Status](https://travis-ci.org/openmrs/openmrs-distro-referenceapplication.svg?branch=master)](https://travis-ci.org/openmrs/openmrs-distro-referenceapplication/branches)

[![Run UI tests on FirefoxInGithubActions](https://github.com/openmrs/openmrs-distro-referenceapplication/actions/workflows/ci.yml/badge.svg)](https://github.com/openmrs/openmrs-distro-referenceapplication/actions/workflows/ci.yml)

Reference Application UI tests are end to end user interface tests driven by Selenium

## Compile & Run
`mvn clean install -DskipTests=false`