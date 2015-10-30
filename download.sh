#!/bin/bash

source _setenv_android.bash

curl -L "http://kent.dl.sourceforge.net/project/expat/expat/2.1.0/expat-2.1.0.tar.gz" -o $_EXPAT_NAME.tar.gz
curl -L "https://www.openssl.org/source/openssl-1.0.2d.tar.gz" -o $_OPENSSL_NAME.tar.gz
curl -L "https://github.com/libevent/libevent/releases/download/release-2.0.22-stable/libevent-2.0.22-stable.tar.gz" -o $_LIBEVENT_NAME.tar.gz
curl -L "https://unbound.net/downloads/unbound-1.5.4.tar.gz" -o $_UNBOUND_NAME.tar.gz
