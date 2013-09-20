class maven2 {
include aptitude

  package { "maven2" :
    ensure  =>  "present",
    require => Class[ "aptitude" ],
    }
  package { "openjdk-6-jdk" :
    ensure  =>  "present",
    require => Class[ "aptitude" ],
  }
  file { "/home/bamboo/.m2/settings.xml":
    ensure => present,
    source => "puppet:///modules/maven2//home/bamboo/.m2/settings.xml",
    owner  => "bamboo",
    group  => "bamboo",
    mode   => 644
  }

}
