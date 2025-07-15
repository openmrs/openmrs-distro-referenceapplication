#!/bin/bash -aeu
#	This Source Code Form is subject to the terms of the Mozilla Public License,
#	v. 2.0. If a copy of the MPL was not distributed with this file, You can
#	obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
#	the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
#
#	Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
#	graphic logo is a trademark of OpenMRS Inc.

CERT_RSA_KEY_SIZE=4096
CERT_TEMP_CERT_DAYS=90
CERTBOT_DATA_PATH="/var/www/certbot"

read -p "Enter Domain [default: 'localhost']: " WEB_DOMAIN
CERT_WEB_DOMAIN_COMMON_NAME=${WEB_DOMAIN:-localhost}
CERT_WEB_DOMAINS=(${CERT_WEB_DOMAIN_COMMON_NAME})
while [ -n "${WEB_DOMAIN}" ]; do
	read -p "Enter Alternate Domain [default: exit loop]: " WEB_DOMAIN
	if [ -n "${WEB_DOMAIN}" ]; then
		CERT_WEB_DOMAINS+=(${WEB_DOMAIN})
	fi
done
read -p "Enter Email [default: '']: " CERT_CONTACT_EMAIL
CERT_CONTACT_EMAIL=${CERT_CONTACT_EMAIL:-}

CERT_ROOT_PATH="/etc/letsencrypt"
OLD_IFS="$IFS"
IFS=","
WEB_DOMAINS_AS_STRING=${CERT_WEB_DOMAINS[*]}
IFS=${OLD_IFS}
docker compose --progress=quiet run --name certgen --rm --no-deps --build \
	--env CERT_WEB_DOMAIN_COMMON_NAME=${CERT_WEB_DOMAIN_COMMON_NAME} \
	--env CERT_RSA_KEY_SIZE=${CERT_RSA_KEY_SIZE} --env CERT_WEB_DOMAINS=${WEB_DOMAINS_AS_STRING} \
	--env CERTBOT_DATA_PATH=${CERTBOT_DATA_PATH} --env CERT_CONTACT_EMAIL=${CERT_CONTACT_EMAIL} \
	--env CERT_ROOT_PATH=${CERT_ROOT_PATH} --env CERT_TEMP_CERT_DAYS=${CERT_TEMP_CERT_DAYS} \
	--entrypoint "/certbot/scripts/initial-startup-create-dirs-files.sh " certbot
echo "Successfully created self-signed certs"

echo "Bringing up all containers ..."
docker compose up -d --build 
docker compose --progress=quiet exec gateway nginx -s reload
