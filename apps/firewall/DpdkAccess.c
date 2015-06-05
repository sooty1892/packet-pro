#define _GNU_SOURCE

#include "DpdkAccess.h"
#include "Utils.h"

#include <sched.h>
#include <sys/types.h>
#include <string.h>
#include <sys/queue.h>
#include <stdarg.h>
#include <errno.h>
#include <getopt.h>
#include <pthread.h>
#include <syscall.h>


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


struct rte_mempool *pktmbuf_pool = NULL;

/* list of enabled ports */
static uint32_t enabled_ports[16];

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

const char *program_name;
const char *core_mask;
const char *port_mask;
const char *memory_channels;
const char *memory;
const char *program_id;
const char **blacklist;
int blacklist_count = 0;


JNIEXPORT void JNICALL Java_DpdkAccess_nat_1set_1core_1mask(JNIEnv *env, jclass class, jstring value) {
	core_mask = (*env)->GetStringUTFChars(env, value, 0);
	//strcpy(core_mask, (*env)->GetStringUTFChars(env, value, JNI_TRUE));
	//(*env)->ReleaseStringUTFChars(env, javaString, nativeString);
}

JNIEXPORT void JNICALL Java_DpdkAccess_nat_1set_1port_1mask(JNIEnv *env, jclass class, jstring value) {
	port_mask = (*env)->GetStringUTFChars(env, value, 0);
}

JNIEXPORT void JNICALL Java_DpdkAccess_nat_1set_1program_1name(JNIEnv *env, jclass class, jstring value) {
	program_name = (*env)->GetStringUTFChars(env, value, 0);
}

JNIEXPORT void JNICALL Java_DpdkAccess_nat_1set_1memory_1channels(JNIEnv *env, jclass class, jstring value) {
	memory_channels = (*env)->GetStringUTFChars(env, value, 0);
}

JNIEXPORT void JNICALL Java_DpdkAccess_nat_1set_1memory(JNIEnv *env, jclass class, jstring value) {
	memory = (*env)->GetStringUTFChars(env, value, 0);
}

JNIEXPORT void JNICALL Java_DpdkAccess_nat_1set_1program_1id(JNIEnv *env, jclass class, jstring value) {
	program_id = (*env)->GetStringUTFChars(env, value, 0);
}

JNIEXPORT void JNICALL Java_DpdkAccess_nat_1set_1blacklist(JNIEnv *env, jclass class, jobjectArray values) {
	blacklist_count = (*env)->GetArrayLength(env, values);

	int i;
	for (i=0; i < blacklist_count; i++) {
		jstring string = (jstring) (*env)->GetObjectArrayElement(env, values, i);
		//blacklist[i] = (*env)->GetStringUTFChars(env, string, 0);
		// Don't forget to call `ReleaseStringUTFChars` when you're done.
	}
}


