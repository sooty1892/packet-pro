/*
 * See DpdkAccess.java for explanation of methods in this class
 * Commenting on certain features may still be used
 */

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
#define SUCCESS 1
#define MBUF_SIZE (2048 + sizeof(struct rte_mbuf) + RTE_PKTMBUF_HEADROOM)


#define CHECK_INTERVAL 100 /* 100ms */
#define MAX_CHECK_TIME 90 /* 9s (90 * 100ms) in total */

struct rte_mempool *pktmbuf_pool = NULL;

int num_ports;
unsigned socketid;

const char *program_name;
const char *memory_channels;
const char *memory;
const char *program_id;
const char **blacklist;
int blacklist_count = 0;
int get_burst = 32;

//TODO: THIS
JNIEXPORT jint JNICALL Java_DpdkAccess_nat_1init_1eal(JNIEnv *env, jclass class) {
	int argc = 1 + // program name
			   2 + // core mask and flag
			   2 + // memory channels and flag
			   2 + // file prefix and flag
			   2 + // memory and flag
			   (2*blacklist_count); // flag and port for each blacklisted port

	//malloc is ok at start of program

	char **argv = malloc(argc * sizeof(char*));
	int i;
	for (i = 0; i < argc; i++) {
		//overkill but works
		argv[i] = malloc(120 * sizeof(char));
	}*/

	memcpy(argv[0], program_name, strlen(program_name));
	memcpy(argv[1], "-c", 2);
	memcpy(argv[2], "0x1", 3);
	memcpy(argv[3], "-n", 2);
	memcpy(argv[4], memory_channels, strlen(memory_channels));
	memcpy(argv[5], "--file-prefix", 13);
	memcpy(argv[6], program_id, strlen(program_id));
	memcpy(argv[7], "-m", 2);
	memcpy(argv[8], memory, strlen(memory));

	for (i = 0; i < blacklist_count; i++) {
		memcpy(argv[9+(i*2)], "-b", 2);
		memcpy(argv[9+((i*2)+1)], blacklist[i], strlen(blacklist[i]));
	}

	/*char *argv[] = {"Pktcap",
					"-c", "0x1",
					"-n", "3",
					"-m", "128",
					"--file-prefix", "fw",
					"-b", "00:08.0",
					"-b", "00:03.0"};*/

	/*char *argv[] = {program_name,
					"-c", "0x1",
					"-n", memory_channels,
					"-m", memory,
					"--file-prefix", program_id,
					""};*/

	int ret = rte_eal_init(argc, argv);
	if (ret < 0) {
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
	return SUCCESS;
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
		return ERROR;
	}
	return SUCCESS;
}

JNIEXPORT jint JNICALL Java_DpdkAccess_nat_1check_1ports(JNIEnv *env, jclass class) {
	num_ports = rte_eth_dev_count();
	if (num_ports == 0) {
		return ERROR;
	}
	return num_ports;
}

JNIEXPORT jint JNICALL Java_DpdkAccess_nat_1configure_1dev(JNIEnv *env, jclass class, jint port_id, jint rx_num, jint tx_num) {
	int ret = rte_eth_dev_configure(port_id, rx_num, tx_num, &port_conf);
	if (ret < 0) {
		return ERROR;
	}
	return SUCCESS;
}

JNIEXPORT jint JNICALL Java_DpdkAccess_nat_1configure_1rx_1queue(JNIEnv *env, jclass class, jint port_id, jint rx_id) {
	int ret = rte_eth_rx_queue_setup(port_id, rx_id, 256,
								socketid,
								&rx_conf, pktmbuf_pool);
	if (ret < 0) {
		return ERROR;
	}
	return SUCCESS;
}

JNIEXPORT jint JNICALL Java_DpdkAccess_nat_1configure_1tx_1queue(JNIEnv *env, jclass class, jint port_id, jint tx_id) {
	int ret = rte_eth_tx_queue_setup(port_id, tx_id, 256, socketid, &tx_conf);
	if (ret < 0) {
		return ERROR;
	}
	return SUCCESS;
}

