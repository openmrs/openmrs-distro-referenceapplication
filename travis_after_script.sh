#!/usr/bin/env bash

if [ "$TRAVIS_PULL_REQUEST" = "false" ]
then
docker logs distro_web_1
fi
