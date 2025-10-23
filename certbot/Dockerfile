# syntax=docker/dockerfile:1.3
FROM certbot/certbot:v4.0.0

RUN apk --no-cache add curl

COPY ./scripts /certbot/scripts
ENTRYPOINT [ "/certbot/scripts/entrypoint.sh" ]