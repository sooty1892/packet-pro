import java.nio.ByteBuffer;

public class Pktcap implements Runnable {

	private static ByteBuffer buffer;
	
	public static void main(String[] args) {
        System.out.println("JAVA: Starting Java app");
        buffer = ByteBuffer.allocateDirect(8*1024);
        //(new Thread(new Pktcap())).start();
        //(new Thread(new PacketInspector(buffer))).start();
        if (DpdkAccess.setup_and_conf() < 0) {
        	System.out.println("JAVA: Error in DPDK setup ");
			return;
        }
        System.out.println("JAVA: Starting recieve queue polling");
        while (true) {
        	buffer = ByteBuffer.allocateDirect(8*1024);
        	int count = DpdkAccess.get_packets(buffer);
        	System.out.println("JAVA: Received " + count + " packets");
        }
	}
	
	public void run() {
		if (DpdkAccess.setup_and_conf(buffer) < 0) {
			System.out.println("JAVA: Error in DPDK setup ");
			return;
		}
	}

}
