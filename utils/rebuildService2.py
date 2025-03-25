# Importing libraries
import json
import os
import subprocess
import sys
from pathlib import Path

# Variables:
COMPOSE_DIR = "../" # Define the directory containing the docker-compose.yml file
DOCKER_FILE = "docker-compose.yml" #File of docker compose

# ANSI Colors
GREEN =     "\033[0;32m"
RED =       "\033[0;31m"
NC =        "\033[0m"  # No Color

# Define which service need to erase volumes and their name
servicesIndications = [
    {
        "service":          "portainer",
        "name":             "peruHCE-portainer",
        "deleteVolumes":    "yes",
        "volumesToDelete":  [
            { "name": "portainer-data" },
        ]
    },
    {
        "service":          "dns",
        "name":             "peruHCE-dns",
        "deleteVolumes":    "true",
        "volumesToDelete":  [
            { "name": "pihole-data" },
            { "name": "pihole-dnsmaq" }
        ]
    },
    {
        "service":          "gateway",
        "name":             "peruHCE-gateway",
        "deleteVolumes":    "false"
    },
    {
        "service":          "frontend",
        "name":          "peruHCE-frontend",
        "deleteVolumes":    "true",
        "volumesToDelete":  [
            { "name": "opemrs-frontend" },
        ]
    },
    {
        "service":          "backend",
        "name":          "peruHCE-backend",
        "deleteVolumes":    "true",
        "volumesToDelete":  [
            { "name": "opemrs-data" },
        ]
    },
    {
        "service":          "db",
        "name":          "peruHCE-db-master",
        "deleteVolumes":    "true",
        "volumesToDelete":  [
            { "name": "db-data" },
            { "name": "db-backup" }
        ]
    },
    {
        "service":          "db-replic",
        "name":             "peruHCE-db-replic",
        "deleteVolumes":    "true",
        "volumesToDelete":  [
            { "name": "db-data" },
            { "name": "db-backup" }
        ]
    }
]

##############################
# BEGIN OF PROMPT ############
##############################

# Go back to compose directory
try:
    os.chdir(COMPOSE_DIR)
except FileNotFoundError:
    print("ERROR: DIRECTORIO DE DOCKER COMPOSE NO EXISTE. ")
    sys.exit(1)

# Check if docker compose file exists
if not os.path.isfile(DOCKER_FILE):
    print(f"{RED}ERROR: docker-compose.yml not found in {COMPOSE_DIR}{NC}")
    sys.exit(1)

# List service to rebuild, back and database should be one
print()
print("LISTA DE SERVICIOS DISPONIBLES:")
for index, indication in servicesIndications:
    print(f"{index+1:02d} {indication['name']}" )

