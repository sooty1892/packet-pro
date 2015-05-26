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
		
		System.out.println("JAVA: Starting receive queue polling");
		boolean t = true;
		while (t) {
			long pointer = unsafe.allocateMemory(12);
			//unsafe.putShort(pointer, (short)5);
			DpdkAccess.dpdk_receive_burst(pointer);
			
			//short counter = Utils.swap(unsafe.getShort(pointer));
			/*byte b = unsafe.getByte(pointer);
			short c = unsafe.getShort(pointer+1);
			int i = unsafe.getInt(pointer+3);
			long l = unsafe.getLong(pointer+7);
			System.out.println("JAVA: byte = " + b);
			System.out.println("JAVA: short = " + c);
			System.out.println("JAVA: int = " + i);
			System.out.println("JAVA: long = " + l);*/

			short count = unsafe.getShort(pointer);
			long add = unsafe.getLong(pointer+2);

			System.out.println("JAVA: count = " + count);
			System.out.println("JAVA: add = " + add);
			
			// do something with packets
			
			//remember to free packets sometime
			if (count > 0) {
				t = false;
			}
		}
	}
	
}
