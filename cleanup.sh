#!/bin/bash

source _setenv_android.bash

echo "remove source folders ($_UNBOUND_NAME $_LIBEVENT_NAME $_OPENSSL_NAME $_EXPAT_NAME)"
rm -rf $_UNBOUND_NAME $_OPENSSL_NAME $_EXPAT_NAME

echo "Unpack $_UNBOUND_NAME.tar.gz into $_UNBOUND_NAME"
tar xf $_UNBOUND_NAME.tar.gz || error "Cannot unpack $_UNBOUND_NAME.tar.gz"
echo "Unpack $_OPENSSL_NAME.tar.gz into $_OPENSSL_NAME"
tar xf $_OPENSSL_NAME.tar.gz || error "Cannot unpack $_OPENSSL_NAME.tar.gz"
echo "Unpack $_EXPAT_NAME.tar.gz into $_EXPAT_NAME"
tar xf $_EXPAT_NAME.tar.bz2 || error "Cannot unpack $_EXPAT_NAME.tar.gz"
echo "Unpack $_LIBEVENT_NAME into $_LIBEVENT_NAME"
cd $_LIBEVENT_NAME
git clean -xdf
git checkout $_LIBEVENT_GIT_TAG
echo "cleanup.sh finished successfully"
