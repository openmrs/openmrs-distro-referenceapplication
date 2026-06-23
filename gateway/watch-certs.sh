#!/bin/sh
#	This Source Code Form is subject to the terms of the Mozilla Public License,
#	v. 2.0. If a copy of the MPL was not distributed with this file, You can
#	obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
#	the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
#
#	Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
#	graphic logo is a trademark of OpenMRS Inc.

# This script watches for certificate renewal signals and reloads nginx

SIGNAL_FILE="/var/www/certbot/.reload-nginx"
CHECK_INTERVAL="${CERT_RELOAD_CHECK_INTERVAL:-30}"

echo "Starting nginx certificate reload watcher..."
echo "Checking for reload signal every ${CHECK_INTERVAL} seconds"

while true; do
    if [ -f "${SIGNAL_FILE}" ]; then
        echo "Certificate reload signal detected, reloading nginx..."
        rm -f "${SIGNAL_FILE}"
        nginx -s reload
        if [ $? -eq 0 ]; then
            echo "Nginx reloaded successfully"
        else
            echo "ERROR: Failed to reload nginx"
        fi
    fi
    sleep "${CHECK_INTERVAL}"
done
