#!/bin/bash

# Define the directory containing the docker-compose.yml file
COMPOSE_DIR="../"

# Navigate to the directory containing the docker-compose.yml file
cd "$COMPOSE_DIR" || { echo "ERROR: Directory $COMPOSE_DIR not found"; exit 1; }

# List services from docker-compose.yml
echo "Available services:"
services=$(docker compose config --services)

if [ -z "$services" ]; then
    echo "ERROR: No services found in docker-compose.yml."
    exit 1
fi

echo "$services"

read -p "Enter the service you want to rebuild: " service_name

# Check if the service exists
if ! echo "$services" | grep -q "^$service_name$"; then
    echo "ERROR: Service '$service_name' not found."
    exit 1
fi

# Ask the user if they want to rebuild with cache (default is NO cache)
read -p "🔄 Do you want to use cache? (y/N): " use_cache
use_cache=${use_cache,,}  

# Convert input to lowercase
if [[ "$use_cache" == "y" || "$use_cache" == "yes" ]]; then
    build_option=""
    echo "Rebuilding '$service_name' WITH cache..."
else
    build_option="--no-cache"
    echo "Rebuilding '$service_name' WITHOUT cache..."
fi

# Rebuild and restart the selected service
docker compose build $build_option "$service_name"
docker compose up -d --no-deps "$service_name"

echo "SUCCESS: Service '$service_name' rebuilt and restarted successfully!"
