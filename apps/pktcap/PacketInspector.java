public class PacketInspector implements Runnable {
	
	private ByteBuffer buffer;
	
	public PacketInspector(ByteBuffer buffer) {
		this.buffer = buffer;
	}
	
	public void run() {
		while (true) {
			int test = (int)buffer.get(0);
			System.out.println("JAVA: Location Buffer 0: " + test);
		}
	}
	
}