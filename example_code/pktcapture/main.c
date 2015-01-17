/*-
 *   BSD LICENSE
 *
 *   Copyright(c) 2010-2014 Intel Corporation. All rights reserved.
 *   All rights reserved.
 *
 *   Redistribution and use in source and binary forms, with or without
 *   modification, are permitted provided that the following conditions
 *   are met:
 *
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in
 *       the documentation and/or other materials provided with the
 *       distribution.
 *     * Neither the name of Intel Corporation nor the names of its
 *       contributors may be used to endorse or promote products derived
 *       from this software without specific prior written permission.
 *
 *   THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 *   "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 *   LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 *   A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 *   OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *   SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *   LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 *   DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 *   THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *   (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 *   OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

#include <stdio.h>
#include <string.h>
#include <stdint.h>
#include <errno.h>
#include <assert.h>
#include <sys/queue.h>

#include <rte_memory.h>
#include <rte_memzone.h>
#include <rte_launch.h>
#include <rte_tailq.h>
#include <rte_eal.h>
#include <rte_per_lcore.h>
#include <rte_lcore.h>
#include <rte_debug.h>
#include <rte_ethdev.h>
#include <rte_ring.h>
#include <rte_mempool.h>
#include <rte_mbuf.h>

#include "main.h"

#define MAX_PKT_BURST 512

#define MAX_PKT_SIZE (2*1024 + sizeof(struct rte_mbuf) + RTE_PKTMBUF_HEADROOM)

#define NB_RX_QUEUE 1

#define NB_TX_QUEUE 1

#define	NB_RX_DESC 256

#define	NB_TX_DESC 256

// For configuring an ethernet port
static struct rte_eth_conf eth_conf = { 
    .rxmode = { 
        .mq_mode = ETH_MQ_RX_RSS,
        .split_hdr_size = 0, 
        .header_split = 0, 
        .hw_ip_checksum = 1, 
        .hw_vlan_filter = 0, 
        .jumbo_frame = 0,
        .hw_strip_crc = 0, 
    }, 
    .rx_adv_conf = {
        .rss_conf = {
            .rss_key = NULL,
            .rss_hf = ETH_RSS_IP,
        },
    },
    .txmode = { 
        .mq_mode = ETH_MQ_TX_NONE, 
    }, 
 }; 

// Configures a TX ring of an Ethernet port
static struct rte_eth_txconf tx_conf = {
    .tx_thresh = {
        .pthresh = 36,
        .hthresh = 0,
        .wthresh = 0,
    },
    .tx_rs_thresh = 0,
    .tx_free_thresh = 0,
    //.txq_flags = (ETH_TXQ_FLAGS_NOMULTSEGS |
    //		ETH_TXQ_FLAGS_NOVLANOFFL |
    //		ETH_TXQ_FLAGS_NOXSUMSCTP |
    //		ETH_TXQ_FLAGS_NOXSUMUDP  |
    //		ETH_TXQ_FLAGS_NOXSUMTCP)
};

// Configures a RX ring of an Ethernet port
static struct rte_eth_rxconf rx_conf = {
    .rx_thresh = {
        .pthresh = 8,
        .hthresh = 8,
        .wthresh = 4,
    },
    .rx_free_thresh = 64,
    .rx_drop_en = 0,
};

/* static int */
/* lcore_hello(__attribute__((unused)) void *arg) */
/* { */
/* 	unsigned lcore_id; */
/* 	lcore_id = rte_lcore_id(); */
/* 	printf("hello from core %u\n", lcore_id); */
/* 	return 0; */
/* } */

// RTE mempool structure
static struct rte_mempool *rx_pool;

