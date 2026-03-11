# syntax=docker/dockerfile:1

### Dev Stage 
#FROM openmrs/openmrs-core:${TAG_CORE:-2.7.3-dev} AS dev
FROM openmrs/openmrs-core:2.8.x-dev-amazoncorretto-21 AS dev
WORKDIR /openmrs_distro

# Setting credentials for Git Hub Maven Package
ARG GHP_USERNAME
ARG GHP_PASSWORD

# Export as environment variables so Maven can use them in settings.xml
ENV GHP_USERNAME=${GHP_USERNAME}
ENV GHP_PASSWORD=${GHP_PASSWORD}
ARG MVN_ARGS_SETTINGS="-s /root/.m2/settings.xml -U -P backend"
ARG MVN_ARGS="install"

# Copy build files
COPY backend/credentials/settings.xml.template /root/.m2/settings.xml
COPY pom.xml ./
COPY backend/pom.xml ./backend/

# Install locally-built sihsalusinterop artifacts into Maven cache
COPY backend/sihsalusinterop-1.0.3.pom /tmp/sihsalus/
COPY backend/sihsalusinterop-api-1.0.3.jar /tmp/sihsalus/
COPY backend/sihsalusinterop-1.0.3.omod /tmp/sihsalus/
RUN --mount=type=cache,target=/root/.m2/repository \
    mvn install:install-file -Dfile=/tmp/sihsalus/sihsalusinterop-1.0.3.pom \
      -DgroupId=org.openmrs.module -DartifactId=sihsalusinterop -Dversion=1.0.3 -Dpackaging=pom -B && \
    mvn install:install-file -Dfile=/tmp/sihsalus/sihsalusinterop-api-1.0.3.jar \
      -DgroupId=org.openmrs.module -DartifactId=sihsalusinterop-api -Dversion=1.0.3 -Dpackaging=jar -B && \
    cp /tmp/sihsalus/sihsalusinterop-1.0.3.omod /tmp/sihsalus/sihsalusinterop-omod-1.0.3.jar && \
    mvn install:install-file -Dfile=/tmp/sihsalus/sihsalusinterop-omod-1.0.3.jar \
      -DgroupId=org.openmrs.module -DartifactId=sihsalusinterop-omod -Dversion=1.0.3 -Dpackaging=jar -B && \
    rm -rf /tmp/sihsalus

# Pre-download dependencies (cached layer)
RUN --mount=type=cache,target=/root/.m2/repository \
    mvn $MVN_ARGS_SETTINGS dependency:resolve -B || true

COPY backend ./backend/

# Build the distro, but only deploy from the amd64 build
RUN --mount=type=cache,target=/root/.m2/repository \
    if [[ "$MVN_ARGS" != "deploy" || "$(arch)" = "x86_64" ]]; then \
    mvn $MVN_ARGS_SETTINGS $MVN_ARGS; \
    else \
    mvn $MVN_ARGS_SETTINGS install; \
    fi

RUN cp /openmrs_distro/backend/target/sdk-distro/web/openmrs_core/openmrs.war /openmrs/distribution/openmrs_core/

RUN cp /openmrs_distro/backend/target/sdk-distro/web/openmrs-distro.properties /openmrs/distribution/
RUN cp -R /openmrs_distro/backend/target/sdk-distro/web/openmrs_modules /openmrs/distribution/openmrs_modules/
RUN cp -R /openmrs_distro/backend/target/sdk-distro/web/openmrs_owas /openmrs/distribution/openmrs_owas/
RUN cp -R /openmrs_distro/backend/target/sdk-distro/web/openmrs_config /openmrs/distribution/openmrs_config/


### Run Stage
# Replace 'nightly' with the exact version of openmrs-core built for production (if available)
#FROM openmrs/openmrs-core:${TAG_CORE:-2.7.3}
FROM openmrs/openmrs-core:2.8.x-dev-amazoncorretto-21

# Do not copy the war if using the correct openmrs-core image version
COPY --from=dev /openmrs/distribution/openmrs_core/openmrs.war /openmrs/distribution/openmrs_core/

COPY --from=dev /openmrs/distribution/openmrs-distro.properties /openmrs/distribution/
COPY --from=dev /openmrs/distribution/openmrs_modules /openmrs/distribution/openmrs_modules
COPY --from=dev /openmrs/distribution/openmrs_owas /openmrs/distribution/openmrs_owas
COPY --from=dev  /openmrs/distribution/openmrs_config /openmrs/distribution/openmrs_config
