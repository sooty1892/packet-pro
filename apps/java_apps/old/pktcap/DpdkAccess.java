import java.nio.ByteBuffer;

public class DpdkAccess {

	private static native int nat_setup_and_conf(ByteBuffer buffer);
	private static native int nat_get_packets(ByteBuffer buffer);
	private static native void nat_test_unsafe(long pointer);

	static { System.loadLibrary("nat_dpdk"); }

	public static int setup_and_conf(ByteBuffer buffer) {
		return nat_setup_and_conf(buffer);
	}

	public static int get_packets(ByteBuffer buffer) {
		return nat_get_packets(buffer);
	}

	public static void test_unsafe(long pointer) {
		nat_test_unsafe(pointer);
	}

}
