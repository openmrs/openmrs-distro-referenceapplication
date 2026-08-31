FROM alpine:3.23

# ca-certificates lets busybox wget download the JMX exporter jar over HTTPS
RUN apk add --no-cache ca-certificates

# Bake the JMX exporter agent into the image
ARG JMX_EXPORTER_VERSION=1.6.0
RUN mkdir -p /opt/jmx-exporter \
    && wget -q -O "/opt/jmx-exporter/jmx_prometheus_javaagent-${JMX_EXPORTER_VERSION}.jar" \
        "https://github.com/prometheus/jmx_exporter/releases/download/${JMX_EXPORTER_VERSION}/jmx_prometheus_javaagent-${JMX_EXPORTER_VERSION}.jar"

WORKDIR /monitoring

COPY . .
COPY entrypoint.sh /usr/local/bin/
RUN chmod +x /usr/local/bin/entrypoint.sh

ENTRYPOINT ["entrypoint.sh"]
