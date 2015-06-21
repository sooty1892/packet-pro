// Threading class used for affinity assignment for pthreads
// to certain cores in cpu

public class AffinityThread extends Thread {
	
	private boolean worked;
	
	// this is the specific proccesor id as set in /proc/cpuinfo
	// and therefore is associated with a certain socket as well
	private int core;
	
	private static final int SUCCESS = 1;

	/*
	 * r: runnable for thread
	 * core: core value to run thread on (eg. 1 to run on 2nd core)
	 * avail: number of cores available
	 */
	public AffinityThread(Runnable r, int core) {
		super(r);
		this.core = core;
		worked = DpdkAccess.set_thread_affinity(core) == SUCCESS;
	}
	
	public AffinityThread(Runnable r, String name, int core) {
		super(r, name);
		this.core = core;
		worked = DpdkAccess.set_thread_affinity(core) == SUCCESS;
	}
	
	/*
	 * core: core value to run thread on (eg. 1 to run on 2nd core)
	 */
	public boolean setNewAffinity(int core) {
		worked = DpdkAccess.set_thread_affinity(core) == SUCCESS;
		return worked;
	}
	
	public boolean hasWorked() {
		return worked;
	}
	
	public int getCore() {
		return core;
	}
	
	@Override
	public void start() {
		super.start();
	}

}