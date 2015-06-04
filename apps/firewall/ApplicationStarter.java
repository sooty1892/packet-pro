import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


// Starter class for firewall using DPDK
public class ApplicationStarter {

	public static void main(String[] args) {
		
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
	
	public static void readConfig() {
		Properties prop = new Properties();
		InputStream input = null;
		try {
			 
			input = new FileInputStream("config.properties");
	 
			// load a properties file
			prop.load(input);
	 
			// get the property value and print it out
			System.out.println(prop.getProperty("database"));
			System.out.println(prop.getProperty("dbuser"));
			System.out.println(prop.getProperty("dbpassword"));
	 
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}
