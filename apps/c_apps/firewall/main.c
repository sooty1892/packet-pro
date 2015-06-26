#include <stdio.h>
#include <string.h>
#include <stdint.h>
#include <errno.h>
#include <assert.h>
#include <sys/queue.h>

#include <rte_ip.h>
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
#include <rte_timer.h>
#include <rte_cycles.h>

#include "main.h"

#define MAX_PKT_BURST 512

#define MAX_PKT_SIZE (2*1024 + sizeof(struct rte_mbuf) + RTE_PKTMBUF_HEADROOM)

#define NB_RX_QUEUE 2

#define NB_TX_QUEUE 2

#define	NB_RX_DESC 256

#define	NB_TX_DESC 256

struct rte_mbuf *rx_mbufs[MAX_PKT_BURST];

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
        .mq_mode = ETH_MQ_TX_VMDQ_ONLY
    }, 
 }; 

static struct rte_eth_txconf tx_conf = {
    .tx_thresh = {
        .pthresh = 36,
        .hthresh = 0,
        .wthresh = 0,
    },
    .tx_rs_thresh = 0,
    .tx_free_thresh = 0,
};

static struct rte_eth_rxconf rx_conf = {
    .rx_thresh = {
        .pthresh = 8,
        .hthresh = 8,
        .wthresh = 4,
    },
    .rx_free_thresh = 64,
    .rx_drop_en = 0,
};


struct mbuf_list {
    unsigned len;
    struct rte_mbuf *list[MAX_PKT_BURST];
} free_list, send_list;

// RTE mempool structure
static struct rte_mempool *rx_pool;
static void send_packet(struct rte_mbuf *);
static void free_packet(struct rte_mbuf *);
static void send_burst(void);

static uint32_t blacklist[] = {0, 1};
int size = 2;

static void send_burst(void) {
    struct rte_mbuf **list = (struct rte_mbuf **)send_list.list;
    int i;
    if (rte_lcore_id() == 1) {
      i = 0;
    } else {
      i = 1;
    }
    int ret = rte_eth_tx_burst(0, i, list, MAX_PKT_BURST);
    
    if (unlikely(ret < MAX_PKT_BURST)) {
        do {
            free_packet(list[ret]);
        } while (++ret < MAX_PKT_BURST);
    }
}

static void send_packet(struct rte_mbuf *m) {

    unsigned len = send_list.len;
    send_list.list[len] = m;
    len++;

    /* enough pkts to be sent */
    if (unlikely(len == MAX_PKT_BURST)) {
        send_burst();
        len = 0;
    }

    send_list.len = len;
}

static void free_packet(struct rte_mbuf *m) {
    rte_pktmbuf_free(m);
}

int main_loop(void *);

int main_loop(__attribute__ ((unused)) void *arg) {
    int recv_cnt, i, ifidx = 0;
    struct ipv4_hdr *iphdr;
    struct rte_mbuf *m;
    int id = rte_lcore_id();
    printf("Core %i starting\n", id);
    fflush(stdout);
    int b;
    if (id == 1) {
        b = 0;
    } else {
        b = 1;
    }
    while(1) {
        recv_cnt = rte_eth_rx_burst(ifidx, b, rx_mbufs, MAX_PKT_BURST);
        if (recv_cnt < 0) {
            if (errno != EAGAIN && errno != EINTR) {
                perror("rte_eth_rx_burst()");
                assert(0);
            }
        }
        if ( recv_cnt > 0) {
            for (i = 0 ; i < recv_cnt; i++) {
                m = rx_mbufs[i];
                iphdr = (struct ipv4_hdr *)rte_pktmbuf_adj(m, (uint16_t)sizeof(struct ether_hdr));
                uint32_t dest_addr = rte_be_to_cpu_32(iphdr->dst_addr);

                int drop = 0;

                int a;
                for (a = 0; a < size; a++) {
                    if (blacklist[a] == dest_addr) {
                        drop = 1;
                    }
                }

                if (drop == 0) {
                    send_packet(m);
                    continue;
                }

                free_packet(m);
            }
        }
    }
    return 0;
}

void timer_setup(void);
void do_stats(struct rte_timer *, void *);

//received bytes
uint64_t pre_ibytes = 0;
//received packets - successful
uint64_t pre_ipackets = 0;

