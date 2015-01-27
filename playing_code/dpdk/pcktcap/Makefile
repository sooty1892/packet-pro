
ifeq ($(RTE_SDK),)
$(error "Please define RTE_SDK environment variable")
endif

# Default target, can be overriden by command line or environment
RTE_TARGET ?= x86_64-native-linuxapp-gcc

include $(RTE_SDK)/mk/rte.vars.mk

# binary name
APP = pktcapture

# all source are stored in SRCS-y
SRCS-y := Wrapper.c

CFLAGS += -O3

CFLAGS_Wrapper.o := -shared -fPIC -o libWrapper.so -I/usr/lib/jvm/java-7-openjdk-i386/include -I/usr/lib/jvm/java-7-openjdk-i386/include/linux

include $(RTE_SDK)/mk/rte.extapp.mk
