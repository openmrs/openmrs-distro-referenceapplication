#!/usr/bin/env bash
set -e
#npx openmrs build --spa-path \${SPA_PATH} --api-url \${API_URL} --target ./target/microfrontends
#npx openmrs build --spa-path /ui --api-url /openmrs --target $1 --config-url  config/config.json
npx openmrs build --spa-path \${SPA_PATH} --api-url \${API_URL} --target $1
