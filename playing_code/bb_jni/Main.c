#include "Main.h"

struct ipv4_hdr {
	uint8_t version_ihl;
	uint8_t type_of_service;
	uint16_t total_length;
	uint16_t packet_id;
	uint16_t fragment_offset;
	uint8_t time_to_live;
	uint8_t next_proto_id;
	uint16_t hdr_checksum;
	uint32_t src_addr;
	uint32_t dst_addr;
};

JNIEXPORT void JNICALL Java_Main_init(JNIEnv *env, jclass cla, jobject buffer) {
	char *buf = (char*)(*env)->GetDirectBufferAddress(env, buffer);
	buf[0] = 5;
}
