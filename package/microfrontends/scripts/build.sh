#!/usr/bin/env bash
set -e
#npx openmrs build --spa-path \${SPA_PATH} --api-url \${API_URL} --target ./target/microfrontends
npx openmrs build --spa-path /mf --api-url /openmrs --target $1
