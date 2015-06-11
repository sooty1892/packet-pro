import java.lang.reflect.Field;

import sun.misc.Unsafe;

public class DirectAccessTestCase extends TestCase {
	
	Unsafe unsafe;
	long pointer;

	public DirectAccessTestCase(String name, int repetitions) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		super(name, repetitions);
		
		Field f = Unsafe.class.getDeclaredField("theUnsafe");
		f.setAccessible(true);
		this.unsafe = (Unsafe)f.get(null);
		
		pointer = unsafe.allocateMemory(8);
	}

	/*@Override
	public void testGetData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void testSetData() {
		// TODO Auto-generated method stub
		
	}*/
	
	@Override
	public void testGetSetData() {
		for (int i = 0; i < repetitions; i++) {
			Main.getPointer(pointer);
			long packetPointer = unsafe.getLong(pointer);
			//DirectPacket op = new DirectPacket(packetPointer, unsafe);
			unsafe.getInt(packetPointer);
			unsafe.getInt(packetPointer + 4);
			unsafe.getInt(packetPointer + 8);
			unsafe.getInt(packetPointer + 12);
			unsafe.getInt(packetPointer + 16);
			unsafe.getInt(packetPointer + 20);
			unsafe.getInt(packetPointer + 24);
			unsafe.getInt(packetPointer + 28);
			unsafe.getInt(packetPointer + 32);
			unsafe.getInt(packetPointer + 36);
			unsafe.getInt(packetPointer + 40);
			unsafe.getInt(packetPointer + 48);
			
			unsafe.putInt(packetPointer, 13);
			unsafe.putInt(packetPointer + 4, 14);
			unsafe.putInt(packetPointer + 8, 15);
			unsafe.putInt(packetPointer + 12, 16);
			unsafe.putInt(packetPointer + 16, 17);
			unsafe.putInt(packetPointer + 20, 18);
			unsafe.putInt(packetPointer + 24, 19);
			unsafe.putInt(packetPointer + 28, 20);
			unsafe.putInt(packetPointer + 32, 21);
			unsafe.putInt(packetPointer + 36, 22);
			unsafe.putLong(packetPointer + 40, 23);
			unsafe.putLong(packetPointer + 48, 24);
			/*op.getVersion();
			op.getIhl();
			op.getDscp();
			op.getEcn();
			op.getTotal_length();
			op.getPacket_id();
			op.getFragment_offset();
			op.getTime_to_live();
			op.getNext_proto_id();
			op.getHdr_checksum();
			op.getSrc_addr();
			op.getDst_addr();
			
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
			op.setDst_addr(24);*/
		}
	}

}