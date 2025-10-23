#!/bin/sh
set -e

# Create templates directory if it doesn't exist
mkdir -p /etc/nginx/templates

# Determine which nginx config to use based on SSL environment variable
if [ -n "${CERT_WEB_DOMAIN_COMMON_NAME}" ]; then
    echo "SSL enabled: Using SSL nginx configuration"
    cp /etc/nginx/conf-templates/default-ssl.conf.template /etc/nginx/templates/default.conf.template

    # Start certificate reload watcher in background
    echo "Starting certificate reload watcher..."
    /usr/local/bin/watch-certs.sh &
else
    echo "SSL disabled: Using standard nginx configuration"
    cp /etc/nginx/conf-templates/default.conf.template /etc/nginx/templates/default.conf.template
fi

# Execute the original nginx entrypoint
exec /docker-entrypoint.sh "$@"
