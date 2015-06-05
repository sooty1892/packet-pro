public class CustomThread extends Thread {

	public CustomThread(Runnable r) {
		super(r);
		DpdkAccess.set_thread_affinity(0,0);
	}

	public int getThreadId() {
		return DpdkAccess.get_thread_id();
	}

}