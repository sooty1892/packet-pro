public class Packet {
	
	private int version;
	private int ihl;
	private int dscp;
	private int ecn;
	private int total_length;
	private int packet_id;
	private int fragment_offset;
	private int time_to_live;
	private int next_proto_id;
	private int hdr_checksum;
	private long src_addr;
	private long dst_addr;
	private long mbuf_pointer;
	private long packet_pointer;

	public Packet() {
		this.version = 0;
		this.ihl = 0;
		this.dscp = 0;
		this.ecn = 0;
		this.total_length = 0;
		this.packet_id = 0;
		this.fragment_offset = 0;
		this.time_to_live = 0;
		this.next_proto_id = 0;
		this.hdr_checksum = 0;
		this.src_addr = 0;
		this.dst_addr = 0;
		this.mbuf_pointer = 0;
		this.packet_pointer = 0;
	}
	
	public Packet(byte version,
				byte ihl,
				byte dscp,
				byte ecn,
				short total_length,
				short packet_id,
				short fragment_offset,
				byte time_to_live,
				byte next_proto_id,
				short hdr_checksum,
				int src_addr,
				int dst_addr,
				long mbuf_pointer,
				long packet_pointer) {
		this.version = version;
		this.ihl = ihl;
		this.dscp = dscp;
		this.ecn = ecn;
		this.total_length = total_length;
		this.packet_id = packet_id;
		this.fragment_offset = fragment_offset;
		this.time_to_live = time_to_live;
		this.next_proto_id = next_proto_id;
		this.hdr_checksum = hdr_checksum;
		this.src_addr = src_addr;
		this.dst_addr = dst_addr;
		this.mbuf_pointer = mbuf_pointer;
		this.packet_pointer = packet_pointer;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Version: " + version + "\n");
		sb.append("Ihl: " + ihl + "\n");
		sb.append("Dscp: " + dscp + "\n");
		sb.append("Ecn: " + ecn + "\n");
		sb.append("Total Length: " + total_length + "\n");
		sb.append("Packed Id: " + packet_id + "\n");
		sb.append("Fragment Offset: " + fragment_offset + "\n");
		sb.append("Time To Live: " + time_to_live + "\n");
		sb.append("Next Proto Id: " + next_proto_id + "\n");
		sb.append("Hdr Checksum: " + hdr_checksum + "\n");
		sb.append("Src Addr: " + src_addr + "\n");
		sb.append("Dst Addr: " + dst_addr + "\n");
		return sb.toString();
	}

	public void set_version_ihl(int version_ihl) {
		version = (int) ((version_ihl >> 4) & (byte) 0xF);
		ihl = (int) (version_ihl & (byte) 0xF);
	}

	public void set_dscp_ecn(int dscp_ecn) {
		dscp = (int) ((dscp_ecn >> 2) & (byte) 0x3F);
		ecn = (int) (dscp_ecn & (byte) 0x3);
	}

	//TODO: fix this - not all fields done - needed?
	public void swapAllEndian() {
		total_length = Utils.swap(total_length);
		packet_id = Utils.swap(packet_id);
		fragment_offset = Utils.swap(fragment_offset);
		hdr_checksum = Utils.swap(hdr_checksum);
		src_addr = Utils.swap(src_addr);
		dst_addr = Utils.swap(dst_addr);
	}

	public int getIhl() {
		return ihl;
	}

	public int getVersion() {
		return version;
	}

	public int getDscp() {
		return dscp;
	}

	public int getEcn() {
		return ecn;
	}

	public int getTotal_length() {
		return total_length;
	}

	public int getPacket_id() {
		return packet_id;
	}

	public int getFragment_offset() {
		return fragment_offset;
	}

	public int getTime_to_live() {
		return time_to_live;
	}

	public int getNext_proto_id() {
		return next_proto_id;
	}

	public int getHdr_checksum() {
		return hdr_checksum;
	}

	public long getSrc_addr() {
		return src_addr;
	}

	public long getDst_addr() {
		return dst_addr;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public void setIhl(int ihl) {
		this.ihl = ihl;
	}

	public void setDscp(int dscp) {
		this.dscp = dscp;
	}

	public void setEcn(int ecn) {
		this.ecn = ecn;
	}

	public void setTotal_length(int total_length) {
		this.total_length = total_length;
	}

	public void setPacket_id(int packet_id) {
		this.packet_id = packet_id;
	}

	public void setFragment_offset(int fragment_offset) {
		this.fragment_offset = fragment_offset;
	}

	public void setTime_to_live(int time_to_live) {
		this.time_to_live = time_to_live;
	}

	public void setNext_proto_id(int next_proto_id) {
		this.next_proto_id = next_proto_id;
	}

	public void setHdr_checksum(int hdr_checksum) {
		this.hdr_checksum = hdr_checksum;
	}

	public void setSrc_addr(long src_addr) {
		this.src_addr = src_addr;
	}

	public void setDst_addr(long dst_addr) {
		this.dst_addr = dst_addr;
	}

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

	
}