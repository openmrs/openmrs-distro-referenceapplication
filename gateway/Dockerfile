# syntax=docker/dockerfile:1.3
FROM nginx:1.25-alpine

ENV FRAME_ANCESTORS ""

COPY nginx.conf /etc/nginx/nginx.conf
COPY default.conf.template /etc/nginx/templates/

#COPY ./ssl/peruHCE-certificate.crt /etc/nginx/ssl/peruHCE-certificate.crt
#COPY ./ssl/peruHCE-certificate.key /etc/nginx/ssl/peruHCE-certificate.key
