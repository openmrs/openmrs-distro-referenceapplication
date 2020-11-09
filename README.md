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

---

## Notes

`package/` Package the OpenMRS distribution with the specified dependencies (omod, microfrontends, configurations...). See [package/README.md](package/README.md) for more info.

`run/` Run the OpenMRS distribution. Currently supports only Docker. See [run/README.md](run/README.md) for more info.
