#!/bin/sh
#	This Source Code Form is subject to the terms of the Mozilla Public License,
#	v. 2.0. If a copy of the MPL was not distributed with this file, You can
#	obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
#	the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
#
#	Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
#	graphic logo is a trademark of OpenMRS Inc.
#
#   Adapted for sihsalus - Self-signed certificate management for internal hospital networks

set -e

# Logging helpers
log_info() {
    echo "[$(date +'%Y-%m-%d %H:%M:%S')] [INFO] $*"
}

log_warn() {
    echo "[$(date +'%Y-%m-%d %H:%M:%S')] [WARN] $*" >&2
}

log_error() {
    echo "[$(date +'%Y-%m-%d %H:%M:%S')] [ERROR] $*" >&2
}

# Configuration with enhanced defaults
SSL_MODE=${SSL_MODE:-dev}
CERT_TEMP_CERT_DAYS=${CERT_TEMP_CERT_DAYS:-365}
CERT_RENEWAL_INTERVAL=${CERT_RENEWAL_INTERVAL:-30d}
CERT_RSA_KEY_SIZE=${CERT_RSA_KEY_SIZE:-4096}
CERT_ORG=${CERT_ORG:-"Centro de Salud Santa Clotilde"}
CERT_OU=${CERT_OU:-"sihsalus"}
CERT_COUNTRY=${CERT_COUNTRY:-"PE"}
CERT_STATE=${CERT_STATE:-"Loreto"}
CERT_LOCALITY=${CERT_LOCALITY:-"Maynas"}

# Parse domain list
OLD_IFS="$IFS"
IFS=","
set -- ${CERT_WEB_DOMAINS}
IFS=${OLD_IFS}

FIRST_DOMAIN="$1"
CERT_WEB_DOMAIN_COMMON_NAME="${CERT_WEB_DOMAIN_COMMON_NAME:-$FIRST_DOMAIN}"

log_info "=== sihsalus Certificate Manager ==="
log_info "SSL Mode: ${SSL_MODE}"
log_info "Primary domain: ${CERT_WEB_DOMAIN_COMMON_NAME}"
log_info "RSA Key Size: ${CERT_RSA_KEY_SIZE} bits"
log_info "Certificate validity: ${CERT_TEMP_CERT_DAYS} days"

# Check if certificates already exist
if [ -f "${CERT_ROOT_PATH}/live/${CERT_WEB_DOMAIN_COMMON_NAME}/fullchain.pem" ]; then
    log_info "Certificates already exist for ${CERT_WEB_DOMAIN_COMMON_NAME}"

    # Check certificate expiration
    CERT_FILE="${CERT_ROOT_PATH}/live/${CERT_WEB_DOMAIN_COMMON_NAME}/fullchain.pem"
    EXPIRY_DATE=$(openssl x509 -enddate -noout -in "${CERT_FILE}" | cut -d= -f2)
    EXPIRY_EPOCH=$(date -d "${EXPIRY_DATE}" +%s)
    CURRENT_EPOCH=$(date +%s)
    DAYS_UNTIL_EXPIRY=$(( (EXPIRY_EPOCH - CURRENT_EPOCH) / 86400 ))

    log_info "Certificate expires in ${DAYS_UNTIL_EXPIRY} days"

    # Renew if less than 30 days remaining
    if [ "${DAYS_UNTIL_EXPIRY}" -lt 30 ]; then
        log_warn "Certificate expiring soon, regenerating..."
        rm -rf "${CERT_ROOT_PATH}/live/${CERT_WEB_DOMAIN_COMMON_NAME}"
    else
        if [ "${SSL_MODE}" = "prod" ]; then
            log_info "Starting certificate renewal daemon..."
            trap 'log_info "Received SIGTERM, shutting down..."; exit 0' TERM
            while :; do
                # Check expiration and renew if needed
                EXPIRY_DATE=$(openssl x509 -enddate -noout -in "${CERT_FILE}" | cut -d= -f2)
                EXPIRY_EPOCH=$(date -d "${EXPIRY_DATE}" +%s)
                CURRENT_EPOCH=$(date +%s)
                DAYS_UNTIL_EXPIRY=$(( (EXPIRY_EPOCH - CURRENT_EPOCH) / 86400 ))

                if [ "${DAYS_UNTIL_EXPIRY}" -lt 30 ]; then
                    log_info "Renewing certificate..."
                    rm -rf "${CERT_ROOT_PATH}/live/${CERT_WEB_DOMAIN_COMMON_NAME}"
                    # Script will restart and regenerate
                    exec "$0" "$@"
                else
                    log_info "Certificate valid for ${DAYS_UNTIL_EXPIRY} more days"
                fi

                sleep "${CERT_RENEWAL_INTERVAL}" &
                wait $!
            done
        else
            log_info "Dev mode: certificates exist and valid. Exiting."
            exit 0
        fi
    fi
