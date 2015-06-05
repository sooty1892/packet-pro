import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class ApplicationStarter {
	
	private static final String PACKET_INSPECTOR = "packetinspector";
	private static final String RX_BURST = "rxburst";
	private static final String TX_BURST = "txburst";
	private static final String FREE_BURST = "freeburst";
	private static final String CORE_MASK = "coremask";
	private static final String PORT_MASK = "portmask";
	private static final String MEMORY_CHANNELS = "memorychannels";
	private static final String PROGRAM_ID = "programid";
	private static final String MEMORY = "memory";
	private static final String BLACKLIST = "blacklist";
	private static final String PROGRAM_NAME = "programname";
	

	public static void main(String[] args) throws InterruptedException, ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		//TODO: make all java output to gui
		
		Map<String, String> config_map = readConfig();
		
		int num_available_cores = Runtime.getRuntime().availableProcessors();
		//System.out.println("CORES: " + num_of_cores);
		
		int num_cores_selected = Integer.bitCount(Integer.decode(config_map.get(CORE_MASK)));

		if (num_available_cores < num_cores_selected) {
			System.out.println("Coremask > num_of_cores on machine");
			System.exit(-1);
		}
		
		sendDPDKInformation(config_map);
		
		//System.out.println("JAVA: Setting up unsafe memory");
        UnsafeAccess ua = new UnsafeAccess();
		
		//System.out.println("JAVA: Starting application " + config_map.get(PROGRAM_NAME));
		
		List<ReceivePoller> receivers = new ArrayList<ReceivePoller>();
		List<PacketSender> transmitters = new ArrayList<PacketSender>();
		
		Class<?> cla = Class.forName("FirewallInspector");
		Constructor<?> con = cla.getConstructor(PacketSender.class, PacketFreeer.class);
		Object object = con.newInstance(new Object[] {null, null});
		
		PacketProcessor pi = (PacketProcessor)object;
		
		Stats stats = new Stats(receivers, transmitters);
		// everything should be started together - lastly
		AffinityThread t = new AffinityThread(stats, 0, num_available_cores);
		if (!t.ifWorked()) {
			System.out.println("DOESNT WORK");
		}
		/*int i = t.getThreadId();
		System.out.println("ID: " + i);
		//Thread.sleep(10000);
		//boolean res = DpdkAccess.set_thread_affinity(i, 1);
		if (res) {
			System.out.println("YAY");
		} else {
			System.out.println("NOOOOO");
		}*/
		
		
		/*int ret = DpdkAccess.dpdk_setup();
		if (ret < 0) {
			System.out.println("JAVA: Error in DPDK setup");
			return;
		}
		
		// here use jni calls to get various information needed later
		// like size of structs, offsets and memory sizes needed
		
		System.out.println("JAVA: Starting receive queue polling");
		ReceivePoller rp = new ReceivePoller(ua);
		rp.start();*/
	}
	
	private static void sendDPDKInformation(Map<String, String> map) {
		DpdkAccess.dpdk_set_core_mask(map.get(CORE_MASK));
		DpdkAccess.dpdk_set_port_mask(map.get(PORT_MASK));
		DpdkAccess.dpdk_set_program_name(map.get(PROGRAM_NAME));
		DpdkAccess.dpdk_set_memory_channels(map.get(MEMORY_CHANNELS));
		DpdkAccess.dpdk_set_memory(map.get(MEMORY));
		DpdkAccess.dpdk_set_program_id(map.get(PROGRAM_ID));
		String[] bl = map.get(BLACKLIST).split(",");
		System.out.println(bl.toString());
		DpdkAccess.dpdk_set_blacklist(bl);
	}
	
	private static Map<String, String> readConfig() {
		Properties prop = new Properties();
		InputStream input = null;
		Map<String, String> map = new HashMap<String, String>();
		try {
			 
			input = new FileInputStream("config.properties");
			
			prop.load(input);
	 
			map.put(PACKET_INSPECTOR, prop.getProperty(PACKET_INSPECTOR));
			map.put(RX_BURST, prop.getProperty(RX_BURST));
			map.put(TX_BURST, prop.getProperty(TX_BURST));
			map.put(FREE_BURST, prop.getProperty(FREE_BURST));
			map.put(CORE_MASK, prop.getProperty(CORE_MASK));
			map.put(MEMORY_CHANNELS, prop.getProperty(MEMORY_CHANNELS));
			map.put(PROGRAM_ID, prop.getProperty(PROGRAM_ID));
			map.put(MEMORY, prop.getProperty(MEMORY));
			map.put(BLACKLIST, prop.getProperty(BLACKLIST));
			map.put(PROGRAM_NAME, prop.getProperty(PROGRAM_NAME));
			map.put(PORT_MASK, prop.getProperty(PORT_MASK));
	 
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
		return map;
	}
	
}
