#!/bin/sh
set -e

# Determine which nginx config to use based on SSL environment variable
if [ -n "${CERT_WEB_DOMAIN_COMMON_NAME}" ]; then
    echo "SSL enabled: Using SSL nginx configuration"
    cp /etc/nginx/conf-templates/default-ssl.conf.template /etc/nginx/templates/default.conf.template
else
    echo "SSL disabled: Using standard nginx configuration"
    cp /etc/nginx/conf-templates/default.conf.template /etc/nginx/templates/default.conf.template
fi

# Execute the original nginx entrypoint
exec /docker-entrypoint.sh "$@"