JNIEXPORT jint JNICALL Java_DpdkAccess_nat_1dev_1start(JNIEnv *env, jclass class, jint port_id) {
	int ret = rte_eth_dev_start(port_id);
	if (ret < 0) {
		return ERROR;
	}
	return SUCCESS;
}

JNIEXPORT jint JNICALL Java_DpdkAccess_nat_1check_1ports_1link_1status(JNIEnv *env, jclass class) {
	struct rte_eth_link link;

	// TODO: get this printing on java side

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
	return SUCCESS;
}

JNIEXPORT void JNICALL Java_DpdkAccess_nat_1set_1receive_1burst(JNIEnv *env, jclass class, jint value) {
	get_burst = value;
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

	blacklist = malloc(blacklist_count * sizeof(char *));

	int i;
	for (i=0; i < blacklist_count; i++) {
		jstring string = (jstring) (*env)->GetObjectArrayElement(env, values, i);
		blacklist[i] = (*env)->GetStringUTFChars(env, string, 0);
		// Don't forget to call `ReleaseStringUTFChars` when you're done.
	}
}

JNIEXPORT void JNICALL Java_DpdkAccess_nat_1receive_1burst(JNIEnv *env, jclass class, jlong mem_pointer, jint port_id, jint rx_id) {
	struct rte_mbuf *pkts_burst[128];

	int offset = 0;

	int nb_rx = rte_eth_rx_burst(port_id, rx_id, pkts_burst, get_burst);

	uint16_t packet_count = (uint16_t)nb_rx;
	uint8_t *point = (uint8_t*)mem_pointer;

	insert16(point, offset, packet_count);
	offset += sizeof(uint16_t);

	if (packet_count > 0) {

		int i;
		for (i = 0; i < packet_count; i++) {
			struct ipv4_hdr* ip = (struct ipv4_hdr *)(rte_pktmbuf_mtod(pkts_burst[i], unsigned char *) + sizeof(struct ether_hdr));
			insert64(point, offset, (uint64_t)pkts_burst[i]);
			offset += sizeof(uint64_t);
			insert64(point, offset, (uint64_t)ip);
			offset += sizeof(uint64_t);
		}

	}

}


JNIEXPORT void JNICALL Java_DpdkAccess_nat_1free_1packets(JNIEnv *env, jclass class, jlong mem_pointer) {
	uint8_t *point = (uint8_t*)mem_pointer;
	int offset = 0;

	uint16_t packet_count = get16(point, 0);
	offset += sizeof(uint16_t);

	int i;
	for (i = 0; i < packet_count; i++) {
		struct rte_mbuf *freeing = (struct rte_mbuf*)get64(point, offset);
		rte_pktmbuf_free(freeing);
		offset += sizeof(uint64_t);
	}
}

JNIEXPORT void JNICALL Java_DpdkAccess_nat_1send_1packets(JNIEnv *env, jclass class, jlong mem_pointer, jint port_id, jint tx_id) {
	uint8_t *point = (uint8_t*)mem_pointer;
	int offset = 0;

	uint16_t packet_count = get16(point, 0);
	offset += sizeof(uint16_t);

	rte_eth_tx_burst(port_id, tx_id, (struct rte_mbuf**)(point+offset), packet_count);


	offset = sizeof(uint16_t);
	int i;
	for (i = 0; i < packet_count; i++) {
		struct rte_mbuf *freeing = (struct rte_mbuf*)get64(point, offset);
		rte_pktmbuf_free(freeing);
		offset += sizeof(uint64_t);
	}
}

JNIEXPORT jint JNICALL Java_DpdkAccess_nat_1set_1thread_1affinity(JNIEnv *env, jclass class, jint core, jint avail) {

	cpu_set_t cpuset;

	pthread_t thread_id = pthread_self();

	CPU_ZERO(&cpuset);

	CPU_SET(core, &cpuset);

	int s = pthread_setaffinity_np(thread_id, sizeof(cpu_set_t), &cpuset);
	if (s != 0) {
		return 0;
	}

   s = pthread_getaffinity_np(thread_id, sizeof(cpu_set_t), &cpuset);
   if (s != 0) {
	   return 0;
   }

   if (!CPU_ISSET(core, &cpuset)) {
	   return 0;
   }

   return 1;
}

