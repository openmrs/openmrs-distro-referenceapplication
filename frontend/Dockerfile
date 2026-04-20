# syntax=docker/dockerfile:1
# Serves the SPA assembled by the sihsalus-esm init container (spa-data volume).
# On startup, brand assets are copied into the shared volume before nginx begins.
FROM nginx:1.25-alpine

RUN apk update && apk upgrade && apk add --no-cache moreutils

RUN rm -rf /usr/share/nginx/html/*

COPY startup.sh /usr/local/bin/startup.sh
RUN chmod +x /usr/local/bin/startup.sh

COPY nginx.conf /etc/nginx/nginx.conf

# Brand assets — written to the spa volume at startup so nginx can serve them
COPY src/main/resources/primary-logo.png /brand/
COPY src/main/resources/alternative-logo.png /brand/
COPY src/main/resources/login-logo.svg /brand/
COPY src/main/resources/favicon.ico /brand/

CMD ["/usr/local/bin/startup.sh"]
