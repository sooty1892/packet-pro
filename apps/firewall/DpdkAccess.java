// Contains native methods to connect to C DPDK libraries

public class DpdkAccess {
	
	private static native int nat_setup();
	private static native int nat_receive_burst();
	
	static {
		System.loadLibrary("nat_dpdk");
	}
	
	public static int dpdk_setup() {
		return nat_setup();
	}
	
	public static int dpdk_receive_burst() {
		return nat_receive_burst();
	}
	
}
