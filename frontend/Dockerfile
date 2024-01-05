# syntax=docker/dockerfile:1.3
ARG NODE_VERSION=18
ARG APP_SHELL_VERSION=next

FROM --platform=$BUILDPLATFORM node:${NODE_VERSION}-alpine as dev

RUN mkdir -p /app
WORKDIR /app

COPY spa-build-config.json .

ARG CACHE_BUST
RUN npx --legacy-peer-deps openmrs@${APP_SHELL_VERSION:-next} assemble --manifest --mode config --config spa-build-config.json --target ./spa
RUN npx --legacy-peer-deps openmrs@${APP_SHELL_VERSION:-next} build --build-config spa-build-config.json --target ./spa
RUN if [ ! -f ./spa/index.html ]; then echo 'Build failed. Please check the logs above for details. This may have happened because of an update to a library that OpenMRS depends on.' >&2 ; exit 1; fi

FROM --platform=$BUILDPLATFORM nginx:1.25-alpine

RUN apk update && \
    apk upgrade && \
    # add more utils for sponge to support our startup script
    apk add --no-cache moreutils perl-utils

# clear any default files installed by nginx
RUN rm -rf /usr/share/nginx/html/*

ENV APP_SHELL_VERSION ${APP_SHELL_VERSION:-next}

# the default nginx image doesn't have Node or NPM, so we copy it from the dev image
COPY --from=dev /usr/lib /usr/lib
COPY --from=dev /usr/local/lib /usr/local/lib
COPY --from=dev /usr/local/include /usr/local/include
COPY --from=dev /usr/local/bin /usr/local/bin

COPY startup.sh /usr/local/bin/startup.sh
RUN chmod +x /usr/local/bin/startup.sh

COPY nginx.conf /etc/nginx/nginx.conf

# allow updating by overwriting spa-build-config.json
RUN mkdir -p /openmrs
COPY --from=dev /app/spa-build-config.json /openmrs/spa-build-config.json
WORKDIR /openmrs
RUN shasum -a 512 spa-build-config.json | tee spa-build-config.json.sha512sum
WORKDIR /

COPY --from=dev /app/spa /usr/share/nginx/html

CMD ["/usr/local/bin/startup.sh"]
