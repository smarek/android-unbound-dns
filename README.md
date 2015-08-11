# Android Unbound DNS

Goal is to get Unbound DNS fully working with UI to configure it and system integration (replacing system DNS)


**Current State**:  

I've managed to compile Unbound DNS in version 1.5.4 against OpenSSL 1.0.2d, LibEvent 2.0.22-stable and Expat 2.1.0, for ARMv7 (armv7 or armv71) platform


**Step-By-Step Guide**:  

Requirements:

  - Android NDK r10d
  - Download libraries into fetched repository
    - expat-2.1.0.tar.gz (ie. http://kent.dl.sourceforge.net/project/expat/expat/2.1.0/expat-2.1.0.tar.gz )
    - openssl-1.0.2d.tar.gz (ie. https://www.openssl.org/source/openssl-1.0.2d.tar.gz )
    - libevent-2.0.22-stable.tar.gz (ie. https://github.com/libevent/libevent/releases/download/release-2.0.22-stable/libevent-2.0.22-stable.tar.gz )
  - Download Unbound into fetched repository
    - unbound-1.5.4.tar.gz (ie. https://unbound.net/downloads/unbound-1.5.4.tar.gz )
  - Expand all archives
    - tar xvf expat-2.1.0.tar.gz && tar xvf openssl-1.0.2d.tar.gz && tar xvf libevent-2.0.22-stable.tar.gz && tar xvf unbound-1.5.4.tar.gz
  - Launch the build script
    - for bash - ./build.sh
    - for fish - ./build.fish
  - Collect all libraries and binaries using packaging script
    - ./package.sh
  - Final compiled binaries and libraries are put within "package" directory in build root

**Running**:  

  - # Upload the package to android device
  - `adb push package /data/local/tmp/`
  - # Get shell
  - `adb shell`
  - # Get to package location
  - `cd /data/local/tmp/`
  - # Set LD_LIBRARY_PATH for binaries to see the linked ssl/event/expat/...
  - `export LD_LIBRARY_PATH=/data/local/tmp/lib/`
  - # Make library symlinks, because `adb push` wont copy those
  - `cd lib`
  - `ln -s libexpat.so.1.6.0 libexpat.so.1`
  - `ln -s libunbound.so.2.3.7 libunbound.so.2`
  - `cd /data/local/tmp/`
  - # Run Unbound apps
  - `./sbin/unbound -h`
  - `./sbin/unbound-anchor -h`
  - `./sbin/unbound-control -h`
  - `./sbin/unbound-checkconf`
  - `./sbin/unbound-host -h`