int MAIN(int argc, char **argv) {
    int i, ret, recv_cnt, ifidx = 0;
    //unsigned lcore_id;
    uint8_t count;

    // used to retrieve link-level information of an
    // Ethernet port. Aligned for atomic64 read/write
    struct rte_eth_link link;

    // contains a packet mbuf
    struct rte_mbuf *rx_mbufs[MAX_PKT_BURST];
    //struct rte_eth_conf eth_conf;

    // initialise eal
    ret = rte_eal_init(argc, argv);
    if (ret < 0) {
        rte_panic("Cannot init EAL\n");
    }

    // Get the total number of Ethernet devices that
    // have been successfully initialized
    count = rte_eth_dev_count();
    printf("# of eth ports = %d\n", count);
    memset(&eth_conf, 0, sizeof eth_conf);
    // Configure an Ethernet device
    ret = rte_eth_dev_configure(ifidx, NB_RX_QUEUE, NB_TX_QUEUE, &eth_conf);
    if (ret < 0) {
        rte_exit(EXIT_FAILURE, "Cannot configure device: error=%d, port=%d\n", ret, ifidx);
    }
    printf("If %d rte_eth_dev_configure() successful\n", ifidx);
    // ID of the execution unit we are running on
    unsigned cpu = rte_lcore_id();
    // Get the ID of the physical socket of the specified lcore
    unsigned socketid = rte_lcore_to_socket_id(cpu);

    // reates a new mempool named 'rx_pool in memory.
    rx_pool = rte_mempool_create("rx_pool", 8*1024, MAX_PKT_SIZE, 0,
                                sizeof (struct rte_pktmbuf_pool_private),
                                rte_pktmbuf_pool_init, NULL,
                                rte_pktmbuf_init, NULL, socketid, 0);
    if (rx_pool == NULL) {
        rte_exit(EXIT_FAILURE, "rte_mempool_create(): error\n");
    }

    // Allocate and set up a receive queue for an Ethernet device.
    ret = rte_eth_rx_queue_setup(ifidx, 0, NB_RX_DESC, socketid, &rx_conf, rx_pool);
    if (ret < -1) {
        rte_exit(EXIT_FAILURE, "rte_eth_rx_dev_queue_setup(): error=%d, port=%d\n", ret, ifidx);
    }
    printf("If %d rte_eth_rx_queue_setup() successful\n", ifidx);

    // Allocate and set up a transmit queue for an Ethernet device.
    ret = rte_eth_tx_queue_setup(ifidx, 0, NB_TX_DESC, socketid, &tx_conf);
    if (ret < 0) {
        rte_exit(EXIT_FAILURE, "rte_eth_tx_queue_setup(): error=%d, port=%d\n", ret, ifidx);
    }
    printf("If %d rte_eth_tx_queue_setup() successful\n", ifidx);

    // Start an Ethernet device.
    ret = rte_eth_dev_start(ifidx);
    if (ret < 0) {
        rte_exit(EXIT_FAILURE, "rte_eth_dev_start(): error=%d, port=%d\n", ret, ifidx);
    }
    printf("If %d rte_eth_dev_start() successful\n", ifidx);

    // Retrieve the status (ON/OFF), the speed (in Mbps) and the
    // mode (HALF-DUPLEX or FULL-DUPLEX) of the physical link of
    // an Ethernet device. It might need to wait up to 9 seconds in it.
    rte_eth_link_get(ifidx, &link);
    if (link.link_status == 0) {
        rte_exit(EXIT_FAILURE, "DPDK interface is down: %d\n", ifidx);
    }
    printf("If %d is UP and RUNNING\n", ifidx);

    // Enable receipt in promiscuous mode for an Ethernet device.
    rte_eth_promiscuous_enable(ifidx);

    /* call lcore_hello() on every slave lcore */
    //RTE_LCORE_FOREACH_SLAVE(lcore_id) {
    //	rte_eal_remote_launch(lcore_hello, NULL, lcore_id);
    //}
    long pktcount = 0;
    /* call it on master lcore too */
    while(1) {
        // Retrieve a burst of input packets from a receive queue of an
        // Ethernet device. The retrieved packets are stored in rte_mbuf
        // structures whose pointers are supplied in the rx_pkts array
        recv_cnt = rte_eth_rx_burst(ifidx, 0, rx_mbufs, MAX_PKT_BURST);
        if (recv_cnt < 0) {
            if (errno != EAGAIN && errno != EINTR) {
                perror("rte_eth_rx_burst()");
                assert(0);
            }
        }
        if ( recv_cnt > 0) {
            pktcount += recv_cnt;
            for (i = 0 ; i < recv_cnt; i++)
                /* drop packet */
                // Free a packet mbuf back into its original mempool.
                // Free an mbuf, and all its segments in case of chained
                // buffers. Each segment is added back into its original mempool.
                rte_pktmbuf_free(rx_mbufs[i]);
            }
            if (pktcount == 10000000)
                printf("Received %ld packets so far\n", pktcount);
            } 
            printf("Received %ld packets\n", pktcount);
        }
        //  rte_eal_mp_wait_lcore();
    }
    return 0;
}
