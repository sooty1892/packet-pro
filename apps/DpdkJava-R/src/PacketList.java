import java.util.Iterator;

public class PacketList implements Iterable<Packet> {
	
	private static final int LONG_SIZE = 8;
	
	private long startPointer;
	private Packet returnedPacket;
	private int size;
	private UnsafeAccess ua;
	
	public PacketList() {
		ua = new UnsafeAccess();
		returnedPacket = new Ipv4Packet(0, 0, ua);
	}

	public PacketList(short size, long startPointer) {
		this.startPointer = startPointer;
		this.size = size;
		ua = new UnsafeAccess();
		returnedPacket = new Ipv4Packet(0, 0, ua);
	}
	
	public void reset(int size, long startPointer) {
		this.size = size;
		this.startPointer = startPointer;
	}
	
	public int getSize() {
		return size;
	}

	@Override
	public Iterator<Packet> iterator() {
		return new PacketIterator();
	}
	
	private class PacketIterator implements Iterator<Packet> {
		
		int current = 0;
		long offset = startPointer;
		
		public PacketIterator() {
			ua.setCurrentPointer(startPointer);
		}

		@Override
		public boolean hasNext() {
			return current < size;
		}

		@Override
		public Packet next() {
			returnedPacket.setMbuf_pointer(ua.getLong());
			returnedPacket.setPacket_pointer(ua.getLong());
			current++;
			return returnedPacket;
		}

		@Override
		public void remove() {
			try {
				throw new CantRemoveFromThisListException();
			} catch (CantRemoveFromThisListException e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}
		
	}
	
	
}
