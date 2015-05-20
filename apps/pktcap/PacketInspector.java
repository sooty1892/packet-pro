import java.nio.ByteBuffer;

public class PacketInspector implements Runnable {
	
	private ByteBuffer buffer;
	private int previousValue;
	
	public PacketInspector(ByteBuffer buffer) {
		this.buffer = buffer;
		previousValue = -1;
	}
	
	public void run() {
		while (true) {
			int test = (int)buffer.get(0);
			if (test != previousValue) {
				previousValue = test;
				System.out.println("JAVA: Location Buffer 0: " + test);
			}
		}
	}
	
}
