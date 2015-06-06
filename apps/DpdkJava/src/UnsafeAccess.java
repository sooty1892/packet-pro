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
	
	public UnsafeAccess() {
		Field f = null;
		try {
			f = Unsafe.class.getDeclaredField("theUnsafe");
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}

        f.setAccessible(true);
        try {
			this.unsafe = (Unsafe)f.get(null);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
        currentPointer = 0;
        offset = 0;
	}
	
	public void putByte(int value) {
		unsafe.putByte(currentPointer + offset, (byte)value);
		offset += BYTE_SIZE;
	}
	
	public void putShort(int value) {
		unsafe.putShort(currentPointer + offset, (short)value);
		offset += SHORT_SIZE;
	}
	
	public void putInt(long value) {
		unsafe.putInt(currentPointer + offset, (int)value);
		offset += INT_SIZE;
	}
	
	public void putLong(long value) {
		unsafe.putLong(currentPointer + offset, value);
		offset += LONG_SIZE;
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
	
	public long allocateMemory(long num_of_bytes) {
		return unsafe.allocateMemory(num_of_bytes);
	}

	public void freeMemory(long pointer) {
		unsafe.freeMemory(pointer);
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
