# syntax=docker/dockerfile:1

### Dev Stage
FROM openmrs/openmrs-core:dev as dev
WORKDIR /app

ENV OPENMRS_SDK_PLUGIN="org.openmrs.maven.plugins:openmrs-sdk-maven-plugin"
ENV MVN_ARGS_SETTINGS="-s /usr/share/maven/ref/settings-docker.xml"

# Setup SDK
RUN mvn $MVN_ARGS_SETTINGS $OPENMRS_SDK_PLUGIN:setup-sdk -DbatchAnswers=n -B

# Copy distro file
COPY openmrs-distro.properties .

# Build the distro
RUN mvn $MVN_ARGS_SETTINGS $OPENMRS_SDK_PLUGIN:build-distro -Ddistro=openmrs-distro.properties -Ddir=docker -B

### Run Stage
# Replace 'nightly' with the exact version of openmrs-core built for production (if available)
FROM openmrs/openmrs-core:nightly

# Do not copy the war if using the correct openmrs-core image version
COPY --from=dev /app/docker/web/openmrs.war /openmrs/distribution/openmrs_core
COPY --from=dev /app/docker/web/modules /openmrs/distribution/openmrs_modules
COPY --from=dev /app/docker/web/owa /openmrs/distribution/openmrs_owas
