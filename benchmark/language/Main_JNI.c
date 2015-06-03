#include "Main_JNI.h"
#include <time.h>
#include <stdlib.h>

JNIEXPORT void JNICALL Java_Main_1JNI_popPacket(JNIEnv *env, jclass class, jobject packet) {
	srand(time(NULL));
	jclass cls = (*env)->FindClass(env, "Packet");
	jmethodID method1 = (*env)->GetMethodID(env, cls, "setA", "(I)V");
	jmethodID method2 = (*env)->GetMethodID(env, cls, "setB", "(I)V");
	jmethodID method3 = (*env)->GetMethodID(env, cls, "setC", "(I)V");
	jmethodID method4 = (*env)->GetMethodID(env, cls, "setD", "(I)V");
	jmethodID method5 = (*env)->GetMethodID(env, cls, "setE", "(I)V");
	(*env)->CallVoidMethod(env, packet, method1, rand());
	(*env)->CallVoidMethod(env, packet, method2, rand());
	(*env)->CallVoidMethod(env, packet, method3, rand());
	(*env)->CallVoidMethod(env, packet, method4, rand());
	(*env)->CallVoidMethod(env, packet, method5, rand());
}

JNIEXPORT void JNICALL Java_Main_1JNI_retPacket(JNIEnv *env, jclass class, jobject packet) {
	jclass cls = (*env)->FindClass(env, "Packet");
	jmethodID method = (*env)->GetMethodID(env, cls, "getResult", "()I");
	int i = (*env)->CallIntMethod(env, packet, method);
	if (i == -1) {
		printf("RESULT ERROR\n");
	}
}
