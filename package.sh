#!/bin/bash

source _setenv_android.bash

# Will fail on default OSX, use gsed provided by Homebrew or edit first line manually to be #!/system/bin/sh
_SED=sed

# remove old files
rm -rf package.zip
rm -rf package
mkdir package

# copy over all built binaries and support files
rsync -aqP $_EXPAT_NAME/build/ package/
rsync -aqP $_LIBEVENT_NAME/build/ package/
rsync -aqP $_OPENSSL_NAME/build/ package/
rsync -aqP $_UNBOUND_NAME/build/ package/

# normalize binary paths
mv package/sbin/* package/bin/

# delete unnecessary stuff
rm -rf package/share/
rm -rf package/lib/pkgconfig/
rm -rf package/ssl/man/
rm -rf package/sbin
rm -rf package/include
rm -f package/bin/c_rehash
find package -name '*.a' -exec rm -f {} \;
find package -name '*.la' -exec rm -f {} \;
find package -name '*.py' -exec rm -f {} \;

# move ssl support folder to bin folder for sake of usability
mv package/ssl package/bin/

# put custom unbound.conf instead of default ones
cp unbound.conf package/bin/unbound.conf.default
cp env.sh package/bin/

# replace default shell with /system/bin/sh available android-wide
$_SED -i "1s/.*/#\!\/system\/bin\/sh/" package/bin/unbound-control-setup 
$_SED -i "1s/.*/#\!\/system\/bin\/sh/" package/bin/ssl/misc/c_hash 
$_SED -i "1s/.*/#\!\/system\/bin\/sh/" package/bin/ssl/misc/c_info
$_SED -i "1s/.*/#\!\/system\/bin\/sh/" package/bin/ssl/misc/c_issuer
$_SED -i "1s/.*/#\!\/system\/bin\/sh/" package/bin/ssl/misc/c_name

# Set binaries executable
chmod +x package/bin/*
# Create ZIP distribution package 
zip -rq package.zip package
# Copy ZIP over old one within Android sources
test -d Android/app/src/main/assets && cp package.zip Android/app/src/main/assets/package.zip
