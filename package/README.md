_Packages a distribution of configs, metadata and binaries to run OpenMRS_

-----

```bash
mvn clean package
```

Output:

`target/openmrs-distro-package/openmrs-distro-package-$version.zip`

Package contents:
* `microfrontends_config/`
<br/>The Configuration for Micro Frontends.
* `openmrs_modules/`
<br/>The required set of OpenMRS modules.
* `openmrs_config/`
<br/>The OpenMRS bespoke configuration (more [here](https://github.com/mekomsolutions/openmrs-config-haiti)) to be processed by the [Initializer module](https://github.com/mekomsolutions/openmrs-module-initializer).
* `openmrs_core/`
<br/>The target version of OpenMRS Core.

----

### Specifying dependencies
#### OpenMRS modules
`omod`s are specified as Maven `<dependency>` in the [pom.xml](pom.xml) file.

#### OpenMRs configuration (Initializer)
OpenMRS config can be set in [openmrs-config/configuration/](openmrs-config/configuration/) folder

#### Micro Frontends
ESMs are set in the [microfrontends.json](microfrontends.json) file.

#### Micro Frontends configuration
MF Config can be set in [microfrontends-config/configuration/](microfrontends-config/configuration/) folder

----
