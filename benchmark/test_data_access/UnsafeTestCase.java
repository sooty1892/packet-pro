import java.lang.reflect.Field;

import sun.misc.Unsafe;

public class UnsafeTestCase extends TestCase {
	
	Unsafe unsafe;
	long pointer;

	public UnsafeTestCase(String name, int repetitions) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		super(name, repetitions);
		
		Field f = Unsafe.class.getDeclaredField("theUnsafe");
        f.setAccessible(true);
        this.unsafe = (Unsafe)f.get(null);
        
        pointer = unsafe.allocateMemory(8);
	}

	/*@Override
	public void testGetData() {
		for (int i = 0; i < repetitions; i++) {
        	pointer = Main.getPointer();
        	System.out.println(unsafe.getInt(pointer) & 0xFFFFFFFFL);
        }
	}

	@Override
	public void testSetData() {
		for (int i = 0; i < repetitions; i++) {
			pointer = Main.getPointer();
        	//unsafe.putInt(pointer, 13);
        }
	}*/
	
	@Override
	public void testGetSetData() {
		for (int i = 0; i < repetitions; i++) {
			Main.getPointer(pointer);
			long packetPointer = unsafe.getLong(pointer);
			ObjectPacket op = new ObjectPacket();
			op.setVersion(unsafe.getInt(packetPointer));
			op.setIhl(unsafe.getInt(packetPointer + 4));
			op.setDscp(unsafe.getInt(packetPointer + 8));
			op.setEcn(unsafe.getInt(packetPointer + 12));
			op.setTotal_length(unsafe.getInt(packetPointer + 16));
			op.setPacket_id(unsafe.getInt(packetPointer + 20));
			op.setFragment_offset(unsafe.getInt(packetPointer + 24));
			op.setTime_to_live(unsafe.getInt(packetPointer + 28));
			op.setNext_proto_id(unsafe.getInt(packetPointer + 32));
			op.setHdr_checksum(unsafe.getInt(packetPointer + 36));
			op.setSrc_addr(unsafe.getLong(packetPointer + 40));
			op.setDst_addr(unsafe.getLong(packetPointer + 48));
			
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
			
			//System.out.println("JAVA: " + Long.toHexString(packetPointer));
			unsafe.putInt(packetPointer, (int)op.getVersion());
			unsafe.putInt(packetPointer + 4, (int)op.getIhl());
			unsafe.putInt(packetPointer + 8, (int)op.getDscp());
			unsafe.putInt(packetPointer + 12, (int)op.getEcn());
			unsafe.putInt(packetPointer + 16, (int)op.getTotal_length());
			unsafe.putInt(packetPointer + 20, (int)op.getPacket_id());
			unsafe.putInt(packetPointer + 24, (int)op.getFragment_offset());
			unsafe.putInt(packetPointer + 28, (int)op.getTime_to_live());
			unsafe.putInt(packetPointer + 32, (int)op.getNext_proto_id());
			unsafe.putInt(packetPointer + 36, (int)op.getHdr_checksum());
			unsafe.putLong(packetPointer + 40, op.getSrc_addr());
			unsafe.putLong(packetPointer + 48, op.getDst_addr());
		}
	}

}