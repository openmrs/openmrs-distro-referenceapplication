#!/bin/bash

#Script to get logs of the initializer module and all tasks regarding the creation and preload of data for OpenMRS server.

# Configuration
CONTAINER_NAME="peruHCE-backend"  # Set your container name
LOG_FILE="creation-log.txt"
SEARCH_TEXT="org.apache.coyote.AbstractProtocol.start Starting ProtocolHandler ["http-nio-8080"]"  # Text that triggers stopping

# Ensure the container exists
if ! docker ps --format '{{.Names}}' | grep -q "^$CONTAINER_NAME$"; then
    echo "Error: Container '$CONTAINER_NAME' not found."
    exit 1
fi

# Capture logs until the search text is found
docker logs -f "$CONTAINER_NAME" | tee "$LOG_FILE" | awk "/$SEARCH_TEXT/ {exit}"

echo "✅ Logs captured in '$LOG_FILE' until '$SEARCH_TEXT' was found."
#!/bin/bash

# Configuration
CONTAINER_NAME="peruHCE-backend"  # Set your container name
LOG_FILE="logs.txt"
SEARCH_TEXT="STOP_LOGGING_HERE"  # Text that triggers stopping

# Ensure the container exists
if ! docker ps --format '{{.Names}}' | grep -q "^$CONTAINER_NAME$"; then
    echo "Error: Container '$CONTAINER_NAME' not found."
    exit 1
fi

# Capture logs until the search text is found
docker logs -f "$CONTAINER_NAME" | tee "$LOG_FILE" | awk "/$SEARCH_TEXT/ {exit}"

echo "✅ Logs captured in '$LOG_FILE' until '$SEARCH_TEXT' was found."
