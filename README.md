# Android Unbound DNS [![Build Status](https://travis-ci.org/smarek/android-unbound-dns.svg)](https://travis-ci.org/smarek/android-unbound-dns)

![Android Unbound DNS logo](https://raw.githubusercontent.com/smarek/android-unbound-dns/master/Android/app/src/main/res/mipmap-xxxhdpi/ic_launcher.png "Android application Logo")

Goal is to get Unbound DNS fully working with UI to configure it and system integration (replacing system DNS)

**Step-By-Step Guide:**  

Compile Unbound DNS for Android:

  - Android NDK r11c
  - Set environment variables according to your local paths
    - required vars are LOCAL_ANDROID_NDK_HOME and LOCAL_ANDROID_NDK_HOST_PLATFORM, see _setenv_android.bash file for reference
    - optional var _NO_CHECK_SIGNATURE, set to non-zero value to skip GPG signatures checking
  - Download Unbound and libraries into fetched repository
    - ./download.sh
  - Run cleanup (will remove old folders and unpack original archives)
    - ./cleanup.sh
  - Launch the build script
    - ./build.sh
  - Collect all libraries and binaries using packaging script
    - ./package.sh
  - Final compiled binaries and libraries are put within "package" directory in build root

**Compile Android application:**  

  - Move to Android directory
    - cd Android
  - Check that package.zip is in place
    - ls -lsa app/src/main/assets/package.zip
  - Compile android app using Gradle
    - ./gradlew clean assemble -q -S
    - ./gradlew check -q -S
  - Install Android application to connected device
    - ./gradlew installDebug

**Running Unbound DNS standalone from ADB SHELL:**  

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
