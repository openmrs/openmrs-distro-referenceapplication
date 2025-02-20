#!/bin/bash

# Set default values
CERT_NAME="peruHCE-certificate"
DAYS_VALID=365
KEY_SIZE=2048
SERGVER_IP=192.168.1.xxxx

# Updating and check last version of openssl
RUN apt-get update && apt-get install -y openssl

# Creating directory to save certificate
RUN mkdir -p ./ssl

# Generate private key
echo "Generating private key and crt with prefix: $CERT_NAME ..."
openssl req -x509 -nodes -days 365 -newkey rsa:2048 -keyout ./ssl/$CERT_NAME.key -out ./ssl/$CERT_NAME.crt -subj "/C=PE/ST=Loreto/L=Maynas/O=Centro de Salud Santa Clotilde/OU=peruHCE/CN=peruHCE-server" -addext "subjectAltName = DNS:localhost, IP:127.0.0.1, IP:192.168.200.240"  # Replace with your LAN IP

echo "Certificate generated successfully:"

# Display certificate details
echo -e "\n🔍 Certificate Details:"
openssl x509 -in "./ssl/$CERT_NAME.crt" -text -noout

# Display private key details
echo -e "\n🔑 Private Key Details:"
openssl rsa -in "./ssl/$CERT_NAME.key" -text -noout
