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

JNIEXPORT jint JNICALL Java_Wrapper_eal_1init(JNIEnv *env, jclass class, jint num, jobjectArray stringArray) {
	/*char **orderedIds;
	orderedIds = malloc(num * sizeof(char*));
	int i;
	for (i=0; i<num; i++) {
	        jstring string = (jstring) (*env)->GetObjectArrayElement(env, stringArray, i);
        	const char *rawString = (*env)->GetStringUTFChars(env, string, 0);
	        int len = strlen(rawString);
	        orderedIds[i] = malloc((len+1) * sizeof(char));
	}

	int ret = rte_eal_init(num, orderedIds);
	// Don't forget to call `ReleaseStringUTFChars` when you're done.
	// also release malloc
	if (ret < 0) {
		rte_panic("Cannot init EAL\n");
	}
	return ret;*/
	/*int stringCount = env->GetArrayLength(stringArray);
	int i;
	for (i=0; i < stringCount; i++) {
		jstring string = (jstring) env->GetObjectArrayElement(env, stringArray, i);
		const char *rawString = env->GetStringUTFChars(env, string, 0);
		printf("C String: %s", rawString);
	}*/
	char *strs[] = {"Wrapper", "-c", "0x01", "-n", "1"};
	int i;
	for (i=0; i < 5; i++) {
		printf("C String: %s\n", strs[i]);
	}
	int ret = rte_eal_init(5, strs);
}

JNIEXPORT jint JNICALL Java_Wrapper_lcore_1id(JNIEnv *env, jclass class) {
	return rte_lcore_id();
}

JNIEXPORT void JNICALL Java_Wrapper_mp_1wait_1lcore(JNIEnv *env, jclass class) {
	rte_eal_mp_wait_lcore();
}
