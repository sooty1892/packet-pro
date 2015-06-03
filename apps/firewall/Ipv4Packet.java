
public class Ipv4Packet extends Packet {
	
	private static final int VERSION_IHL_OFFSET = 0;
	private static final int TYPE_OF_SERVICE_OFFSET = 1;
	private static final int TOTAL_LENGTH_OFFSET = 2;
	private static final int PACKED_ID_OFFSET = 4;
	private static final int FRAGMENT_OFFSET_OFFSET = 6;
	private static final int TIME_TO_LIVE_OFFSET = 8;
	private static final int NEXT_PROTO_ID_OFFSET = 9;
	private static final int HDR_CHECKSUM_OFFSET = 10;
	private static final int SRC_ADDR_OFFSET = 12;
	private static final int DST_ADDR_OFFSET = 16;

	public Ipv4Packet(long mbuf, long packet) {
		super(mbuf, packet);
	}
	
	public int getVersionIhl() {
		ua.setCurrentPointer(packet_pointer + VERSION_IHL_OFFSET);
		return ua.getByte();
	}
	
	public int getTypeOfService() {
		ua.setCurrentPointer(packet_pointer + TYPE_OF_SERVICE_OFFSET);
		return ua.getByte();
	}
	
	public int getTotalLength() {
		ua.setCurrentPointer(packet_pointer + TOTAL_LENGTH_OFFSET);
		return ua.getShort();
	}
	
	public int getPackedId() {
		ua.setCurrentPointer(packet_pointer + PACKED_ID_OFFSET);
		return ua.getShort();
	}
	
	public int getFragmentOffset() {
		ua.setCurrentPointer(packet_pointer + FRAGMENT_OFFSET_OFFSET);
		return ua.getShort();
	}
	
	public int getTimeToLive() {
		ua.setCurrentPointer(packet_pointer + TIME_TO_LIVE_OFFSET);
		return ua.getByte();
	}
	
	public int getNextProtoId() {
		ua.setCurrentPointer(packet_pointer + NEXT_PROTO_ID_OFFSET);
		return ua.getByte();
	}
	
	public int getHdrChecksum() {
		ua.setCurrentPointer(packet_pointer + HDR_CHECKSUM_OFFSET);
		return ua.getShort();
	}
	
	public long getSrcAddr() {
		ua.setCurrentPointer(packet_pointer + SRC_ADDR_OFFSET);
		return ua.getInt();
	}
	
	public long getDst_Addr() {
		ua.setCurrentPointer(packet_pointer + DST_ADDR_OFFSET);
		return ua.getInt();
	}

}
