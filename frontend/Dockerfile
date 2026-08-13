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

# Build @openmrs/esm-patient-registration-app, @openmrs/esm-patient-list-management-app,
# AND @openmrs/esm-service-queues-app from our fork instead of the published "next"
# package, so fixes to any of them ship as soon as they land on main.
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
    yarn workspace @openmrs/esm-service-queues-app run build && \
    (cd packages/esm-patient-registration-app && npm pack --pack-destination /app) && \
    (cd packages/esm-patient-list-management-app && npm pack --pack-destination /app) && \
    (cd packages/esm-service-queues-app && npm pack --pack-destination /app)

# Build @openmrs/esm-patient-chart-app, @openmrs/esm-patient-programs-app,
# @openmrs/esm-patient-medications-app, @openmrs/esm-patient-orders-app, AND
# @openmrs/esm-patient-conditions-app from our fork instead of the published "next"
# package. esm-patient-chart-app is just the dashboard-shell package -- it does NOT
# depend on the others, so each widget package must be built and packed separately,
# and spa-assemble-config.json must point each at its own tarball (not "next") or
# none of this has any effect on what's actually served.
ARG PATIENT_CHART_REPO=https://github.com/PHCC-Openmrs/openmrs-esm-patient-chart.git
ARG PATIENT_CHART_REF=main
ARG PATIENT_CHART_CACHE_BUST=0
RUN echo "cache-bust=${PATIENT_CHART_CACHE_BUST}" && \
    git clone --branch ${PATIENT_CHART_REF} --depth 1 ${PATIENT_CHART_REPO} openmrs-esm-patient-chart && \
    corepack enable && \
    cd openmrs-esm-patient-chart && \
    yarn install && \
    yarn workspace @openmrs/esm-patient-chart-app run build && \
    yarn workspace @openmrs/esm-patient-programs-app run build && \
    yarn workspace @openmrs/esm-patient-medications-app run build && \
    yarn workspace @openmrs/esm-patient-orders-app run build && \
    yarn workspace @openmrs/esm-patient-conditions-app run build && \
    (cd packages/esm-patient-chart-app && npm pack --pack-destination /app) && \
    (cd packages/esm-patient-programs-app && npm pack --pack-destination /app) && \
    (cd packages/esm-patient-medications-app && npm pack --pack-destination /app) && \
    (cd packages/esm-patient-orders-app && npm pack --pack-destination /app) && \
    (cd packages/esm-patient-conditions-app && npm pack --pack-destination /app)

# Build @openmrs/esm-dispensing-app from our fork instead of the published "next"
# package, so the auto-complete-on-dispense and dispensed-quantity fixes ship.
ARG DISPENSING_REPO=https://github.com/PHCC-Openmrs/openmrs-esm-dispensing-app.git
ARG DISPENSING_REF=main
ARG DISPENSING_CACHE_BUST=0
RUN echo "cache-bust=${DISPENSING_CACHE_BUST}" && \
    git clone --branch ${DISPENSING_REF} --depth 1 ${DISPENSING_REPO} openmrs-esm-dispensing-app && \
    corepack enable && \
    cd openmrs-esm-dispensing-app && \
    yarn install && \
    yarn build && \
    yarn pack --out /app/esm-dispensing-app.tgz

# Build @openmrs/esm-login-app, @openmrs/esm-app-shell, AND @openmrs/esm-framework
# (which re-exports esm-offline) from our fork's cache-issue-fix branch instead of
# the published "next" packages. That branch removes the "Built with OpenMRS"
# footer, clears the service worker cache on logout, and fixes stale-content
# caching bugs in the app shell's service worker. esm-app-shell and esm-framework
# are pinned CLI dependencies (not entries in spa-assemble-config.json), so their
# builds are only packed here -- they get spliced into node_modules below.
ARG ESM_CORE_REPO=https://github.com/PHCC-Openmrs/openmrs-esm-core.git
ARG ESM_CORE_REF=cache-issue-fix
ARG ESM_CORE_CACHE_BUST=0
RUN echo "cache-bust=${ESM_CORE_CACHE_BUST}" && \
    git clone --branch ${ESM_CORE_REF} --depth 1 ${ESM_CORE_REPO} openmrs-esm-core && \
    corepack enable && \
    cd openmrs-esm-core && \
    yarn install && \
    npx turbo run build --filter=...@openmrs/esm-login-app --filter=...@openmrs/esm-app-shell --filter=...@openmrs/esm-framework && \
    (cd packages/apps/esm-login-app && npm pack --pack-destination /app)

