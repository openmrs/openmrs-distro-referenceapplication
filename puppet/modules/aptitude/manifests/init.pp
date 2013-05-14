class aptitude {
  exec { "updateApt":
    command => "/usr/bin/apt-get update",
  }
}