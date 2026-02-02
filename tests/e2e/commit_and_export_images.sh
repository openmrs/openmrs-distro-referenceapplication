#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )

COMPOSE_FILE="${SCRIPT_DIR}/docker-compose-build.yml"

get_container_id() {
  local svc="$1"
  id="$(docker compose -f "$COMPOSE_FILE" ps -q "$svc" || true)"
  if [[ -z "$id" ]]; then
    echo "ERROR: no running container found for service '$svc' (docker compose -f $COMPOSE_FILE ps -q $svc returned empty)" >&2
    exit 1
  fi
  echo "$id"
}

frontend_container_id="$(get_container_id frontend)"
gateway_container_id="$(get_container_id gateway)"
backend_container_id="$(get_container_id backend)"
db_container_id="$(get_container_id db)"

# Commit containers to explicitly named images
docker commit "${frontend_container_id}" e2e/frontend:latest
docker commit "${gateway_container_id}" e2e/gateway:latest
docker commit "${backend_container_id}" e2e/backend:latest
docker commit "${db_container_id}" e2e/db:latest

tarfile="e2e_release_env_images.tar"
gzipfile="${tarfile}.gz"

# Save the images we just created
docker save e2e/frontend:latest e2e/gateway:latest e2e/backend:latest e2e/db:latest -o "$tarfile"

# compress and verify
gzip -c "$tarfile" > "$gzipfile"

# Cross-platform file size reporting
if [[ "$(uname)" == "Darwin" ]]; then
  # macOS
  echo "Created: $tarfile ($(stat -f '%z' "$tarfile") bytes)"
  echo "Created: $gzipfile ($(stat -f '%z' "$gzipfile") bytes)"
else
  # Linux
  echo "Created: $tarfile ($(stat -c '%s' "$tarfile") bytes)"
  echo "Created: $gzipfile ($(stat -c '%s' "$gzipfile") bytes)"
fi
ls -lh "$tarfile" "$gzipfile"
