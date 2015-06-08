import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/*
 * Contains main methods to start dpdk and assign
 * ports, queues, cores etc
 */

public class ApplicationStarter {
	
	private static final String RX_BURST = "rxburst";
	private static final String TX_BURST = "txburst";
	private static final String FREE_BURST = "freeburst";
	private static final String NUMCORES = "numcores";
	private static final String NUMPORTS = "numports";
	private static final String MEMORY_CHANNELS = "memorychannels";
	private static final String PROGRAM_ID = "programid";
	private static final String MEMORY = "memory";
	private static final String BLACKLIST = "blacklist";
	private static final String PROGRAM_NAME = "programname";
	
	private static final int ERROR = -1;
	
	// results from config.properties files
	Map<String, String> config_map;
	// threads to run on start
	List<AffinityThread> aff_threads = null;
	// number of cores available to use
	int num_available_cores;
	// stats thread - must be loaded first to use gui
	Stats stats = null;
	
	public ApplicationStarter() {
		num_available_cores = Runtime.getRuntime().availableProcessors();
	}
	
	// Puts all runnables into affinity thread and assigns them a core
	public void createAffinityThreads(List<CoreThread> threads) {
		aff_threads = new ArrayList<AffinityThread>();
		if (threads.size() > num_available_cores) {
			System.out.println("-----Too many threads for number of cores");
			return ;
		}
		int core = 0;
		for (CoreThread ct : threads) {
			AffinityThread af = new AffinityThread(ct, core, num_available_cores);
			if (!af.ifWorked()) {
				System.out.println("-----Affinity thread creation failed on core assignment");
			}
			aff_threads.add(af);
			core++;
		}
	}
	
	// sets up gui if value set to true
	// this must be called first as application class first call if qui is wanted
	public void setupGui(boolean gui) {
		stats = new Stats(gui);
		if (gui) {
			System.out.println("Number of cores: " + num_available_cores);
		}
	}
	
	// used to create stats with/out gui
	public void setupStats(List<ReceivePoller> receivers, List<PacketSender> transmitters, boolean gui) {
		stats = new Stats(receivers, transmitters, gui);
	}
	
	// updates which pollers and senders to get stats from
	public void updateStatsInfo(List<ReceivePoller> receivers, List<PacketSender> transmitters) {
		stats.addReceivers(receivers);
		stats.addTransmitters(transmitters);
	}
	
	public Stats getStats() {
		return stats;
	}
	
	// initialise dpdk environment abstraction layer (EAL)
	public void dpdk_init_eal() {
		if (DpdkAccess.dpdk_init_eal() < 0) {
			System.out.println("-----dpdk_init_eal failed");
		} else {
			System.out.println("EAL initialised");
		}
	}
	
	// creates mempool
	//TODO: currently c code only handles 1 mempool
	public void dpdk_create_mempool(String name, int num_el, int cache_size) {
		if (DpdkAccess.dpdk_create_mempool(name, num_el, cache_size) < 0) {
			System.out.println("-----dpdk_create_mempool failed");
		} else {
			System.out.println("Created mempool " + name);
		}
	}
	
	// checks available ports
	public void dpdk_check_ports() {
		int ports = DpdkAccess.dpdk_check_ports();
		if (ports < 0) {
			System.out.println("-----dpdk_check_ports failed");
		} else {
			System.out.println("Ports available: " + ports);
		}
	}
	
	// configures devices for the number of rx and tx queues required
	public void dpdk_configure_dev(int port_id, int rx_num, int tx_num) {
		if (DpdkAccess.dpdk_configure_dev(port_id, rx_num, rx_num) < 0) {
			System.out.println("-----dpdk_configure_dev failed");
		} else {
			System.out.println("Configured port " + port_id);
		}
	}
	
	// configure rx queue for device
	public void dpdk_configure_rx_queue(int port_id, int rx_id) {
		if (DpdkAccess.dpdk_configure_rx_queue(port_id, rx_id) < 0) {
			System.out.println("-----dpdk_configure_rx_queue failed");
		} else {
			System.out.println("Configured rx queue " + rx_id + " on port " + port_id);
		}
	}
	
	// configure tx queue for device
	public void dpdk_configure_tx_queue(int port_id, int tx_id) {
		if (DpdkAccess.dpdk_configure_tx_queue(port_id, tx_id) < 0) {
			System.out.println("-----dpdk_configure_tx_queue failed");
		} else {
			System.out.println("Configured tx queue " + tx_id + " on port " + port_id);
		}
	}
	
	// starts given device
	public void dpdk_dev_start(int port_id) {
		if (DpdkAccess.dpdk_dev_start(port_id) < 0) {
			System.out.println("-----dpdk_dev_start failed");
		} else {
			System.out.println("Started port " + port_id);
		}
	}
	
	// checks link status of all ports
	public void dpdk_check_ports_link_status() {
		System.out.println(DpdkAccess.dpdk_check_ports_link_status());
	}
	
	// starts all threads - affinity threads and stats thread
	public void startAll() {
		//TODO: access to ethernet header?
		//TODO: check dpdkaccess.c for comments and sorting out some of the methods
        
        if (stats != null) {
        	new Thread(stats).start();
        }
        if (aff_threads != null) {
        	for (AffinityThread af : aff_threads) {
	        	af.start();
	        }
        }
        
	}
	
	// sends parameters in config file to dpdk library
	public void sendDPDKInformation() {
		DpdkAccess.dpdk_set_program_name(config_map.get(PROGRAM_NAME));
		DpdkAccess.dpdk_set_memory_channels(config_map.get(MEMORY_CHANNELS));
		DpdkAccess.dpdk_set_memory(config_map.get(MEMORY));
		DpdkAccess.dpdk_set_program_id(config_map.get(PROGRAM_ID));
		DpdkAccess.dpdk_set_receive_burst(Integer.parseInt(config_map.get(RX_BURST)));
		String[] bl = config_map.get(BLACKLIST).split(",");
		DpdkAccess.dpdk_set_blacklist(bl);
	}
	
	// reads the given config file
	public void readConfig(InputStream input) {
		Properties prop = new Properties();
		config_map = new HashMap<String, String>();
		try {

			prop.load(input);
	 
			config_map.put(RX_BURST, prop.getProperty(RX_BURST));
			config_map.put(TX_BURST, prop.getProperty(TX_BURST));
			config_map.put(FREE_BURST, prop.getProperty(FREE_BURST));
			config_map.put(NUMCORES, prop.getProperty(NUMCORES));
			config_map.put(MEMORY_CHANNELS, prop.getProperty(MEMORY_CHANNELS));
			config_map.put(PROGRAM_ID, prop.getProperty(PROGRAM_ID));
			config_map.put(MEMORY, prop.getProperty(MEMORY));
			config_map.put(BLACKLIST, prop.getProperty(BLACKLIST));
			config_map.put(PROGRAM_NAME, prop.getProperty(PROGRAM_NAME));
			config_map.put(NUMPORTS, prop.getProperty(NUMPORTS));
			
	 
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
		
		int num_cores_selected = Integer.bitCount(Integer.decode(config_map.get(NUMCORES)));

		if (num_available_cores < num_cores_selected) {
			System.out.println("-----Config selects more cores than are available");
			System.exit(ERROR);
		}
		
	}
	
}
