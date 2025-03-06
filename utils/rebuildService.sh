#!/bin/bash

# Define the directory containing the docker-compose.yml file
COMPOSE_DIR="../"

# Navigate to the directory containing the docker-compose.yml file
cd "$COMPOSE_DIR" || { echo "Directory $COMPOSE_DIR not found"; exit 1; }

# List services from docker-compose.yml
echo "Available services:"
services=$(docker compose config --services)

if [ -z "$services" ]; then
    echo "No services found in docker-compose.yml."
    exit 1
fi

echo "$services"

read -p "Enter the service you want to rebuild: " service_name

# Check if the service exists
if ! echo "$services" | grep -q "^$service_name$"; then
    echo "Error: Service '$service_name' not found."
    exit 1
fi

# Rebuild and restart the selected service
echo "Rebuilding and restarting '$service_name'..."
docker compose build "$service_name"
docker compose up -d --no-deps "$service_name"

echo "✅ Service '$service_name' rebuilt and restarted successfully!"




