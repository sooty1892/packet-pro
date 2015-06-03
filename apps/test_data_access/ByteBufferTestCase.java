import java.nio.ByteBuffer;

public class ByteBufferTestCase extends TestCase {
	
	ByteBuffer bb = ByteBuffer.allocateDirect(1024);

	public ByteBufferTestCase(String name, int repetitions) {
		super(name, repetitions);
	}

	@Override
	public void testGetData() {
		for (int i = 0; i < repetitions; i++) {
			bb.clear();
        	Main.getData(bb);
        }
	}

	@Override
	public void testSetData() {
		for (int i = 0; i < repetitions; i++) {
			bb.clear();
        	bb.putInt(13);
        	bb.putInt(14);
        	bb.putInt(15);
        	bb.putInt(16);
        	bb.putInt(17);
        	bb.putInt(18);
        	bb.putInt(19);
        	bb.putInt(20);
        	bb.putInt(21);
        	bb.putInt(22);
        	bb.putLong(23L);
        	bb.putLong(24L);
        }
	}

}