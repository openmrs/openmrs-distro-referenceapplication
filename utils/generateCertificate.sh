#!/bin/bash

set -e  # Detiene el script si ocurre un error

# Set default values
CERT_NAME="sihsalus-certificate"
DAYS_VALID=365
KEY_SIZE=2048
SERVER_IP=192.168.0.200
SSL_DIR="./gateway/ssl"

# Updating and checking for OpenSSL
echo "[INFO] Updating packages and installing openssl..."
sudo apt update && sudo apt install -y openssl
command -v openssl >/dev/null 2>&1 || { echo "[ERROR] OpenSSL is not installed or not found in PATH"; exit 1; }

# Erase previous certificates
echo "[INFO] Removing previous certificates in ${SSL_DIR}..."
rm -rf "${SSL_DIR}"

# Creating directory to save certificates
echo "[INFO] Creating directory ${SSL_DIR}..."
mkdir -p "${SSL_DIR}" || { echo "[ERROR] Failed to create directory ${SSL_DIR}"; exit 1; }

# Generate private key and certificate
echo "[INFO] Generating private key and certificate..."
openssl req -x509 -nodes -days "${DAYS_VALID}" -newkey rsa:"${KEY_SIZE}" \
  -keyout "${SSL_DIR}/${CERT_NAME}.key" \
  -out "${SSL_DIR}/${CERT_NAME}.crt" \
  -subj "/C=PE/ST=Loreto/L=Maynas/O=Centro de Salud Santa Clotilde/OU=sihsalus/CN=sihsalus.hsc" \
  -addext "subjectAltName = DNS:localhost, DNS:openmrs.sihsalus.hsc, IP:127.0.0.1, IP:${SERVER_IP}"

# Check if the files were created
if [[ -f "${SSL_DIR}/${CERT_NAME}.key" && -f "${SSL_DIR}/${CERT_NAME}.crt" ]]; then
  echo "[SUCCESS] Certificate and key generated successfully:"
  ls -l "${SSL_DIR}"
else
  echo "[ERROR] Certificate or key file not found in ${SSL_DIR}"
  exit 1
fi
