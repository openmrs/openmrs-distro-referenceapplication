## Quick start

#### Package the distribution and prepare the run

```
mvn clean package
```

#### Run the app
```
cd run/docker
docker-compose up
```

New OpenMRS UI is accessible at http://localhost/openmrs/spa

OpenMRS Legacy UI is accessible at http://localhost/openmrs

---

## Notes

`package/` Package the OpenMRS distribution with the specified dependencies (omod, microfrontends, configurations...). See [package/README.md](package/README.md) for more info.

`run/` Run the OpenMRS distribution. Currently supports only Docker. See [run/README.md](run/README.md) for more info.
