# OpenMRS 3.0 Reference Application

This project holds the build configuration for the OpenMRS 3.0 reference application, found on
https://dev3.openmrs.org and https://o3.openmrs.org.

## Quick start

### Package the distribution and prepare the run

```
docker compose build
```

### Run the app

```
docker compose up
```

The new OpenMRS UI is accessible at http://localhost/openmrs/spa

OpenMRS Legacy UI is accessible at http://localhost/openmrs

## Overview

This distribution consists of four images:

* db - This is just the standard MariaDB image supplied to use as a database
* backend - This image is the OpenMRS backend. It is built from the main Dockerfile included in the root of the project and
  based on the core OpenMRS Docker file. Additional contents for this image are drawn from the `distro` sub-directory which
  includes a full Initializer configuration for the reference application intended as a starting point.
* frontend - This image is a simple nginx container that embeds the 3.x frontend, including the modules described in  the
  `frontend/spa-build-config.json` file.
* proxy - This image is an even simpler nginx reverse proxy that sits in front of the `backend` and `frontend` containers
  and provides a common interface to both. This helps mitigate CORS issues.

## Contributing to the configuration

This project uses the [Initializer](https://github.com/mekomsolutions/openmrs-module-initializer) module
to configure metadata for this project. The Initializer configuration can be found in the configuration
subfolder of the distro folder. Any files added to this will be automatically included as part of the
metadata for the RefApp.

Eventually, we would like to split this metadata into two packages:

* `openmrs-core`, which will contain all the metadata necessary to run OpenMRS
* `openmrs-demo`, which will include all of the sample data we use to run the RefApp

The `openmrs-core` package will eventually be a standard part of the distribution, with the `openmrs-demo`
provided as an optional add-on. Most data in this configuration _should_ be regarded as demo data. We
anticipate that implementation-specific metadata will replace data in the `openmrs-demo` package,
though they may use that metadata as a starting point for that customization.

To help us keep track of things, we ask that you suffix any files you add with either
`-core_demo` for files that should be part of the demo package and `-core_data` for
those that should be part of the core package. For example, a form named `test_form.json` would become
`test_core-core_demo.json`.

Frontend configuration can be found in `frontend/config-core_demo.json`.

# Adding a Custom Developed Module to the OpenMRS Frontend
This section outlines the steps to integrate a custom-developed module into the OpenMRS frontend environment. For demonstration purposes, we will use the digipath custom module as an example.

## Step 1: Locate the Custom Module
Find the project repository where the custom module resides.
Example: https://github.com/Gamshan/openmrs-esm-patient-chart

Navigate to the module directory:
`openmrs-esm-patient-chart/packages/esm-patient-digipaths-app/`

## Step 2: Build the Module
To generate the production-ready files:
```
yarn build
```

This will create a /dist directory inside the module.

## Step 3: Create the NPM Package
Package the module as a .tgz file:
```
npm pack
```
This will generate a file named:  `openmrs-esm-patient-digipath-app-<version>.tgz`

## Step 4: Move the Package
Move the generated .tgz file to the following location: `openmrs-distro-referenceapplication/frontend`

## Step 5: Update Build Configuration
Before building the frontend, make the following changes to integrate the custom module:

1. Modify `frontend/Dockerfile`
   Add the following line to copy the `.tgz` file:
```
COPY openmrs-esm-patient-digipath-app-<version>.tgz .
```

2. Update `frontend/spa-assemble-config.json`
      Include the custom module in the `frontendModules` section:
```
"frontendModules": {
       ...
      "@openmrs/esm-patient-digipaths-app": "file:openmrs-esm-patient-digipath-app-<version>.tgz"
}
```
## Final Note
   After completing the above steps, proceed with the usual frontend build process. This will include the custom module in the OpenMRS frontend application.
   Thanks!
