#!/bin/bash

source _setenv_android.bash

# Will fail on default OSX, use gsed provided by Homebrew or edit first line manually to be #!/system/bin/sh
_SED=gsed

rm -rf package.zip
rm -rf package
mkdir package

rsync -aqP $_EXPAT_NAME/build/ package/
rsync -aqP $_LIBEVENT_NAME/build/ package/
rsync -aqP $_OPENSSL_NAME/build/ package/
rsync -aqP $_UNBOUND_NAME/build/ package/

rm -rf package/share/
rm -rf package/lib/pkgconfig/
rm -rf package/ssl/man/
mv package/sbin/* package/bin/
rm -rf package/sbin
rm -rf package/include
rm -f package/bin/c_rehash
find package -name '*.a' -exec rm -f {} \;
find package -name '*.la' -exec rm -f {} \;
find package -name '*.py' -exec rm -f {} \;
mv package/ssl package/bin/

cp unbound.conf package/bin/unbound.conf.default
rm package/bin/unbound-control-setup
cp unbound-control-setup package/bin/
cp env.sh package/bin/

$_SED -i "1s/.*/#\!\/system\/bin\/sh/" package/bin/unbound-control-setup 
$_SED -i "1s/.*/#\!\/system\/bin\/sh/" package/bin/ssl/misc/c_hash 
$_SED -i "1s/.*/#\!\/system\/bin\/sh/" package/bin/ssl/misc/c_info
$_SED -i "1s/.*/#\!\/system\/bin\/sh/" package/bin/ssl/misc/c_issuer
$_SED -i "1s/.*/#\!\/system\/bin\/sh/" package/bin/ssl/misc/c_name

chmod +x package/bin/*

zip -rq package.zip package
