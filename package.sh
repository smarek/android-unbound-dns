#!/bin/bash

rm -rf package.zip
rm -rf package
mkdir package

rsync -aP expat-2.1.0/build/ package/
rsync -aP libevent-2.0.22-stable/build/ package/
rsync -aP openssl-1.0.2d/build/ package/
rsync -aP unbound-1.5.4/build/ package/
cp unbound.conf package/bin/

rm -rf package/share/
rm -rf package/lib/pkgconfig/
rm -rf package/ssl/man/
mv package/sbin/* package/bin/
rm -rf package/sbin
rm -rf package/include
rm -f package/bin/c_rehash
find package -name '*.la' -exec rm -f {} \;
find package -name '*.py' -exec rm -f {} \;

# Will fail on default OSX, use gsed provided by Homebrew or edit first line manually to be #!/system/bin/sh
sed -i "1s/.*/#\!\/system\/bin\/sh/" unbound-control-setup 

zip -r package.zip package
