import java.nio.ByteBuffer;

public class Main {
	
	private static native void init(ByteBuffer bb);
	
	static { System.loadLibrary("playing"); }

	public static void main(String[] args) {
		System.out.println("J: Initiating");
		ByteBuffer bb = ByteBuffer.allocateDirect(1024);
		init(bb);
		System.out.println(bb.get(0));
	}

}