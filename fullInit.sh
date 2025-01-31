#!/bin/bash

# Define the directory containing the docker-compose.yml file
COMPOSE_DIR="."

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
echo "Building and starting the Docker Compose stack..."
sudo docker compose up -d --build

# Check if the stack started successfully
if [ $? -eq 0 ]; then
    echo "Docker Compose stack started successfully."
else
    echo "Failed to start Docker Compose stack."
    exit 1
fi
