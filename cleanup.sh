#!/bin/bash

podman rm openmrs-distro-referenceapplication-frontend-1 openmrs-distro-referenceapplication-gateway-1 openmrs-distro-referenceapplication-db-1 openmrs-distro-referenceapplication-backend-1
podman volume rm openmrs-distro-referenceapplication_db-data openmrs-distro-referenceapplication_openmrs-data
podman rmi docker.io/library/openmrs-distro-referenceapplication-backend