void do_stats(__attribute__ ((unused)) struct rte_timer *tim, __attribute__ ((unused)) void *arg) {
    struct rte_eth_stats stats;

    rte_eth_stats_get(0, &stats);
    uint64_t diff_bytes = stats.ibytes - pre_ibytes;
    pre_ibytes = stats.ibytes;
    uint64_t diff_packets = stats.ipackets - pre_ipackets;
    pre_ipackets = stats.ipackets;
    printf("Bytes: %lu\n", diff_bytes);
    printf("Packets: %lu\n", diff_packets);
    fflush(stdout);
}

static struct rte_timer timer;

void timer_setup(void) {
    int lcore_id = rte_get_master_lcore();
    rte_timer_subsystem_init();
    rte_timer_init(&timer);
    int ret = rte_timer_reset(&timer, rte_get_timer_hz(), PERIODICAL, lcore_id, do_stats, NULL);
    if (ret != 0) {
        printf("TIMER_ERROR");
        fflush(stdout);
    }
}

int main(int argc, char **argv) {
    int ret, ifidx = 0;
    uint8_t count;

    struct rte_eth_link link;

    // initialise eal
    ret = rte_eal_init(argc, argv);
    if (ret < 0) {
        rte_panic("Cannot init EAL\n");
    }

    count = rte_eth_dev_count();
    printf("# of eth ports = %d\n", count);
    //memset(&eth_conf, 0, sizeof eth_conf);
    ret = rte_eth_dev_configure(0, 2, 2, &eth_conf);
    if (ret < 0) {
        rte_exit(EXIT_FAILURE, "Cannot configure device: error=%d, port=%d\n", ret, 0);
    }
    printf("If %d rte_eth_dev_configure() successful\n", ifidx);

    rx_pool = rte_mempool_create("rx_pool", 16*1024, MAX_PKT_SIZE, 0,
                                sizeof (struct rte_pktmbuf_pool_private),
                                rte_pktmbuf_pool_init, NULL,
                                rte_pktmbuf_init, NULL, 1, 0);

   
    if (rx_pool == NULL) {
        rte_exit(EXIT_FAILURE, "rte_mempool_create(): error\n");
    }

    ret = rte_eth_rx_queue_setup(ifidx, 0, NB_RX_DESC, 1, &rx_conf, rx_pool);
    if (ret < -1) {
        rte_exit(EXIT_FAILURE, "rte_eth_rx_dev_queue_setup(): error=%d, port=%d\n", ret, ifidx);
    }
    printf("If %d rte_eth_rx_queue_setup() successful\n", ifidx);

   ret = rte_eth_rx_queue_setup(0, 1, NB_RX_DESC, 1, &rx_conf, rx_pool);
    if (ret < -1) {
	rte_exit(EXIT_FAILURE, "2 probs");
    }

    ret = rte_eth_tx_queue_setup(ifidx, 0, NB_TX_DESC, 1, &tx_conf);
    if (ret < 0) {
        rte_exit(EXIT_FAILURE, "rte_eth_tx_queue_setup(): error=%d, port=%d\n", ret, ifidx);
    }
    printf("If %d rte_eth_tx_queue_setup() successful\n", ifidx);

    ret = rte_eth_tx_queue_setup(ifidx, 1, NB_TX_DESC, 1, &tx_conf);
    if (ret < 0) {
        rte_exit(EXIT_FAILURE, "rte_eth_tx_queue_setup(): error=%d, port=%d\n", ret, ifidx);
    }
    printf("If %d rte_eth_tx_queue_setup() successful\n", ifidx);

    ret = rte_eth_dev_start(ifidx);
    if (ret < 0) {
        rte_exit(EXIT_FAILURE, "rte_eth_dev_start(): error=%d, port=%d\n", ret, ifidx);
    }
    printf("If %d rte_eth_dev_start() successful\n", ifidx);

    rte_eth_link_get(ifidx, &link);
    if (link.link_status == 0) {
        rte_exit(EXIT_FAILURE, "DPDK interface is down: %d\n", ifidx);
    }
    printf("If %d is UP and RUNNING\n", ifidx);

    rte_eth_promiscuous_enable(ifidx);

    struct lcore_config lc = lcore_config[0];
    printf("%i", lc.core_id);

    uint32_t a;
    for (a = 0; a < 32; a++) {
        if (a == rte_get_master_lcore() || !rte_lcore_is_enabled(a)) {
            continue;
        }
        ret = rte_eal_remote_launch(main_loop, NULL, a);
        if (ret != 0) {
            printf("ERROR");
        }
    }
    rte_delay_ms(1000);
    timer_setup();

    for(;;) {
        rte_timer_manage();
    }

   rte_eal_mp_wait_lcore();

    return 0;
}
