# syntax=docker/dockerfile:1

### Dev Stage 
#FROM openmrs/openmrs-core:${TAG_CORE:-2.7.3-dev} AS dev
FROM openmrs/openmrs-core:2.8.x-dev-amazoncorretto-21 AS dev
WORKDIR /openmrs_distro

ARG MVN_ARGS_SETTINGS="-s /root/.m2/settings.xml -U -P backend"
ARG MVN_ARGS="install"

# Copy build files
COPY backend/credentials/settings.xml.template /root/.m2/settings.xml
COPY pom.xml ./
COPY backend/pom.xml ./backend/

# Pre-download dependencies (cached layer)
RUN --mount=type=cache,target=/root/.m2/repository \
    --mount=type=secret,id=GHP_USERNAME \
    --mount=type=secret,id=GHP_PASSWORD \
    GHP_USERNAME=$(cat /run/secrets/GHP_USERNAME) \
    GHP_PASSWORD=$(cat /run/secrets/GHP_PASSWORD) \
    mvn $MVN_ARGS_SETTINGS dependency:resolve -B || true

COPY backend ./backend/

# Build the distro, but only deploy from the amd64 build
RUN --mount=type=cache,target=/root/.m2/repository \
    --mount=type=secret,id=GHP_USERNAME \
    --mount=type=secret,id=GHP_PASSWORD \
    GHP_USERNAME=$(cat /run/secrets/GHP_USERNAME) \
    GHP_PASSWORD=$(cat /run/secrets/GHP_PASSWORD) \
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
FROM openmrs/openmrs-core:2.8.5-amazoncorretto-21

# Do not copy the war if using the correct openmrs-core image version
COPY --from=dev /openmrs/distribution/openmrs_core/openmrs.war /openmrs/distribution/openmrs_core/

COPY --from=dev /openmrs/distribution/openmrs-distro.properties /openmrs/distribution/
COPY --from=dev /openmrs/distribution/openmrs_modules /openmrs/distribution/openmrs_modules
COPY --from=dev /openmrs/distribution/openmrs_owas /openmrs/distribution/openmrs_owas
COPY --from=dev  /openmrs/distribution/openmrs_config /openmrs/distribution/openmrs_config
