AM_CXXFLAGS = -fPIC -g -O2 -Idpdk/build/include -include dpdk/build/include/rte_config.h 

DPDK_FLAGS=-Ldpdk/build/lib \
	-Wl,--whole-archive \
	-Wl,-lrte_distributor \
	-Wl,-lrte_kni \
	-Wl,-lrte_pipeline \
	-Wl,-lrte_table \
	-Wl,-lrte_port \
	-Wl,-lrte_timer \
	-Wl,-lrte_hash \
	-Wl,-lrte_lpm \
	-Wl,-lrte_power \
	-Wl,-lrte_meter \
	-Wl,-lrte_sched \
	-Wl,-lrte_kvargs \
	-Wl,-lrte_mbuf \
	-Wl,-lrte_ip_frag \
	-Wl,-lethdev \
	-Wl,-lrte_malloc \
	-Wl,-lrte_cmdline \
	-Wl,-lrte_cfgfile \
	-Wl,-lrte_eal \
	-Wl,-lrte_mempool \
	-Wl,-lrte_ring \
	-Wl,-lrte_pmd_bond \
	-Wl,-lrte_pmd_vmxnet3_uio \
	-Wl,-lrte_pmd_virtio_uio \
	-Wl,-lrte_pmd_i40e \
	-Wl,-lrte_pmd_ixgbe \
	-Wl,-lrte_pmd_e1000 \
	-Wl,-lrte_pmd_ring \
	-Wl,-lrt -Wl,-lm -Wl,-ldl \
	-Wl,--no-whole-archive

lib_LTLIBRARIES = libdpdk.la

libdpdk_la_LDFLAGS = -module -avoid-version -pthread $(DPDK_FLAGS)

libdpdk_la_SOURCES = DpdkInit.cpp 

bin_PROGRAMS = test
test_SOURCES = test.cpp
#test_LDFLAGS = -Wl,--no-as-needed
test_LDADD = libdpdk.la

