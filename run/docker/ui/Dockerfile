FROM nginx:1.21-alpine

RUN apk update && \
    apk upgrade && \
    # add dumb-init so it's less silly to run startup.sh as our command
    # add more utils for sponge to support our startup script
    apk add --no-cache dumb-init moreutils

# clear any default files installed by nginx
RUN rm -rf /usr/share/nginx/html/*

COPY startup.sh /usr/local/bin/startup.sh
RUN chmod +x /usr/local/bin/startup.sh

COPY nginx.conf /etc/nginx/nginx.conf

CMD ["dumb-init", "/usr/local/bin/startup.sh"]
