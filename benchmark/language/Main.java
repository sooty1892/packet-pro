import java.util.Random;

public class Main {
	
	private static final int NUM_ITER = 1000;
	private static final int NUM_PACKS = 100000;

	public static void main(String[] args) {
		long total_time = 0;
		for (int i = 0; i < NUM_ITER; i++) {
			total_time += process_data();
		}
		double seconds = ((double)(total_time) / 1000000000.0);
		System.out.printf("Average was " + seconds);
	}
	
	private static void popPacket(Packet p) {
		Random r = new Random();
		p.setA(r.nextInt());
		p.setB(r.nextInt());
		p.setC(r.nextInt());
		p.setD(r.nextInt());
		p.setE(r.nextInt());
	}
	
	private static void proPacket(Packet p) {
		int result = p.getA() * p.getB() * p.getC() * p.getD() * p.getE();
		p.setResult(result);
	}
	
	private static long process_data() {
		long start = System.nanoTime();
		for (int i = 0; i < NUM_PACKS; i++) {
			Packet p = new Packet();
			popPacket(p);
			proPacket(p);
			if (p.getResult() == -1) {
				System.out.println("RESULT ERROR");
			}
		}
		return System.nanoTime() - start;
	}
	
}
