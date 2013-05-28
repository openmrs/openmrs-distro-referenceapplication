class tomcat7 {
include aptitude
  package { "tomcat7" :
    ensure  => "present",
    require => Class[ "aptitude" ],
  }
  file { "/etc/default/tomcat7" :
    ensure  => present,
    mode    => 644,
    owner   => "root",
    group   => "root",
    source  => "puppet:///modules/tomcat7/etc/default/tomcat7",
    require => Package[ "tomcat7" ],
    notify  => Service[ "tomcat7"],
  }
  service { "tomcat7" :
    ensure  => running,
    require => Package[ "tomcat7"],
  }
  file { "/var/lib/tomcat7" :
    ensure  => present,
    owner   => "tomcat7",
    group   => "tomcat7",
    require => Package[ "tomcat7"],
  }
}