
public class Ipv6Packet extends Packet {
	
	private static final int VTC_FLOW_OFFSET = 0;
	private static final int PAYLOAD_LENGTH_OFFSET = 4;
	private static final int PROTO_OFFSET = 6;
	private static final int HOP_LIMITS_OFFSET = 7;
	private static final int SRC_ADDR_OFFSET = 8;
	private static final int DST_ADDR_OFFSET = 24;

	public Ipv6Packet(long mbuf, long packet) {
		super(mbuf, packet);
	}
	
	//check struct is packed
	
	public long getVtcFlow() {
		ua.setCurrentPointer(packet_pointer + VTC_FLOW_OFFSET);
		return ua.getInt();
	}
	
	public int getPayloadLen() {
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
		byte[] res = new byte[16];
		for (int i = 0; i < 16; i++) {
			res[i] = (byte)ua.getByte();
		}
		return res;
	}
	
	public byte[] getDstAddr() {
		ua.setCurrentPointer(packet_pointer + DST_ADDR_OFFSET);
		byte[] res = new byte[16];
		for (int i = 0; i < 16; i++) {
			res[i] = (byte)ua.getByte();
		}
		return res;
	}

}
