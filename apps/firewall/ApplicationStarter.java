
// Starter class for firewall using DPDK
public class ApplicationStarter {

	public static void main(String[] args) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, InterruptedException {
		
		System.out.println("JAVA: Setting up unsafe memory");
        UnsafeAccess ua = new UnsafeAccess();
		
		System.out.println("JAVA: Starting Firewall");
		
		int ret = DpdkAccess.dpdk_setup();
		if (ret < 0) {
			System.out.println("JAVA: Error in DPDK setup");
			return;
		}
		
		// int ether_hdr_size = DpdkAccess.dpdk_size_of_ether_hdr();
		// int mbuf_size = DpdkAccess.dpdk_size_of_mbuf();
		// int void_pointer_size = DpdkAccess.dpdk_size_of_void_pointer();
		
		// here use jni calls to get various information needed later
		// like size of structs, offsets and memory sizes needed
		
		System.out.println("JAVA: Starting receive queue polling");
		ReceivePoller rp = new ReceivePoller(ua);
		rp.start();
	}
	
}
