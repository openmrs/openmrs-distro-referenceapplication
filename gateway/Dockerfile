# syntax=docker/dockerfile:1.3
FROM nginx:stable-alpine

# Patch all Alpine packages to eliminate known CVEs in the base image
RUN apk upgrade --no-cache && \
    apk add --no-cache curl

# Set default environment variables
ENV FRAME_ANCESTORS="https://dyaku.minsa.gob.pe/fhir" \
    CERT_RELOAD_CHECK_INTERVAL=30

# Copy nginx base configuration
COPY nginx.conf /etc/nginx/nginx.conf

# Copy both config templates to conf-templates directory
# The entrypoint script will decide which one to use
RUN mkdir -p /etc/nginx/conf-templates
COPY default.conf.template /etc/nginx/conf-templates/default.conf.template
COPY default-ssl.conf.template /etc/nginx/conf-templates/default-ssl.conf.template

# Copy scripts
COPY watch-certs.sh /usr/local/bin/watch-certs.sh
COPY docker-entrypoint.sh /usr/local/bin/custom-entrypoint.sh
RUN chmod +x /usr/local/bin/watch-certs.sh /usr/local/bin/custom-entrypoint.sh

# Create directory for ACME challenges (for future Let's Encrypt support)
RUN mkdir -p /var/www/certbot

# Use custom entrypoint that wraps the nginx entrypoint
ENTRYPOINT ["/usr/local/bin/custom-entrypoint.sh"]
CMD ["nginx", "-g", "daemon off;"]
