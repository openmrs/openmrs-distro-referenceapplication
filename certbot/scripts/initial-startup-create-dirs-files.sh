#!/bin/sh
#	This Source Code Form is subject to the terms of the Mozilla Public License,
#	v. 2.0. If a copy of the MPL was not distributed with this file, You can
#	obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
#	the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
#
#	Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
#	graphic logo is a trademark of OpenMRS Inc.

SUBJECT_ALT_NAME_ARG=""
FIRST_LOOP=true
WEB_DOMAIN_COMMON_NAME=""
if [ -z "${WEB_DOMAINS}" ]; then
	echo "No web domains provided. Please set WEB_DOMAINS environment variable."
	exit 1
fi
OLD_IFS="$IFS"
IFS=":"
set -- ${WEB_DOMAINS}
IFS=${OLD_IFS}
i=0
for WEB_DOMAIN in "$@"; do
	i=$((i + 1))
	if [ "${FIRST_LOOP}" = true ]; then
		FIRST_LOOP=false
		WEB_DOMAIN_COMMON_NAME="${WEB_DOMAIN}"

		echo "[req]" >> sslconfig.conf
		echo "distinguished_name=req_distinguished_name" >> sslconfig.conf
		echo "x509_extensions = v3_ca" >> sslconfig.conf
		echo "[req_distinguished_name]" >> sslconfig.conf
		echo "CN = ${WEB_DOMAIN_COMMON_NAME}" >> sslconfig.conf
		echo "[v3_ca]" >> sslconfig.conf
		echo "subjectAltName=@alternate_names" >> sslconfig.conf
		echo "[alternate_names]" >> sslconfig.conf
		echo "DNS.${i} = ${WEB_DOMAIN}" >> sslconfig.conf
		SUBJECT_ALT_NAME_ARG="-config sslconfig.conf"
		# SUBJECT_ALT_NAME_ARG="-addext \"subjectAltName = DNS:${WEB_DOMAIN}"
	else
		echo "DNS.${i} = ${WEB_DOMAIN}" >> sslconfig.conf
		# SUBJECT_ALT_NAME_ARG="${SUBJECT_ALT_NAME_ARG}, DNS:${WEB_DOMAIN}"
	fi
done
# SUBJECT_ALT_NAME_ARG="${SUBJECT_ALT_NAME_ARG}\""

if [ -d "${DATA_PATH}/conf" ]; then
	while true; do
		echo "Existing configuration data found for ${WEB_DOMAIN_COMMON_NAME}. "
		if [ -z "${OVERWRITE_CERTS_CONFIRM}"]; then
			read -p "Continue and replace existing certificate? (y/n) [default: 'n']: " OVERWRITE_CERTS_CONFIRM
		fi
		OVERWRITE_CERTS_CONFIRM=${OVERWRITE_CERTS_CONFIRM:-n}
		case ${OVERWRITE_CERTS_CONFIRM} in
		[Yy]*)
			echo "replacing existing certificate ..."
			break
			;;
		[Nn]*)
			echo "exiting certificate generation..."
			exit 1
			;;
		*)
			echo "Please answer y or n."
			OVERWRITE_CERTS_CONFIRM=""
			;;
		esac
	done
fi
if [ ! -e "${DATA_PATH}/conf/options-ssl-nginx.conf" ] || [ ! -e "${DATA_PATH}/conf/ssl-dhparams.pem" ]; then
	echo "### Downloading recommended TLS parameters ..."
	mkdir -p "${DATA_PATH}/conf"
	curl -s https://raw.githubusercontent.com/certbot/certbot/master/certbot-nginx/certbot_nginx/_internal/tls_configs/options-ssl-nginx.conf >"${DATA_PATH}/conf/options-ssl-nginx.conf"
	curl -s https://raw.githubusercontent.com/certbot/certbot/master/certbot/certbot/ssl-dhparams.pem >"${DATA_PATH}/conf/ssl-dhparams.pem"
	echo
fi
mkdir -p "${DATA_PATH}/conf/live/${WEB_DOMAIN_COMMON_NAME}"
mkdir -p "${CERT_PATH}"

echo "### Creating dummy certificate for ${WEB_DOMAIN_COMMON_NAME} ..."
echo "openssl req -x509 -nodes -newkey rsa:${RSA_KEY_SIZE} -days ${DAYS} \
	-keyout \"${CERT_PATH}/privkey.pem\" \
	-out \"${CERT_PATH}/fullchain.pem\" \
	-subj \"/CN=${WEB_DOMAIN_COMMON_NAME}\" \
	${SUBJECT_ALT_NAME_ARG} "
openssl req -x509 -nodes -newkey rsa:${RSA_KEY_SIZE} -days ${DAYS} \
	-keyout "${CERT_PATH}/privkey.pem" \
	-out "${CERT_PATH}/fullchain.pem" \
	-subj "/CN=${WEB_DOMAIN_COMMON_NAME}" \
	${SUBJECT_ALT_NAME_ARG} 
