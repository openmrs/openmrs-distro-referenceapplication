#!/bin/bash -aeu
#	This Source Code Form is subject to the terms of the Mozilla Public License,
#	v. 2.0. If a copy of the MPL was not distributed with this file, You can
#	obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
#	the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
#
#	Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
#	graphic logo is a trademark of OpenMRS Inc.

RSA_KEY_SIZE=4096
DAYS=90
DATA_PATH="/var/www/certbot"

read -p "Enter Domain [default: 'localhost']: " WEB_DOMAIN
WEB_DOMAIN_COMMON_NAME=${WEB_DOMAIN:-localhost}
WEB_DOMAINS=(${WEB_DOMAIN_COMMON_NAME})
while [ -n "${WEB_DOMAIN}" ]; do
	read -p "Enter Alternate Domain [default: exit loop]: " WEB_DOMAIN
	if [ -n "${WEB_DOMAIN}" ]; then
		WEB_DOMAINS+=(${WEB_DOMAIN})
	fi
done
read -p "Enter Email [default: '']: " EMAIL
EMAIL=${EMAIL:-}

CERT_PATH="/etc/letsencrypt"
OLD_IFS="$IFS"
IFS=","
WEB_DOMAINS_AS_STRING=${WEB_DOMAINS[*]}
IFS=${OLD_IFS}
docker compose --progress=quiet run --name certgen --rm --no-deps --build \
	--env WEB_DOMAIN_COMMON_NAME=${WEB_DOMAIN_COMMON_NAME} \
	--env RSA_KEY_SIZE=${RSA_KEY_SIZE} --env WEB_DOMAINS=${WEB_DOMAINS_AS_STRING} \
	--env DATA_PATH=${DATA_PATH} --env EMAIL=${EMAIL} --env CERT_PATH=${CERT_PATH} --env DAYS=${DAYS} \
	--entrypoint "/certbot/scripts/initial-startup-create-dirs-files.sh " certbot
echo "Successfully created self-signed certs"

echo "Bringing up all containers ..."
docker compose up -d --build --always-recreate-deps