# Build @openmrs/esm-styleguide from our fork's sidebar-fix branch instead of
# the published version, to fix the redundant/duplicate scrollbar that shows
# up in form workspaces (workspace header used box-sizing: content-box, so
# its 1px border wasn't accounted for in --desktop-workspace-window-height,
# leaving the workspace content 1px taller than its container).
ARG ESM_CORE_STYLEGUIDE_REPO=https://github.com/PHCC-Openmrs/openmrs-esm-core.git
ARG ESM_CORE_STYLEGUIDE_REF=sidebar-fix
ARG ESM_CORE_STYLEGUIDE_CACHE_BUST=0
RUN echo "cache-bust=${ESM_CORE_STYLEGUIDE_CACHE_BUST}" && \
    git clone --branch ${ESM_CORE_STYLEGUIDE_REF} --depth 1 ${ESM_CORE_STYLEGUIDE_REPO} openmrs-esm-core-styleguide && \
    corepack enable && \
    cd openmrs-esm-core-styleguide && \
    yarn install && \
    npx turbo run build --filter=...@openmrs/esm-styleguide

ARG CACHE_BUST
# Install the openmrs CLI locally (instead of via npx) so we know exactly
# where it puts its own pinned @openmrs/esm-app-shell, @openmrs/esm-framework,
# and @openmrs/esm-styleguide, and can overwrite them with our patched builds
# above before the shell itself gets compiled. None of these three are
# modules in spa-assemble-config.json -- the app shell is compiled directly
# by `openmrs build` from its own hard-pinned dependencies, so that config
# has no way to override them; this is the only place that actually takes
# effect.
RUN npm install --no-save --legacy-peer-deps openmrs@${APP_SHELL_VERSION:-next} && \
    STYLEGUIDE_DIR=$(find node_modules -type d -path '*@openmrs/esm-styleguide' | head -n1) && \
    echo "Found esm-styleguide at: ${STYLEGUIDE_DIR}" && \
    test -n "${STYLEGUIDE_DIR}" && \
    rm -rf "${STYLEGUIDE_DIR}/dist" && \
    cp -r openmrs-esm-core-styleguide/packages/framework/esm-styleguide/dist "${STYLEGUIDE_DIR}/dist" && \
    APP_SHELL_DIR=$(find node_modules -type d -path '*@openmrs/esm-app-shell' | head -n1) && \
    echo "Found esm-app-shell at: ${APP_SHELL_DIR}" && \
    test -n "${APP_SHELL_DIR}" && \
    rm -rf "${APP_SHELL_DIR}/dist" && \
    cp -r openmrs-esm-core/packages/shell/esm-app-shell/dist "${APP_SHELL_DIR}/dist" && \
    FRAMEWORK_DIR=$(find node_modules -type d -path '*@openmrs/esm-framework' | head -n1) && \
    echo "Found esm-framework at: ${FRAMEWORK_DIR}" && \
    test -n "${FRAMEWORK_DIR}" && \
    rm -rf "${FRAMEWORK_DIR}/dist" && \
    cp -r openmrs-esm-core/packages/framework/esm-framework/dist "${FRAMEWORK_DIR}/dist"
RUN node_modules/.bin/openmrs assemble --manifest --mode config --config spa-assemble-config.json --target ./spa
RUN node_modules/.bin/openmrs build --build-config spa-build-config.json --target ./spa
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
