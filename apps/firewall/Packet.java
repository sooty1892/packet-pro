public abstract class Packet {
	
	protected long mbuf_pointer;
	protected long packet_pointer;
	
	protected UnsafeAccess ua;
	
	public static final int IPV4 = 4;
	public static final int IPV6 = 6;
	
	public Packet(long mbuf, long packet) {
		mbuf_pointer = mbuf;
		packet_pointer = packet;
		ua = new UnsafeAccess();
		ua.setCurrentPointer(packet_pointer);
	}
	
	//static methods to split packets up further

	public long getMbuf_pointer() {
		return mbuf_pointer;
	}

	public void setMbuf_pointer(long mbuf_pointer) {
		this.mbuf_pointer = mbuf_pointer;
	}

	public long getPacket_pointer() {
		return packet_pointer;
	}

	public void setPacket_pointer(long packet_pointer) {
		this.packet_pointer = packet_pointer;
	}

	public int whichIP() {
		ua.setCurrentPointer(packet_pointer);
		int version = ua.getByte() >> 4;
		if (version == 4 || version == 6) {
			return version;
		} else {
			System.out.println("ERROR - VERSION NOT 6 or 4");
			return 0;
		}
	}
	
	public abstract String toString();
	
}