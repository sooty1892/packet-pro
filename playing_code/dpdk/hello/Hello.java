

public class Hello {

	public static void main(String[] args) {
		int ret = Wrapper.eal_init_wrap(args);
		if (ret < 0) {
			System.out.println("Cannot init EAL");
		}

		int current_core = Wrapper.lcore_id_wrap();
		System.out.println("Hello from core " + current_core);
		Wrapper.mp_wait_lcore_wrap();
	}

}
