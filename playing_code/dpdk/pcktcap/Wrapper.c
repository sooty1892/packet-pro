#include <stdio.h>
#include "Wrapper.h"

#include <stdio.h>
#include <string.h>
#include <stdint.h>
#include <errno.h>
#include <sys/queue.h>

#include <rte_memory.h>
#include <rte_memzone.h>
#include <rte_launch.h>
#include <rte_tailq.h>
#include <rte_eal.h>
#include <rte_per_lcore.h>
#include <rte_lcore.h>
#include <rte_debug.h>

JNIEXPORT jint JNICALL Java_Wrapper_eal_1init (JNIEnv *env, jclass class, jobjectArray stringArray, jint num) {
	char **orderedIds;
	orderedIds = malloc(num * sizeof(char*));
	for (int i=0; i<num; i++) {
        jstring string = (jstring) GetObjectArrayElement(env, stringArray, i);
        const char *rawString = GetStringUTFChars(env, string, 0);
        len = strlen(rawString);
        orderedIds[i] = malloc((len+1) * sizeof(char));
    }

	ret = rte_eal_init(num, orderedIds);
	// Don't forget to call `ReleaseStringUTFChars` when you're done.
	// also release malloc
	if (ret < 0) {
		rte_panic("Cannot init EAL\n");
	}
	return ret;
}
