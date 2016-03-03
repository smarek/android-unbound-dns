#!/bin/bash

# Local environment configuration, can be placed in .bash_profile (or .bashrc or whatever is bash using on your system)
test -z "$LOCAL_ANDROID_NDK_HOME" && export LOCAL_ANDROID_NDK_HOME=""
test -z "$LOCAL_ANDROID_NDK_HOST_PLATFORM" && export LOCAL_ANDROID_NDK_HOST_PLATFORM=""

# functions:
error ( ) {
        echo "$0 fatal error: $1"
        exit 1
}

export _UNBOUND_NAME=unbound-1.5.7
export _LIBEVENT_NAME=libevent-2.0.22-stable
export _OPENSSL_NAME=openssl-1.0.2g
export _EXPAT_NAME=expat-2.1.0

export _UNBOUND_URL="https://unbound.net/downloads/unbound-1.5.7.tar.gz"
export _LIBEVENT_URL="https://github.com/libevent/libevent/releases/download/release-2.0.22-stable/libevent-2.0.22-stable.tar.gz"
export _OPENSSL_URL="https://www.openssl.org/source/openssl-1.0.2g.tar.gz"
export _EXPAT_URL="http://kent.dl.sourceforge.net/project/expat/expat/2.1.0/expat-2.1.0.tar.gz"

export ANDROID_NDK_HOME="$LOCAL_ANDROID_NDK_HOME"
test -n "$ANDROID_NDK_HOME" || error "Fill your NDK HOME in _setenv_android.bash (var LOCAL_ANDROID_NDK_HOME)"

export ANDROID_NDK_PLATFORM=21
export ANDROID_NDK_ARCH=arm
export ANDROID_NDK_TARGET=arm-linux-androideabi-4.9

export ANDROID_NDK_HOST_PLATFORM="$LOCAL_ANDROID_NDK_HOST_PLATFORM"
test -n "$ANDROID_NDK_HOST_PLATFORM" || error "fill in your host platform, ie. darwin-x86_64, in _setenv_android.bash (var LOCAL_ANDROID_NDK_HOST_PLATFORM)"

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
export CC=$ANDROID_TARGET_PLATFORM-gcc
export AR=$ANDROID_TARGET_PLATFORM-gcc-ar
export CPP=$ANDROID_TARGET_PLATFORM-cpp
export CXXCPP=$ANDROID_TARGET_PLATFORM-cpp
export CXX=$ANDROID_TARGET_PLATFORM-g++
export LD=$ANDROID_TARGET_PLATFORM-ld
export NM=$ANDROID_TARGET_PLATFORM-gcc-nm
export RANLIB=$ANDROID_TARGET_PLATFORM-ranlib
export CPPFLAGS="--sysroot=$ANDROID_NDK_SYSROOT -fPIC -pie -O3 -fpic"
export CXXFLAGS="--sysroot=$ANDROID_NDK_SYSROOT -fPIC -pie -O3 -fpic"
export CFLAGS="--sysroot=$ANDROID_NDK_SYSROOT -fPIC -pie -O3 -fpic"
export LDFLAGS="--sysroot=$ANDROID_NDK_SYSROOT -fPIC -pie -O3 -fpic"
export PATH=$ANDROID_NDK_HOME/toolchains/$ANDROID_NDK_TARGET/prebuilt/$ANDROID_NDK_HOST_PLATFORM/bin:$PATH
