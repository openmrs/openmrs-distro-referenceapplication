#!/bin/sh
set -e

# Apache gets grumpy about PID files pre-existing
rm -f /usr/local/apache2/logs/httpd.pid

# Substitute envvars in packaged files
#sed -e "s/\${SPA_URL}/1/" -e "s/\${word}/dog/" template.txt
# envsubst < "/var/www/index.html" | sponge "/var/www/index.html"
# envsubst < "/var/www/openmrs.js" | sponge "/var/www/openmrs.js"
# envsubst < "/var/www/openmrs.js.map" | sponge "/var/www/openmrs.js.map"
# envsubst < "/var/www/openmrs.css" | sponge "/var/www/openmrs.css"
# envsubst < "/var/www/openmrs.css.map" | sponge "/var/www/openmrs.css.map"

exec httpd -DFOREGROUND "$@"
