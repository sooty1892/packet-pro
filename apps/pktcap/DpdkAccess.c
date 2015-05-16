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

JNIEXPORT jint JNICALL Java_DpdkAccess_nat_1setup_1and_1conf(JNIEnv *env, jclass class) {
	char *strs[] = {"Pktcap", "-c", "0x01", "-n", "1"};
	int i;
	for (i=0; i < 5; i++) {
		printf("C String: %s\n", strs[i]);
	}
	int ret = rte_eal_init(5, strs);
	return ret;
}