// Threading class used for affinity assignment for pthreads
// to certain cores in cpu

public class AffinityThread extends Thread {
	
	private boolean worked;
	private int avail;
	
	private static final int SUCCESS = 1;

	/*
	 * r: runnable for thread
	 * core: core value to run thread on (eg. 1 to run on 2nd core)
	 * avail: number of cores available
	 */
	public AffinityThread(Runnable r, int core, int avail) {
		super(r);
		worked = DpdkAccess.set_thread_affinity(core, avail) == SUCCESS;
		this.avail = avail;
	}
	
	public AffinityThread(Runnable r, String name, int core, int avail) {
		super(r, name);
		worked = DpdkAccess.set_thread_affinity(core, avail) == SUCCESS;
		this.avail = avail;
	}
	
	/*
	 * core: core value to run thread on (eg. 1 to run on 2nd core)
	 */
	public boolean setNewAffinity(int core) {
		worked = DpdkAccess.set_thread_affinity(core, avail) == SUCCESS;
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