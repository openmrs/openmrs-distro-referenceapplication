#!/bin/sh
#	This Source Code Form is subject to the terms of the Mozilla Public License,
#	v. 2.0. If a copy of the MPL was not distributed with this file, You can
#	obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
#	the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
#
#	Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
#	graphic logo is a trademark of OpenMRS Inc.

set -e

SSL_MODE=${SSL_MODE:-dev}
CERT_TEMP_CERT_DAYS=${CERT_TEMP_CERT_DAYS:-90}

# Parse domain list
OLD_IFS="$IFS"
IFS=","
set -- ${CERT_WEB_DOMAINS}
IFS=${OLD_IFS}

FIRST_DOMAIN="$1"
CERT_WEB_DOMAIN_COMMON_NAME=${CERT_WEB_DOMAIN_COMMON_NAME:-$FIRST_DOMAIN}

echo "SSL Mode: ${SSL_MODE}"
echo "Primary domain: ${CERT_WEB_DOMAIN_COMMON_NAME}"

# Check if certificates already exist
if [ -f "${CERT_ROOT_PATH}/live/${CERT_WEB_DOMAIN_COMMON_NAME}/fullchain.pem" ]; then
    echo "Certificates already exist for ${CERT_WEB_DOMAIN_COMMON_NAME}"

    if [ "${SSL_MODE}" = "prod" ]; then
        echo "Starting certificate renewal daemon..."
        trap exit TERM
        while :; do
            certbot renew --webroot -w ${CERTBOT_DATA_PATH}
            sleep 12h &
            wait $!
        done
    else
        echo "Dev mode: certificates exist, nothing to do. Exiting."
        exit 0
    fi
fi

# Initial certificate setup
echo "Setting up initial certificates..."

if [ "${SSL_MODE}" = "dev" ]; then
    echo "=== Development Mode: Creating self-signed certificates ==="

    mkdir -p "${CERT_ROOT_PATH}/live/${CERT_WEB_DOMAIN_COMMON_NAME}"
    mkdir -p "${CERTBOT_DATA_PATH}/conf"

    # Create minimal SSL config files for dev mode
    if [ ! -e "${CERTBOT_DATA_PATH}/conf/options-ssl-nginx.conf" ]; then
        echo "Creating minimal SSL configuration for development..."
        cat > "${CERTBOT_DATA_PATH}/conf/options-ssl-nginx.conf" <<EOF
# Minimal SSL configuration for development
ssl_session_cache shared:le_nginx_SSL:10m;
ssl_session_timeout 1440m;
ssl_session_tickets off;
ssl_protocols TLSv1.2 TLSv1.3;
ssl_prefer_server_ciphers off;
EOF
    fi

    # Create minimal DH parameters for dev mode (or skip if exists)
    if [ ! -e "${CERTBOT_DATA_PATH}/conf/ssl-dhparams.pem" ]; then
        echo "Creating minimal DH parameters for development..."
        # Use a small DH param for dev to avoid long generation times
        openssl dhparam -out "${CERTBOT_DATA_PATH}/conf/ssl-dhparams.pem" 2048
    fi

    # Build subject alternative names
    SUBJECT_ALT_NAME_ARG=""
    DNS_NUM=0
    IP_NUM=0

    for WEB_DOMAIN in "$@"; do
        if expr "X$WEB_DOMAIN" : 'X[.0-9]+' >/dev/null; then
            echo "IP.${IP_NUM} = ${WEB_DOMAIN}" >> /tmp/sslconfig.conf
            IP_NUM=$((IP_NUM + 1))
        else
            echo "DNS.${DNS_NUM} = ${WEB_DOMAIN}" >> /tmp/sslconfig.conf
            DNS_NUM=$((DNS_NUM + 1))
        fi
    done

    # Create SSL config
    cat > /tmp/sslconfig.conf <<EOF
[req]
distinguished_name=req_distinguished_name
x509_extensions = v3_ca
[req_distinguished_name]
CN = ${CERT_WEB_DOMAIN_COMMON_NAME}
[v3_ca]
subjectAltName=@alternate_names
[alternate_names]
$(cat /tmp/sslconfig.conf)
EOF

    echo "Creating self-signed certificate for ${CERT_WEB_DOMAIN_COMMON_NAME}..."
    openssl req -x509 -nodes -newkey rsa:${CERT_RSA_KEY_SIZE} -days ${CERT_TEMP_CERT_DAYS} \
        -keyout "${CERT_ROOT_PATH}/live/${CERT_WEB_DOMAIN_COMMON_NAME}/privkey.pem" \
        -out "${CERT_ROOT_PATH}/live/${CERT_WEB_DOMAIN_COMMON_NAME}/fullchain.pem" \
        -subj "/CN=${CERT_WEB_DOMAIN_COMMON_NAME}" \
        -config /tmp/sslconfig.conf

    echo "Self-signed certificates created successfully!"
    echo "Note: You will need to accept the certificate warning in your browser."

