#!/bin/bash -aeu
#	This Source Code Form is subject to the terms of the Mozilla Public License,
#	v. 2.0. If a copy of the MPL was not distributed with this file, You can
#	obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
#	the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
#
#	Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
#	graphic logo is a trademark of OpenMRS Inc.
COMPOSE_BAKE=true
SCRIPT_DIR=$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" &>/dev/null && pwd)
RSA_KEY_SIZE=4096
DAYS=1
DATA_PATH="/var/www/certbot"
WEB_DOMAIN=""
EMAIL=""
PRODUCTION_CONFIRM=""
LOCAL_BUILD_CONFIRM=""
OVERWRITE_CERTS_CONFIRM=""
WEB_DOMAIN_SET=false
EMAIL_SET=false
while getopts ":s:d:e:p:l:o:" opt; do
	case $opt in
	s)
		RSA_KEY_SIZE="${OPTARG}"
		;;
	d)
		WEB_DOMAIN="${OPTARG}"
		WEB_DOMAIN_SET=true
		;;
	e)
		EMAIL="${OPTARG}"
		EMAIL_SET=true
		;;
	p)
		PRODUCTION_CONFIRM="${OPTARG}"
		;;
	l)
		LOCAL_BUILD_CONFIRM="${OPTARG}"
		;;
	o)
		OVERWRITE_CERTS_CONFIRM="${OPTARG}"
		;;
	\?)
		echo "Invalid option -${OPTARG}" >&2
		exit 1
		;;
	esac

	case ${OPTARG} in
	-*)
		echo "Option $opt needs a valid argument"
		exit 1
		;;
	esac
done

if [ "${WEB_DOMAIN_SET}" = false ]; then
	if [ -z "${WEB_DOMAIN}" ]; then
		read -p "Enter Domain [default: 'example.com']: " WEB_DOMAIN
		WEB_DOMAIN=${WEB_DOMAIN:-example.com}
		WEB_DOMAIN_SET=true
	fi
fi


if [ "${EMAIL_SET}" = false ]; then
	if [ -z "${EMAIL}" ]; then
		read -p "Enter Email [default: '']: " EMAIL
		EMAIL=${EMAIL:-}
		EMAIL_SET=true
	fi
fi

while true; do
	if [ -z "${PRODUCTION_CONFIRM}"]; then
		read -p "Is this a production environment? (y/n) [default: 'n']: " PRODUCTION_CONFIRM
	fi
	PRODUCTION_CONFIRM=${PRODUCTION_CONFIRM:-n}
	case ${PRODUCTION_CONFIRM} in
	[Yy]*)
		echo "using production environment ..."
		STAGING=0
		break
		;;
	[Nn]*)
		echo "using staging environment ..."
		STAGING=1
		break
		;;
	*)
		echo "Please answer y or n."
		PRODUCTION_CONFIRM=""
		;;
	esac
done

DOCKER_FILE_ARG="--file docker-compose.yml"
BUILD_ARG=""
if [ ${STAGING} != "0" ]; then
	while true; do
		if [ -z "${LOCAL_BUILD_CONFIRM}"]; then
			read -p "Would you like to build local images? (y/n) [default: 'n']: " LOCAL_BUILD_CONFIRM
		fi
		LOCAL_BUILD_CONFIRM=${LOCAL_BUILD_CONFIRM:-n}
		case ${LOCAL_BUILD_CONFIRM} in
		[Yy]*)
			echo "will build local images from this file system"
			DOCKER_FILE_ARG=""
			BUILD_ARG="--build"
			break
			;;
		[Nn]*)
			echo "will pull official images"
			break
			;;
		*)
			echo "Please answer y or n."
			LOCAL_BUILD_CONFIRM=""
			;;
		esac
	done
fi

CERT_PATH="/etc/letsencrypt/live/${WEB_DOMAIN}"
docker compose ${DOCKER_FILE_ARG} --progress=quiet run ${BUILD_ARG} --name certgen --rm --no-deps \
	--env RSA_KEY_SIZE=${RSA_KEY_SIZE} --env WEB_DOMAIN=${WEB_DOMAIN} \
	--env DATA_PATH=${DATA_PATH} --env OVERWRITE_CERTS_CONFIRM=${OVERWRITE_CERTS_CONFIRM} \
	--env EMAIL=${EMAIL} --env CERT_PATH=${CERT_PATH} --env DAYS=${DAYS} \
	--entrypoint "/certbot/scripts/initial-startup-create-dirs-files.sh" \
	certbot
echo
echo "Successfully created temporary self-signed certs"

echo "### Starting gateway ..."
docker compose ${DOCKER_FILE_ARG} --progress=quiet up ${BUILD_ARG} --force-recreate -d gateway
echo

echo "### Deleting dummy certificate for ${WEB_DOMAIN} ..."
docker compose ${DOCKER_FILE_ARG} --progress=quiet run ${BUILD_ARG} --rm --no-deps --entrypoint "
  rm -Rf /etc/letsencrypt/live/${WEB_DOMAIN}" certbot
docker compose ${DOCKER_FILE_ARG} --progress=quiet run ${BUILD_ARG} --rm --no-deps --entrypoint "
  rm -Rf /etc/letsencrypt/archive/${WEB_DOMAIN}" certbot
docker compose ${DOCKER_FILE_ARG} --progress=quiet run ${BUILD_ARG} --rm --no-deps --entrypoint "
  rm -Rf /etc/letsencrypt/renewal/${WEB_DOMAIN}.conf" certbot
echo "Removed dummy certificate for ${WEB_DOMAIN}"
echo

echo "### Requesting Let's Encrypt certificate for ${WEB_DOMAIN} ..."
DOMAIN_ARGS="-d ${WEB_DOMAIN}"

# Select appropriate email arg
case "${EMAIL}" in
"") EMAIL_ARG="--register-unsafely-without-email" ;;
" ") EMAIL_ARG="--register-unsafely-without-email" ;;
*) EMAIL_ARG="--email ${EMAIL}" ;;
esac

# Enable staging mode if needed
if [ ${STAGING} != "0" ]; then STAGING_ARG="--staging"; else STAGING_ARG=""; fi

docker compose ${DOCKER_FILE_ARG} --progress=quiet run ${BUILD_ARG} --rm --entrypoint "\
  certbot certonly --webroot -w /var/www/certbot \
    ${STAGING_ARG} \
    ${EMAIL_ARG} \
    ${DOMAIN_ARGS} \
    --rsa-key-size ${RSA_KEY_SIZE} \
    --no-eff-email \
    --agree-tos" certbot
echo

echo "### Reloading nginx ..."
docker compose ${DOCKER_FILE_ARG} --progress=quiet exec gateway nginx -s reload
echo

echo "### Adding cron job for certificate renewal ..."
crontab -l | grep -q "${SCRIPT_DIR}/certbot/scripts/renew_certs.sh" && echo 'crontab task already exists' ||
	(
		crontab -l 2>/dev/null || true
		echo "0 0 * * * ${SCRIPT_DIR}/certbot/scripts/renew_certs.sh ${STAGING_ARG} ${EMAIL_ARG} ${DOMAIN_ARGS} --rsa-key-size ${RSA_KEY_SIZE}"
	) | crontab -
