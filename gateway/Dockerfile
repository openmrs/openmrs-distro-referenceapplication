# syntax=docker/dockerfile:1.3
FROM nginx:1.28-alpine

# Patch all Alpine packages to eliminate known CVEs in the base image,
# set up directories and permissions in a single layer.
RUN apk upgrade --no-cache && \
    mkdir -p /etc/nginx/conf-templates /var/www/certbot

# Set default environment variables
ENV FRAME_ANCESTORS="https://dyaku.minsa.gob.pe/fhir" \
    CERT_RELOAD_CHECK_INTERVAL=30

# Copy nginx base configuration
COPY nginx.conf /etc/nginx/nginx.conf

# Copy both config templates — entrypoint selects which one to use
COPY default.conf.template /etc/nginx/conf-templates/
COPY default-ssl.conf.template /etc/nginx/conf-templates/

# Copy scripts
COPY docker-entrypoint.sh /usr/local/bin/
COPY watch-certs.sh /usr/local/bin/
RUN chmod +x /usr/local/bin/docker-entrypoint.sh /usr/local/bin/watch-certs.sh

ENTRYPOINT ["/usr/local/bin/docker-entrypoint.sh"]
CMD ["nginx", "-g", "daemon off;"]