fi

# Initial certificate setup
log_info "Setting up initial certificates..."
log_info "=== Self-Signed Certificate Mode ==="
log_info "For internal hospital networks and development"

mkdir -p "${CERT_ROOT_PATH}/live/${CERT_WEB_DOMAIN_COMMON_NAME}"
mkdir -p "${CERTBOT_DATA_PATH}/conf"

# Create enhanced SSL config files
if [ ! -e "${CERTBOT_DATA_PATH}/conf/options-ssl-nginx.conf" ]; then
    log_info "Creating enhanced SSL configuration..."
    cat > "${CERTBOT_DATA_PATH}/conf/options-ssl-nginx.conf" <<'EOF'
# Enhanced SSL configuration for self-signed certificates
# Based on Mozilla Intermediate configuration
ssl_session_cache shared:SSL:10m;
ssl_session_timeout 1440m;
ssl_session_tickets off;

# Protocols - Only TLS 1.2 and 1.3
ssl_protocols TLSv1.2 TLSv1.3;
ssl_prefer_server_ciphers off;

# Modern cipher suite
ssl_ciphers ECDHE-ECDSA-AES128-GCM-SHA256:ECDHE-RSA-AES128-GCM-SHA256:ECDHE-ECDSA-AES256-GCM-SHA384:ECDHE-RSA-AES256-GCM-SHA384:ECDHE-ECDSA-CHACHA20-POLY1305:ECDHE-RSA-CHACHA20-POLY1305:DHE-RSA-AES128-GCM-SHA256:DHE-RSA-AES256-GCM-SHA384;

# OCSP Stapling - disabled for self-signed
# ssl_stapling on;
# ssl_stapling_verify on;
EOF
fi

# Create strong DH parameters
if [ ! -e "${CERTBOT_DATA_PATH}/conf/ssl-dhparams.pem" ]; then
    log_info "Creating strong DH parameters (${CERT_RSA_KEY_SIZE} bits)..."
    log_info "This may take several minutes..."
    openssl dhparam -out "${CERTBOT_DATA_PATH}/conf/ssl-dhparams.pem" ${CERT_RSA_KEY_SIZE}
    log_info "DH parameters created successfully!"
fi

# Build subject alternative names
DNS_NUM=0
IP_NUM=0
SUBJECT_ALTERNATE_NAMES=""

for WEB_DOMAIN in "$@"; do
    if expr "X$WEB_DOMAIN" : 'X[.0-9]+' >/dev/null; then
        SUBJECT_ALTERNATE_NAMES="${SUBJECT_ALTERNATE_NAMES}IP.${IP_NUM} = ${WEB_DOMAIN}
"
        IP_NUM=$((IP_NUM + 1))
        log_info "  - IP: ${WEB_DOMAIN}"
    else
        SUBJECT_ALTERNATE_NAMES="${SUBJECT_ALTERNATE_NAMES}DNS.${DNS_NUM} = ${WEB_DOMAIN}
"
        DNS_NUM=$((DNS_NUM + 1))
        log_info "  - Domain: ${WEB_DOMAIN}"
    fi
done

# Create enhanced SSL config with extended key usage
cat > /tmp/sslconfig.conf <<EOF
[req]
distinguished_name = req_distinguished_name
x509_extensions = v3_ca
prompt = no

[req_distinguished_name]
C = ${CERT_COUNTRY}
ST = ${CERT_STATE}
L = ${CERT_LOCALITY}
O = ${CERT_ORG}
OU = ${CERT_OU}
CN = ${CERT_WEB_DOMAIN_COMMON_NAME}

