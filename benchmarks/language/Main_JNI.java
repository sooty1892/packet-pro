public class Main_JNI {
	
	private static native void popPacket(Packet p);
	private static native void retPacket(Packet p);
	
	static {
		System.loadLibrary("benchmark");
	}   

	private static final int NUM_ITER = 1000;
	private static final int NUM_PACKS = 100000;
	
	public static void main(String[] args) {
		long total_time = 0;
		for (int i = 0; i < NUM_ITER; i++) {
			total_time += process_data();
		}
		double seconds = ((double)(total_time) / 1000000000.0);
		//System.out.printf("Average was " + seconds);
	}
	
	private static void proPacket(Packet p) {
		int i = p.getA() * p.getB() * p.getC() * p.getD() * p.getE();
		p.setResult(i);
	}
	
	private static long process_data() {
		//long start = System.nanoTime();
		for (int i = 0; i < NUM_PACKS; i++) {
			Packet p = new Packet();
			popPacket(p);
			proPacket(p);
			retPacket(p);
		}
		return 0;//System.nanoTime() - start;
	}
	
}