import java.util.Iterator;

public class PacketList implements Iterable<Packet> {
	
	private static final int LONG_SIZE = 8;
	
	private long startPointer;
	private Packet returnedPacket;
	private int size;
	private UnsafeAccess ua_packet;
	private UnsafeAccess ua_list;
	
	public PacketList() {
		ua_packet = new UnsafeAccess();
		ua_list = new UnsafeAccess();
		returnedPacket = new Ipv4Packet(0, 0, ua_packet);
	}

	public PacketList(short size, long startPointer) {
		this.startPointer = startPointer;
		this.size = size;
		ua_packet = new UnsafeAccess();
		ua_list = new UnsafeAccess();
		returnedPacket = new Ipv4Packet(0, 0, ua_packet);
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
		
		public PacketIterator() {
			ua_list.setCurrentPointer(startPointer);
		}

		@Override
		public boolean hasNext() {
			return current < size;
		}

		@Override
		public Packet next() {
			returnedPacket.setMbuf_pointer(ua_list.getLong());
			returnedPacket.setPacket_pointer(ua_list.getLong());
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
