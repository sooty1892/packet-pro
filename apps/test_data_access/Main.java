import java.nio.ByteBuffer;

public class Main {
	
	public static native void getData(ObjectPacket p);
	public static native void setData(ObjectPacket p);
	public static native long getPointer();
	
	public static native void getData(ByteBuffer p);
	
	static {
		System.loadLibrary("benchmark");
	}

	public static final int REPETITIONS_1 = 1 * 1000 * 1000;
	public static final int REPETITIONS_2 = 1 * 1000;
	public static final int REPETITIONS = REPETITIONS_2;

	public static void main(String[] args) throws Exception {
		TestCase otc = new ObjectsTestCase("Objects", REPETITIONS);
		TestCase bbtc = new ByteBufferTestCase("ByteBuffer", REPETITIONS);
		TestCase utc = new UnsafeTestCase("Unsafe", REPETITIONS);
		TestCase testCases[] = {utc};
		for (TestCase tc : testCases) {
			for (int i = 0; i < 5; i++) {
				tc.performTest();
				System.out.format("%d %s\twrite=%,dns read=%,dns total=%,dns\n",
                    i,
                    tc.getName(),
                    tc.getWriteTimeNanos(),
                    tc.getReadTimeNanos(),
                    tc.getWriteTimeNanos() + 
                    tc.getReadTimeNanos());
				System.gc();
				Thread.sleep(3000);
			}
		}
	}

}