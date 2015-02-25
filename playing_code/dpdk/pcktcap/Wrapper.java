public class Wrapper {
	
		private static native int eal_init(String[] args, int num);
		private static native int eth_dev_count();

    	static { System.loadLibrary("Wrapper"); }
	
		public static int wrap_eal_init(String[] args) {
			return eal_init(args, args.length);
		}

		public static int wrap_eth_dev_count() {
			return eth_dev_count();
		}
	
}
