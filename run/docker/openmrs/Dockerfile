FROM tomcat:7-jre8

RUN apt update \
  && apt install -y mysql-client gettext-base

COPY setenv.sh /usr/local/tomcat/bin/setenv.sh

COPY wait-for-it.sh /usr/local/tomcat/wait-for-it.sh
COPY startup.sh /usr/local/tomcat/startup.sh

RUN rm -rf /usr/local/tomcat/webapps

RUN sed -i '/Connector port="8080"/a URIEncoding="UTF-8" relaxedPathChars="[]|" relaxedQueryChars="[]|{}^&#x5c;&#x60;&quot;&lt;&gt;"' /usr/local/tomcat/conf/server.xml

RUN chmod +x /usr/local/tomcat/wait-for-it.sh
RUN chmod +x /usr/local/tomcat/startup.sh

CMD ["/usr/local/tomcat/startup.sh"]
