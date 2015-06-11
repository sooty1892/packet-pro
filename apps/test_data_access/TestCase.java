abstract class TestCase {
	
	private final String name;
	protected final int repetitions;
	//private long getTimeNanos;
	//private long setTimeNanos;
	private long avgTime;

	public TestCase(String name, int repetitions) {
		this.name = name;
		this.repetitions = repetitions;
	}

	public String getName() {
		return name;
	}

	/*public long getGetTimeNanos() {
		return getTimeNanos;
	}

	public long getSetTimeNanos() {
		return setTimeNanos;
	}*/
	
	public long getAvgTime() {
		return avgTime;
	}

	public void performTest() {
		//final long startGetNanos = System.nanoTime();
		//testGetData();
		//getTimeNanos = (System.nanoTime() - startGetNanos) / repetitions;
 
        //final long startSetNanos = System.nanoTime();
        //testSetData();
        //setTimeNanos = (System.nanoTime() - startSetNanos) / repetitions;
		
		final long start = System.nanoTime();
		testGetSetData();
		avgTime = ((System.nanoTime() - start) / (long)repetitions);
	}

	//public abstract void testGetData();
	//public abstract void testSetData();
	
	public abstract void testGetSetData();

}