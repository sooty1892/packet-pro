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

#define ERROR -1;
#define MBUF_SIZE (2048 + sizeof(struct rte_mbuf) + RTE_PKTMBUF_HEADROOM);
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
		printf("C: init error");
		return ERROR;
	}

	int nb_ports = rte_eth_dev_count();
	printf("C: %d ports enabled\n", nb_ports);
	if (nb_ports == 0) {
		printf("C: 0 ports error");
		return ERROR;
	}

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
		printf("C: Cannot init mbuf pool");
		return ERROR;
	}
	printf("C: Mempool created");

	ret = rte_eth_dev_configure(port_to_conf, 1, 1, &port_conf)
	if (ret < 0) {
		printf("C: Cannot configure ethernet port");
		return ERROR;
	}
	printf("C: Ethernet port configured");

	ret = rte_eth_rx_queue_setup(port_to_conf, 0, 256,
								rte_eth_dev_socket_id(port_to_conf),
								NULL, rx_pool);
	if (ret < 0) {
		printf("C: Error setting up rx queue");
		return ERROR;
	}
	printf("C: RX queue setup");

	ret = rte_eth_dev_start(port_to_conf);
	if (ret < 0) {
		printf("C: Cannot start ethernet device");
		return ERROR;
	}
	printf("C: ethernet device started");

	printf("C: setup complete");
}

JNIEXPORT jint JNICALL Java_DpdkAccess_nat_1receive_1burst(JNIEnv *env, jclass class) {

}




