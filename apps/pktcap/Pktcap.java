import java.nio.ByteBuffer;

public class Pktcap implements Runnable {

	private ByteBuffer buffer;
	
	public static void main(String[] args) {
        System.out.println("JAVA: Starting Java app");
        buffer = ByteBuffer.allocateDirect(8*1024);
        (new Thread(new Pktcap())).start();
        (new Thread(new PacketInspector())).start();
	}
	
	public void run() {
		if (DpdkAccess.setup_and_conf(buffer) < 0) {
			System.out.println("JAVA: Error in DPDK setup ");
			return;
		}
	}

}
