# syntax=docker/dockerfile:1
# Slim nginx image — SPA files come from the sihsalus-esm init container via shared volume.
FROM nginx:1.28-alpine

RUN apk update && \
    apk upgrade && \
    apk add --no-cache moreutils

COPY nginx.conf /etc/nginx/nginx.conf
COPY startup.sh /usr/local/bin/startup.sh
RUN chmod +x /usr/local/bin/startup.sh

CMD ["/usr/local/bin/startup.sh"]
