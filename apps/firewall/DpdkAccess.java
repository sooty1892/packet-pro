// Contains native methods to connect to C DPDK libraries

public class DpdkAccess {
	
	private static native int nat_setup();
	private static native void nat_receive_burst(long pointer);
	private static native int nat_size_of_ether_hdr();
	private static native int nat_size_of_mbuf();
	private static native int nat_size_of_void_pointer();
	private static native void nat_free_packets(long pointer);
	private static native void nat_send_packets(long pointer);
	
	static {
		System.loadLibrary("nat_dpdk");
	}
	
	public static int dpdk_setup() {
		return nat_setup();
	}
	
	public static void dpdk_receive_burst(long pointer) {
		nat_receive_burst(pointer);
	}
	
	public static int dpdk_size_of_ether_hdr() {
		return nat_size_of_ether_hdr();
	}
	
	public static int dpdk_size_of_mbuf() {
		return nat_size_of_mbuf();
	}
	
	public static int dpdk_size_of_void_pointer() {
		return nat_size_of_void_pointer();
	}
	
	public static void dpdk_free_packets(long pointer) {
		nat_free_packets(pointer);
	}
	
	public static void dpdk_send_packets(long pointer) {
		nat_send_packets(pointer);
	}
	
}
