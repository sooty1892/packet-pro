#define _GNU_SOURCE

#include "DpdkAccess.h"
#include "Utils.h"
#include "Vars.h"

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

#define CHECK_INTERVAL 100 /* 100ms */
#define MAX_CHECK_TIME 90 /* 9s (90 * 100ms) in total */

struct rte_mempool *pktmbuf_pool = NULL;

int num_ports;
unsigned socketid;

const char *program_name;
const char *core_mask;
const char *port_mask;
const char *memory_channels;
const char *memory;
const char *program_id;
const char **blacklist;
int blacklist_count = 0;


JNIEXPORT jint JNICALL Java_DpdkAccess_nat_1init_1eal(JNIEnv *env, jclass class) {
	int argc = 1 + // program name
			   2 + // core mask and flag
			   2 + // memory channels and flag
			   2 + // file prefix and flag
			   2 + // memory and flag
			   (2*blacklist_count); // flag and port for each blacklisted port
	//char **argv;

	//malloc is ok at start of program

	// i think core mask should just be 1 as we don't want
	// c threads creating

	/*argv = malloc(argc * sizeof(char*));
	int i;
	for (i = 0; i < argc; i++) {
		//overkill but works
		argv[i] = malloc(120 * sizeof(char));
	}*/

	char *argv[] = {"Pktcap",
					"-c", "0x1",
					"-n", "3",
					"-m", "128",
					"--file-prefix", "fw",
					"-b", "00:08.0",
					"-b", "00:03.0"};

	int port_to_conf = 0;


	int ret = rte_eal_init(argc, argv);
	if (ret < 0) {
		printf("C: EAL init error\n");
		// free args
		/*int i;
		for (i = 0; i < argc; i++) {
			dealloc(argv[i]);
		}
		dealloc(argv);*/
		return ERROR;
	} /*else {
		// free args
		int i;
		for (i = 0; i < argc; i++) {
			dealloc(argv[i]);
		}
		dealloc(argv);
	}*/
}

JNIEXPORT jint JNICALL Java_DpdkAccess_nat_1create_1mempool(JNIEnv *env, jclass class, jstring name, jint num_el, jint cache_size) {
	const char *n = (*env)->GetStringUTFChars(env, name, 0);

	// ID of the execution unit we are running on
	unsigned cpu = rte_lcore_id();
	// Get the ID of the physical socket of the specified lcore
	socketid = rte_lcore_to_socket_id(cpu);

	//TODO: Change cache size?
	pktmbuf_pool = rte_mempool_create(
						n, //name of mempool
						num_el, //number of elements in mempool
						MBUF_SIZE, //size of element
						cache_size, // cache_size
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
}

JNIEXPORT jint JNICALL Java_DpdkAccess_nat_1check_1ports(JNIEnv *env, jclass class) {
	num_ports = rte_eth_dev_count();
	printf("C: %d ports enabled\n", num_ports);
	if (num_ports == 0) {
		printf("C: 0 ports error\n");
		return ERROR;
	}
}

JNIEXPORT jint JNICALL Java_DpdkAccess_nat_1configure_1dev(JNIEnv *env, jclass class, jint port_id, jint rx_num, jint tx_num) {
	int ret = rte_eth_dev_configure(port_id, rx_num, tx_num, &port_conf);
	if (ret < 0) {
		printf("C: Cannot configure ethernet port\n");
		return ERROR;
	}
	printf("C: Ethernet port configured\n");
}

JNIEXPORT jint JNICALL Java_DpdkAccess_nat_1configure_1rx_1queue(JNIEnv *env, jclass class, jint port_id, jint rx_id) {
	int ret = rte_eth_rx_queue_setup(port_id, rx_id, 256,
								socketid,
								&rx_conf, pktmbuf_pool);
	if (ret < 0) {
		printf("C: Error setting up rx queue\n");
		return ERROR;
	}
	printf("C: RX queue setup\n");
}

JNIEXPORT jint JNICALL Java_DpdkAccess_nat_1configure_1tx_1queue(JNIEnv *env, jclass class, jint port_id, jint tx_id) {
	int ret = rte_eth_tx_queue_setup(port_id, tx_id, 256, socketid, &tx_conf);
	if (ret < 0) {
		rte_exit(EXIT_FAILURE, "rte_eth_tx_queue_setup(): error=%d, port=%d\n", ret, 0);
	}
	printf("If %d rte_eth_tx_queue_setup() successful\n", 0);
}

JNIEXPORT jint JNICALL Java_DpdkAccess_nat_1dev_1start(JNIEnv *env, jclass class, jint port_id) {
	int ret = rte_eth_dev_start(port_id);
	if (ret < 0) {
		printf("C: Cannot start ethernet device\n");
		return ERROR;
	}
	printf("C: ethernet device started\n");
}

JNIEXPORT jint JNICALL Java_DpdkAccess_nat_1check_1ports_1link_1status(JNIEnv *env, jclass class) {
	struct rte_eth_link link;

	uint8_t portid, count, all_ports_up, print_flag = 0;

	for (count = 0; count <= MAX_CHECK_TIME; count++) {
		all_ports_up = 1;
		for (portid = 0; portid < num_ports; portid++) {
			//if ((port_mask & (1 << portid)) == 0)
			//	continue;
			memset(&link, 0, sizeof(link));
			rte_eth_link_get_nowait(portid, &link);
			/* print link status if flag set */
			if (print_flag == 1) {
				if (link.link_status)
					printf("Port %d Link Up - speed %u "
						"Mbps - %s\n", (uint8_t)portid,
						(unsigned)link.link_speed,
				(link.link_duplex == ETH_LINK_FULL_DUPLEX) ?
					("full-duplex") : ("half-duplex\n"));
				else
					printf("Port %d Link Down\n",
						(uint8_t)portid);
				continue;
			}
			/* clear all_ports_up flag if any link down */
			if (link.link_status == 0) {
				all_ports_up = 0;
				break;
			}
		}
		/* after finally printing all link status, get out */
		if (print_flag == 1)
			break;

		if (all_ports_up == 0) {
			printf(".");
			fflush(stdout);
			rte_delay_ms(CHECK_INTERVAL);
		}

		/* set the print_flag if all ports up or timeout */
		if (all_ports_up == 1 || count == (MAX_CHECK_TIME - 1)) {
			print_flag = 1;
			printf("done\n");
		}
	}
}

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

JNIEXPORT jint JNICALL Java_DpdkAccess_nat_1set_1thread_1affinity(JNIEnv *env, jclass class, jint core, jint avail) {

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

