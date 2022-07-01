# syntax=docker/dockerfile:1.3
FROM nginx:1.21-alpine AS base

RUN apk update && \
    apk upgrade && \
    # add more utils for sponge to support our startup script
    apk add --no-cache moreutils

# clear any default files installed by nginx
RUN rm -rf /usr/share/nginx/html/*

COPY startup.sh /usr/local/bin/startup.sh
RUN chmod +x /usr/local/bin/startup.sh

COPY nginx.conf /etc/nginx/nginx.conf

CMD ["/usr/local/bin/startup.sh"]

FROM base AS embedded
COPY --from=openmrs/3-reference-distro:latest /openmrs/spa /usr/share/nginx/html
