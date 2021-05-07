#!/usr/bin/env bash
set -e
npx openmrs@$1 build --spa-path \${SPA_PATH} --api-url \${API_URL} --target $2
