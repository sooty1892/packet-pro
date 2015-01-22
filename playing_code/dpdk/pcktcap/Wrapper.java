public class Wrapper {
	
		private static native int eal_init(String[] args, int num);

    	static { System.loadLibrary("Wrapper"); }
	
		public static int wrap_eal_init(String[] args) {
			return eal_init(args, args.length);
		}
	
}