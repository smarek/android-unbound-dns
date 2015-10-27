#!/bin/bash -x

export ANDROID_NDK_HOME={fill in your NDK path}
export ANDROID_NDK_PLATFORM=21
export ANDROID_NDK_ARCH=arm
export ANDROID_NDK_TARGET=arm-linux-androideabi-4.9
export ANDROID_NDK_HOST_PLATFORM={fill in your host platform, ie. darwin-x86_64}
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
export CPPFLAGS="--sysroot=$ANDROID_NDK_SYSROOT"
export CXXFLAGS="--sysroot=$ANDROID_NDK_SYSROOT"
export CFLAGS="--sysroot=$ANDROID_NDK_SYSROOT -fPIC -pie"
export LDFLAGS="--sysroot=$ANDROID_NDK_SYSROOT -fPIC -pie"
export PATH=$ANDROID_NDK_HOME/toolchains/$ANDROID_NDK_TARGET/prebuilt/$ANDROID_NDK_HOST_PLATFORM/bin:$PATH
