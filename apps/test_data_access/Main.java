import java.nio.ByteBuffer;

public class Main {
	
	// for objects
	public static native void getData(ObjectPacket p);
	public static native void setData(ObjectPacket p);
	
	// for byte buffer
	public static native void getData(ByteBuffer p);
	public static native void setData(ByteBuffer p);
	
	// for unsafe and direct access
	public static native void getPointer(long pointer);
	
	static {
		System.loadLibrary("benchmark");
	}

	public static final int REPETITIONS_1 = 1 * 1000 * 1000;
	public static final int REPETITIONS_2 = 1 * 1000;
	public static final int REPETITIONS = REPETITIONS_1;

	public static void main(String[] args) throws Exception {
		TestCase otc = new ObjectsTestCase("Objects", REPETITIONS);
		TestCase bbtc = new ByteBufferTestCase("ByteBuffer", REPETITIONS);
		TestCase utc = new UnsafeTestCase("Unsafe", REPETITIONS);
		TestCase datc = new DirectAccessTestCase("Direct Access", REPETITIONS);
		TestCase testCases[] = {otc, bbtc, utc, datc};
		for (TestCase tc : testCases) {
			long total = 0;
			for (int i = 0; i < 10; i++) {
				tc.performTest();
				/*System.out.format("%d %s\tget=%,dns set=%,dns total=%,dns\n",
                    i,
                    tc.getName(),
                    tc.getGetTimeNanos(),
                    tc.getSetTimeNanos(),
                    tc.getGetTimeNanos() + 
                    tc.getSetTimeNanos());*/
				System.out.format("%d %s\tavg = %d\n", i, tc.getName(), tc.getAvgTime());
				total += tc.getAvgTime();
				//System.gc();
				//Thread.sleep(2000);
			}
			System.out.println(tc.getName() + ": Avg = " + total / 10);
		}
	}
	
	//TODO: test data is been set correctly

}