#!/bin/sh
set -e

# if we are using the $IMPORTMAP_URL environment variable, we have to make this useful,
# so we change "importmap.json" into "$IMPORTMAP_URL" allowing it to be changed by envsubst
if [ -n "${IMPORTMAP_URL}" ]; then
  if [ -n "$SPA_PATH" ]; then
    sed -i -e 's/"$SPA_PATH\/importmap.json"/"$IMPORTMAP_URL"/g' "/usr/share/nginx/html/index.html"
  else
    sed -i -e 's/"importmap.json"/"$IMPORTMAP_URL"/g' "/usr/share/nginx/html/index.html"
  fi
fi

# setting the config urls to "" causes an error reported in the console, so if we aren't using
# the SPA_CONFIG_URLS, we'll leave it alone
if [ -z "$SPA_CONFIG_URLS" ]; then
  sed -i -e 's/"$SPA_CONFIG_URLS"//' "/usr/share/nginx/html/index.html"
# otherwise convert the URLs into a Javascript list
# we support two formats, a comma-separated list or a space separated list
else
  old_IFS="$IFS"
  if echo "$SPA_CONFIG_URLS" | grep , >/dev/null; then
    IFS=","
  fi

  CONFIG_URLS=
  for url in $SPA_CONFIG_URLS;
  do
    if [ -z "$CONFIG_URLS" ]; then
      CONFIG_URLS="\"${url}\""
    else
      CONFIG_URLS="$CONFIG_URLS,\"${url}\""
    fi
  done

  IFS="$old_IFS"
  export SPA_CONFIG_URLS=$CONFIG_URLS
  sed -i -e 's/"$SPA_CONFIG_URLS"/$SPA_CONFIG_URLS/' "/usr/share/nginx/html/index.html"
fi

# Substitute environment variables in the html file
# This allows us to override parts of the compiled file at runtime
if [ -f "/usr/share/nginx/html/index.html" ]; then
  envsubst < "/usr/share/nginx/html/index.html" | sponge "/usr/share/nginx/html/index.html"
fi

exec nginx -g "daemon off;"
