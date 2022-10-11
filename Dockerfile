# syntax=docker/dockerfile:1

### Dev Stage
FROM --platform=$BUILDPLATFORM openmrs/openmrs-core:dev as dev
WORKDIR /app

ARG MVN_ARGS_SETTINGS="-s /usr/share/maven/ref/settings-docker.xml -U"
ARG MVN_ARGS="install"

# Copy build files
COPY pom.xml ./
COPY distro ./distro/

# Build the distro
RUN --mount=type=secret,id=m2,target=/root/.m2 mvn $MVN_ARGS_SETTINGS $MVN_ARGS

### Run Stage
# Replace 'nightly' with the exact version of openmrs-core built for production (if available)
FROM openmrs/openmrs-core:nightly

# Do not copy the war if using the correct openmrs-core image version
COPY --from=dev /app/distro/target/sdk-distro/web/openmrs.war /openmrs/distribution/openmrs_core

COPY --from=dev /app/distro/target/sdk-distro/web/openmrs-distro.properties /openmrs/distribution

COPY --from=dev /app/distro/target/sdk-distro/web/modules /openmrs/distribution/openmrs_modules
COPY --from=dev /app/distro/target/sdk-distro/web/owa /openmrs/distribution/openmrs_owas
COPY --from=dev /app/distro/configuration /openmrs/distribution/openmrs_config
