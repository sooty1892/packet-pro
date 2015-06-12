ifeq ($(RTE_SDK),)
$(error "Please define RTE_SDK environment variable")
endif

# Default target, can be overriden by command line or environment
RTE_TARGET ?= x86_64-native-linuxapp-gcc

DpdkJava = /home/dpdk64/packet-pro/apps/DpdkJava/src/

include $(RTE_SDK)/mk/rte.vars.mk

# binary name
SHARED = libnat_dpdk.so

# all source are stored in SRCS-y
SRCS-y := $(DpdkJava)DpdkAccess.c $(DpdkJava)Utils.c $(DpdkJava)Vars.c

RTE_BUILD_SHARED_LIB=y

CFLAGS += -fPIC
LDLIBS += -lrt -lrte_eal -lrte_mempool -lrte_mbuf -lrte_cmdline -lrte_timer -lrte_ring -lrte_pmd_ring -lrte_malloc -lethdev -lrte_pmd_e1000 -lrte_pmd_null -lrte_pmd_bond -lrte_pmd_enic -lrte_pmd_fm10k -lrte_pmd_i40e -lrte_pmd_ixgbe -lrte_port
CFLAGS += -O3
CFLAGS += $(WERROR_FLAGS)
CFLAGS += -Wall
CFLAGS += -I/usr/lib/jvm/java-7-openjdk-amd64/include
CFLAGS += -L/usr/local/lib

include $(RTE_SDK)/mk/rte.extshared.mk
