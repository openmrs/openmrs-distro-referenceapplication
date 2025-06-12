#!/bin/sh
#	This Source Code Form is subject to the terms of the Mozilla Public License,
#	v. 2.0. If a copy of the MPL was not distributed with this file, You can
#	obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
#	the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
#
#	Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
#	graphic logo is a trademark of OpenMRS Inc.

SUBJECT_ALT_NAME_ARG=""
CERT_TEMP_CERT_DAYS="${CERT_TEMP_CERT_DAYS:-1}" # number of days that temp cert is valid for
FIRST_LOOP=true
if [ -z "${CERT_WEB_DOMAINS}" ]; then
	echo "No web domain provided. Please set CERT_WEB_DOMAINS environment variable."
	exit 1
fi
OLD_IFS="$IFS"
IFS=","
set -- ${CERT_WEB_DOMAINS}
IFS=${OLD_IFS}
DNS_NUM=0
IP_NUM=0
for WEB_DOMAIN in "$@"; do
	if [ "${FIRST_LOOP}" = true ]; then
		FIRST_LOOP=false
		CERT_WEB_DOMAIN_COMMON_NAME="${WEB_DOMAIN}"
		echo "[req]" >> sslconfig.conf
		echo "distinguished_name=req_distinguished_name" >> sslconfig.conf
		echo "x509_extensions = v3_ca" >> sslconfig.conf
		echo "[req_distinguished_name]" >> sslconfig.conf
		echo "CN = ${CERT_WEB_DOMAIN_COMMON_NAME}" >> sslconfig.conf
		echo "[v3_ca]" >> sslconfig.conf
		echo "subjectAltName=@alternate_names" >> sslconfig.conf
		echo "[alternate_names]" >> sslconfig.conf
		SUBJECT_ALT_NAME_ARG="-config sslconfig.conf"
		echo "Adding primary domain ${CERT_WEB_DOMAIN_COMMON_NAME} to certificate ..."
		# SUBJECT_ALT_NAME_ARG="-addext \"subjectAltName = DNS:${WEB_DOMAIN}"
	else	
		echo "Adding alternate domain ${WEB_DOMAIN} to certificate ..."
		# SUBJECT_ALT_NAME_ARG="${SUBJECT_ALT_NAME_ARG}, DNS:${WEB_DOMAIN}"
	fi
	if expr "X$WEB_DOMAIN" : 'X[.0-9]+' >/dev/null; then
		echo "IP.${IP_NUM} = ${WEB_DOMAIN}" >> sslconfig.conf
		IP_NUM=$((IP_NUM + 1))
	else 
		echo "DNS.${DNS_NUM} = ${WEB_DOMAIN}" >> sslconfig.conf
		DNS_NUM=$((DNS_NUM + 1))
	fi
done
# SUBJECT_ALT_NAME_ARG="${SUBJECT_ALT_NAME_ARG}\""

if [ -d "${CERTBOT_DATA_PATH}/conf" ]; then
	while true; do
		echo "Existing configuration data found for ${CERT_WEB_DOMAIN_COMMON_NAME}. "
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
if [ ! -e "${CERTBOT_DATA_PATH}/conf/options-ssl-nginx.conf" ] || [ ! -e "${CERTBOT_DATA_PATH}/conf/ssl-dhparams.pem" ]; then
	echo "### Downloading recommended TLS parameters ..."
	mkdir -p "${CERTBOT_DATA_PATH}/conf"
	curl -s https://raw.githubusercontent.com/certbot/certbot/master/certbot-nginx/certbot_nginx/_internal/tls_configs/options-ssl-nginx.conf >"${CERTBOT_DATA_PATH}/conf/options-ssl-nginx.conf"
	curl -s https://raw.githubusercontent.com/certbot/certbot/master/certbot/certbot/ssl-dhparams.pem >"${CERTBOT_DATA_PATH}/conf/ssl-dhparams.pem"
	echo
fi
mkdir -p "${CERTBOT_DATA_PATH}/conf/live/${CERT_WEB_DOMAIN_COMMON_NAME}"
mkdir -p "${CERT_ROOT_PATH}/live/${CERT_WEB_DOMAIN_COMMON_NAME}"

echo "### Creating dummy certificate for ${CERT_WEB_DOMAIN_COMMON_NAME} ..."
openssl req -x509 -nodes -newkey rsa:${CERT_RSA_KEY_SIZE} -days ${CERT_TEMP_CERT_DAYS} \
	-keyout "${CERT_ROOT_PATH}/live/${CERT_WEB_DOMAIN_COMMON_NAME}/privkey.pem" \
	-out "${CERT_ROOT_PATH}/live/${CERT_WEB_DOMAIN_COMMON_NAME}/fullchain.pem" \
	-subj "/CN=${CERT_WEB_DOMAIN_COMMON_NAME}" \
	${SUBJECT_ALT_NAME_ARG} 
