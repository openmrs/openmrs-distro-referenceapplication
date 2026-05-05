#!/bin/sh
set -e

echo "Initializing monitoring configuration..."

echo "Copying Loki config..."
mkdir -p /etc/loki
cp /monitoring/loki-config.yaml /etc/loki/local-config.yaml

echo "Fixing Loki data privileges..."
chown -R 10001:10001 /loki/data

echo "Copying Alloy config..."
mkdir -p /etc/alloy
cp /monitoring/config.alloy /etc/alloy/config.alloy

echo "Copying Grafana config..."
mkdir -p /etc/grafana/provisioning/datasources
cp /monitoring/grafana-datasources.yaml /etc/grafana/provisioning/datasources/datasources.yaml

mkdir -p /etc/grafana/provisioning/dashboards/json
cp /monitoring/grafana-dashboards.yaml /etc/grafana/provisioning/dashboards/dashboards.yaml
cp /monitoring/dashboards/*.json /etc/grafana/provisioning/dashboards/json/

echo "Copying Prometheus config..."
mkdir -p /etc/prometheus
cp /monitoring/prometheus/prometheus.yml /etc/prometheus/prometheus.yml

echo "Copying Blackbox config..."
mkdir -p /config
cp /monitoring/blackbox.yml /config/blackbox.yml

echo "Configuration initialization complete."