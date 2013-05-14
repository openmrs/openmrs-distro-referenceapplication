class tomcat7 {
include aptitude
  package { "tomcat7" :
    ensure  => "present",
    require => Class[ "aptitude" ],
  }
}