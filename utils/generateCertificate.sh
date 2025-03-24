#!/bin/bash

# Set default values
CERT_NAME="sihsalus-certificate"
DAYS_VALID=365
KEY_SIZE=2048
SERVER_IP=192.168.0.200

# Updating and check last version of openssl
apt-get update && apt-get install -y openssl

# Erase previous certificates
rm -rf ../gateway/ssl

# Creating directory to save certificates
mkdir -p ../gateway/ssl 

# Generate private key
echo "Generating private key and crt with prefix: ${CERT_NAME} ..."
openssl req -x509 -nodes -days 365 -newkey rsa:2048 -keyout ../gateway/ssl/${CERT_NAME}.key -out ../gateway/ssl/${CERT_NAME}.crt -subj "/C=PE/ST=Loreto/L=Maynas/O=Centro de Salud Santa Clotilde/OU=sihsalus/CN=sihsalus-hsc-server" -addext "subjectAltName = DNS:localhost, DNS:sihsalus.hsc, DNS:openmrs.sihsalus.hsc, IP:127.0.0.1, IP:${SERVER_IP}"  # Replace with your LAN IP

echo "Certificate generated successfully:"

# Display certificate details
# echo -e "\n🔍 Certificate Details:"
# openssl x509 -in "./ssl/${CERT_NAME}.crt" -text -noout -check

# Display private key details
# echo -e "\n🔑 Private Key Details:"
# openssl rsa -in "./ssl/${CERT_NAME}.key" -text -noout -check
