#include "DpdkAccess.h"

#include <stdio.h>
#include <string.h>
#include <stdint.h>

#include <rte_memory.h>
#include <rte_memzone.h>
#include <rte_launch.h>
#include <rte_tailq.h>
#include <rte_eal.h>
#include <rte_per_lcore.h>
#include <rte_lcore.h>
#include <rte_debug.h>
#include <rte_ethdev.h>
#include <rte_mbuf.h>

#define ERROR -1;

#define MAX_PKT_BURST 512

#define MAX_PKT_SIZE (2*1024 + sizeof(struct rte_mbuf) + RTE_PKTMBUF_HEADROOM)

// For configuring an ethernet port
static struct rte_eth_conf eth_conf = {
    .rxmode = {
        .mq_mode = ETH_MQ_RX_RSS,
        .split_hdr_size = 0,
        .header_split = 0,
        .hw_ip_checksum = 1,
        .hw_vlan_filter = 0,
        .jumbo_frame = 0,
        .hw_strip_crc = 0,
    },
    .rx_adv_conf = {
        .rss_conf = {
            .rss_key = NULL,
            .rss_hf = ETH_RSS_IP,
        },
    },
    .txmode = {
        .mq_mode = ETH_MQ_TX_NONE,
    },
 };

// Configures a TX ring of an Ethernet port
static struct rte_eth_txconf tx_conf = {
    .tx_thresh = {
        .pthresh = 36,
        .hthresh = 0,
        .wthresh = 0,
    },
    .tx_rs_thresh = 0,
    .tx_free_thresh = 0,
    //.txq_flags = (ETH_TXQ_FLAGS_NOMULTSEGS |
    //		ETH_TXQ_FLAGS_NOVLANOFFL |
    //		ETH_TXQ_FLAGS_NOXSUMSCTP |
    //		ETH_TXQ_FLAGS_NOXSUMUDP  |
    //		ETH_TXQ_FLAGS_NOXSUMTCP)
};

// Configures a RX ring of an Ethernet port
static struct rte_eth_rxconf rx_conf = {
    .rx_thresh = {
        .pthresh = 8,
        .hthresh = 8,
        .wthresh = 4,
    },
    .rx_free_thresh = 64,
    .rx_drop_en = 0,
};

// RTE mempool structure
static struct rte_mempool *rx_pool;

JNIEXPORT jint JNICALL Java_DpdkAccess_nat_1setup_1and_1conf(JNIEnv *env, jclass class) {
	char *strs[] = {"Pktcap", "-c", "0x3", "-n", "1"};

	int port_to_conf = 0;

	int ret = rte_eal_init(5, strs);
	int count = rte_eth_dev_count();
	printf("C: %d ports enabled\n", count);
	if (count == 0) {
		return ERROR;
	}

	memset(&eth_conf, 0, sizeof eth_conf);
	// Configure an Ethernet device
	ret = rte_eth_dev_configure(port_to_conf, 1, 1, &eth_conf);
	if (ret < 0) {
		printf("C: Cannot configure device: error=%d, port=%d\n", ret, 0);
		return ERROR;
	}
	printf("%d rte_eth_dev_configure() successful\n", port_to_conf);
	// ID of the execution unit we are running on
	unsigned cpu = rte_lcore_id();
	// Get the ID of the physical socket of the specified lcore
	unsigned socketid = rte_lcore_to_socket_id(cpu);

	// creates a new mempool named 'rx_pool in memory.
	rx_pool = rte_mempool_create("rx_pool", 8*1024, MAX_PKT_SIZE, 0,
								sizeof (struct rte_pktmbuf_pool_private),
								rte_pktmbuf_pool_init, NULL,
								rte_pktmbuf_init, NULL, socketid, 0);
	if (rx_pool == NULL) {
		printf("rte_mempool_create(): error\n");
		return ERROR;
	}

	// Allocate and set up a receive queue for an Ethernet device.
	ret = rte_eth_rx_queue_setup(port_to_conf, 0, 256, socketid, &rx_conf, rx_pool);
	if (ret < -1) {
		printf("rte_eth_rx_dev_queue_setup(): error=%d, port=%d\n", ret, port_to_conf);
		return ERROR;
	}
	printf("%d rte_eth_rx_queue_setup() successful\n", port_to_conf);

	// Allocate and set up a transmit queue for an Ethernet device.
	ret = rte_eth_tx_queue_setup(port_to_conf, 0, 256, socketid, &tx_conf);
	if (ret < 0) {
		printf("rte_eth_tx_queue_setup(): error=%d, port=%d\n", ret, port_to_conf);
		return ERROR;
	}
	printf("If %d rte_eth_tx_queue_setup() successful\n", port_to_conf);

	// Start an Ethernet device.
	ret = rte_eth_dev_start(port_to_conf);
	if (ret < 0) {
		printf("rte_eth_dev_start(): error=%d, port=%d\n", ret, port_to_conf);
		return ERROR;
	}
	printf("%d rte_eth_dev_start() successful\n", port_to_conf);

	// used to retrieve link-level information of an
	// Ethernet port. Aligned for atomic64 read/write
	struct rte_eth_link link;

	// Retrieve the status (ON/OFF), the speed (in Mbps) and the
	// mode (HALF-DUPLEX or FULL-DUPLEX) of the physical link of
	// an Ethernet device. It might need to wait up to 9 seconds in it.
	rte_eth_link_get(port_to_conf, &link);
	if (link.link_status == 0) {
		printf("DPDK interface is down: %d\n", port_to_conf);
		return ERROR;
	}
	printf("If %d is UP and RUNNING\n", port_to_conf);

	// Enable receipt in promiscuous mode for an Ethernet device.
	rte_eth_promiscuous_enable(port_to_conf);

	long pktcount = 0;
	int recv_cnt = 0;
	int i = 0;
	// contains a packet mbuf
	struct rte_mbuf *rx_mbufs[MAX_PKT_BURST];
	/* call it on master lcore too */
	while(1) {
		// Retrieve a burst of input packets from a receive queue of an
		// Ethernet device. The retrieved packets are stored in rte_mbuf
		// structures whose pointers are supplied in the rx_pkts array
		recv_cnt = rte_eth_rx_burst(port_to_conf, 0, rx_mbufs, MAX_PKT_BURST);
		if (recv_cnt < 0) {
			if (errno != EAGAIN && errno != EINTR) {
				perror("rte_eth_rx_burst()");
				assert(0);
			}
		}
		if ( recv_cnt > 0) {
			pktcount += recv_cnt;
			for (i = 0 ; i < recv_cnt; i++) {
				/* drop packet */
				// Free a packet mbuf back into its original mempool.
				// Free an mbuf, and all its segments in case of chained
				// buffers. Each segment is added back into its original mempool.
				rte_pktmbuf_free(rx_mbufs[i]);
			}
			if (pktcount == 10000000)
				printf("Received %ld packets so far\n", pktcount);
			printf("Received %ld packets\n", pktcount);
		}
		//  rte_eal_mp_wait_lcore();
	}
	return 0;
}
