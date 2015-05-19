public class Pktcap {

	public static void main(String[] args) {
        System.out.println("JAVA: Starting Java app");
        ByteBuffer buffer = ByteBuffer.allocateDirect(8*1024);
		if (DpdkAccess.setup_and_conf(buffer) < 0) {
			System.out.println("JAVA: Error in DPDK setup ");
			return;
		}
		System.out.println("JAVA: Setup complete");
	}

}
