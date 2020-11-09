FROM httpd:2.4

COPY httpd.conf /usr/local/apache2/conf/httpd.conf

RUN rm -rf /usr/local/apache2/conf/extra/*
COPY proxy.conf /usr/local/apache2/conf/extra/proxy.conf
