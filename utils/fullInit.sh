#!/bin/bash

# Define the directory containing the docker-compose.yml file
MODE="development"
COMPOSE_DIR="../"
ENV_FILE="enviromentVariables.env"

# Function to display mode of deployment
usage() {
    echo "Usage: $0 -m [production|development]"
    exit 1
}

# Parse options
while getopts ":m:" opt; do
    case ${opt} in
        m)
            if [[ "$OPTARG" == "production" || "$OPTARG" == "development" ]]; then
                MODE="$OPTARG"
            else
                usage
            fi
            ;;
        *)
            usage
            ;;
    esac
done

# Ensure mode is set
if [[ -z "$MODE" ]]; then
    usage
fi

# Navigate to the directory containing the docker-compose.yml file
cd "$COMPOSE_DIR" || { echo "Directory $COMPOSE_DIR not found"; exit 1; }

# Stop and remove all containers defined in the docker-compose.yml file
if sudo docker compose ps -q > /dev/null 2>&1; then
    echo "Stopping and removing containers..."
    sudo docker compose down
else
    echo "No containers to stop and remove."
fi

# Remove all associated volumes
if sudo docker compose ps -q > /dev/null 2>&1; then
    echo "Removing volumes..."
    sudo docker compose down -v
else
    echo "No volumes to remove."
fi

# Build and start the Docker Compose stack
if [[ "$MODE" == "production" ]]; then
    echo "Starting in production mode..."
    docker compose up -d --build --env-file "$ENV_FILE"
else
    echo "Starting in development mode..."
    echo "WARNING: Using default environment variables. DO NOT DEPLOY IN A PRODUCTION ENVIRONMENT."
    docker compose up -d --build
fi

# Check if the stack started successfully
if [ $? -eq 0 ]; then
    echo "Docker Compose stack started successfully."
else
    echo "ERROR: Failed to start Docker Compose stack."
    exit 1
fi
