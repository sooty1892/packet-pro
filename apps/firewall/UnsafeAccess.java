import java.lang.reflect.Field;

import sun.misc.Unsafe;

public class UnsafeAccess {
	
	private static final int LONG_SIZE = Long.SIZE / Byte.SIZE;
	private static final int INT_SIZE = Integer.SIZE / Byte.SIZE;
	private static final int SHORT_SIZE = Short.SIZE / Byte.SIZE;
	private static final int BYTE_SIZE = Byte.SIZE / Byte.SIZE;
	
	Unsafe unsafe;
	long currentPointer;
	int offset;
	
	public UnsafeAccess() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field f = Unsafe.class.getDeclaredField("theUnsafe");
        f.setAccessible(true);
        this.unsafe = (Unsafe)f.get(null);
        currentPointer = 0;
        offset = 0;
	}
	
	public int getShort() {
		short temp = unsafe.getShort(currentPointer + offset);
		offset += SHORT_SIZE;
		return Utils.signToUnsign(temp);
	}
	
	public int getByte() {
		byte temp = unsafe.getByte(currentPointer + offset);
		offset += BYTE_SIZE;
		return Utils.signToUnsign(temp);
	}
	
	public long getInt() {
		int temp = unsafe.getInt(currentPointer + offset);
		offset += INT_SIZE;
		return Utils.signToUnsign(temp);
	}
	
	//TODO: return type of this?
	public long getLong() {
		long temp = unsafe.getLong(currentPointer + offset);
		offset += LONG_SIZE;
		return temp;
	}
	
	public long allocateMemory(int num_of_bytes) {
		return unsafe.allocateMemory(num_of_bytes);
	}
	
	public void setCurrentPointer(long currentPointer) {
		this.currentPointer = currentPointer;
		offset = 0;
	}
	
	public long getCurrentPointer() {
		return currentPointer;
	}
	
	public int getOffset() {
		return offset;
	}
	
	public void setOffset(int offset) {
		this.offset = offset;
	}
	
	public long longSize() {
		return LONG_SIZE;
	}
	
	public int intSize() {
		return INT_SIZE;
	}
	
	public byte byteSize() {
		return BYTE_SIZE;
	}
	
	public short shortSize() {
		return SHORT_SIZE;
	}

}
