import java.lang.reflect.Field;
import sun.misc.Unsafe;

// Starter class for firewall using DPDK
public class ApplicationStarter {
	
	private static final int LONG_SIZE = Long.SIZE / Byte.SIZE;
	private static final int INT_SIZE = Integer.SIZE / Byte.SIZE;
	private static final int SHORT_SIZE = Short.SIZE / Byte.SIZE;
	private static final int BYTE_SIZE = Byte.SIZE / Byte.SIZE;

	public static void main(String[] args) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, InterruptedException {
		
		System.out.println("JAVA: Setting up unsafe memory");
		Field f = Unsafe.class.getDeclaredField("theUnsafe");
        f.setAccessible(true);
        Unsafe unsafe = (Unsafe)f.get(null);
        UnsafeMemory unsafeMem = new UnsafeMemory(unsafe);
		
		System.out.println("JAVA: Starting Firewall");
		
		int ret = DpdkAccess.dpdk_setup();
		if (ret < 0) {
			System.out.println("JAVA: Error in DPDK setup");
			return;
		}
		
		int ether_hdr_size = DpdkAccess.dpdk_size_of_ether_hdr();
		int mbuf_size = DpdkAccess.dpdk_size_of_mbuf();
		int void_pointer_size = DpdkAccess.dpdk_size_of_void_pointer();

		System.out.println("JAVA: ether_hdr_size = " + ether_hdr_size);
		System.out.println("JAVA: mbuf_size = " + mbuf_size);
		System.out.println("JAVA: void_pointer_size = " + void_pointer_size);
		
		// here use jni calls to get various information needed later
		// like size of structs, offsets and memory sizes needed
		
		System.out.println("JAVA: Starting receive queue polling");
		while (true) {
			int memory_size = ((Long.SIZE / Byte.SIZE) * 512) + 2;
			long mem_pointer = unsafe.allocateMemory(memory_size);
			
			DpdkAccess.dpdk_receive_burst(mem_pointer);
			
			int offset = 0;
			
			short packet_count = unsafe.getShort(mem_pointer + offset);
			offset += SHORT_SIZE;


			//remember to free packets sometime
			if (packet_count > 0) {
				System.out.println("JAVA: Parsing " + packet_count + " packets!");
				
				for (int i = 0; i < packet_count; i++) {
					long ip_hdr_pointer = unsafe.getLong(mem_pointer + offset + (i*LONG_SIZE));
					System.out.println("JAVA: Packet " + i + " pointer = " + ip_hdr_pointer);
				}
				
				System.out.println();
				System.out.println();
				Thread.sleep(1000);
			}
		}
	}
	
}
