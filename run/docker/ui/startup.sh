#!/bin/sh
set -e

# Apache gets grumpy about PID files pre-existing
rm -f /usr/local/apache2/logs/httpd.pid

if [ -n "${IMPORTMAP_URL}" ]; then
  # substitute importmap.json for $IMPORTMAP_URL allowimg us to populate it from a URL
  sed -i -e 's/"importmap.json"/"$IMPORTMAP_URL"/g' "/var/www/ui/index.html"
fi

# Substitute envvars in packaged files
envsubst < "/var/www/ui/index.html" | sponge "/var/www/ui/index.html"

exec httpd -DFOREGROUND "$@"
