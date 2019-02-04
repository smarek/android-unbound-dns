#!/bin/bash

# Local environment configuration, can be placed in .bash_profile (or .bashrc or whatever is bash using on your system)
test -z "$LOCAL_ANDROID_NDK_HOME" && export LOCAL_ANDROID_NDK_HOME=""
test -z "$LOCAL_ANDROID_NDK_HOST_PLATFORM" && export LOCAL_ANDROID_NDK_HOST_PLATFORM=""

# functions:
error ( ) {
        echo "$0 fatal error: $1"
        exit 1
}

export _UNBOUND_NAME=unbound-1.8.3
export _LIBEVENT_NAME=libevent-2.1.8-stable
export _OPENSSL_NAME=openssl-1.1.1a
export _EXPAT_NAME=expat-2.2.6

export _UNBOUND_URL="https://nlnetlabs.nl/downloads/unbound/unbound-1.8.3.tar.gz"
export _LIBEVENT_URL="https://github.com/libevent/libevent.git"
export _OPENSSL_URL="https://www.openssl.org/source/openssl-1.1.1a.tar.gz"
export _EXPAT_URL="https://github.com/libexpat/libexpat/releases/download/R_2_2_6/expat-2.2.6.tar.bz2"

export _UNBOUND_URL_SIGNATURE="https://nlnetlabs.nl/downloads/unbound/unbound-1.8.3.tar.gz.asc"
export _UNBOUND_URL_SIGNATURE_FINGERPRINT="EDFAA3F2CA4E6EB05681AF8E9F6F1C2D7E045F8D"
export _OPENSSL_URL_SIGNATURE="https://www.openssl.org/source/openssl-1.1.1a.tar.gz.asc"
export _OPENSSL_URL_SIGNATURE_FINGERPRINT="8657ABB260F056B1E5190839D9C4D26D0E604491"
export _EXPAT_URL_SIGNATURE="https://github.com/libexpat/libexpat/releases/download/R_2_2_6/expat-2.2.6.tar.bz2.asc"
export _EXPAT_URL_SIGNATURE_FINGERPRINT="3D7E959D89FACFEE38371921B00BC66A401A1600"
export _LIBEVENT_URL_SIGNATURE=""
export _LIBEVENT_URL_SIGNATURE_FINGERPRINT=""
export _LIBEVENT_GIT_TAG="release-2.1.8-stable"

export ANDROID_NDK_HOME="$LOCAL_ANDROID_NDK_HOME"
test -n "$ANDROID_NDK_HOME" || error "Fill your NDK HOME in _setenv_android.bash (or set env variable LOCAL_ANDROID_NDK_HOME)"

export ANDROID_NDK_PLATFORM=28
export ANDROID_NDK_ARCH=arm
export ANDROID_NDK_TARGET=arm-linux-androideabi-4.9
export TOOLCHAIN=llvm

export ANDROID_NDK_HOST_PLATFORM="$LOCAL_ANDROID_NDK_HOST_PLATFORM"
test -n "$ANDROID_NDK_HOST_PLATFORM" || error "fill in your host platform, ie. linux_x86_64 or darwin-x86_64, in _setenv_android.bash (or set env variable LOCAL_ANDROID_NDK_HOST_PLATFORM)"

export ANDROID_NDK_ROOT=$ANDROID_NDK_HOME
export ANDROID_NDK_SYSROOT=$ANDROID_NDK_HOME/platforms/android-$ANDROID_NDK_PLATFORM/arch-$ANDROID_NDK_ARCH
export ANDROID_DEV=$ANDROID_NDK_SYSROOT/usr
export ANDROID_SYSROOT=$ANDROID_NDK_SYSROOT
export ANDROID_TARGET_PLATFORM=arm-linux-androideabi

# For openssl-fips config
export _ANDROID_EABI=$ANDROID_NDK_TARGET
export _ANDROID_NDK=$ANDROID_NDK_HOME
export _ANDROID_API=android-$ANDROID_NDK_PLATFORM
export MACHINE=armv7
export RELEASE=2.6.37
export SYSTEM=android
export ARCH=arm
export HOSTCC=gcc

export CROSS_COMPILE=arm-linux-androideabi-
export CC=clang
export AR=$ANDROID_TARGET_PLATFORM-ar
export CPP=clang
export CXXCPP=clang
export CXX=clang++
export LD=$ANDROID_TARGET_PLATFORM-ld
export NM=$ANDROID_TARGET_PLATFORM-nm
export RANLIB=$ANDROID_TARGET_PLATFORM-ranlib
export CPPFLAGS="--sysroot=$ANDROID_NDK_SYSROOT -fPIC -pie -O3 -fpic"
export CXXFLAGS="--sysroot=$ANDROID_NDK_SYSROOT -fPIC -pie -O3 -fpic"
export CFLAGS="--sysroot=$ANDROID_NDK_SYSROOT -fPIC -pie -O3 -fpic"
export LDFLAGS="--sysroot=$ANDROID_NDK_SYSROOT -fPIC -pie -O3 -fpic"
export PATH=$ANDROID_NDK_HOME/toolchains/$TOOLCHAIN/prebuilt/$ANDROID_NDK_HOST_PLATFORM/bin:$PATH
export ANDROID_NDK=$_ANDROID_NDK
export CROSS_SYSROOT=$ANDROID_NDK_SYSROOT
