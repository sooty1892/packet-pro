package hello;

public class Wrapper {

	private static native int eal_init(int num, String[] args);
	private static native int lcore_id();
	private static native void mp_wait_lcore();

	static { System.loadLibrary("Wrapper"); }

	//initialise eal
	public static int eal_init_wrap(String[] args) {
		return eal_init(args.length, args);
	}

	//get id of current core
	public static int lcore_id_wrap() {
		return lcore_id();
	}

	//wait until all cores have finished
	public static void mp_wait_lcore_wrap() {
		mp_wait_lcore();
	}


}