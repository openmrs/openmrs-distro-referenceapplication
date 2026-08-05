#!/usr/bin/env bash
# Invoked by the `webhook` listener (see hooks.json.example) once the GitHub
# HMAC signature has been verified. Runs from the repo checkout on the VM
# (command-working-directory in hooks.json), pulls the images built by
# .github/workflows/deploy-onprem.yml, and rolls the stack over to them.
set -euo pipefail

TAG="${1:-latest}"
export TAG

REPO_DIR="/home/openmrsdev/openmrsdev/openmrs-distro-referenceapplication"
cd "${REPO_DIR}"

echo "[deploy] pulling images for tag=${TAG}"
docker compose pull

echo "[deploy] recreating changed services"
docker compose up -d --remove-orphans

echo "[deploy] pruning dangling images"
docker image prune -f

echo "[deploy] done"
