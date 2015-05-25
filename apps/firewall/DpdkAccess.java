// Contains native methods to connect to C DPDK libraries

public class DpdkAccess {
	
	private static native int nat_setup();
	private static native void nat_receive_burst(long pointer);
	
	static {
		System.loadLibrary("nat_dpdk");
	}
	
	public static int dpdk_setup() {
		return nat_setup();
	}
	
	public static void dpdk_receive_burst(long pointer) {
		nat_receive_burst(pointer);
	}
	
}
