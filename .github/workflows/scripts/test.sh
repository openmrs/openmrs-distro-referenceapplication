#!/bin/bash
export ISANTEPLUS_VERSION=main
rm -rf ./build_area
mkdir ./build_area
cd build_area

git clone -b ci https://github.com/IsantePlus/docker-isanteplus-server.git
git clone -b ci https://github.com/IsantePlus/docker-isanteplus-db.git
git clone -b ci https://github.com/IsantePlus/isanteplus-qaframework.git

docker build ./docker-isanteplus-db -t isanteplus/isanteplus-db:test
docker build ./docker-isanteplus-server -t isanteplus/isanteplus-server:test

cd docker-isanteplus-server
docker-compose -f ci.docker-compose.yaml up -d

sleep 120

docker-compose ci.docker-compose.yml ps

while [[ "$(curl -s -o /dev/null -w ''%{http_code}'' http://localhost:8080/openmrs/login.htm)" != "200" ]]; do sleep 1; done

cd ../isanteplus-qaframework

echo "cucumber.publish.enabled=false" > src/test/resources/cucumber.properties
mvn clean install -DskipTests=true
cp -f src/test/resources/test-local.properties src/test/resources/test.properties
mvn test

cd ../..

rm -rf ./build_area
