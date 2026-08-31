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
# Clear stale dashboards so ones removed from source don't linger in the volume
rm -f /etc/grafana/provisioning/dashboards/json/*.json
cp /monitoring/grafana/dashboards/*.json /etc/grafana/provisioning/dashboards/json/

echo "Copying Grafana alerting rules..."
mkdir -p /etc/grafana/provisioning/alerting
# Clear stale rule files so ones removed from source don't linger in the volume
rm -f /etc/grafana/provisioning/alerting/*.yaml
cp /monitoring/grafana/alerting/*.yaml /etc/grafana/provisioning/alerting/

echo "Copying Prometheus config..."
cp /monitoring/prometheus/prometheus.yml /etc/prometheus/prometheus.yml

echo "Copying Blackbox config..."
mkdir -p /config
cp /monitoring/blackbox.yml /config/blackbox.yml

echo "Setting up jmx exporter..."
mkdir -p /jmx-exporter-data/
# The agent jar is baked into this image at build time (see Dockerfile); copy it into the
# shared volume so the backend can load it as a -javaagent. Install it under a stable,
# unversioned name so the -javaagent path in docker-compose.grafana.yml never has to track
# JMX_EXPORTER_VERSION — if that path pointed at a jar the volume didn't have, the backend
# JVM would refuse to start, so a monitoring-only version bump would take the app down.
# Clear stale jars so a previously pinned version doesn't linger in the volume.
rm -f /jmx-exporter-data/*.jar
cp /opt/jmx-exporter/jmx_prometheus_javaagent-*.jar /jmx-exporter-data/jmx_prometheus_javaagent.jar
cp /monitoring/jmx_config.yml /jmx-exporter-data/jmx_config.yml

echo "Configuration initialization complete."
