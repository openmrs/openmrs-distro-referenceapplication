#!/usr/bin/env bash
set -e
npx openmrs build --spa-path \${SPA_PATH} --api-url \${API_URL} --target $1
