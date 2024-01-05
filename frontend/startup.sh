#!/bin/sh
set -e

rebuild_frontend() {
  npx --legacy-peer-deps openmrs@${APP_SHELL_VERSION:-next} assemble --manifest --mode config --config spa-build-config.json --target ./spa
  npx --legacy-peer-deps openmrs@${APP_SHELL_VERSION:-next} build --build-config spa-build-config.json --target ./spa
  # we exit 0 below so that build failures do not stop the image
  if [ ! -f ./spa/index.html ]; then echo 'Rebuild failed. Please check the logs above for details. This may have happened because of an update to a library that OpenMRS depends on.' >&2; popd; exit 0; fi
  shasum -a 512 spa-build-config.json > spa-build-config.json.sha512sum

  rm -rf /usr/share/nginx/html/*
  cp -r spa/* /usr/share/nginx/html
}

if [ -d "/openmrs" ]; then
  if [ -f "/openmrs/spa-build-config.json" ]; then
    if [ -f "/openmrs/spa-build-config.json.sha512sum" ]; then
      cd "/openmrs"
      shasum -a 512 -c /openmrs/spa-build-config.json.sha512sum > /dev/null || {
        echo "Checksum mismatch. Rebuilding frontend..."
        rebuild_frontend
      }
    else
      echo "Checksum file not found. Rebuilding frontend..."
      rebuild_frontend
    fi
  fi
fi

# if we are using the $IMPORTMAP_URL environment variable, we have to make this useful,
# so we change "importmap.json" into "$IMPORTMAP_URL" allowing it to be changed by envsubst
if [ -n "${IMPORTMAP_URL}" ]; then
  if [ -n "$SPA_PATH" ]; then
    [ -f "/usr/share/nginx/html/index.html"  ] && \
      sed -i -e 's/\("|''\)$SPA_PATH\/importmap.json\("|''\)/\1$IMPORTMAP_URL\1/g' "/usr/share/nginx/html/index.html"

    [ -f "/usr/share/nginx/html/service-worker.js" ] && \
      sed -i -e 's/\("|''\)$SPA_PATH\/importmap.json\("|''\)/\1$IMPORTMAP_URL\1/g' "/usr/share/nginx/html/service-worker.js"
  else
    [ -f "/usr/share/nginx/html/index.html"  ] && \
      sed -i -e 's/\("|''\)importmap.json\("|''\)/\1$IMPORTMAP_URL\1/g' "/usr/share/nginx/html/index.html"

    [ -f "/usr/share/nginx/html/service-worker.js" ] && \
      sed -i -e 's/\("|''\)importmap.json\("|''\)/\1$IMPORTMAP_URL\1/g' "/usr/share/nginx/html/service-worker.js"
  fi
fi

# setting the config urls to "" causes an error reported in the console, so if we aren't using
# the SPA_CONFIG_URLS, we remove it from the source, leaving config urls as []
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

SPA_DEFAULT_LOCALE=${SPA_DEFAULT_LOCALE:-en_GB}

# Substitute environment variables in the html file
# This allows us to override parts of the compiled file at runtime
if [ -f "/usr/share/nginx/html/index.html" ]; then
  envsubst '${IMPORTMAP_URL} ${SPA_PATH} ${API_URL} ${SPA_CONFIG_URLS} ${SPA_DEFAULT_LOCALE}' < "/usr/share/nginx/html/index.html" | sponge "/usr/share/nginx/html/index.html"
fi

if [ -f "/usr/share/nginx/html/service-worker.js" ]; then
  envsubst '${IMPORTMAP_URL} ${SPA_PATH} ${API_URL}' < "/usr/share/nginx/html/service-worker.js" | sponge "/usr/share/nginx/html/service-worker.js"
fi

exec nginx -g "daemon off;"
