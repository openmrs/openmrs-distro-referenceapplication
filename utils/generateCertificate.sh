#!/bin/bash

# Set default values
CERT_NAME="sihsalus-certificate"
DAYS_VALID=365
KEY_SIZE=2048
SERVER_IP=192.168.0.200

# Updating and check last version of openssl
sudo apt update && sudo apt install -y openssl

# Erase previous certificates
sudo rm -rf ../gateway/ssl

# Creating directory to save certificates
sudo mkdir -p ../gateway/ssl 

# Generate private key
echo "Generating private key and crt with prefix: ${CERT_NAME} ..."
openssl req -x509 -nodes -days ${DAYS_VALID} -newkey rsa:${KEY_SIZE} -keyout ../gateway/ssl/${CERT_NAME}.key -out ../gateway/ssl/${CERT_NAME}.crt -subj "/C=PE/ST=Loreto/L=Maynas/O=Centro de Salud Santa Clotilde/OU=sihsalus/CN=sihsalus.hsc" -addext "subjectAltName = DNS:localhost, DNS:openmrs.sihsalus.hsc, IP:127.0.0.1, IP:${SERVER_IP}"  # Replace with your LAN IP

echo "Certificate generated successfully"
