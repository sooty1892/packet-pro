public class AffinityThread extends Thread {
	
	private boolean worked;
	private int avail;

	public AffinityThread(Runnable r, int core, int avail) {
		super(r);
		worked = DpdkAccess.set_thread_affinity(core, avail);
		this.avail = avail;
	}
	
	public boolean setNewAffinity(int core) {
		worked = DpdkAccess.set_thread_affinity(core, avail);
		return worked;
	}
	
	public boolean ifWorked() {
		return worked;
	}
	
	@Override
	public void start() {
		super.start();
	}

}