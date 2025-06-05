#!/bin/sh
#	This Source Code Form is subject to the terms of the Mozilla Public License,
#	v. 2.0. If a copy of the MPL was not distributed with this file, You can
#	obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
#	the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
#
#	Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
#	graphic logo is a trademark of OpenMRS Inc.
CERT_TEMP_CERT_DAYS=1 #number of days that temp cert is valid for

if [ -d "${CERTBOT_DATA_PATH}/conf" ]; then
	# If the directory exists, we assume that the initial setup has already been done
	echo "Initial setup already done. Skipping initial startup script."
	# Select appropriate email arg
	case "${CERT_CONTACT_EMAIL}" in
	"") EMAIL_ARG="--register-unsafely-without-email" ;;
	" ") EMAIL_ARG="--register-unsafely-without-email" ;;
	*) EMAIL_ARG="--email ${CERT_CONTACT_EMAIL}" ;;
	esac

	OLD_IFS="$IFS"
	IFS=","
	set -- ${CERT_WEB_DOMAINS}
	IFS=${OLD_IFS}
	DOMAIN_ARGS=""
	for CERT_WEB_DOMAIN in "$@"; do
		DOMAIN_ARGS+="-d ${CERT_WEB_DOMAIN} "
	done
	
	certbot certonly --keep-until-expiring \
	--webroot -w ${CERTBOT_DATA_PATH}  \
	${EMAIL_ARG} ${DOMAIN_ARGS} --rsa-key-size ${CERT_RSA_KEY_SIZE}
    "$@" \
    --agree-tos \
    --no-eff-email
else
	# If the directory does not exist, we run the initial startup script
	echo "Running initial startup script..."
	/certbot/scripts/initial-startup-create-dirs-files.sh 
fi
