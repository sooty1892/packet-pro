public class Pktcap {

	public static void main(String[] args) {
        System.out.println("JAVA: Starting Java app");
		if (DpdkAccess.setup_and_conf() < 0) {
			System.out.println("JAVA: Error in DPDK setup ");
			return;
		}
		System.out.println("JAVA: Setup complete");
	}

}
