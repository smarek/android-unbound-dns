#!/bin/bash

source _setenv_android.bash

echo "remove source folders ($_UNBOUND_NAME $_LIBEVENT_NAME $_OPENSSL_NAME $_EXPAT_NAME)"
rm -rf $_UNBOUND_NAME $_OPENSSL_NAME $_EXPAT_NAME

echo "Unpack $_UNBOUND_NAME.tar.gz into $_UNBOUND_NAME"
tar xf $_UNBOUND_NAME.tar.gz || error "Cannot unpack $_UNBOUND_NAME.tar.gz"
echo "Unpack $_OPENSSL_NAME.tar.gz into $_OPENSSL_NAME"
tar xf $_OPENSSL_NAME.tar.gz || error "Cannot unpack $_OPENSSL_NAME.tar.gz"
echo "Unpack $_EXPAT_NAME.tar.gz into $_EXPAT_NAME"
tar xf $_EXPAT_NAME.tar.gz || error "Cannot unpack $_EXPAT_NAME.tar.gz"
echo "Reseting $_LIBEVENT_NAME to known state"
cd $_LIBEVENT_NAME
git reset --hard
git checkout release-2.0.22-stable
git apply < ../libevent-2.0.22-stable.patch
cd ..

echo "cleanup.sh finished successfully"