[v3_ca]
subjectAltName = @alternate_names
basicConstraints = critical, CA:TRUE
keyUsage = critical, digitalSignature, keyCertSign, keyEncipherment
extendedKeyUsage = serverAuth

[alternate_names]
${SUBJECT_ALTERNATE_NAMES}
EOF

log_info "Creating self-signed certificate for ${CERT_WEB_DOMAIN_COMMON_NAME}..."
log_info "Organization: ${CERT_ORG}"
log_info "Organizational Unit: ${CERT_OU}"
log_info "Subject Alternative Names:"

openssl req -x509 -nodes -newkey "rsa:${CERT_RSA_KEY_SIZE}" -days "${CERT_TEMP_CERT_DAYS}" \
    -keyout "${CERT_ROOT_PATH}/live/${CERT_WEB_DOMAIN_COMMON_NAME}/privkey.pem" \
    -out "${CERT_ROOT_PATH}/live/${CERT_WEB_DOMAIN_COMMON_NAME}/fullchain.pem" \
    -config /tmp/sslconfig.conf

# Set proper permissions
chmod 600 "${CERT_ROOT_PATH}/live/${CERT_WEB_DOMAIN_COMMON_NAME}/privkey.pem"
chmod 644 "${CERT_ROOT_PATH}/live/${CERT_WEB_DOMAIN_COMMON_NAME}/fullchain.pem"

log_info "✓ Self-signed certificates created successfully!"
log_info "✓ Certificate fingerprint:"
openssl x509 -noout -fingerprint -sha256 -in "${CERT_ROOT_PATH}/live/${CERT_WEB_DOMAIN_COMMON_NAME}/fullchain.pem"

log_info ""
log_info "IMPORTANT: Certificate Installation Instructions"
log_info "================================================"
log_info "1. Download the certificate from: ${CERT_ROOT_PATH}/live/${CERT_WEB_DOMAIN_COMMON_NAME}/fullchain.pem"
log_info "2. Install it in your browser/system trust store"
log_info "3. For Windows: Import to 'Trusted Root Certification Authorities'"
log_info "4. For Linux: Copy to /usr/local/share/ca-certificates/ and run update-ca-certificates"
log_info "5. For macOS: Import to Keychain Access and set to 'Always Trust'"
log_info ""

# Signal nginx to reload with the certificate
if [ -d "${CERTBOT_DATA_PATH}" ]; then
    log_info "Signaling nginx to reload configuration..."
    touch "${CERTBOT_DATA_PATH}/.reload-nginx"
fi

# Start renewal daemon if in production mode
if [ "${SSL_MODE}" = "prod" ]; then
    log_info "Starting certificate renewal daemon..."
    log_info "Checking for renewal every ${CERT_RENEWAL_INTERVAL}"
    trap 'log_info "Received SIGTERM, shutting down..."; exit 0' TERM
    while :; do
        sleep "${CERT_RENEWAL_INTERVAL}" &
        wait $!

        # Check expiration
        CERT_FILE="${CERT_ROOT_PATH}/live/${CERT_WEB_DOMAIN_COMMON_NAME}/fullchain.pem"
        if [ -f "${CERT_FILE}" ]; then
            EXPIRY_DATE=$(openssl x509 -enddate -noout -in "${CERT_FILE}" | cut -d= -f2)
            EXPIRY_EPOCH=$(date -d "${EXPIRY_DATE}" +%s)
            CURRENT_EPOCH=$(date +%s)
            DAYS_UNTIL_EXPIRY=$(( (EXPIRY_EPOCH - CURRENT_EPOCH) / 86400 ))

            log_info "Certificate check: ${DAYS_UNTIL_EXPIRY} days until expiry"

            if [ "${DAYS_UNTIL_EXPIRY}" -lt 30 ]; then
                log_info "Certificate expiring soon, regenerating..."
                rm -rf "${CERT_ROOT_PATH}/live/${CERT_WEB_DOMAIN_COMMON_NAME}"
                # Script will restart and regenerate
                exec "$0" "$@"
            fi
        fi
    done
else
    log_info "Dev mode: Certificate generated. Exiting."
fi
