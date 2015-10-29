#!/bin/bash

# Will fail on default OSX, use gsed provided by Homebrew or edit first line manually to be #!/system/bin/sh
_SED=gsed

rm -rf package.zip
rm -rf package
mkdir package

rsync -aqP expat-2.1.0/build/ package/
rsync -aqP libevent-2.0.22-stable/build/ package/
rsync -aqP openssl-1.0.2d/build/ package/
rsync -aqP unbound-1.5.4/build/ package/
cp unbound.conf package/bin/

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

$_SED -i "1s/.*/#\!\/system\/bin\/sh/" package/bin/unbound-control-setup 
$_SED -i "1s/.*/#\!\/system\/bin\/sh/" package/bin/ssl/misc/c_hash 
$_SED -i "1s/.*/#\!\/system\/bin\/sh/" package/bin/ssl/misc/c_info
$_SED -i "1s/.*/#\!\/system\/bin\/sh/" package/bin/ssl/misc/c_issuer
$_SED -i "1s/.*/#\!\/system\/bin\/sh/" package/bin/ssl/misc/c_name

zip -rq package.zip package
