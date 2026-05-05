# syntax=docker/dockerfile:1.3
FROM nginx:stable-alpine

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
COPY default.conf.template /etc/nginx/conf-templates/default.conf.template
COPY default-ssl.conf.template /etc/nginx/conf-templates/default-ssl.conf.template

# Copy scripts
COPY watch-certs.sh /usr/local/bin/watch-certs.sh
COPY docker-entrypoint.sh /usr/local/bin/custom-entrypoint.sh
RUN chmod +x /usr/local/bin/watch-certs.sh /usr/local/bin/custom-entrypoint.sh

# Use custom entrypoint that wraps the nginx entrypoint
ENTRYPOINT ["/usr/local/bin/custom-entrypoint.sh"]
CMD ["nginx", "-g", "daemon off;"]
