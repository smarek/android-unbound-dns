#!/bin/bash

source _setenv_android.bash

curl "http://kent.dl.sourceforge.net/project/expat/expat/2.1.0/expat-2.1.0.tar.gz" > $_EXPAT_NAME.tar.gz
curl "https://www.openssl.org/source/openssl-1.0.2d.tar.gz" > $_OPENSSL_NAME.tar.gz
curl "https://github.com/libevent/libevent/releases/download/release-2.0.22-stable/libevent-2.0.22-stable.tar.gz" > $_LIBEVENT_NAME.tar.gz
curl "https://unbound.net/downloads/unbound-1.5.4.tar.gz" > $_UNBOUND_NAME.tar.gz
