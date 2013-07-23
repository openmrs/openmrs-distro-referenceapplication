class browsers {
  package { "chromium-browser":
    ensure => latest,
  }
  package { "firefox":
    ensure => latest,
  }
package { "xvfbfofofofof":
  ensure => present,
}

}
