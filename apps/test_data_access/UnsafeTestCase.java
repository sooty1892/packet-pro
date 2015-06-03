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
	}

	@Override
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
	}

}