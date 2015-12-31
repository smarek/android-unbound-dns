#!/bin/bash

source _setenv_android.bash

curl -L $_EXPAT_URL -o $_EXPAT_NAME.tar.gz
curl -L $_OPENSSL_URL -o $_OPENSSL_NAME.tar.gz
curl -L $_LIBEVENT_URL -o $_LIBEVENT_NAME.tar.gz
curl -L $_UNBOUND_URL -o $_UNBOUND_NAME.tar.gz
