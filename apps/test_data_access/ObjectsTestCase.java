public class ObjectsTestCase extends TestCase {

	public ObjectsTestCase(String name, int repetitions) {
		super(name, repetitions);
	}

	@Override
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
    }

}