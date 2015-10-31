#!/bin/bash

# setup environment
source _setenv_android.bash

# clean build folders if env requires it
test ! -n "$CLEAN_BEFORE_BUILD" || source build_clean.sh

# Update environment variables
export CC=gcc

# Build OpenSSL library

cd $_OPENSSL_NAME
test ! -x build || mkdir build
./Configure no-ssl2 no-ssl3 no-shared no-zlib no-comp no-hw -fPIC -pie -fpic --install_prefix=`pwd`/build/ --prefix=".//" --openssldir="ssl" android
make -j4 depend
make -j4
make -j4 install_sw
make -j4 install_sw
cd ..

# Update environment variables
export CC=arm-linux-androideabi-gcc

# Build Expat library (LibExpat)
cd $_EXPAT_NAME
test ! -x build || mkdir build
./configure --prefix=`pwd`/build --with-sysroot=$ANDROID_SYSROOT --host=arm-linux-androideabi --disable-shared
make -j4
make -j4 install
cd ..

# Build Libevent
export CFLAGS="--sysroot=$ANDROID_SYSROOT -pie -fPIE -march=armv7-a -mfloat-abi=softfp -mfpu=vfpv3-d16"
cd $_LIBEVENT_NAME
test ! -x build || mkdir build
./configure --with-sysroot=$ANDROID_SYSROOT --host=arm-linux-androideabi --prefix=`pwd`/build/ --disable-shared
make -j4
make -j4 install
cd ..

# setup environment
unset CC
source _setenv_android.bash

# Configure and build Unbound
cd $_UNBOUND_NAME
test ! -x build || mkdir build
./configure --prefix=`pwd`/build --with-sysroot=$ANDROID_NDK_SYSROOT --host=arm-linux-androideabi --with-ssl=`pwd`/../openssl-1.0.2d/build/ --with-libexpat=`pwd`/../expat-2.1.0/build/ --with-libevent=`pwd`/../libevent-2.0.22-stable/build/ --enable-checking --with-pthreads --with-pic --with-run-dir="." --with-pidfile="unbound.pid" --with-chroot-dir="." --with-conf-file="unbound.conf" --with-rootkey-file="root.key"
make -j4
make -j4 install
