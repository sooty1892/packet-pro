#include "Jni.h"

JNIEXPORT void JNICALL Java_Jni_objectPrint(JNIEnv *env, jclass class, jobject obj) {
	jclass cls = (*env)->FindClass(env, "Jni");
	jmethodID method = (*env)->GetMethodID(env, cls, "getX", "()I");
	int i = (*env)->CallIntMethod(env, obj, method);
	printf("Object x value is %i\n", i);
}