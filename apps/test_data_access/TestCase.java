abstract class TestCase {
	
	private final String name;
	protected final int repetitions;
	private long writeTimeNanos;
	private long readTimeNanos;
	
	protected final ObjectPacket op = new ObjectPacket();

	public TestCase(String name, int repetitions) {
		this.name = name;
		this.repetitions = repetitions;
		op.setVersion(13);
		op.setIhl(14);
		op.setDscp(15);
		op.setEcn(16);
		op.setTotal_length(17);
		op.setPacket_id(18);
		op.setFragment_offset(19);
		op.setTime_to_live(20);
		op.setNext_proto_id(21);
		op.setHdr_checksum(22);
		op.setSrc_addr(23);
		op.setDst_addr(24);
	}

	public String getName() {
		return name;
	}

	public long getWriteTimeNanos() {
		return writeTimeNanos;
	}

	public long getReadTimeNanos() {
		return readTimeNanos;
	}

	public void performTest() {
		final long startWriteNanos = System.nanoTime();
		testGetData();
        writeTimeNanos = (System.nanoTime() - startWriteNanos) / repetitions;
 
        final long startReadNanos = System.nanoTime();
        testSetData();
        readTimeNanos = (System.nanoTime() - startReadNanos) / repetitions;
		
	}

	public abstract void testGetData();
	public abstract void testSetData();

}