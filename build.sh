#!/usr/bin/env sh
set -eu

help() {
  echo "Script to build the RefApp distribution"
  echo "Usage: $(basename "$0") [options]"
  printf "\t-h print this help message\n"
  printf "\t-T run this build with the given target. Should be one of \"base\" or \"embedded\"\n"
}

echoerr() {
  echo "$@" 1>&2
}

TARGET=base
APP_SHELL_VERSION=3.1.1-pre.188

while getopts "h:T:a:" opt; do
  case $opt in
    h) help;exit 0;;
    T) TARGET=$OPTARG;;
    a) APP_SHELL_VERSION=$OPTARG;;
    *) echoerr "[ERROR] Unrecognized option $opt"; help; exit 1;;
  esac
done

docker build -f Dockerfile.build --tag openmrs/3-reference-distro:latest --target distro .
docker build -f docker/backend/Dockerfile --tag openmrs/openmrs-referenceapplication-backend:latest --target "$TARGET" docker/backend
docker build -f docker/frontend/Dockerfile --tag openmrs/openmrs-referenceapplication-frontend:latest --target "$TARGET" --build-arg APP_SHELL_VERSION="$APP_SHELL_VERSION" docker/frontend
docker build -f docker/gateway/Dockerfile --tag openmrs/openmrs-referenceapplication-gateway:latest docker/gateway
