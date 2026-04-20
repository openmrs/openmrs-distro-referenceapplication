#!/bin/sh
set -e

# Copy brand assets into the shared SPA volume (populated by frontend-init).
# We use cp -n so we never overwrite files the init container placed there.
cp -n /brand/* /usr/share/nginx/html/ 2>/dev/null || true

exec nginx -g "daemon off;"
