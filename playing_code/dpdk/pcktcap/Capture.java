public class Capture {

	private class Rte_eth_conf {
		public Rte_eth_conf() {};
		public Rte_eth_rxmode;
	}

	private class Rte_eth_rxmode {
		public Rte_eth_rxmode() {};
	}

	private static int MAX_PKT_BURST = 512;
	//private static int MAX_PKT_SIZE = ???
	private static int NB_RX_QUEUE = 1;
	private static int NB_TX_QUEUE = 1;
	private static int NB_RX_DESC = 256;
	private static int NB_TX_DESC = 256;

	//structs here

	public static void main(String[] args) {
		int i = 0;
		int ret = 0;
		int recv_cnt = 0;
		int ifidx = 0;
		int count = 0;

		//structs here

		//initialise eal
		Wrapper.wrap_eal_init(args);

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
