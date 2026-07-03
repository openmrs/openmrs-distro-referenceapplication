FROM alpine:3.23

# ca-certificates lets busybox wget download the JMX exporter jar over HTTPS
RUN apk add --no-cache ca-certificates

WORKDIR /monitoring

COPY . .
COPY entrypoint.sh /usr/local/bin/
RUN chmod +x /usr/local/bin/entrypoint.sh

ENTRYPOINT ["entrypoint.sh"]