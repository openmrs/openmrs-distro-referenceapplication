#!/bin/bash

# Define the directory containing the docker-compose.yml file
COMPOSE_DIR="../"

# Colors for better output
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Navigate to the directory containing the docker-compose.yml file
cd "$COMPOSE_DIR" || { echo -e "${RED}ERROR: Directory $COMPOSE_DIR not found${NC}"; exit 1; }

# Check if docker-compose.yml exists
if [ ! -f "docker-compose.yml" ]; then
    echo -e "${RED}ERROR: docker-compose.yml not found in $COMPOSE_DIR${NC}"
    exit 1
fi

# List services from docker-compose.yml
echo "Available services:"
services=$(docker compose config --services)

if [ -z "$services" ]; then
    echo -e "${RED}ERROR: No services found in docker-compose.yml${NC}"
    exit 1
fi

# Convert services to array and display with numbers
mapfile -t service_array <<< "$services"
for i in "${!service_array[@]}"; do
    echo "$((i+1)). ${service_array[i]}"
done

# Get user selection by number
while true; do
    read -p "Enter the number of the service you want to rebuild (1-${#service_array[@]}): " service_num
    # Check if input is a valid number
    if ! [[ "$service_num" =~ ^[0-9]+$ ]] || [ "$service_num" -lt 1 ] || [ "$service_num" -gt "${#service_array[@]}" ]; then
        echo -e "${RED}ERROR: Please enter a valid number between 1 and ${#service_array[@]}${NC}"
    else
        break
    fi
done

# Get the service name from the array (subtract 1 from input since array is 0-based)
service_name="${service_array[$((service_num-1))]}"

# Ask the user if they want to rebuild with cache (default is NO cache)
while true; do
    read -p "Do you want to use cache? (y/N): " use_cache
    use_cache=${use_cache,,}  # Convert to lowercase
    if [[ "$use_cache" =~ ^(y|yes|n|no|)$ ]]; then
        break
    else
        echo "Please enter y/yes or n/no (or press Enter for default 'no')"
    fi
done

if [[ "$use_cache" == "y" || "$use_cache" == "yes" ]]; then
    build_option=""
    echo "Rebuilding '$service_name' WITH cache..."
else
    build_option="--no-cache"
    echo "Rebuilding '$service_name' WITHOUT cache..."
fi

# Confirmation to proceed
while true; do
    read -p "Do you want to continue with rebuilding '$service_name'? (y/N): " confirm
    confirm=${confirm,,}
    if [[ "$confirm" =~ ^(y|yes|n|no|)$ ]]; then
        break
    else
        echo "Please enter y/yes or n/no (or press Enter for default 'no')"
    fi
done

if [[ "$confirm" != "y" && "$confirm" != "yes" ]]; then
    echo "Operation cancelled by user."
    exit 0
fi

# Stop the service
echo "Stopping service '$service_name'..."
docker compose stop "$service_name"

# Find the volume(s) associated with the service
echo "Checking for volumes associated with '$service_name'..."

container_id=$(docker ps -a -q --filter name="$service_name")

echo "Container '$container_id'..."

if [ -n "$container_id" ]; then
    volumes=$(docker inspect --format '{{ range .Mounts }}{{ .Name }} {{ end }}' "$container_id" | xargs)
fi

# If there are volumes, remove them
if [ -n "$volumes" ]; then
    echo "Removing volume(s) associated with the service '$service_name': $volumes"
    docker volume rm $volumes 2>/dev/null || echo "Warning: Could not remove some volumes (possibly in use)"
else
    echo "No volumes found associated with the service '$service_name'."
fi

# Rebuild and restart the selected service
echo "Rebuilding service '$service_name'..."
docker compose build $build_option "$service_name" || { echo -e "${RED}ERROR: Build failed${NC}"; exit 1; }
echo "Starting service '$service_name'..."
docker compose up -d --no-deps "$service_name" || { echo -e "${RED}ERROR: Service start failed${NC}"; exit 1; }

echo -e "${GREEN}SUCCESS: Service '$service_name' rebuilt and restarted successfully!${NC}"