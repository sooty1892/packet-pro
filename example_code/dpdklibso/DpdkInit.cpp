/*
 * DpdkInit.cpp
 *
 *  Created on: 15 Oct 2014
 *      Author: Abdul Alim <a.alim@imperial.ac.uk>
 */

#include <stdint.h>
#include <signal.h>

#include <rte_common.h>
#include <rte_byteorder.h>
#include <rte_log.h>
#include <rte_debug.h>
#include <rte_cycles.h>
#include <rte_memory.h>
#include <rte_memcpy.h>
#include <rte_memzone.h>
#include <rte_launch.h>
#include <rte_tailq.h>
#include <rte_eal.h>
#include <rte_per_lcore.h>
#include <rte_lcore.h>
#include <rte_atomic.h>
#include <rte_branch_prediction.h>
#include <rte_ring.h>
#include <rte_mempool.h>
#include <rte_malloc.h>
#include <rte_mbuf.h>
#include <rte_interrupts.h>
#include <rte_pci.h>
#include <rte_ether.h>
#include <rte_ethdev.h>
#include <rte_string_fns.h>
#ifdef RTE_LIBRTE_PMD_XENVIRT
#include <rte_eth_xenvirt.h>
#endif

#include <iostream>
#include "DpdkInit.h"

DpdkInit *DpdkInit::INSTANCE = NULL;

DpdkInit::DpdkInit(int argc, char **argv, int cpu) : m_cpu(cpu) {
	// Initialize DPDK
	if (rte_eal_init(argc, argv) < 0)
		throw new RuntimeException("DpdkInit() failed to initialize DPDK.");
	// Make sure there are DPDK enabled interface
	int n = (uint8_t)rte_eth_dev_count();
	// printf("DpdkMtcp() found %d DPDK enabled interfaces\n", n);
	if (n <= 0) throw new RuntimeException("DpdkInit() found no DPDK enabled port.");
}

DpdkInit::~DpdkInit() {
}
