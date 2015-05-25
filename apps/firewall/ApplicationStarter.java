import java.lang.reflect.Field;
import sun.misc.Unsafe;

// Starter class for firewall using DPDK
public class ApplicationStarter {

	public static void Main(String[] args) {
		System.out.println("JAVA: Starting Firewall");
		
		int ret = DpdkAccess.dpdk_setup();
		if (ret < 0) {
			System.out.println("JAVA: Error in DPDK setup");
			return;
		}
		
		// here use jni calls to get various information needed later
		// like size of structs, offsets and memory sizes needed
		
		System.out.println("JAVA: Starting receive queue polling");
		while (true) {
			int count = DpdkAccess.dpdk_receive_burst();
			// do something with packets
		}
	}
	
}
