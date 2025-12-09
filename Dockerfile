# syntax=docker/dockerfile:1

ARG BUILD_TYPE="distro"

### Shared Base
# This layer is the base development layer for both the base distro and the sites
FROM openmrs/openmrs-core:2.7.x-dev-amazoncorretto-17 AS base
WORKDIR /openmrs_distro

### Base Distro Dev Layer
# This layer just builds the base distribution used by all sites
FROM base AS distro_dev

ARG MVN_PROJECT="distro"
ARG MVN_ARGS_SETTINGS="-s /usr/share/maven/ref/settings-docker.xml -U -P $MVN_PROJECT"
ARG MVN_ARGS="install"

COPY pom.xml ./
COPY distro distro

RUN --mount=type=secret,id=m2settings,target=/usr/share/maven/ref/settings-docker.xml if [[ "$MVN_ARGS" != "deploy" || "$(arch)" = "x86_64" ]]; then mvn $MVN_ARGS_SETTINGS $MVN_ARGS; else mvn $MVN_ARGS_SETTINGS install; fi

RUN cp ./distro/target/sdk-distro/web/openmrs_core/openmrs.war /openmrs/distribution/openmrs_core/
RUN cp ./distro/target/sdk-distro/web/openmrs-distro.properties /openmrs/distribution/
RUN cp -R ./distro/target/sdk-distro/web/openmrs_modules /openmrs/distribution/openmrs_modules/
RUN cp -R ./distro/target/sdk-distro/web/openmrs_owas /openmrs/distribution/openmrs_owas/
RUN cp -R ./distro/target/sdk-distro/web/openmrs_config /openmrs/distribution/openmrs_config/

### Site Dev Layer
# This layer builds a site-specific distribution
FROM base AS site_dev

ARG MVN_PROJECT="distro"
ARG MVN_ARGS_SETTINGS="-s /usr/share/maven/ref/settings-docker.xml -U -P $MVN_PROJECT"
ARG MVN_ARGS="install"

COPY pom.xml .
COPY sites/$MVN_PROJECT sites/$MVN_PROJECT

RUN --mount=type=secret,id=m2settings,target=/usr/share/maven/ref/settings-docker.xml if [[ "$MVN_ARGS" != "deploy" || "$(arch)" = "x86_64" ]]; then mvn $MVN_ARGS_SETTINGS $MVN_ARGS; else mvn $MVN_ARGS_SETTINGS install; fi

RUN cp ./sites/$MVN_PROJECT/target/sdk-distro/web/openmrs_core/openmrs.war /openmrs/distribution/openmrs_core/
RUN cp ./sites/$MVN_PROJECT/target/sdk-distro/web/openmrs-distro.properties /openmrs/distribution/
RUN cp -R ./sites/$MVN_PROJECT/target/sdk-distro/web/openmrs_modules /openmrs/distribution/openmrs_modules/
RUN cp -R ./sites/$MVN_PROJECT/target/sdk-distro/web/openmrs_owas /openmrs/distribution/openmrs_owas/
RUN cp -R ./sites/$MVN_PROJECT/target/sdk-distro/web/openmrs_config /openmrs/distribution/openmrs_config/

### Unified Dev Stage
# This selects the correct dev stage for this build based on the BUILD_TYPE
FROM ${BUILD_TYPE}_dev AS dev

### Run Stage
FROM openmrs/openmrs-core:2.7.x-amazoncorretto-17

COPY --from=dev /openmrs/distribution/openmrs_core/openmrs.war /openmrs/distribution/openmrs_core/
COPY --from=dev /openmrs/distribution/openmrs-distro.properties /openmrs/distribution/
COPY --from=dev /openmrs/distribution/openmrs_modules /openmrs/distribution/openmrs_modules
COPY --from=dev /openmrs/distribution/openmrs_owas /openmrs/distribution/openmrs_owas
COPY --from=dev /openmrs/distribution/openmrs_config /openmrs/distribution/openmrs_config
