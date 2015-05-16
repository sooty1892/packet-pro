public class Pktcap {

	public static void main(String[] args) {
        System.out.println("JAVA: Starting Java app");
		int ret = DpdkAccess.setup_and_conf();
		if (ret < 0) {
			System.out.println("JAVA: Error - setup returned " + ret);
		}

		//int current_core = Wrapper.lcore_id_wrap();
		//System.out.println("Hello from core " + current_core);
		//Wrapper.mp_wait_lcore_wrap();
	}

}
