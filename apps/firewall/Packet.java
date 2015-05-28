public class Packet {
	
	private byte version;
	private byte ihl;
	private byte dscp;
	private byte ecn;
	private short total_length;
	private short packet_id;
	private short fragment_offset;
	private byte time_to_live;
	private byte next_proto_id;
	private short hdr_checksum;
	private int src_addr;
	private int dst_addr;
	
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
				int dst_addr) {
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
	}

	public void set_version_ihl(byte version_ihl) {
		version = (byte) ((version_ihl >> 4) & (byte) 0xF);
		ihl = (byte) (version_ihl & 0xF);
	}

	public void set_dscp_ecn(byte dscp_ecn) {
		dscp = (byte) ((dscp_ecn >> 2) & (byte) 0x3F);
		ecn = (byte) (dscp_ecn & (byte) 0x3);
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

	public byte getIhl() {
		return ihl;
	}

	public byte getVersion() {
		return version;
	}

	public byte getDscp() {
		return dscp;
	}

	public byte getEcn() {
		return ecn;
	}

	public short getTotal_length() {
		return total_length;
	}

	public short getPacket_id() {
		return packet_id;
	}

	public short getFragment_offset() {
		return fragment_offset;
	}

	public byte getTime_to_live() {
		return time_to_live;
	}

	public byte getNext_proto_id() {
		return next_proto_id;
	}

	public short getHdr_checksum() {
		return hdr_checksum;
	}

	public int getSrc_addr() {
		return src_addr;
	}

	public int getDst_addr() {
		return dst_addr;
	}

	public void setVersion(byte version) {
		this.version = version;
	}

	public void setIhl(byte ihl) {
		this.ihl = ihl;
	}

	public void setDscp(byte dcsp) {
		this.dscp = dscp;
	}

	public void setEcn(byte ecn) {
		this.ecn = ecn;
	}

	public void setTotal_length(short total_length) {
		this.total_length = total_length;
	}

	public void setPacket_id(short packet_id) {
		this.packet_id = packet_id;
	}

	public void setFragment_offset(short fragment_offset) {
		this.fragment_offset = fragment_offset;
	}

	public void setTime_to_live(byte time_to_live) {
		this.time_to_live = time_to_live;
	}

	public void setNext_proto_id(byte next_proto_id) {
		this.next_proto_id = next_proto_id;
	}

	public void setHdr_checksum(short hdr_checksum) {
		this.hdr_checksum = hdr_checksum;
	}

	public void setSrc_addr(int src_addr) {
		this.src_addr = src_addr;
	}

	public void setDst_addr(int dst_addr) {
		this.dst_addr = dst_addr;
	}

	
}