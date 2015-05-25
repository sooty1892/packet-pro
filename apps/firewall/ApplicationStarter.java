import java.lang.reflect.Field;
import sun.misc.Unsafe;

// Starter class for firewall using DPDK
public class ApplicationStarter {

	public static void main(String[] args) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		
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
		
		// here use jni calls to get various information needed later
		// like size of structs, offsets and memory sizes needed
		
		/*System.out.println("JAVA: Starting receive queue polling");
		//while (true) {
			long pointer = unsafe.allocateMemory(12);
			//unsafe.putShort(pointer, (short)5);
			DpdkAccess.dpdk_receive_burst(pointer);
			
			//short counter = Utils.swap(unsafe.getShort(pointer));
			short counter = unsafe.getShort(pointer);
			byte test = unsafe.getByte(pointer+2);
			System.out.println("JAVA: Count = " + counter);
			System.out.println("JAVA: Test = " + test);
			
			// do something with packets
			
			//remember to free packets sometime
		//}*/
	}
	
}
