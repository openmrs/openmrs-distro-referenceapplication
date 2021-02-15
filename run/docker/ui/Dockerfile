FROM httpd:2.4

RUN apt update \
  && apt install -y gettext-base moreutils curl

COPY httpd.conf /usr/local/apache2/conf/httpd.conf

RUN rm -rf /usr/local/apache2/conf/extra/*

COPY startup.sh /usr/local/apache2/startup.sh
RUN chmod +x /usr/local/apache2/startup.sh

CMD ["./startup.sh"]
