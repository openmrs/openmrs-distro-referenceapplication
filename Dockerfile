# syntax=docker/dockerfile:1

### Dev Stage
FROM openmrs/openmrs-core:dev as dev
WORKDIR /openmrs_distro

ARG MVN_ARGS_SETTINGS="-s /usr/share/maven/ref/settings-docker.xml -U"
ARG MVN_ARGS="install"

# Copy build files
COPY pom.xml ./
COPY distro ./distro/

# Build the distro
RUN --mount=type=secret,id=m2settings,target=/root/.m2/settings.xml mvn $MVN_ARGS_SETTINGS $MVN_ARGS

RUN cp /openmrs_distro/distro/target/sdk-distro/web/openmrs.war /openmrs/distribution/openmrs_core/

RUN cp /openmrs_distro/distro/target/sdk-distro/web/openmrs-distro.properties /openmrs/distribution/
RUN cp -R /openmrs_distro/distro/target/sdk-distro/web/modules /openmrs/distribution/openmrs_modules
RUN cp -R /openmrs_distro/distro/target/sdk-distro/web/owa /openmrs/distribution/openmrs_owas

# Clean up after copying needed artifacts
RUN mvn clean $MVN_ARGS_SETTINGS

### Run Stage
# Replace 'nightly' with the exact version of openmrs-core built for production (if available)
FROM openmrs/openmrs-core:nightly

# Do not copy the war if using the correct openmrs-core image version
COPY --from=dev /openmrs/distribution/openmrs_core/openmrs.war /openmrs/distribution/openmrs_core/

COPY --from=dev /openmrs/distribution/openmrs-distro.properties /openmrs/distribution/
COPY --from=dev /openmrs/distribution/openmrs_modules /openmrs/distribution/openmrs_modules
COPY --from=dev /openmrs/distribution/openmrs_owas /openmrs/distribution/openmrs_owas
COPY --from=dev /openmrs_distro/distro/configuration /openmrs/distribution/openmrs_config
