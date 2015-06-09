/*
 * IPv6 packet to access all fields of packet headers
 */

public class Ipv6Packet extends Packet {
	
	private static final int VTC_FLOW_OFFSET = 0;
	private static final int PAYLOAD_LENGTH_OFFSET = 4;
	private static final int PROTO_OFFSET = 6;
	private static final int HOP_LIMITS_OFFSET = 7;
	private static final int SRC_ADDR_OFFSET = 8;
	private static final int DST_ADDR_OFFSET = 24;
	
	private static final int BYTES_IPV6 = 16;

	public Ipv6Packet(long mbuf, long packet) {
		super(mbuf, packet);
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Vtc Flow: " + getVtcFlow() + "\n");
		sb.append("Payload Length: " + getLength() + "\n");
		sb.append("Proto: " + getProto() + "\n");
		sb.append("Hop Limits: " + getHopLimits() + "\n");
		sb.append("Src Addr: " + getSrcAddr().toString() + "\n");
		sb.append("Dst Addr: " + getDstAddr().toString() + "\n");
		return sb.toString();
	}
	
	public void setVtcFlow(long vtc_flow) {
		ua.setCurrentPointer(packet_pointer + VTC_FLOW_OFFSET);
		ua.putInt(vtc_flow);
	}
	
	public void setPayloadLength(int payload_length) {
		ua.setCurrentPointer(packet_pointer + PAYLOAD_LENGTH_OFFSET);
		ua.putShort(payload_length);
	}
	
	public void setProto(int proto) {
		ua.setCurrentPointer(packet_pointer + PROTO_OFFSET);
		ua.putByte(proto);
	}
	
	public void setHopLimits(int hop_limits) {
		ua.setCurrentPointer(packet_pointer + HOP_LIMITS_OFFSET);
		ua.putByte(hop_limits);
	}
	
	public void setSrcAddr(byte[] src_addr) {
		ua.setCurrentPointer(packet_pointer + SRC_ADDR_OFFSET);
		for (int i = 0; i < 16; i++) {
			ua.putByte(src_addr[i]);
		}
	}
	
	public void setDstAddr(byte[] dst_addr) {
		ua.setCurrentPointer(packet_pointer + DST_ADDR_OFFSET);
		for (int i = 0; i < 16; i++) {
			ua.putByte(dst_addr[i]);
		}
	}
	
	public int getVersion() {
		return (int)(getVtcFlow() >> 28);
	}
	
	public long getVtcFlow() {
		ua.setCurrentPointer(packet_pointer + VTC_FLOW_OFFSET);
		return ua.getInt();
	}
	
	public int getLength() {
		ua.setCurrentPointer(packet_pointer + PAYLOAD_LENGTH_OFFSET);
		return ua.getShort();
	}
	
	public int getProto() {
		ua.setCurrentPointer(packet_pointer + PROTO_OFFSET);
		return ua.getByte();
	}
	
	public int getHopLimits() {
		ua.setCurrentPointer(packet_pointer + HOP_LIMITS_OFFSET);
		return ua.getByte();
	}
	
	public byte[] getSrcAddr() {
		ua.setCurrentPointer(packet_pointer + SRC_ADDR_OFFSET);
		byte[] res = new byte[BYTES_IPV6];
		for (int i = 0; i < BYTES_IPV6; i++) {
			res[i] = (byte)ua.getByte();
		}
		return res;
	}
	
	public byte[] getDstAddr() {
		ua.setCurrentPointer(packet_pointer + DST_ADDR_OFFSET);
		byte[] res = new byte[BYTES_IPV6];
		for (int i = 0; i < BYTES_IPV6; i++) {
			res[i] = (byte)ua.getByte();
		}
		return res;
	}

}
