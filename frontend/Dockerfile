# syntax=docker/dockerfile:1
# Serves the SPA assembled by the sihsalus-esm init container (spa-data volume).
FROM nginx:1.25-alpine

RUN rm -rf /usr/share/nginx/html/*

COPY nginx.conf /etc/nginx/nginx.conf

CMD ["nginx", "-g", "daemon off;"]
