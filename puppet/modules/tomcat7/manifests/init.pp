class tomcat7 {
  package { "tomcat7" :
    ensure  =>  "present",
    require => Exec["updateApt"],
  }
  exec { "updateApt":
    command => "/usr/bin/apt-get update",
  }
}