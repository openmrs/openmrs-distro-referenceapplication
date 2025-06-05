#!/bin/bash -aeu
#	This Source Code Form is subject to the terms of the Mozilla Public License,
#	v. 2.0. If a copy of the MPL was not distributed with this file, You can
#	obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
#	the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
#
#	Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
#	graphic logo is a trademark of OpenMRS Inc.

usage() {
cat << EOF
Usage: $0 [-s size] ([-d domain])* [-e email] [-p prod_confirm] [-l local_build_confirm] [-o overwrite_certs_confirm] [-c cron_job_confirm]
Options:
	-h      Show this help message	
    -s      size of the RSA key to generate; takes in 1 argument (default: 4096)
	-d      Domain to generate certificate for; can be specified multiple times (default: 'localhost')	
	-e      Email address for Let's Encrypt registration; takes in 1 argument (default: '')
	-p      Production confirmation; takes in 1 argument (default: 'n')
	-l      Local build confirmation; takes in 1 argument (default: 'n')
	-o      Overwrite existing certificates confirmation; takes in 1 argument (default: 'n')
	-c      Cron job confirmation for auto-renewal; takes in 1 argument (default: 'y')
Example:
	$0 -s 4096 -d example.com 
EOF
}


COMPOSE_BAKE=true
SCRIPT_DIR=$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" &>/dev/null && pwd)

RSA_KEY_SIZE="${RSA_KEY_SIZE:-4096}"
DATA_PATH="/var/www/certbot"
WEB_DOMAINS=("${WEB_DOMAINS:-}")
OLD_IFS="$IFS"
IFS=","
set -- ${WEB_DOMAINS}
IFS=${OLD_IFS}
i=0
for WEB_DOMAIN in "$@"; do
	WEB_DOMAINS+=(${WEB_DOMAIN})
done
WEB_DOMAIN_PARAM_OVERRIDE_OCCURRED=false
PRODUCTION_CONFIRM=""
LOCAL_BUILD_CONFIRM=""
OVERWRITE_CERTS_CONFIRM=""
CRON_JOB_CONFIRM=""
while getopts ":s:d:e:p:l:o:c:" opt; do
	case $opt in
	s)
		RSA_KEY_SIZE="${OPTARG}"
		;;
	d)
		if [ "${WEB_DOMAIN_PARAM_OVERRIDE_OCCURRED}" = false ]; then
			WEB_DOMAINS=()  # Reset the array if this is the first domain argument
			WEB_DOMAIN_PARAM_OVERRIDE=true
		fi
		WEB_DOMAINS+=("${OPTARG}")
		;;
	e)
		EMAIL="${OPTARG}"
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
	c)
		CRON_JOB_CONFIRM="${OPTARG}"
		;;
	h)
		usage
		exit 0
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
shift "$((OPTIND-1))"

WEB_DOMAIN=""
if [ -z "${WEB_DOMAINS[@]}" ]; then
	read -p "Enter Domain [default: 'localhost']: " WEB_DOMAIN
	WEB_DOMAIN_COMMON_NAME=${WEB_DOMAIN:-localhost}
	WEB_DOMAINS=(${WEB_DOMAIN_COMMON_NAME})
	WEB_DOMAIN_SET=true
fi
while [ -n "${WEB_DOMAIN}" ]; do
	read -p "Enter Alternate Domain [default: exit loop]: " WEB_DOMAIN
	if [ -n "${WEB_DOMAIN}" ]; then
		WEB_DOMAINS+=(${WEB_DOMAIN})
	fi
done

if [ -v EMAIL ]; then
	echo "Using contact email: ${EMAIL}"
else 
	read -p "Enter Email [default: '']: " EMAIL
	EMAIL=${EMAIL:-}
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
CERT_PATH="/etc/letsencrypt/live/${WEB_DOMAIN_COMMON_NAME}"
OLD_IFS="$IFS"
IFS=","
WEB_DOMAINS_AS_STRING=${WEB_DOMAINS[*]}
IFS=${OLD_IFS}
docker compose ${DOCKER_FILE_ARG} --progress=quiet run ${BUILD_ARG} --name certgen --rm --no-deps \
	--env RSA_KEY_SIZE=${RSA_KEY_SIZE} --env WEB_DOMAINS=${WEB_DOMAINS_AS_STRING} \
	--env DATA_PATH=${DATA_PATH} --env OVERWRITE_CERTS_CONFIRM=${OVERWRITE_CERTS_CONFIRM} \
	--env EMAIL=${EMAIL} --env CERT_PATH=${CERT_PATH} \
	--entrypoint "/certbot/scripts/initial-startup-create-dirs-files.sh" \
	certbot
echo
IFS=${OLD_IFS}
echo "Successfully created temporary self-signed certs"

echo "### Starting gateway ..."
docker compose ${DOCKER_FILE_ARG} --progress=quiet up ${BUILD_ARG} --force-recreate -d gateway
echo

echo "### Deleting dummy certificate for ${WEB_DOMAIN_COMMON_NAME} ..."
docker compose ${DOCKER_FILE_ARG} --progress=quiet run ${BUILD_ARG} --rm --no-deps --entrypoint "
  rm -Rf /etc/letsencrypt/live/${WEB_DOMAIN_COMMON_NAME}" certbot
docker compose ${DOCKER_FILE_ARG} --progress=quiet run ${BUILD_ARG} --rm --no-deps --entrypoint "
  rm -Rf /etc/letsencrypt/archive/${WEB_DOMAIN_COMMON_NAME}" certbot
docker compose ${DOCKER_FILE_ARG} --progress=quiet run ${BUILD_ARG} --rm --no-deps --entrypoint "
  rm -Rf /etc/letsencrypt/renewal/${WEB_DOMAIN_COMMON_NAME}.conf" certbot
echo "Removed dummy certificate for ${WEB_DOMAIN_COMMON_NAME}"
echo

echo "### Requesting Let's Encrypt certificate for ${WEB_DOMAIN_COMMON_NAME} ..."
DOMAIN_ARGS=""
for WEB_DOMAIN in "${WEB_DOMAINS[@]}"; do
	DOMAIN_ARGS+="-d ${WEB_DOMAIN} "
done

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

while true; do
	if [ -z "${CRON_JOB_CONFIRM}"]; then
		read -p "Would you like to schedule auto-cert-renewal? (y/n) [default: 'n']: " CRON_JOB_CONFIRM
	fi
	CRON_JOB_CONFIRM=${CRON_JOB_CONFIRM:-n}
	case ${CRON_JOB_CONFIRM} in
	[Yy]*)
		echo "### Adding cron job for certificate renewal ..."
		crontab -l | grep -q "${SCRIPT_DIR}/certbot/scripts/renew_certs.sh" && echo 'crontab task already exists' ||
			(	crontab -l 2>/dev/null || true
				echo "0 0 * * * ${SCRIPT_DIR}/certbot/scripts/renew_certs.sh ${STAGING_ARG} ${EMAIL_ARG} ${DOMAIN_ARGS} --rsa-key-size ${RSA_KEY_SIZE}"
			) | crontab - 
		break
		;;
	[Nn]*)
		break
		;;
	*)
		echo "Please answer y or n."
		CRON_JOB_CONFIRM=""
		;;
	esac
done

echo "### Reloading nginx ..."
docker compose ${DOCKER_FILE_ARG} --progress=quiet exec gateway nginx -s reload
echo