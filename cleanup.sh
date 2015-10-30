#!/bin/bash

source _setenv_android.bash

rm -rf $_UNBOUND_NAME $_LIBEVENT_NAME $_OPENSSL_NAME $_EXPAT_NAME

tar xvf $_UNBOUND_NAME.tar.gz || error "Cannot unpack $_UNBOUND_NAME.tar.gz"
tar xvf $_LIBEVENT_NAME.tar.gz || error "Cannot unpack $_LIBEVENT_NAME.tar.gz"
tar xvf $_OPENSSL_NAME.tar.gz || error "Cannot unpack $_OPENSSL_NAME.tar.gz"
tar xvf $_EXPAT_NAME.tar.gz || error "Cannot unpack $_EXPAT_NAME.tar.gz"
