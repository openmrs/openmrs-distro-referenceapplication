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
cp /monitoring/dashboards/*.json /etc/grafana/provisioning/dashboards/json/

echo "Copying Prometheus config..."
mkdir -p /etc/prometheus/rules
cp /monitoring/prometheus/prometheus.yml /etc/prometheus/prometheus.yml
# Clear stale rule files so ones removed from source don't linger in the volume
rm -f /etc/prometheus/rules/*.yml
cp /monitoring/prometheus/alert.rules.yml /etc/prometheus/rules/alert.rules.yml

echo "Copying Alertmanager config..."
mkdir -p /etc/alertmanager
cp /monitoring/alertmanager/alertmanager.yml /etc/alertmanager/alertmanager.yml

echo "Copying Blackbox config..."
mkdir -p /config
cp /monitoring/blackbox.yml /config/blackbox.yml

echo "Setting up jmx exporter..."
JMX_EXPORTER_VERSION=1.6.0
JMX_AGENT_JAR="jmx_prometheus_javaagent-${JMX_EXPORTER_VERSION}.jar"
JMX_AGENT_URL="https://github.com/prometheus/jmx_exporter/releases/download/v${JMX_EXPORTER_VERSION}/${JMX_AGENT_JAR}"
mkdir -p /jmx-exporter-data/
# Download the agent jar. Skip if it's already in the volume.
# Download to a temp file and move on success so a failed download never leaves a broken jar.
if [ ! -f "/jmx-exporter-data/${JMX_AGENT_JAR}" ]; then
  echo "Downloading ${JMX_AGENT_JAR}..."
  wget -q -O "/jmx-exporter-data/${JMX_AGENT_JAR}.tmp" "${JMX_AGENT_URL}"
  mv "/jmx-exporter-data/${JMX_AGENT_JAR}.tmp" "/jmx-exporter-data/${JMX_AGENT_JAR}"
fi
cp /monitoring/jmx_exporter/jmx_config.yml /jmx-exporter-data/jmx_config.yml

echo "Configuration initialization complete."
