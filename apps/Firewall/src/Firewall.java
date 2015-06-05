import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;


public class Firewall {

	public static void main(String[] args) throws ClassNotFoundException,
												  NoSuchMethodException,
												  SecurityException,
												  InstantiationException,
												  IllegalAccessException,
												  IllegalArgumentException,
												  InvocationTargetException, InterruptedException, FileNotFoundException {
		
		ApplicationStarter as = new ApplicationStarter();
		as.readConfig(new FileInputStream("config.properties"));
		as.sendDPDKInformation();
		
		List<CoreThread> threads = new ArrayList<CoreThread>();
		
		ReceivePoller rp = new ReceivePoller(0, 0);
		PacketSender ps = new PacketSender(0, 0);
		PacketFreeer pf = new PacketFreeer();
		
		threads.add(new FirewallProcessor(ps, pf, rp));
		
		as.createAffinityThreads(threads);
		
		as.dpdk_init_eal();
		as.dpdk_create_mempool("mbufs", 8192, 32);
		as.dpdk_check_ports();
		as.dpdk_configure_dev(0, 1, 1);
		as.dpdk_configure_rx_queue(0, 0);
		as.dpdk_configure_tx_queue(0, 0);
		as.dpdk_dev_start(0);
		as.dpdk_check_ports_link_status();
		
		//as.setupStats();
		
		as.startAll();
	}
	
}
