class mysql {
include aptitude
##This password is only used for boxes created by Vagrant for developer testing.
$mysqlPassword = Admin123

  package { "mysql-client" :
    ensure  =>  "present",
    require => Class[ "aptitude" ],
    }
  package { "mysql-server" :
    ensure  =>  "present",
    require => Class[ "aptitude" ],
  }

  service { "mysql" :
    ensure => running,
    enable => true,
    require => Package["mysql-server"]
  }
  exec {"setmysqlpassword":
    command => "/usr/bin/mysqladmin -u root PASSWORD ${mysqlPassword}; /bin/true",
    require => [Package["mysql-server"], Package["mysql-client"] , Service["mysql"]]
  }
  file {"/etc/mysql/my.cnf" :
    ensure => present,
    owner  => "root",
    group  => "root",
    mode   => 644,
    source => "puppet:///modules/mysql/etc/mysql/my.cnf",
    notify => Service[ "mysql" ],
    require => Package["mysql-server"]

  }
}
