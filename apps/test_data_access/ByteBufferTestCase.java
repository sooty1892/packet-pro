import java.nio.ByteBuffer;

public class ByteBufferTestCase extends TestCase {
	
	ByteBuffer bb = ByteBuffer.allocateDirect(1024);
	int start;

	public ByteBufferTestCase(String name, int repetitions) {
		super(name, repetitions);
		start = bb.position();
	}

	/*@Override
	public void testGetData() {
		for (int i = 0; i < repetitions; i++) {
			bb.clear();
        	Main.getData(bb);
        }
	}

	@Override
	public void testSetData() {
		for (int i = 0; i < repetitions; i++) {
			bb.clear();
        	bb.putInt(13);
        	bb.putInt(14);
        	bb.putInt(15);
        	bb.putInt(16);
        	bb.putInt(17);
        	bb.putInt(18);
        	bb.putInt(19);
        	bb.putInt(20);
        	bb.putInt(21);
        	bb.putInt(22);
        	bb.putLong(23L);
        	bb.putLong(24L);
        }
	}*/
	
	@Override
	public void testGetSetData() {
		for (int i = 0; i < repetitions; i++) {
			bb.clear();
			Main.getData(bb);
			ObjectPacket op = new ObjectPacket();
			op.setVersion(bb.getInt());
			op.setIhl(bb.getInt());
			op.setDscp(bb.getInt());
			op.setEcn(bb.getInt());
			op.setTotal_length(bb.getInt());
			op.setPacket_id(bb.getInt());
			op.setFragment_offset(bb.getInt());
			op.setTime_to_live(bb.getInt());
			op.setNext_proto_id(bb.getInt());
			op.setHdr_checksum(bb.getInt());
			op.setSrc_addr(bb.getLong());
			op.setDst_addr(bb.getLong());
			
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
			
			bb.position(start);
			bb.putInt((int)op.getVersion());
        	bb.putInt((int)op.getIhl());
        	bb.putInt((int)op.getDscp());
        	bb.putInt((int)op.getEcn());
        	bb.putInt((int)op.getTotal_length());
        	bb.putInt((int)op.getPacket_id());
        	bb.putInt((int)op.getFragment_offset());
        	bb.putInt((int)op.getTime_to_live());
        	bb.putInt((int)op.getNext_proto_id());
        	bb.putInt((int)op.getHdr_checksum());
        	bb.putLong(op.getSrc_addr());
        	bb.putLong(op.getDst_addr());
		}
	}

}