#!/bin/sh

echo "Cleaning build folders"

source _setenv_android.bash

test_del ( ) {
	echo "test_del $1"
        test ! -e $1 || rm -rf $1
}

test_del $_OPENSSL_NAME/build
test_del $_LIBEVENT_NAME/build
test_del $_EXPAT_NAME/build
test_del $_UNBOUND_NAME/build
