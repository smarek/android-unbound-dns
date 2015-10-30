#!/bin/bash

source _setenv_android.bash

rm -rf $_UNBOUND_NAME $_LIBEVENT_NAME $_OPENSSL_NAME $_EXPAT_NAME
find . -maxdepth 1 -name '*.tar.gz' -exec tar xvf \{\} \;
