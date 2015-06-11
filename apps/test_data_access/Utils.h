/*
 * methods for getting and inserting different data types
 * into and out of continuous memory
 */

#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <inttypes.h>

//void printIpv4Data(struct ipv4_hdr *, int);

uint8_t get8(uint8_t *, int);

uint16_t get16(uint8_t *, int);

uint32_t get32(uint8_t *, int);

uint64_t get64(uint8_t *, int);

void insert8(uint8_t *, int, uint8_t);

void insert16(uint8_t *, int, uint16_t);

void insert32(uint8_t *, int, uint32_t);

void insert64(uint8_t *, int, uint64_t);
