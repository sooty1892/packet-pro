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
	
	Map<String, String> config_map;
	List<AffinityThread> aff_threads;
	int num_available_cores;
	Stats stats = null;
	
	public ApplicationStarter() {
		num_available_cores = Runtime.getRuntime().availableProcessors();
		//System.out.println("CORES: " + num_of_cores);
	}
	
	public void createAffinityThreads(List<CoreThread> threads) {
		aff_threads = new ArrayList<AffinityThread>();
		if (threads.size() > num_available_cores) {
			System.out.println("NOPE");
			return ;
		}
		int core = 0;
		for (CoreThread ct : threads) {
			AffinityThread af = new AffinityThread(ct, core, num_available_cores);
			if (!af.ifWorked()) {
				System.out.println("AFFINITY NOPE");
			}
			aff_threads.add(af);
			core++;
		}
	}
	
	public void setupStats() {
		//TODO
		/*Stats stats = new Stats(receivers, transmitters);
		AffinityThread t = new AffinityThread(stats, 0, num_available_cores);
		if (!t.ifWorked()) {
			System.out.println("DOESNT WORK");
		}*/
	}
	
	public void DpdkSetup() {
		int ret = DpdkAccess.dpdk_setup();
		if (ret < 0) {
			System.out.println("JAVA: Error in DPDK setup");
			return;
		}
	}
	
	public void startAll() throws InterruptedException, ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		//TODO: make all java output to gui
		
		
		//System.out.println("JAVA: Starting application " + config_map.get(PROGRAM_NAME));
		
		/*
		Class<?> cla = Class.forName("FirewallInspector");
		Constructor<?> con = cla.getConstructor(PacketSender.class, PacketFreeer.class);
		Object object = con.newInstance(new Object[] {null, null});
		*/
        
        if (stats != null) {
        	new Thread(stats).start();
        }
        for (AffinityThread af : aff_threads) {
        	af.start();
        }
        
	}
	
	public void sendDPDKInformation() {
		DpdkAccess.dpdk_set_core_mask(config_map.get(CORE_MASK));
		DpdkAccess.dpdk_set_port_mask(config_map.get(PORT_MASK));
		DpdkAccess.dpdk_set_program_name(config_map.get(PROGRAM_NAME));
		DpdkAccess.dpdk_set_memory_channels(config_map.get(MEMORY_CHANNELS));
		DpdkAccess.dpdk_set_memory(config_map.get(MEMORY));
		DpdkAccess.dpdk_set_program_id(config_map.get(PROGRAM_ID));
		String[] bl = config_map.get(BLACKLIST).split(",");
		System.out.println(bl.toString());
		DpdkAccess.dpdk_set_blacklist(bl);
	}
	
	public void readConfig(InputStream input) {
		Properties prop = new Properties();
		config_map = new HashMap<String, String>();
		try {

			prop.load(input);
	 
			config_map.put(PACKET_INSPECTOR, prop.getProperty(PACKET_INSPECTOR));
			config_map.put(RX_BURST, prop.getProperty(RX_BURST));
			config_map.put(TX_BURST, prop.getProperty(TX_BURST));
			config_map.put(FREE_BURST, prop.getProperty(FREE_BURST));
			config_map.put(CORE_MASK, prop.getProperty(CORE_MASK));
			config_map.put(MEMORY_CHANNELS, prop.getProperty(MEMORY_CHANNELS));
			config_map.put(PROGRAM_ID, prop.getProperty(PROGRAM_ID));
			config_map.put(MEMORY, prop.getProperty(MEMORY));
			config_map.put(BLACKLIST, prop.getProperty(BLACKLIST));
			config_map.put(PROGRAM_NAME, prop.getProperty(PROGRAM_NAME));
			config_map.put(PORT_MASK, prop.getProperty(PORT_MASK));
	 
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
		
		int num_cores_selected = Integer.bitCount(Integer.decode(config_map.get(CORE_MASK)));

		if (num_available_cores < num_cores_selected) {
			System.out.println("Coremask > num_of_cores on machine");
			System.exit(-1);
		}
		
	}
	
}
