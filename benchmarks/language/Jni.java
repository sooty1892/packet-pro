public class Jni {

	int x = 5;

	public int getX() {
		return x;
	}

	static { System.loadLibrary("jni"); }

	public static native void objectPrint(Jni o);

	public static void main(String args[]) {
		Jni jni = new Jni();
		objectPrint(jni);
	}

}