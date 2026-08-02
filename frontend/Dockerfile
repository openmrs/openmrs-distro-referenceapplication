# syntax=docker/dockerfile:1.3
#--------------------------------------------
# Dev Stage - Assembles and Builds Frontend
#--------------------------------------------
FROM --platform=$BUILDPLATFORM node:22-alpine as dev

ARG APP_SHELL_VERSION=next

RUN mkdir -p /app
WORKDIR /app

COPY spa-assemble-config.json .
COPY spa-build-config.json .
COPY esm-labtestreport-app ./esm-labtestreport-app/
COPY esm-stock-management-app-patched ./esm-stock-management-app-patched/
COPY esm-patient-registration-app-patched ./esm-patient-registration-app-patched/

RUN cd esm-labtestreport-app && npm install && npm run build && npm pack --pack-destination /app

RUN cd esm-patient-registration-app-patched && npm install --legacy-peer-deps && npm run build && npm pack --pack-destination /app

RUN corepack enable && \
    cd esm-stock-management-app-patched && \
    yarn install && \
    yarn build && \
    yarn pack --out /app/esm-stock-management-app-patched.tgz

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
