#This will cover all the stuff to make a vagrant box a functional app server.
node default {
  include tomcat7, mysql, maven2
}

#This is the CI Bamboo server for the Reference Application
node 'gw78.iu.xsede.org' {
  include browsers, maven2
}


