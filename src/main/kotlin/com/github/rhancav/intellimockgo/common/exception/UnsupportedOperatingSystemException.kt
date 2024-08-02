package com.github.rhancav.intellimockgo.common.exception

class UnsupportedOperatingSystemException(os: String) :
    Throwable("Unsupported Operating System $os")
