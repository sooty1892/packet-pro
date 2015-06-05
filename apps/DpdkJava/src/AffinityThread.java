public class AffinityThread extends Thread {
	
	private boolean worked;
	private int avail;
	
	private static final int SUCCESS = 1;

	public AffinityThread(Runnable r, int core, int avail) {
		super(r);
		worked = DpdkAccess.set_thread_affinity(core, avail) == SUCCESS;
		this.avail = avail;
	}
	
	public boolean setNewAffinity(int core) {
		worked = DpdkAccess.set_thread_affinity(core, avail) == SUCCESS;
		return worked;
	}
	
	public boolean ifWorked() {
		return worked;
	}
	
	@Override
	public void start() {
		System.out.println("CALLED");
		super.start();
	}

}