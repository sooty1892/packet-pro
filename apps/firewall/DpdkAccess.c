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
#define MAX_PKT_BURST 128
#define NB_RX_QUEUE 1
#define NB_TX_QUEUE 1
#define	NB_RX_DESC 256
#define	NB_TX_DESC 256


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

void printIpv4Data(struct ipv4_hdr* hdr, int packet_num) {
	printf("C: Packet %d version_ihl = %" PRIu8 " : %p\n", packet_num, hdr->version_ihl, &hdr->version_ihl);
	printf("C: Packet %d type_of_service = %" PRIu8 " : %p\n", packet_num, hdr->type_of_service, &hdr->type_of_service);
	printf("C: Packet %d total_length = %" PRIu16 " : %p\n", packet_num, hdr->total_length, &hdr->total_length);
	printf("C: Packet %d packet_id = %" PRIu16 " : %p\n", packet_num, hdr->packet_id, &hdr->packet_id);
	printf("C: Packet %d fragment_offset = %" PRIu16 " : %p\n", packet_num, hdr->fragment_offset, &hdr->fragment_offset);
	printf("C: Packet %d time_to_live = %" PRIu8 " : %p\n", packet_num, hdr->time_to_live, &hdr->time_to_live);
	printf("C: Packet %d next_proto_id = %" PRIu8 " : %p\n", packet_num, hdr->next_proto_id, &hdr->next_proto_id);
	printf("C: Packet %d hdr_checksum = %" PRIu16 " : %p\n", packet_num, hdr->hdr_checksum, &hdr->hdr_checksum);
	printf("C: Packet %d src_addr = %" PRIu32 " : %p\n", packet_num, hdr->src_addr, &hdr->src_addr);
	/*int i;
	unsigned int maxpow = 1 << (sizeof(uint32_t)*8 - 1);
	uint32_t num = hdr->src_addr;
	for (i = 0; i < sizeof(uint32_t)*8; ++i) {
		printf("%u ", num * maxpow ? 1 : 0);
		num = num >> 1;
	}
	printf("\n");*/
	printf("C: Packet %d dst_addr = %" PRIu32 " : %p\n", packet_num, hdr->dst_addr, &hdr->dst_addr);
	/*num = hdr->dst_addr;
	for (i = 0; i < sizeof(uint32_t)*8; ++i) {
		printf("%u ", num * maxpow ? 1 : 0);
		num = num >> 1;
	}*/
	printf("\n");
}

uint8_t get8(uint8_t *pointer, int position) {
	return pointer[position];
}

uint16_t get16(uint8_t *pointer, int position) {
	uint16_t value = pointer[position+1] << 8;
	value = value + pointer[position];
	return value;
}

uint32_t get32(uint8_t *pointer, int position) {
	uint32_t value = pointer[position+3] << 24;
	value = value + (pointer[position+2] << 16);
	value = value + (pointer[position+1] << 8);
	value = value + pointer[position];
	return value;
}

uint64_t get64(uint8_t *pointer, int position) {
	uint64_t value = (uint64_t)(pointer[position+7]) << 56;
	value = value + ((uint64_t)(pointer[position+6]) << 48);
	value = value + ((uint64_t)(pointer[position+5]) << 40);
	value = value + ((uint64_t)(pointer[position+4]) << 32);
	value = value + ((uint64_t)(pointer[position+3]) << 24);
	value = value + ((uint64_t)(pointer[position+2]) << 16);
	value = value + ((uint64_t)(pointer[position+1]) << 8);
	value = value + (uint64_t)(pointer[position]);
	return value;
}

void insert8(uint8_t *pointer, int position, uint8_t value) {
	pointer[position] = value ;
}

void insert16(uint8_t *pointer, int position, uint16_t value) {
	pointer[position+1] = (uint8_t)((value >> 8) & 0xFF);
	pointer[position] = (uint8_t)((value) & 0xFF);
}

void insert32(uint8_t *pointer, int position, uint32_t value) {
	pointer[position+3] = (uint8_t)((value >> 24) & 0xFF);
	pointer[position+2] = (uint8_t)((value >> 16) & 0xFF);
	pointer[position+1] = (uint8_t)((value >> 8) & 0XFF);
	pointer[position] = (uint8_t)((value) & 0XFF);
}

void insert64(uint8_t *pointer, int position, uint64_t value) {
	pointer[position+7] = (uint8_t)((value >> 56) & 0xFF);
	pointer[position+6] = (uint8_t)((value >> 48) & 0xFF);
	pointer[position+5] = (uint8_t)((value >> 40) & 0XFF);
	pointer[position+4] = (uint8_t)((value >> 32) & 0XFF);
	pointer[position+3] = (uint8_t)((value >> 24) & 0XFF);
	pointer[position+2] = (uint8_t)((value >> 16) & 0XFF);
	pointer[position+1] = (uint8_t)((value >> 8) & 0XFF);
	pointer[position] = (uint8_t)((value) & 0XFF);
}

