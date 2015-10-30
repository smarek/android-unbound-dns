#!/system/bin/sh

export PATH=$PATH:`pwd`
# Needed just for unbound-anchor purposes
export LD_LIBRARY_PATH=$(dirname `pwd`)/lib/

# Drop configured shell
sh
