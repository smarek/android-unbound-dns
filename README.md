# Android Unbound DNS

Goal is to get Unbound DNS fully working with UI to configure it and system integration (replacing system DNS)


**Current State**:  

I've managed to compile Unbound DNS in version 1.5.4 against OpenSSL 1.0.2d, LibEvent 2.0.22-stable and Expat 2.1.0, for ARMv7 (armv7 or armv71) platform


**Step-By-Step Guide**:  

Requirements:

  - Android NDK r10d
  - Download Unbound and libraries into fetched repository
    - ./download.sh
  - Run cleanup (will remove old folders and unpack original archives)
    - ./cleanup.sh
  - Launch the build script
    - ./build.sh
  - Collect all libraries and binaries using packaging script
    - ./package.sh
  - Final compiled binaries and libraries are put within "package" directory in build root

**Running**:  

  - # Upload the package to android device
  - `adb push package.zip /data/local/tmp/`
  - # Get shell
  - `adb shell`
  - # Get to package location
  - `cd /data/local/tmp/`
  - # Expand zip archive
  - `unzip package.zip`
  - `cd /data/local/tmp/package/bin`
  - # Set Environment PATH variable
  - `./env.sh`
  - # Setup remote control certificate
  - `unbound-control-setup`
  - # Setup DNSSEC root key
  - `unbound-anchor -h`
  - # If you've edited unbound.conf, check it's correct
  - `unbound-checkconf`
  - # Run applications from `bin` folder
  - `unbound -v -c unbound.conf`
  - # Check if the server is running and unbound-control set up correctly
  - `unbound-control status`
  - # Check if the DNSSEC verification works correctly
  - `unbound-host -d -C unbound.conf google.com`