JNIEXPORT jint JNICALL Java_DpdkAccess_nat_1setup(JNIEnv *env, jclass class) {
	char *args[] = {"Pktcap",
					"-c", "0x7",
					"-n", "3",
					"-m", "128",
					"--file-prefix", "fw",
					"-b", "00:08.0",
					"-b", "00:03.0"};

	int port_to_conf = 0;

	// used to retrieve link-level information of an
    // Ethernet port. Aligned for atomic64 read/write
    struct rte_eth_link link;

	int ret = rte_eal_init(13, args);
	if (ret < 0) {
		printf("C: EAL init error\n");
		return ERROR;
	}

	int nb_ports = rte_eth_dev_count();
	printf("C: %d ports enabled\n", nb_ports);
	if (nb_ports == 0) {
		printf("C: 0 ports error\n");
		return ERROR;
	}

	// memset???
	ret = rte_eth_dev_configure(port_to_conf, 1, 1, &eth_conf);
	if (ret < 0) {
		printf("C: Cannot configure ethernet port\n");
		return ERROR;
	}
	printf("C: Ethernet port configured\n");

	// ID of the execution unit we are running on
    unsigned cpu = rte_lcore_id();
    // Get the ID of the physical socket of the specified lcore
    unsigned socketid = rte_lcore_to_socket_id(cpu);

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
						socketid,
						0);
	if (rx_pool == NULL) {
		printf("C: Cannot init mbuf pool\n");
		return ERROR;
	}
	printf("C: Mempool created\n");


	ret = rte_eth_rx_queue_setup(port_to_conf, 0, 256,
								socketid,
								&rx_conf, rx_pool);
	if (ret < 0) {
		printf("C: Error setting up rx queue\n");
		return ERROR;
	}
	printf("C: RX queue setup\n");

	// Allocate and set up a transmit queue for an Ethernet device.
    ret = rte_eth_tx_queue_setup(0, 0, NB_TX_DESC, socketid, &tx_conf);
    if (ret < 0) {
        rte_exit(EXIT_FAILURE, "rte_eth_tx_queue_setup(): error=%d, port=%d\n", ret, 0);
    }
    printf("If %d rte_eth_tx_queue_setup() successful\n", 0);

	ret = rte_eth_dev_start(port_to_conf);
	if (ret < 0) {
		printf("C: Cannot start ethernet device\n");
		return ERROR;
	}
	printf("C: ethernet device started\n");

	rte_eth_link_get(0, &link);
    if (link.link_status == 0) {
        rte_exit(EXIT_FAILURE, "DPDK interface is down: %d\n", 0);
    }
    printf("If %d is UP and RUNNING\n", 0);

	printf("C: setup complete\n");
}

JNIEXPORT void JNICALL Java_DpdkAccess_nat_1receive_1burst(JNIEnv *env, jclass class, jlong mem_pointer) {
	struct rte_mbuf *pkts_burst[128];

	int offset = 0;

	int nb_rx = rte_eth_rx_burst(0, 0, pkts_burst, MAX_PKT_BURST);

	uint16_t packet_count = (uint16_t)nb_rx;
	uint8_t *point = (uint8_t*)mem_pointer;

	insert16(point, offset, packet_count);
	offset += sizeof(uint16_t);

	if (packet_count > 0) {
		//printf("C: Parsing %d packets!\n", packet_count);

		int i;
		for (i = 0; i < packet_count; i++) {
			printf("NEED TO CHECK FOR IP VERSION IN C");
			struct ipv4_hdr* ip = (struct ipv4_hdr *)(rte_pktmbuf_mtod(pkts_burst[i], unsigned char *) + sizeof(struct ether_hdr));
			printf("C: %p\n", pkts_burst[i]);
			insert64(point, offset, (uint64_t)pkts_burst[i]);
			offset += sizeof(uint64_t);
			insert64(point, offset, (uint64_t)ip);
			//printf("C: Packet %d ip_hdr_add = %p\n", i, ip);
			offset += sizeof(uint64_t);
			//printf("C: Packet %d mbuf_add = %" PRIu64 "\n", i, pkts_burst[i]);
			//printf("C: Packet %d ip_hdr_add = %" PRIu64 "\n", i, ip);
			printIpv4Data(ip, i);
		}

	}

}

JNIEXPORT jint JNICALL Java_DpdkAccess_nat_1size_1of_1ether_1hdr(JNIEnv *env, jclass class) {
	return sizeof(struct ether_hdr);
}

JNIEXPORT jint JNICALL Java_DpdkAccess_nat_1size_1of_1mbuf(JNIEnv *env, jclass class) {
	return sizeof(struct rte_mbuf);
}

JNIEXPORT jint JNICALL Java_DpdkAccess_nat_1size_1of_1void_1pointer(JNIEnv *env, jclass class) {
	int num = 0;
	void *ptr;
	num = 15;
	ptr = &num;
	return sizeof(ptr);
}

JNIEXPORT void JNICALL Java_DpdkAccess_nat_1free_1packets(JNIEnv *env, jclass class, jlong mem_pointer) {
	uint8_t *point = (uint8_t*)mem_pointer;
	int offset = 0;

	uint16_t packet_count = get16(point, 0);
	printf("C: free count = %d\n", packet_count);
	offset += sizeof(uint16_t);

	//get64(point, 2);
	//printf("C: freeing at %" PRIu64 "\n", get64(point, 2));

	//uint8_t *np = point+2;

	int i;
	for (i = 0; i < packet_count; i++) {
		//struct rte_mbuf **hi = (struct rte_mbuf *)point[2];
		//struct rte_mbuf *freeing = hi[0];
		struct rte_mbuf *freeing = (struct rte_mbuf*)get64(point, offset);
		printf("C: freeing at %p\n", freeing);
		rte_pktmbuf_free(freeing);
		offset += sizeof(uint64_t);
	}
}

JNIEXPORT void JNICALL Java_DpdkAccess_nat_1send_1packets(JNIEnv *env, jclass class, jlong mem_pointer) {
	uint8_t *point = (uint8_t*)mem_pointer;
	int offset = 0;

	uint16_t packet_count = *point;
	printf("C: send count = %d\n", packet_count);
	offset += sizeof(uint16_t);

	struct rte_mbuf *packets[packet_count];

	int i;
	for (i = 0; i < packet_count; i++) {
		rte_eth_tx_burst(0, 0, packets, packet_count);
	}

	//free packets
}


