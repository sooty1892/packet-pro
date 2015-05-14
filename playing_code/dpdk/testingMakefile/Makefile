ifeq ($(RTE_SDK),)
$(error "Please define RTE_SDK environment variable")
endif

# Default target, can be overriden by command line or environment
RTE_TARGET ?= x86_64-native-linuxapp-gcc

include $(RTE_SDK)/mk/rte.vars.mk

# binary name
SHARED = libWrapper.so

# all source are stored in SRCS-y
SRCS-y := Wrapper.c

RTE_BUILD_SHARED_LIB=y

CFLAGS += -fPIC
LDLIBS += -lrte_eal
CFLAGS += -O3
#CFLAGS += $(WERROR_FLAGS)
CFLAGS += -I/usr/lib/jvm/java-7-openjdk-amd64/include

include $(RTE_SDK)/mk/rte.extshared.mk
