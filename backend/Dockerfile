# syntax=docker/dockerfile:1

### Dev Stage
#FROM openmrs/openmrs-core:${TAG_CORE:-2.7.3-dev} AS dev
FROM openmrs/openmrs-core:2.8.x-dev-amazoncorretto-21 AS dev
WORKDIR /openmrs_distro

ARG MVN_ARGS_SETTINGS="-s /root/.m2/settings.xml -U -P backend"
ARG MVN_ARGS="install"

ARG CONTENT_VERSION=1.8.23
ARG FUA_VERSION=1.0.47

# Copy build files
COPY backend/credentials/settings.xml.template /root/.m2/settings.xml
COPY pom.xml ./
COPY backend/pom.xml ./backend/

# Pre-download all dependencies into the BuildKit cache.
# SIHSALUS artifacts (fua-omod, content) are fetched from public GitHub Releases
# — no credentials required. All other deps come from public Maven repos.
RUN --mount=type=cache,target=/root/.m2/repository \
    set -e; \
    CONTENT_DIR="/root/.m2/repository/org/openmrs/content/sihsalus/${CONTENT_VERSION}"; \
    FUA_DIR="/root/.m2/repository/org/openmrs/module/fua/${FUA_VERSION}"; \
    FUA_API_DIR="/root/.m2/repository/org/openmrs/module/fua-api/${FUA_VERSION}"; \
    FUA_OMOD_DIR="/root/.m2/repository/org/openmrs/module/fua-omod/${FUA_VERSION}"; \
    mkdir -p "$CONTENT_DIR" "$FUA_DIR" "$FUA_API_DIR" "$FUA_OMOD_DIR"; \
    BASE_CONTENT="https://github.com/sihsalus/sihsalus-content-referenceapplication/releases/download/v${CONTENT_VERSION}"; \
    BASE_FUA="https://github.com/sihsalus/openmrs-module-FUA/releases/download/v${FUA_VERSION}"; \
    curl -fsSL "${BASE_CONTENT}/sihsalus-${CONTENT_VERSION}.zip" -o "${CONTENT_DIR}/sihsalus-${CONTENT_VERSION}.zip"; \
    curl -fsSL "${BASE_CONTENT}/sihsalus-${CONTENT_VERSION}.pom" -o "${CONTENT_DIR}/sihsalus-${CONTENT_VERSION}.pom"; \
    curl -fsSL "${BASE_FUA}/fua-${FUA_VERSION}.pom"              -o "${FUA_DIR}/fua-${FUA_VERSION}.pom"; \
    curl -fsSL "${BASE_FUA}/fua-api-${FUA_VERSION}.jar"          -o "${FUA_API_DIR}/fua-api-${FUA_VERSION}.jar"; \
    curl -fsSL "${BASE_FUA}/fua-api-${FUA_VERSION}.pom"          -o "${FUA_API_DIR}/fua-api-${FUA_VERSION}.pom"; \
    curl -fsSL "${BASE_FUA}/fua-omod-${FUA_VERSION}.jar"         -o "${FUA_OMOD_DIR}/fua-omod-${FUA_VERSION}.jar"; \
    curl -fsSL "${BASE_FUA}/fua-omod-${FUA_VERSION}.pom"         -o "${FUA_OMOD_DIR}/fua-omod-${FUA_VERSION}.pom"; \
    mvn $MVN_ARGS_SETTINGS dependency:resolve -B || true

COPY backend ./backend/

# Build the distro
RUN --mount=type=cache,target=/root/.m2/repository \
    mvn $MVN_ARGS_SETTINGS $MVN_ARGS

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

RUN mkdir -p /openmrs/data
