#!/bin/bash

# Normal setup
#   different cores for each port.

name=`uname -n`

# Use 'sudo -E ./setup.sh' to include environment variables

if [ -z ${RTE_SDK} ] ; then
    echo "*** RTE_SDK is not set, did you forget to do 'sudo -E ./setup.sh'"
    exit 1
else
    sdk=${RTE_SDK}
fi

if [ -z ${RTE_TARGET} ]; then
    echo "*** RTE_TARGET is not set, did you forget to do 'sudo -E ./setup.sh'"
    target=x86_64-pktgen-linuxapp-gcc
else
    target=${RTE_TARGET}
fi


./app/app/${RTE_TARGET}/pktgen -c 0x7 -n 3 --proc-type primary --socket-mem 128 --file-prefix pg -b 00:0a.0 -b 00:09.0 -d ${RTE_SDK}/${RTE_TARGET}/lib/librte_pmd_e1000.so -- -P -m "1.0, 2.1" -f themes/black-yellow.theme

