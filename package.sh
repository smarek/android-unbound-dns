#!/bin/bash

mkdir package

rsync -aP expat-2.1.0/build/ package/
rsync -aP libevent-2.0.22-stable/build/ package/
rsync -aP openssl-1.0.2d/build/ package/
rsync -aP unbound-1.5.4/build/ package/

rm -rf package/share/
rm -rf package/lib/pkgconfig/
rm -rf package/ssl/man/
