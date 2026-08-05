# syntax=docker/dockerfile:1.3
#--------------------------------------------
# Dev Stage - Assembles and Builds Frontend
#--------------------------------------------
FROM --platform=$BUILDPLATFORM node:22-alpine as dev

ARG APP_SHELL_VERSION=next

RUN apk add --no-cache git

RUN mkdir -p /app
WORKDIR /app

COPY spa-assemble-config.json .
COPY spa-build-config.json .
COPY esm-labtestreport-app ./esm-labtestreport-app/
COPY esm-stock-management-app-patched ./esm-stock-management-app-patched/

RUN cd esm-labtestreport-app && npm install && npm run build && npm pack --pack-destination /app

RUN corepack enable && \
    cd esm-stock-management-app-patched && \
    yarn install && \
    yarn build && \
    yarn pack --out /app/esm-stock-management-app-patched.tgz

# Build @openmrs/esm-patient-registration-app from our fork instead of the
# published "next" package, so registration fixes ship as soon as they land
# on main.
ARG PATIENT_MANAGEMENT_REPO=https://github.com/PHCC-Openmrs/openmrs-esm-patient-management.git
ARG PATIENT_MANAGEMENT_REF=main
# Bump this (e.g. --build-arg PATIENT_MANAGEMENT_CACHE_BUST=$(date +%s)) to force
# a fresh clone; otherwise Docker has no way to know the remote branch moved and
# will happily reuse a stale cached clone.
ARG PATIENT_MANAGEMENT_CACHE_BUST=0
RUN echo "cache-bust=${PATIENT_MANAGEMENT_CACHE_BUST}" && \
    git clone --branch ${PATIENT_MANAGEMENT_REF} --depth 1 ${PATIENT_MANAGEMENT_REPO} openmrs-esm-patient-management && \
    corepack enable && \
    cd openmrs-esm-patient-management && \
    yarn install && \
    yarn workspace @openmrs/esm-patient-registration-app run build && \
    yarn workspace @openmrs/esm-patient-list-management-app run build && \
    (cd packages/esm-patient-registration-app && npm pack --pack-destination /app) && \
    (cd packages/esm-patient-list-management-app && npm pack --pack-destination /app)

# Build @openmrs/esm-patient-chart-app from our fork instead of the
# published "next" package.
ARG PATIENT_CHART_REPO=https://github.com/PHCC-Openmrs/openmrs-esm-patient-chart.git
ARG PATIENT_CHART_REF=main
ARG PATIENT_CHART_CACHE_BUST=0
RUN echo "cache-bust=${PATIENT_CHART_CACHE_BUST}" && \
    git clone --branch ${PATIENT_CHART_REF} --depth 1 ${PATIENT_CHART_REPO} openmrs-esm-patient-chart && \
    corepack enable && \
    cd openmrs-esm-patient-chart && \
    yarn install && \
    yarn workspace @openmrs/esm-patient-chart-app run build && \
    (cd packages/esm-patient-chart-app && npm pack --pack-destination /app)

# Build @openmrs/esm-login-app from our fork instead of the published "next"
# package, to remove the "Built with OpenMRS" footer.
ARG ESM_CORE_REPO=https://github.com/PHCC-Openmrs/openmrs-esm-core.git
ARG ESM_CORE_REF=remove-openmrs-footer
ARG ESM_CORE_CACHE_BUST=0
RUN echo "cache-bust=${ESM_CORE_CACHE_BUST}" && \
    git clone --branch ${ESM_CORE_REF} --depth 1 ${ESM_CORE_REPO} openmrs-esm-core && \
    corepack enable && \
    cd openmrs-esm-core && \
    yarn install && \
    npx turbo run build --filter=...@openmrs/esm-login-app && \
    (cd packages/apps/esm-login-app && npm pack --pack-destination /app)

ARG CACHE_BUST
RUN npx --legacy-peer-deps openmrs@${APP_SHELL_VERSION:-next} assemble --manifest --mode config --config spa-assemble-config.json --target ./spa
RUN npx --legacy-peer-deps openmrs@${APP_SHELL_VERSION:-next} build --build-config spa-build-config.json --target ./spa
RUN if [ ! -f ./spa/index.html ]; then echo 'Build failed. Please check the logs above for details. This may have happened because of an update to a library that OpenMRS depends on.'; exit 1; fi

#--------------------------------------------
# Runtime Stage - Published Image
#--------------------------------------------
FROM nginx:1.31-alpine

RUN apk update && \
    apk upgrade && \
    # add more utils for sponge and envsubst
    apk add --no-cache moreutils

# clear any default files installed by nginx
RUN rm -rf /usr/share/nginx/html/*

COPY startup.sh /usr/local/bin/startup.sh
RUN chmod +x /usr/local/bin/startup.sh

COPY nginx.conf /etc/nginx/nginx.conf

COPY --from=dev /app/spa /usr/share/nginx/html
COPY config-core_demo.json /usr/share/nginx/html

CMD ["/usr/local/bin/startup.sh"]
