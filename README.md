openmrs-distro-referenceapplication
===================================

[![Build Status](https://travis-ci.org/openmrs/openmrs-distro-referenceapplication.svg?branch=master)](https://travis-ci.org/openmrs/openmrs-distro-referenceapplication)

[![Build Status](https://saucelabs.com/browser-matrix/rkorytkowski.svg)](https://saucelabs.com/open_sauce/user/openmrs)

## Running locally with Vagrant 
https://wiki.openmrs.org/x/CIC3Ag

### Running ui-tests locally:

By default tests are run with Firefox 42.0 ([download here](https://ftp.mozilla.org/pub/firefox/releases/42.0/)), so please be sure to have it installed.
Also tests are run against http://int02.openmrs.org/openmrs so confirm it is accessible from your machine.
It is also possible that tests start failing due to int02.openmrs.org being redeployed. If it happens, please wait for int02 to be available again and run tests again.

1. Clone the repo
2. Go to the ui-tests directory using command line
3. Run `mvn clean install -Prun-all-tests`

### Running ui-tests on Travis CI with SauceLabs

This project contains configuration for running tests on [Travis CI](https://travis-ci.org/) with [SauceLabs](https://saucelabs.com/). These platforms provide free plan for Open Source projects. 

If you want to know why a test fails you can view logs, screenshots and even screencasts on [SauceLabs](https://saucelabs.com/u/rkorytkowski) and see build logs on [Travis CI](https://travis-ci.org/openmrs/openmrs-distro-referenceapplication/builds).

To setup Continuous Integration on your fork, execute following steps: 

1. Create accounts on [Travis CI](https://travis-ci.org/)(it has to be synced with Github owner of fork repository) and [SauceLabs](https://saucelabs.com/)
2. Enable CI on your fork repository on Travis CI (URL is like https://travis-ci.org/profile/${travis_username})
3. Get your SauceLabs access key on 'My Account' view (current URL is https://saucelabs.com/beta/users/${saulabs_username})
4. Add Environment variables to your repository on Travis.org:
  - 'SAUCELABS_USERNAME' equal to your SauceLabs username
  - 'SAUCELABS_ACCESSKEY' equal to access key you got in step 3.
  
  [Guide for adding environment variables from official Travis CI documentation](https://docs.travis-ci.com/user/environment-variables/#Defining-Variables-in-Repository-Settings)
5. Push any commit to your master branch to trigger Travis CI build.

And that's it!

If you would like to setup a similar configuraiton for your distribution, please have a look at [.travis.yml](https://github.com/openmrs/openmrs-distro-referenceapplication/blob/master/.travis.yml). The test server is created using `openmrs-sdk:build-distro` and started with `docker-compose up` on Travis-CI. 

Travis-CI creates a tunnel to SauceLabs, which allows SauceLabs to access the test server and execute tests against that server in a browser. In order to speed up the build, we always run 5 UI tests in parallel using agents provided by SauceLabs. The test server is automatically terminated by Travis-CI once tests are done. 
