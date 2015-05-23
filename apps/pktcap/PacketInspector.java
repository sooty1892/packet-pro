import java.nio.ByteBuffer;

public class PacketInspector implements Runnable {
	
	private ByteBuffer buffer;
	private int previousValue;
	
	public PacketInspector(ByteBuffer buffer) {
		this.buffer = buffer;
		previousValue = -1;
	}
	
	public void run() {
		//while (true) {
			char test = (char)buffer.get(0);
			char test1 = (char)buffer.get(1);
			System.out.println("JAVA: " + test + " : " + test1);
			//if (test != previousValue) {
			//	previousValue = test;
			//	System.out.println("JAVA: Location Buffer 0: " + test);
			//}
		//}
	}
	
}
