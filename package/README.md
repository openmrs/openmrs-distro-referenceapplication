_Packages a distribution of configs, metadata and binaries to run OpenMRS_

-----

```bash
mvn clean package
```

Output:

`target/openmrs-distro-package/openmrs-distro-package-$version.zip`

Package contents:
* `spa_config/`
  
  The Configuration for OpenMRS frontend.
* `openmrs_modules/`
  
  The required set of OpenMRS modules.
* `openmrs_config/`
  
  The OpenMRS local configuration (more [here](https://github.com/mekomsolutions/openmrs-config-haiti)) to be processed by the [Initializer module](https://github.com/mekomsolutions/openmrs-module-initializer).
* `openmrs_core/`
  
  The target version of OpenMRS Core.

----

### Specifying dependencies
#### OpenMRS modules
`omod`s are specified as Maven `<dependency>` in the [pom.xml](pom.xml) file.

#### OpenMRS Configuration (Initializer)
OpenMRS config can be set in [openmrs-config/configuration/](openmrs-config/configuration/) folder

#### Micro Frontends
ESMs are set in the [spa-config.json](spa-config.json) file.

#### Micro Frontends configuration
MF Config can be set in [spa-config/configuration/](spa-config/configuration/) folder

----
