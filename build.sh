#!/bin/bash -x

# setup environment
source _setenv_android.bash

# clean build folders if env requires it
test -n "$CLEAN_BEFORE_BUILD" && source build_clean.sh || echo "TIP: set CLEAN_BEFORE_BUILD variable to run ./build_clean.sh before build"

# Update environment variables
export CC=clang

# Build OpenSSL library

cd $_OPENSSL_NAME
test -d build || mkdir build
echo "OpenSSL configure"
#./Configure no-ssl2 no-ssl3 no-shared no-zlib no-comp no-hw -fPIC -pie -fpic --install_prefix=`pwd`/build/ --prefix=".//" --openssldir="ssl" android &> configure.log
./Configure no-ssl no-shared no-zlib no-comp no-hw -FPIC -pie -fpic --prefix=`pwd`/build --openssldir="ssl" android-armeabi &> configure.log
echo "OpenSSL make"
make -j depend &> make.depend.log
make -j &> make.log
echo "OpenSSL make install_sw"
make -j install_sw &> make.install_sw.log
make -j install_sw 2>&1 >> make.install_sw.log
cd ..
# Update environment variables
export CC=clang

# Build Expat library (LibExpat)
cd $_EXPAT_NAME
mkdir -p build
mkdir -p prefix
echo "Expat configure"
cd build
cmake -DCMAKE_TOOLCHAIN_FILE=$ANDROID_NDK/build/cmake/android.toolchain.cmake -DBUILD_shared=OFF -DBUILD_examples=OFF -DBUILD_doc=OFF -DBUILD_tests=OFF -DCMAKE_INSTALL_PREFIX=`pwd`/../prefix ..
echo "Expat make"
make -j &> make.log
echo "Expat make install"
make -j install &> make.install.log
cd ../..

# Build Libevent
export CFLAGS="--sysroot=$ANDROID_SYSROOT -pie -fPIE -march=armv7-a -mfloat-abi=softfp -mfpu=vfpv3-d16"
cd $_LIBEVENT_NAME
mkdir -p build
mkdir -p prefix
echo "LibEvent configure"
cd build
cmake -DANDROID_PLATFORM=23 -DCMAKE_TOOLCHAIN_FILE=$ANDROID_NDK/build/cmake/android.toolchain.cmake -DBUILD_TESTING=OFF -DCMAKE_INSTALL_PREFIX=`pwd`/../prefix -DOPENSSL_ROOT_DIR=`pwd`/../../$_OPENSSL_NAME/build/ -DOPENSSL_LIBRARIES=`pwd`/../../$_OPENSSL_NAME/build/lib/ -DOPENSSL_INCLUDE_DIR=`pwd`/../../$_OPENSSL_NAME/build/include/ ..
echo "LibEvent make"
make -j &> make.log
echo "LibEvent make install"
make -j install &> make.install.log
cd ../..

# setup environment
unset CC
source _setenv_android.bash

# Configure and build Unbound
cd $_UNBOUND_NAME
test -d build || mkdir build
echo "Unbound configure"
./configure --prefix=`pwd`/build --with-sysroot=$ANDROID_NDK_SYSROOT --host=arm-linux-androideabi --with-ssl=`pwd`/../$_OPENSSL_NAME/build/ --with-libexpat=`pwd`/../$_EXPAT_NAME/prefix/ --with-libevent=`pwd`/../$_LIBEVENT_NAME/prefix/ --enable-checking --with-pthreads --with-pic --with-run-dir="." --with-pidfile="unbound.pid" --with-chroot-dir="." --with-conf-file="unbound.conf" --with-rootkey-file="root.key" --disable-shared --disable-flto &> configure.log
sed -i "s/#define HAVE_GETPWNAM 1/#undef HAVE_GETPWNAM/" config.h
echo "Unbound make"
make -j &> make.log
echo "Unbound make install"
make -j install &> make.install.log
bash

echo "Build finished"
echo "TIP: Create deployment package by running ./package.sh"
