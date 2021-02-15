_Runs the OpenMRS distribution_

----

### Run the OpenMRS distribution

Unpack your OpenMRS distro package in the `docker/distro/`.

Then run:
```bash
cd docker/
docker-compose up
```

OpenMRS Microfrontends UI is accessible at http://localhost/ui

OpenMRS Legacy UI is accessible at http://localhost/openmrs


### Notes

Unpacking the OpenMRS distro package **is not needed** if you've already run `mvn clean package` from the [root folder](../) project
