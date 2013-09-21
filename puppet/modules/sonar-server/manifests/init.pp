class sonar-server {
  file { "/home/bamboo/.m2/settings.xml":
    ensure => present,
    source => "puppet:///modules/sonar-server/home/bamboo/.m2/settings.xml",
    owner  => "bamboo",
    group  => "bamboo",
    mode   => 644
  }


}