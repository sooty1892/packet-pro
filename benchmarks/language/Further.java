public class Further {
	
	static {
		System.loadLibrary("further");
	}
	
	private static native int method1();
	private static native int method2(Packet p);
	private static native Packet method3();
	private static native void method4(Packet p);
	
	private static final int NUM_ITER = 1000000;
	
	public static int method0() {
		return 0;
	}
	
	public static void main(String[] args) {
		long start = 0;
		long diff = 0;
		double seconds = 0;
		
		start = System.nanoTime();
		for (int i = 0; i < NUM_ITER; i++) {
			int b = method1();
		}
		diff = System.nanoTime() - start;
		seconds = ((double)(diff) / 1000000000.0);
		System.out.println("Method1 = " + seconds);
		
		start = System.nanoTime();
		for (int i = 0; i < NUM_ITER; i++) {
			Packet p = new Packet();
			int b = method2(p);
		}
		diff = System.nanoTime() - start;
		seconds = ((double)(diff) / 1000000000.0);
		System.out.println("Method2 = " + seconds);
		
		start = System.nanoTime();
		for (int i = 0; i < NUM_ITER; i++) {
			Packet p = method3();
		}
		diff = System.nanoTime() - start;
		seconds = ((double)(diff) / 1000000000.0);
		System.out.println("Method3 = " + seconds);
		
		start = System.nanoTime();
		for (int i = 0; i < NUM_ITER; i++) {
			Packet p = new Packet();
			method4(p);
			if (p.getResult() != 0) {
				System.out.println("ERROR");
			}
		}
		diff = System.nanoTime() - start;
		seconds = ((double)(diff) / 1000000000.0);
		System.out.println("Method4 = " + seconds);
		
		start = System.nanoTime();
		for (int i = 0; i < NUM_ITER; i++) {
			int b = method0();
		}
		diff = System.nanoTime() - start;
		seconds = ((double)(diff) / 1000000000.0);
		System.out.println("Method0 = " + seconds);
				
	}
	
}