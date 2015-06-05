import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;


public class Firewall {

	public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InterruptedException, FileNotFoundException {
		
		ApplicationStarter as = new ApplicationStarter();
		as.readConfig(new FileInputStream("condif.properties"));
		as.sendDPDKInformation();
		
		List<CoreThread> threads = new ArrayList<CoreThread>();
		
		as.createAffinityThreads(threads);
		
		//as.DpdkSetup();
		
		as.setupStats();
		
		as.startAll();
	}
	
}
