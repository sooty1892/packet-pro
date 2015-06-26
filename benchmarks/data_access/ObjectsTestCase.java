public class ObjectsTestCase extends TestCase {

	public ObjectsTestCase(String name, int repetitions) {
		super(name, repetitions);
	}

	/*@Override
	public void testGetData() {
        for (int i = 0; i < repetitions; i++) {
        	ObjectPacket p = new ObjectPacket();
        	Main.getData(p);
        }
    }

	@Override
    public void testSetData() {
		for (int i = 0; i < repetitions; i++) {
        	ObjectPacket p = new ObjectPacket();
        	Main.getData(p);
        }
    }*/
	
	@Override
	public void testGetSetData() {
		for (int i = 0; i < repetitions; i++) {
			ObjectPacket op = new ObjectPacket();
			Main.getData(op);
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
			Main.setData(op);
		}
	}

}