JNIEXPORT jint JNICALL Java_DpdkAccess_nat_1setup(JNIEnv *env, jclass class) {
	int argc = 1 + // program name
			   2 + // core mask and flag
			   2 + // memory channels and flag
			   2 + // file prefix and flag
			   2 + // memory and flag
			   (2*blacklist_count); // flag and port for each blacklisted port
	char **argv;

	//malloc is ok at start of program

	// i think core mask should just be 1 as we don't want
	// c threads creating

	argv = malloc(argc * sizeof(char*));
	int i;
	for (i = 0; i < argc; i++) {
		//overkill but works
		argv[i] = malloc(120 * sizeof(char));
	}

	/*char *argv[] = {"Pktcap",
					"-c", "0x7",
					"-n", "3",
					"-m", "128",
					"--file-prefix", "fw",
					"-b", "00:08.0",
					"-b", "00:03.0"};*/

	int port_to_conf = 0;

	// used to retrieve link-level information of an
    // Ethernet port. Aligned for atomic64 read/write
    struct rte_eth_link link;

	int ret = rte_eal_init(argc, argv);
	if (ret < 0) {
		printf("C: EAL init error\n");
		// free args
		for (i = 0; i < argc; i++) {
			dealloc(argv[i]);
		}
		dealloc(argv);
		return ERROR;
	} else {
		// free args
		int i;
		for (i = 0; i < argc; i++) {
			dealloc(argv[i]);
		}
		dealloc(argv);
	}

	// ID of the execution unit we are running on
	unsigned cpu = rte_lcore_id();
	// Get the ID of the physical socket of the specified lcore
	unsigned socketid = rte_lcore_to_socket_id(cpu);

	//TODO: Change cache size?
	pktmbuf_pool = rte_mempool_create(
						"mbuf_pool", //name of mempool
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
	if (pktmbuf_pool == NULL) {
		printf("C: Cannot init mbuf pool\n");
		return ERROR;
	}
	printf("C: Mempool created\n");


	int nb_ports = rte_eth_dev_count();
	printf("C: %d ports enabled\n", nb_ports);
	if (nb_ports == 0) {
		printf("C: 0 ports error\n");
		return ERROR;
	}

	//reset enabled ports
	int portid;
	for (portid = 0; portid < nb_ports; portid++) {
		enabled_ports[portid] = 0;
	}


	// configure


	// memset???
	ret = rte_eth_dev_configure(port_to_conf, 1, 1, &eth_conf);
	if (ret < 0) {
		printf("C: Cannot configure ethernet port\n");
		return ERROR;
	}
	printf("C: Ethernet port configured\n");







	ret = rte_eth_rx_queue_setup(port_to_conf, 0, 256,
								socketid,
								&rx_conf, pktmbuf_pool);
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
			//printf("NEED TO CHECK FOR IP VERSION IN C???");
			struct ipv4_hdr* ip = (struct ipv4_hdr *)(rte_pktmbuf_mtod(pkts_burst[i], unsigned char *) + sizeof(struct ether_hdr));
			//printf("C: %p\n", pkts_burst[i]);
			insert64(point, offset, (uint64_t)pkts_burst[i]);
			offset += sizeof(uint64_t);
			insert64(point, offset, (uint64_t)ip);
			//printf("C: Packet %d ip_hdr_add = %p\n", i, ip);
			offset += sizeof(uint64_t);
			//printf("C: Packet %d mbuf_add = %" PRIu64 "\n", i, pkts_burst[i]);
			//printf("C: Packet %d ip_hdr_add = %" PRIu64 "\n", i, ip);
			//printIpv4Data(ip, i);
		}

	}

}


JNIEXPORT void JNICALL Java_DpdkAccess_nat_1free_1packets(JNIEnv *env, jclass class, jlong mem_pointer) {
	uint8_t *point = (uint8_t*)mem_pointer;
	int offset = 0;

	uint16_t packet_count = get16(point, 0);
	printf("C: free count = %d\n", packet_count);
	offset += sizeof(uint16_t);

	int i;
	for (i = 0; i < packet_count; i++) {
		struct rte_mbuf *freeing = (struct rte_mbuf*)get64(point, offset);
		printf("C: freeing at %p\n", freeing);
		rte_pktmbuf_free(freeing);
		offset += sizeof(uint64_t);
	}
}

JNIEXPORT void JNICALL Java_DpdkAccess_nat_1send_1packets(JNIEnv *env, jclass class, jlong mem_pointer) {
	uint8_t *point = (uint8_t*)mem_pointer;
	int offset = 0;

	uint16_t packet_count = get16(point, 0);
	printf("C: send count = %d\n", packet_count);
	offset += sizeof(uint16_t);

	//TODO: DO THIS
	/*int i;
	for (i = 0; i < packet_count; i++) {
		rte_eth_tx_burst(0, 0, packets, packet_count);
		struct rte_mbuf *freeing = (struct rte_mbuf*)get64(point, offset);
		printf("C: freeing at %p\n", freeing);
		rte_pktmbuf_free(freeing);
		offset += sizeof(uint64_t);
	}*/

	//free packets
}

JNIEXPORT jboolean JNICALL Java_DpdkAccess_nat_1set_1thread_1affinity(JNIEnv *env, jclass class, jint core, jint avail) {

	cpu_set_t cpuset;

	pthread_t thread_id = pthread_self();

	CPU_ZERO(&cpuset);

	CPU_SET(core, &cpuset);

	int s = pthread_setaffinity_np(thread_id, sizeof(cpu_set_t), &cpuset);
	if (s != 0) {
		printf("AFFINITY ERROR 1");
		return 0;
	}

   s = pthread_getaffinity_np(thread_id, sizeof(cpu_set_t), &cpuset);
   if (s != 0) {
	   printf("AFFINITY ERROR 2");
	   return 0;
   }

   if (!CPU_ISSET(core, &cpuset)) {
	   printf("AFFINITY ERROR 3");
	   return 0;
   }

   return 1;
}

