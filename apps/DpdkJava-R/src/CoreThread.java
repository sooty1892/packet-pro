/*
 * Customized runnable to be used for afffinity threads
 */

public abstract class CoreThread implements Runnable {
	
	private String name;
	
	public CoreThread() {
		name = null;
	}
	
	public CoreThread(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public abstract void run();

}
