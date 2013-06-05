class refapp {
  include tomcat7
  file { "/usr/share/tomcat7/.OpenMRS/openmrs-runtime.properties" :
      ensure => present,
      owner  => "tomcat7",
      group  => "tomcat7",
      mode   => 644,
      source => "puppet:///modules/refapp/usr/share/tomcat7/.OpenMRS/openmrs-runtime.properties",
      notify => Service[ "tomcat7" ]

  }

}