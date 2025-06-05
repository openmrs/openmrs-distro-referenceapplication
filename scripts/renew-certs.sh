#!/bin/bash -aeu
#	This Source Code Form is subject to the terms of the Mozilla Public License, 
#	v. 2.0. If a copy of the MPL was not distributed with this file, You can 
#	obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under 
#	the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
#	
#	Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS 
#	graphic logo is a trademark of OpenMRS Inc.

SCRIPT_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )

echo "### attempting to refresh certs ..."
docker compose -f "${SCRIPT_DIR}/../docker-compose.yml" run --rm --entrypoint "\
  certbot certonly --keep-until-expiring \
    "$@" \
    --agree-tos \
    --no-eff-email" certbot

echo "### Reloading nginx ..."
docker-compose -f "${SCRIPT_DIR}/../docker-compose.yaml" exec gateway nginx -s reload