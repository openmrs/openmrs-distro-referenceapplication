#!/bin/bash

# Define paths
DEFAULT_REFAPP_CONFIG_PATH="./frontend/config-core_demo.json"
E2E_CONFIG_PATH="./e2e_test_support_files/config-core_demo.json"

# Check if the RefApp configuration file exists
if [ -f "$DEFAULT_REFAPP_CONFIG_PATH" ]; then
  # Sync the configuration file
  cp "$DEFAULT_REFAPP_CONFIG_PATH" "$E2E_CONFIG_PATH"
  echo "Configuration synced successfully to $E2E_CONFIG_PATH!"
else
  echo "Error: RefApp configuration file not found at '$DEFAULT_REFAPP_CONFIG_PATH'."
  echo "Please check the file path or verify if the file exists."
  echo "You can use the following command to locate it:"
  echo "  find . -name 'config-core_demo.json'"
  exit 1
fi
