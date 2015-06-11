import java.util.Random;

public class Main {
	
	private static final int NUM_ITER = 10;
	private static final int NUM_PACKS = 1000000;

	public static void main(String[] args) {
		for (int i = 0; i < NUM_ITER; i++) {
			System.out.println(process_data());
			System.out.println(process_data() / NUM_PACKS);
		}
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
