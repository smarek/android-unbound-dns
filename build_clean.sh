#!/bin/bash

echo "Cleaning build folders"

[ -f _setenv_android.bash ] || { echo "Could not find _setenv_android.bash file" ; exit 1 ; }

source _setenv_android.bash || { echo "Could not set enviroment variables" ; exit 1 ;}

test_del ( ) {
	echo "test_del $1"
        test ! -e $1 || rm -rf $1
}

test_del $_OPENSSL_NAME/build
test_del $_LIBEVENT_NAME/build
test_del $_EXPAT_NAME/build
test_del $_UNBOUND_NAME/build
