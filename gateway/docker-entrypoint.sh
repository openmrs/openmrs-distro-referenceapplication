#!/bin/sh
set -e

# Create templates directory if it doesn't exist
mkdir -p /etc/nginx/templates

# Derive CERT_WEB_DOMAIN_COMMON_NAME from first domain in CERT_WEB_DOMAINS if not explicitly set
if [ -z "${CERT_WEB_DOMAIN_COMMON_NAME}" ] && [ -n "${CERT_WEB_DOMAINS}" ]; then
    CERT_WEB_DOMAIN_COMMON_NAME=$(echo "${CERT_WEB_DOMAINS}" | cut -d',' -f1)
    export CERT_WEB_DOMAIN_COMMON_NAME
fi

# Determine which nginx config to use based on SSL environment variable
if [ -n "${CERT_WEB_DOMAIN_COMMON_NAME}" ]; then
    echo "SSL enabled: Waiting for certificates..."

    CERT_FILE="/etc/letsencrypt/live/${CERT_WEB_DOMAIN_COMMON_NAME}/fullchain.pem"
    KEY_FILE="/etc/letsencrypt/live/${CERT_WEB_DOMAIN_COMMON_NAME}/privkey.pem"
    DH_FILE="/var/www/certbot/conf/ssl-dhparams.pem"
    SSL_CONF="/var/www/certbot/conf/options-ssl-nginx.conf"
    MAX_WAIT=1800
    WAITED=0

    while [ ! -f "${CERT_FILE}" ] || [ ! -f "${KEY_FILE}" ] || \
          [ ! -f "${DH_FILE}" ] || [ ! -f "${SSL_CONF}" ]; do
        if [ ${WAITED} -ge ${MAX_WAIT} ]; then
            echo "ERROR: Timed out waiting for certificates after ${MAX_WAIT}s"
            exit 1
        fi
        echo "Waiting for certificates... (${WAITED}s)"
        sleep 5
        WAITED=$((WAITED + 5))
    done

    echo "Certificates found. Configuring SSL..."
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
