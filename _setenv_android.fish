#!/usr/bin/env fish

set -x ANDROID_NDK_HOME {fill in your NDK path}
set -x ANDROID_NDK_PLATFORM 21
set -x ANDROID_NDK_ARCH arm
set -x ANDROID_NDK_TARGET arm-linux-androideabi-4.9
set -x ANDROID_NDK_HOST_PLATFORM {fill in your host platform, ie. darwin-x86_64}
set -x ANDROID_NDK_ROOT $ANDROID_NDK_HOME
set -x ANDROID_NDK_SYSROOT $ANDROID_NDK_HOME/platforms/android-$ANDROID_NDK_PLATFORM/arch-$ANDROID_NDK_ARCH
set -x ANDROID_DEV $ANDROID_NDK_SYSROOT/usr
set -x ANDROID_SYSROOT $ANDROID_NDK_SYSROOT
set -x ANDROID_TARGET_PLATFORM arm-linux-androideabi

# For openssl-fips config
set -x _ANDROID_EABI $ANDROID_NDK_TARGET
set -x _ANDROID_NDK $ANDROID_NDK_HOME
set -x _ANDROID_API android-$ANDROID_NDK_PLATFORM
set -x MACHINE armv7
set -x RELEASE 2.6.37
set -x SYSTEM android
set -x ARCH arm
set -x HOSTCC gcc

set -x CROSS_COMPILE arm-linux-androideabi-
set -x CC $ANDROID_TARGET_PLATFORM-gcc
set -x AR $ANDROID_TARGET_PLATFORM-gcc-ar
set -x CPP $ANDROID_TARGET_PLATFORM-cpp
set -x CXXCPP $ANDROID_TARGET_PLATFORM-cpp
set -x CXX $ANDROID_TARGET_PLATFORM-g++
set -x LD $ANDROID_TARGET_PLATFORM-ld
set -x NM $ANDROID_TARGET_PLATFORM-gcc-nm
set -x RANLIB $ANDROID_TARGET_PLATFORM-ranlib
set -x CPPFLAGS "--sysroot=$ANDROID_NDK_SYSROOT"
set -x CXXFLAGS "--sysroot=$ANDROID_NDK_SYSROOT"
set -x CFLAGS "--sysroot=$ANDROID_NDK_SYSROOT -fPIE -pie"
set -x LDFLAGS "--sysroot=$ANDROID_NDK_SYSROOT -fPIE -pie -Wl,Bstatic"
set -x PATH $ANDROID_NDK_HOME/toolchains/$ANDROID_NDK_TARGET/prebuilt/$ANDROID_NDK_HOST_PLATFORM/bin $PATH
