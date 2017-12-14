class aptitude {
  exec { "updateApt":
    command => "/usr/bin/apt-get update",
    require => File[ "/etc/apt/sources.list"],
  }
  file { "/etc/apt/sources.list" :
    ensure => present,
    source => "puppet:///modules/aptitude/etc/apt/sources.list",
    owner  => "root",
    group  => "root",
    mode   => 644
  }
}