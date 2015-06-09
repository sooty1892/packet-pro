/*
 * IPv4 packet to access all fields of packet headers
 */

public class Ipv4Packet extends Packet {
	
	private static final int VERSION_IHL_OFFSET = 0;
	private static final int TYPE_OF_SERVICE_OFFSET = 1;
	private static final int TOTAL_LENGTH_OFFSET = 2;
	private static final int PACKET_ID_OFFSET = 4;
	private static final int FRAGMENT_OFFSET_OFFSET = 6;
	private static final int TIME_TO_LIVE_OFFSET = 8;
	private static final int NEXT_PROTO_ID_OFFSET = 9;
	private static final int HDR_CHECKSUM_OFFSET = 10;
	private static final int SRC_ADDR_OFFSET = 12;
	private static final int DST_ADDR_OFFSET = 16;

	public Ipv4Packet(long mbuf, long packet) {
		super(mbuf, packet);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Version_ihl: " + getVersionIhl() + "\n");
		sb.append("Type Of Service: " + getTypeOfService() + "\n");
		sb.append("Total Length: " + getLength() + "\n");
		sb.append("Packet Id: " + getPacketId() + "\n");
		sb.append("Fragment Offset: " + getFragmentOffset() + "\n");
		sb.append("Time To Live: " + getTimeToLive() + "\n");
		sb.append("Next Proto Id: " + getNextProtoId() + "\n");
		sb.append("Hdr Checksum: " + getHdrChecksum() + "\n");
		sb.append("Src Addr: " + getSrcAddr() + "\n");
		sb.append("Dst Addr: " + getDst_Addr() + "\n");
		return sb.toString();
	}
	
	public void setVersionIhl(int version_ihl) {
		ua.setCurrentPointer(packet_pointer + VERSION_IHL_OFFSET);
		ua.putByte(version_ihl);
	}
	
	public void setTypeOfService(int type_of_service) {
		ua.setCurrentPointer(packet_pointer + TYPE_OF_SERVICE_OFFSET);
		ua.putByte(type_of_service);
	}
	
	public void setTotalLength(int total_length) {
		ua.setCurrentPointer(packet_pointer + TOTAL_LENGTH_OFFSET);
		ua.putShort(total_length);
	}
	
	public void setPacketID(int packet_id) {
		ua.setCurrentPointer(packet_pointer + PACKET_ID_OFFSET);
		ua.putShort(packet_id);
	}
	
	public void setFragmentOffset(int fragment_offset) {
		ua.setCurrentPointer(packet_pointer + FRAGMENT_OFFSET_OFFSET);
		ua.putShort(fragment_offset);
	}
	
	public void setTimeToLive(int time_to_live) {
		ua.setCurrentPointer(packet_pointer + TIME_TO_LIVE_OFFSET);
		ua.putByte(time_to_live);
	}
	
	public void setNextProtoID(int next_proto_id) {
		ua.setCurrentPointer(packet_pointer + NEXT_PROTO_ID_OFFSET);
		ua.putByte(next_proto_id);
	}
	
	public void setHdrChecksum(int hdr_checksum) {
		ua.setCurrentPointer(packet_pointer + HDR_CHECKSUM_OFFSET);
		ua.putShort(hdr_checksum);
	}
	
	public void setSrcAddr(long src_addr) {
		ua.setCurrentPointer(packet_pointer + SRC_ADDR_OFFSET);
		ua.putInt(src_addr);
	}
	
	public void setDstAddr(long dst_addr) {
		ua.setCurrentPointer(packet_pointer + DST_ADDR_OFFSET);
		ua.putInt(dst_addr);
	}
	
	public int getVersionIhl() {
		ua.setCurrentPointer(packet_pointer + VERSION_IHL_OFFSET);
		return ua.getByte();
	}
	
	public int getVersion() {
		return getVersionIhl() >> 4;
	}
	
	public int getTypeOfService() {
		ua.setCurrentPointer(packet_pointer + TYPE_OF_SERVICE_OFFSET);
		return ua.getByte();
	}
	
	public int getLength() {
		ua.setCurrentPointer(packet_pointer + TOTAL_LENGTH_OFFSET);
		return ua.getShort();
	}
	
	public int getPacketId() {
		ua.setCurrentPointer(packet_pointer + PACKET_ID_OFFSET);
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
