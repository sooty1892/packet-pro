// Contains native methods to connect to C DPDK libraries

public class DpdkAccess {
	
	private static native int nat_setup();
	private static native void nat_receive_burst(long pointer);
	private static native void nat_free_packets(long pointer);
	private static native void nat_send_packets(long pointer);
	private static native void nat_set_core_mask(String value);
	private static native void nat_set_port_mask(String value);
	private static native void nat_set_program_name(String value);
	private static native void nat_set_memory_channels(String value);
	private static native void nat_set_memory(String value);
	private static native void nat_set_program_id(String value);
	private static native void nat_set_blacklist(String[] value);
	
	static {
		System.loadLibrary("nat_dpdk");
	}
	
	public static int dpdk_setup() {
		return nat_setup();
	}
	
	public static void dpdk_receive_burst(long pointer) {
		nat_receive_burst(pointer);
	}
	
	public static void dpdk_free_packets(long pointer) {
		nat_free_packets(pointer);
	}
	
	public static void dpdk_send_packets(long pointer) {
		nat_send_packets(pointer);
	}
	
	public static void dpdk_set_core_mask(String value) {
		nat_set_core_mask(value);
	}
	
	public static void dpdk_set_port_mask(String value) {
		nat_set_port_mask(value);
	}
	
	public static void dpdk_set_program_name(String value) {
		nat_set_program_name(value);
	}
	
	public static void dpdk_set_memory_channels(String value) {
		nat_set_memory_channels(value);
	}
	
	public static void dpdk_set_memory(String value) {
		nat_set_memory(value);
	}
	
	public static void dpdk_set_program_id(String value) {
		nat_set_program_id(value);
	}
	
	public static void dpdk_set_blacklist(String[] value) {
		nat_set_blacklist(value);
	}
	
}
