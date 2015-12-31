#!/bin/bash

source _setenv_android.bash

echo "Downloading Expat from $_EXPAT_URL"
curl -s -L $_EXPAT_URL -o $_EXPAT_NAME.tar.gz || error "Could not download Expat from $_EXPAT_URL"
echo "Downloading OpenSSL from $_OPENSSL_URL"
curl -s -L $_OPENSSL_URL -o $_OPENSSL_NAME.tar.gz || error "Could not download OpenSSL from $_OPENSSL_URL"
echo "Downloading LibEvent from $_LIBEVENT_URL"
curl -s -L $_LIBEVENT_URL -o $_LIBEVENT_NAME.tar.gz || error "Could not download LibEvent from $_LIBEVENT_URL"
echo "Downloading Unbound from $_UNBOUND_URL"
curl -s -L $_UNBOUND_URL -o $_UNBOUND_NAME.tar.gz || error "Could not download Unbound from $_UNBOUND_URL"

echo "All Downloads finished successfully"