else
    echo "=== Production Mode: Setting up Let's Encrypt certificates ==="

    mkdir -p "${CERTBOT_DATA_PATH}/conf"
    mkdir -p "${CERT_ROOT_PATH}/live/${CERT_WEB_DOMAIN_COMMON_NAME}"

    # Download recommended TLS parameters if needed
    if [ ! -e "${CERTBOT_DATA_PATH}/conf/options-ssl-nginx.conf" ]; then
        echo "Downloading recommended TLS parameters..."
        curl -fsSL -o "${CERTBOT_DATA_PATH}/conf/options-ssl-nginx.conf" \
            https://raw.githubusercontent.com/certbot/certbot/master/certbot-nginx/certbot_nginx/_internal/tls_configs/options-ssl-nginx.conf
        curl -fsSL -o "${CERTBOT_DATA_PATH}/conf/ssl-dhparams.pem" \
            https://raw.githubusercontent.com/certbot/certbot/master/certbot/certbot/ssl-dhparams.pem
    fi

    # Create dummy certificate to allow nginx to start
    echo "Creating dummy certificate to bootstrap nginx..."
    openssl req -x509 -nodes -newkey rsa:2048 -days 1 \
        -keyout "${CERT_ROOT_PATH}/live/${CERT_WEB_DOMAIN_COMMON_NAME}/privkey.pem" \
        -out "${CERT_ROOT_PATH}/live/${CERT_WEB_DOMAIN_COMMON_NAME}/fullchain.pem" \
        -subj "/CN=${CERT_WEB_DOMAIN_COMMON_NAME}"

    echo "Dummy certificate created. Waiting for nginx to start..."
    sleep 10

    # Wait for nginx to be ready
    until wget --spider -q http://gateway/.well-known/acme-challenge/ 2>/dev/null; do
        echo "Waiting for nginx gateway to be ready..."
        sleep 5
    done

    echo "Nginx is ready. Requesting Let's Encrypt certificate..."

    # Delete dummy certificate
    rm -rf "${CERT_ROOT_PATH}/live/${CERT_WEB_DOMAIN_COMMON_NAME}"
    rm -rf "${CERT_ROOT_PATH}/archive/${CERT_WEB_DOMAIN_COMMON_NAME}"
    rm -f "${CERT_ROOT_PATH}/renewal/${CERT_WEB_DOMAIN_COMMON_NAME}.conf"

    # Build domain arguments
    DOMAIN_ARGS=""
    for CERT_WEB_DOMAIN in "$@"; do
        DOMAIN_ARGS="${DOMAIN_ARGS} -d ${CERT_WEB_DOMAIN}"
    done

    # Select appropriate email arg
    case "${CERT_CONTACT_EMAIL}" in
    "") EMAIL_ARG="--register-unsafely-without-email" ;;
    *) EMAIL_ARG="--email ${CERT_CONTACT_EMAIL}" ;;
    esac

    # Enable staging mode if requested
    STAGING_ARG=""
    if [ "${SSL_STAGING:-false}" = "true" ]; then
        echo "Using Let's Encrypt staging environment (for testing)"
        STAGING_ARG="--staging"
    fi

    # Request certificate
    certbot certonly --webroot -w ${CERTBOT_DATA_PATH} \
        ${STAGING_ARG} \
        ${EMAIL_ARG} \
        ${DOMAIN_ARGS} \
        --rsa-key-size ${CERT_RSA_KEY_SIZE} \
        --agree-tos \
        --no-eff-email

    echo "Let's Encrypt certificate obtained successfully!"

    # Reload nginx to use the real certificate
    echo "Reloading nginx with real certificate..."
    docker compose exec gateway nginx -s reload || true

    # Start renewal daemon
    echo "Starting certificate renewal daemon..."
    trap exit TERM
    while :; do
        certbot renew --webroot -w ${CERTBOT_DATA_PATH}
        sleep 12h &
        wait $!
    done
fi
