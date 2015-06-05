#include "Utils.h"

void printIpv4Data(struct ipv4_hdr* hdr, int packet_num) {
	printf("C: Packet %d version_ihl = %" PRIu8 " : %p\n", packet_num, hdr->version_ihl, &hdr->version_ihl);
	printf("C: Packet %d type_of_service = %" PRIu8 " : %p\n", packet_num, hdr->type_of_service, &hdr->type_of_service);
	printf("C: Packet %d total_length = %" PRIu16 " : %p\n", packet_num, hdr->total_length, &hdr->total_length);
	printf("C: Packet %d packet_id = %" PRIu16 " : %p\n", packet_num, hdr->packet_id, &hdr->packet_id);
	printf("C: Packet %d fragment_offset = %" PRIu16 " : %p\n", packet_num, hdr->fragment_offset, &hdr->fragment_offset);
	printf("C: Packet %d time_to_live = %" PRIu8 " : %p\n", packet_num, hdr->time_to_live, &hdr->time_to_live);
	printf("C: Packet %d next_proto_id = %" PRIu8 " : %p\n", packet_num, hdr->next_proto_id, &hdr->next_proto_id);
	printf("C: Packet %d hdr_checksum = %" PRIu16 " : %p\n", packet_num, hdr->hdr_checksum, &hdr->hdr_checksum);
	printf("C: Packet %d src_addr = %" PRIu32 " : %p\n", packet_num, hdr->src_addr, &hdr->src_addr);
	/*int i;
	unsigned int maxpow = 1 << (sizeof(uint32_t)*8 - 1);
	uint32_t num = hdr->src_addr;
	for (i = 0; i < sizeof(uint32_t)*8; ++i) {
		printf("%u ", num * maxpow ? 1 : 0);
		num = num >> 1;
	}
	printf("\n");*/
	printf("C: Packet %d dst_addr = %" PRIu32 " : %p\n", packet_num, hdr->dst_addr, &hdr->dst_addr);
	/*num = hdr->dst_addr;
	for (i = 0; i < sizeof(uint32_t)*8; ++i) {
		printf("%u ", num * maxpow ? 1 : 0);
		num = num >> 1;
	}*/
	printf("\n");
}

uint8_t get8(uint8_t *pointer, int position) {
	return pointer[position];
}

uint16_t get16(uint8_t *pointer, int position) {
	uint16_t value = pointer[position+1] << 8;
	value = value + pointer[position];
	return value;
}

uint32_t get32(uint8_t *pointer, int position) {
	uint32_t value = pointer[position+3] << 24;
	value = value + (pointer[position+2] << 16);
	value = value + (pointer[position+1] << 8);
	value = value + pointer[position];
	return value;
}

uint64_t get64(uint8_t *pointer, int position) {
	uint64_t value = (uint64_t)(pointer[position+7]) << 56;
	value = value + ((uint64_t)(pointer[position+6]) << 48);
	value = value + ((uint64_t)(pointer[position+5]) << 40);
	value = value + ((uint64_t)(pointer[position+4]) << 32);
	value = value + ((uint64_t)(pointer[position+3]) << 24);
	value = value + ((uint64_t)(pointer[position+2]) << 16);
	value = value + ((uint64_t)(pointer[position+1]) << 8);
	value = value + (uint64_t)(pointer[position]);
	return value;
}


void insert8(uint8_t *pointer, int position, uint8_t value) {
	pointer[position] = value ;
}

void insert16(uint8_t *pointer, int position, uint16_t value) {
	pointer[position+1] = (uint8_t)((value >> 8) & 0xFF);
	pointer[position] = (uint8_t)((value) & 0xFF);
}

void insert32(uint8_t *pointer, int position, uint32_t value) {
	pointer[position+3] = (uint8_t)((value >> 24) & 0xFF);
	pointer[position+2] = (uint8_t)((value >> 16) & 0xFF);
	pointer[position+1] = (uint8_t)((value >> 8) & 0XFF);
	pointer[position] = (uint8_t)((value) & 0XFF);
}

void insert64(uint8_t *pointer, int position, uint64_t value) {
	pointer[position+7] = (uint8_t)((value >> 56) & 0xFF);
	pointer[position+6] = (uint8_t)((value >> 48) & 0xFF);
	pointer[position+5] = (uint8_t)((value >> 40) & 0XFF);
	pointer[position+4] = (uint8_t)((value >> 32) & 0XFF);
	pointer[position+3] = (uint8_t)((value >> 24) & 0XFF);
	pointer[position+2] = (uint8_t)((value >> 16) & 0XFF);
	pointer[position+1] = (uint8_t)((value >> 8) & 0XFF);
	pointer[position] = (uint8_t)((value) & 0XFF);
}
