#!/bin/bash

rm -rf expat-2.1.0 libevent-2.0.22-stable openssl-1.0.2d unbound-1.5.4
find . -maxdepth 1 -name '*.tar.gz' -exec tar xvf \{\} \;
