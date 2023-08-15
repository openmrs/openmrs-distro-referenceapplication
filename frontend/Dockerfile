# syntax=docker/dockerfile:1.3
FROM --platform=$BUILDPLATFORM node:18-alpine as dev

ARG APP_SHELL_VERSION=next

RUN mkdir -p /app
WORKDIR /app

COPY spa-build-config.json .

ARG CACHE_BUST
RUN npx --legacy-peer-deps openmrs@${APP_SHELL_VERSION:-next} assemble --manifest --mode config --config spa-build-config.json --target ./spa
RUN npx --legacy-peer-deps openmrs@${APP_SHELL_VERSION:-next} build --build-config spa-build-config.json --target ./spa
COPY ohri-config.json ./spa/ohri-config.json
COPY ohri_logo_dark.svg ./spa/ohri_logo_dark.svg
COPY ohri_logo_light.svg ./spa/ohri_logo_light.svg

FROM nginx:1.23-alpine

RUN apk update && \
    apk upgrade && \
    # add more utils for sponge to support our startup script
    apk add --no-cache moreutils

# clear any default files installed by nginx
RUN rm -rf /usr/share/nginx/html/*

COPY startup.sh /usr/local/bin/startup.sh
RUN chmod +x /usr/local/bin/startup.sh

COPY nginx.conf /etc/nginx/nginx.conf

COPY --from=dev /app/spa /usr/share/nginx/html

CMD ["/usr/local/bin/startup.sh"]
