
public class PacketListCreate {
	
	private long startPointer;
	private int max_size;
	private int current_size;
	private UnsafeAccess ua;
	
	public PacketListCreate(int max_size) {
		this.ua = new UnsafeAccess();
		this.max_size = max_size;
		startPointer = ua.allocateMemory((8*this.max_size)+2);
		this.current_size = 0;
		ua.setCurrentPointer(startPointer + 2);
	}
	
	public void add(Packet p) {
		ua.putLong(p.getMbuf_pointer());
		current_size++;
	}
	
	public int size() {
		return current_size;
	}
	
	public void reset() {
		current_size = 0;
		ua.setCurrentPointer(startPointer + 2);
	}
	
	public long getNativePointer() {
		ua.setCurrentPointer(startPointer);
		ua.putShort(current_size);
		return startPointer;
	}
	
	// should be called after last use of object occurs
	public void close() {
		ua.freeMemory(startPointer);
	}

}
