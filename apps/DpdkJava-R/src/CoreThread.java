/*
 * Customized runnable to be used for afffinity threads
 */

public abstract class CoreThread implements Runnable {
	
	private String name;
	private int core;
	
	public CoreThread(int core) {
		name = "NULL";
		this.core = core;
	}
	
	public CoreThread(int core, String name) {
		this.name = name;
		this.core = core;
	}
	
	public int getCore() {
		return core;
	}
	
	public String getName() {
		return name;
	}
	
	public abstract void run();

}
