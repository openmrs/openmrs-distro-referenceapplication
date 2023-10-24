#!/bin/bash

# Define the Docker Compose project name and output tarball name
TARBALL_NAME="compose_snapshot.tar"
backend_container_id=$(docker ps --filter "name=backend" --format "{{.ID}}")
db_container_id=$(docker ps --filter "name=db" --format "{{.ID}}")
frontend_container_id=$(docker ps --filter "name=frontend" --format "{{.ID}}")
gateway_container_id=$(docker ps --filter "name=gateway" --format "{{.ID}}")

docker commit $frontend_container_id frontend
docker commit $gateway_container_id gateway
docker commit $backend_container_id backend
docker commit $db_container_id db

docker save frontend gateway backend db > e2e_release_env_images.tar

# compress the file (to decrease the upload file size)
tar czvf e2e_release_env_images.tar.gz e2e_release_env_images.tar
