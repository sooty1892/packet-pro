#include "Further.h"

JNIEXPORT jint JNICALL Java_Further_method1(JNIEnv *env, jclass class) {
	return 0;
}

JNIEXPORT jint JNICALL Java_Further_method2(JNIEnv *env, jclass class, jobject packet) {
	jclass cls = (*env)->FindClass(env, "Packet");
	jmethodID method = (*env)->GetMethodID(env, cls, "getResult", "()I");
	int i = (*env)->CallIntMethod(env, packet, method);
	return i;
}

JNIEXPORT jobject JNICALL Java_Further_method3(JNIEnv *env, jclass class) {
	jclass cls = (*env)->FindClass(env, "Packet");
	jmethodID constructor = (*env)->GetMethodID(env, cls, "<init>", "()V");
	jobject p = (*env)->NewObject(env, cls, constructor);
	return p;
}

JNIEXPORT void JNICALL Java_Further_method4(JNIEnv *env, jclass class, jobject packet) {
	jclass cls = (*env)->FindClass(env, "Packet");
	jmethodID method = (*env)->GetMethodID(env, cls, "getResult", "()I");
	int i = (*env)->CallIntMethod(env, packet, method);
	method = (*env)->GetMethodID(env, cls, "setResult", "(I)V");
	(*env)->CallVoidMethod(env, packet, method, i+1);
}
