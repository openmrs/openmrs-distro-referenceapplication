class maven2 {
include aptitude

  package { "maven2" :
    ensure  =>  "present",
    require => Class[ "aptitude" ],
    }

}
