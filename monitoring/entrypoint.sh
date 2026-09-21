#!/bin/sh
set -e

echo "Initializing monitoring configuration..."

MONITORING_MODE="${MONITORING_MODE:-bundled}"
ALLOY_OTLP_PROTOCOL="${ALLOY_OTLP_PROTOCOL:-http}"

echo "Monitoring mode: ${MONITORING_MODE}"

#
# Alloy
#

echo "Copying Alloy config..."
mkdir -p /etc/alloy

# Clear configs from a previous mode/version
rm -f /etc/alloy/*.alloy

case "${MONITORING_MODE}" in
  bundled)
    cp /monitoring/alloy/logs.alloy /etc/alloy/logs.alloy
    cp /monitoring/alloy/prometheus.alloy /etc/alloy/prometheus.alloy
    cp /monitoring/alloy/cadvisor-bundled.alloy /etc/alloy/cadvisor-bundled.alloy
    ;;

  otlp)
    if [ -z "${ALLOY_OTLP_ENDPOINT:-}" ]; then
      echo "ERROR: ALLOY_OTLP_ENDPOINT is required when MONITORING_MODE=otlp"
      exit 1
    fi

    cp /monitoring/alloy/openmrs.alloy /etc/alloy/openmrs.alloy
    cp /monitoring/alloy/cadvisor-otlp.alloy /etc/alloy/cadvisor.alloy

    case "${ALLOY_OTLP_PROTOCOL}" in
      grpc)
        cp /monitoring/alloy/external-otlp-grpc.alloy \
          /etc/alloy/export.alloy
        ;;

      http)
        cp /monitoring/alloy/external-otlp-http.alloy \
          /etc/alloy/export.alloy
        ;;

      *)
        echo "ERROR: Unsupported ALLOY_OTLP_PROTOCOL: ${ALLOY_OTLP_PROTOCOL}"
        echo "Supported values: grpc, http"
        exit 1
        ;;
    esac

    # The two exporters expect different endpoint formats, so validate the
    # endpoint against the protocol that was selected above.
    case "${ALLOY_OTLP_PROTOCOL}" in
      http)
        case "${ALLOY_OTLP_ENDPOINT}" in
          http://*|https://*) ;;
          *)
            echo "ERROR: ALLOY_OTLP_ENDPOINT must start with http:// or https:// when ALLOY_OTLP_PROTOCOL=http"
            echo "Got: ${ALLOY_OTLP_ENDPOINT}"
            exit 1
            ;;
        esac

        case "${ALLOY_OTLP_ENDPOINT}" in
          */v1/metrics|*/v1/metrics/)
            echo "WARNING: ALLOY_OTLP_ENDPOINT ends with /v1/metrics, which the exporter appends itself."
            echo "Use the base URL instead, e.g. https://otlp-gateway-prod-eu-west-2.grafana.net/otlp"
            ;;
        esac
        ;;

      grpc)
        # The gRPC exporter takes a bare host:port, so drop any scheme first.
        otlp_host_port="${ALLOY_OTLP_ENDPOINT#*://}"

        case "${otlp_host_port}" in
          */*)
            echo "ERROR: ALLOY_OTLP_ENDPOINT must not contain a path when ALLOY_OTLP_PROTOCOL=grpc"
            echo "Got: ${ALLOY_OTLP_ENDPOINT}"
            exit 1
            ;;
        esac

        case "${otlp_host_port}" in
          ?*:[0-9]*) ;;
          *)
            echo "ERROR: ALLOY_OTLP_ENDPOINT must be host:port when ALLOY_OTLP_PROTOCOL=grpc"
            echo "Expected something like: collector.example.org:4317"
            echo "Got: ${ALLOY_OTLP_ENDPOINT}"
            exit 1
            ;;
        esac
        ;;
    esac
    ;;

  *)
    echo "ERROR: Unsupported MONITORING_MODE: ${MONITORING_MODE}"
    echo "Supported values: bundled, otlp"
    exit 1
    ;;
esac


#
# Bundled monitoring
#

if [ "${MONITORING_MODE}" = "bundled" ]; then
  echo "Copying Loki config..."
  mkdir -p /etc/loki
  cp /monitoring/loki-config.yaml /etc/loki/local-config.yaml

  echo "Fixing Loki data privileges..."
  chown -R 10001:10001 /loki/data

  echo "Copying Grafana config..."
  mkdir -p /etc/grafana/provisioning/datasources
  cp /monitoring/grafana-datasources.yaml \
    /etc/grafana/provisioning/datasources/datasources.yaml

  mkdir -p /etc/grafana/provisioning/dashboards/json
  cp /monitoring/grafana-dashboards.yaml \
    /etc/grafana/provisioning/dashboards/dashboards.yaml

  # Clear stale dashboards so ones removed from source don't linger in the volume
  rm -f /etc/grafana/provisioning/dashboards/json/*.json
  cp /monitoring/grafana/dashboards/*.json \
    /etc/grafana/provisioning/dashboards/json/

  echo "Copying Prometheus config..."
  mkdir -p /etc/prometheus
  cp /monitoring/prometheus/prometheus.yml \
    /etc/prometheus/prometheus.yml

  echo "Copying Blackbox config..."
  mkdir -p /config
  cp /monitoring/blackbox.yml /config/blackbox.yml
fi

echo "Configuration initialization complete."

