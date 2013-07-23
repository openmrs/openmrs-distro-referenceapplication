class browsers {
  package { "chromium-browser":
    ensure => latest,
  }
  package { "firefox":
    ensure => latest,
  }
  package { "xvfb":
    ensure => present,
  }

}
