#!/bin/sh
set -e

# Apache gets grumpy about PID files pre-existing
rm -f /usr/local/apache2/logs/httpd.pid

# Substitute envvars in packaged files
envsubst < "/var/www/ui/index.html" | sponge "/var/www/ui/index.html"

exec httpd -DFOREGROUND "$@"
