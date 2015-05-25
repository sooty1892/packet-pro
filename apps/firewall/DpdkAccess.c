#include "DpdkAccess.h"

#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <inttypes.h>
#include <sys/types.h>
#include <string.h>
#include <sys/queue.h>
#include <stdarg.h>
#include <errno.h>
#include <getopt.h>

#include <rte_common.h>
#include <rte_byteorder.h>
#include <rte_log.h>
#include <rte_memory.h>
#include <rte_memcpy.h>
#include <rte_memzone.h>
#include <rte_eal.h>
#include <rte_per_lcore.h>
#include <rte_launch.h>
#include <rte_atomic.h>
#include <rte_cycles.h>
#include <rte_prefetch.h>
#include <rte_lcore.h>
#include <rte_per_lcore.h>
#include <rte_branch_prediction.h>
#include <rte_interrupts.h>
#include <rte_pci.h>
#include <rte_random.h>
#include <rte_debug.h>
#include <rte_ether.h>
#include <rte_ethdev.h>
#include <rte_ring.h>
#include <rte_mempool.h>
#include <rte_mbuf.h>
#include <rte_malloc.h>
#include <rte_fbk_hash.h>
#include <rte_ip.h>

#define ERROR -1
#define MBUF_SIZE (2048 + sizeof(struct rte_mbuf) + RTE_PKTMBUF_HEADROOM)
#define NB_MBUF 8192

struct rte_mempool *rx_pool = NULL;

// ethernet port config - more options available
static const struct rte_eth_conf port_conf = {
	.rxmode = {
		.split_hdr_size = 0,
		.header_split   = 0, /**< Header Split disabled */
		.hw_ip_checksum = 0, /**< IP checksum offload disabled */
		.hw_vlan_filter = 0, /**< VLAN filtering disabled */
		.jumbo_frame    = 0, /**< Jumbo Frame Support disabled */
		.hw_strip_crc   = 0, /**< CRC stripped by hardware */
	},
	.txmode = {
		.mq_mode = ETH_MQ_TX_NONE,
	},
};

JNIEXPORT jint JNICALL Java_DpdkAccess_nat_1setup(JNIEnv *env, jclass class) {
	char *args[] = {"Pktcap",
					"-c", "0x3",
					"-n", "3",
					"-m", "128",
					"--file-prefix", "fw",
					"-b", "00:08.0",
					"-b", "00:03.0"};

	int port_to_conf = 0;

	int ret = rte_eal_init(13, args);
	if (ret < 0) {
		printf("C: init error\n");
		return ERROR;
	}

	int nb_ports = rte_eth_dev_count();
	printf("C: %d ports enabled\n", nb_ports);
	if (nb_ports == 0) {
		printf("C: 0 ports error\n");
		return ERROR;
	}
/*
	//TODO: Change cache size?
	rx_pool = rte_mempool_create(
						"rx_pool", //name of mempool
						NB_MBUF, //number of elements in mempool
						MBUF_SIZE, //size of element
						0, // cache_size
						sizeof(struct rte_pktmbuf_pool_private),
						rte_pktmbuf_pool_init,
						NULL,
						rte_pktmbuf_init,
						NULL,
						rte_socket_id(),
						0);
	if (rx_pool == NULL) {
		printf("C: Cannot init mbuf pool\n");
		return ERROR;
	}
	printf("C: Mempool created\n");

	ret = rte_eth_dev_configure(port_to_conf, 1, 1, &port_conf);
	if (ret < 0) {
		printf("C: Cannot configure ethernet port\n");
		return ERROR;
	}
	printf("C: Ethernet port configured\n");

	ret = rte_eth_rx_queue_setup(port_to_conf, 0, 256,
								rte_eth_dev_socket_id(port_to_conf),
								NULL, rx_pool);
	if (ret < 0) {
		printf("C: Error setting up rx queue\n");
		return ERROR;
	}
	printf("C: RX queue setup\n");

	ret = rte_eth_dev_start(port_to_conf);
	if (ret < 0) {
		printf("C: Cannot start ethernet device\n");
		return ERROR;
	}
	printf("C: ethernet device started\n");

	printf("C: setup complete\n");*/


	//memset(&eth_conf, 0, sizeof eth_conf);
	// Configure an Ethernet device
	ret = rte_eth_dev_configure(port_to_conf, 1, 1, &port_conf);
	if (ret < 0) {
		printf("C: Cannot configure device: error=%d, port=%d\n", ret, 0);
		return ERROR;
	}
	printf("C: %d rte_eth_dev_configure() successful\n", port_to_conf);
	// ID of the execution unit we are running on
	unsigned cpu = rte_lcore_id();
	// Get the ID of the physical socket of the specified lcore
	unsigned socketid = rte_lcore_to_socket_id(cpu);

	// creates a new mempool named 'rx_pool in memory.
	rx_pool = rte_mempool_create("rx_pool", 8*1024, 2048, 0,
								sizeof (struct rte_pktmbuf_pool_private),
								rte_pktmbuf_pool_init, NULL,
								rte_pktmbuf_init, NULL, socketid, 0);
	if (rx_pool == NULL) {
		printf("C: rte_mempool_create(): error\n");
		return ERROR;
	}

	// Allocate and set up a receive queue for an Ethernet device.
	ret = rte_eth_rx_queue_setup(port_to_conf, 0, 256, socketid, NULL, rx_pool);
	if (ret < -1) {
		printf("C: rte_eth_rx_dev_queue_setup(): error=%d, port=%d\n", ret, port_to_conf);
		return ERROR;
	}
	printf("C: %d rte_eth_rx_queue_setup() successful\n", port_to_conf);


	// Start an Ethernet device.
	ret = rte_eth_dev_start(port_to_conf);
	if (ret < 0) {
		printf("C: rte_eth_dev_start(): error=%d, port=%d\n", ret, port_to_conf);
		return ERROR;
	}
	printf("C: %d rte_eth_dev_start() successful\n", port_to_conf);

	// used to retrieve link-level information of an
	// Ethernet port. Aligned for atomic64 read/write
	struct rte_eth_link link;

	// Retrieve the status (ON/OFF), the speed (in Mbps) and the
	// mode (HALF-DUPLEX or FULL-DUPLEX) of the physical link of
	// an Ethernet device. It might need to wait up to 9 seconds in it.
	rte_eth_link_get(port_to_conf, &link);
	if (link.link_status == 0) {
		printf("C: DPDK interface is down: %d\n", port_to_conf);
		return ERROR;
	}
	printf("C: %d is UP and RUNNING\n", port_to_conf);
}

JNIEXPORT jint JNICALL Java_DpdkAccess_nat_1receive_1burst(JNIEnv *env, jclass class, jlong pointer) {
	uint8_t *point = (uint_8*)pointer;
	struct rte_mbuf *pkts_burst[MAX_PKT_BURST];
	int nb_rx = rte_eth_rx_burst((uint8_t) 0, 0, pkts_burst, 256);
	point[0] = nb_rx;
	point[4] = pkts_burst;
}




