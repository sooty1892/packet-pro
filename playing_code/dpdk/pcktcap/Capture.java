public class Capture {

	private class Rte_eth_conf {
		public Rte_eth_conf() {};
		public Rte_eth_rxmode rxmode;
		public Rte_eth_txmode txmode;
		Rte_eth_rss_conf rss_conf;
	}

	private class Rte_eth_rxmode {
		public Rte_eth_rxmode() {};
		public int split_hdr_size;
		public int header_split;
		public int hw_ip_checksum;
		public int hw_vlan_filter;
		public int jumbo_frame;
		public int hw_strip_crc;
		public String mq_mode;
	}

	private class Rte_eth_txmode {
		public Rte_eth_txmode() {};
		public String mq_mode;
	}

	private class Rte_eth_rss_conf {
		public Rte_eth_rss_conf() {};
		public int rss_key;
		public int rss_hf;
	}

	private void setup() {
		Rte_eth_rxmode rxmode = new Rte_eth_rxmode();
		Rte_eth_txmode txmode = new Rte_eth_txmode();
		Rte_eth_rss_conf rss_conf = new Rte_eth_rss_conf();
	}

	private static int MAX_PKT_BURST = 512;
	//private static int MAX_PKT_SIZE = ???
	private static int NB_RX_QUEUE = 1;
	private static int NB_TX_QUEUE = 1;
	private static int NB_RX_DESC = 256;
	private static int NB_TX_DESC = 256;

	public static void main(String[] args) {

		setup();

		int i = 0;
		int ret = 0;
		int recv_cnt = 0;
		int ifidx = 0;

		//structs here

		//initialise eal
		Wrapper.wrap_eal_init(args);

		int count = Wrapper.wrap_eth_dev_count();
		System.out.println("eth ports = " + count);

		//configure ethernet devices

		//create mempool

		// Allocate and set up a receive queue for an Ethernet device.

		// Allocate and set up a transmit queue for an Ethernet device.

		// Start an Ethernet device.

		// Retrieve the status (ON/OFF), the speed (in Mbps) and the
    	// mode (HALF-DUPLEX or FULL-DUPLEX) of the physical link of
    	// an Ethernet device. It might need to wait up to 9 seconds in it.

    	// Enable receipt in promiscuous mode for an Ethernet device.

    	//while loop
	}

}
