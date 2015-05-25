public class Packet {
	
	private byte version_ihl;
	private byte type_of_service;
	private short total_length;
	private short packet_id;
	private short fragment_offset;
	private byte time_to_live;
	private byte next_proto_id;
	private short hdr_checksum;
	private int src_addr;
	private int dst_addr;
	
	public Packet(byte version_ihl,
				byte type_of_service,
				short total_length,
				short packet_id,
				short fragment_offset,
				byte time_to_live,
				byte next_proto_id,
				short hdr_checksum,
				int src_addr,
				int dst_addr) {
		this.version_ihl = version_ihl;
		this.type_of_service = type_of_service;
		this.total_length = total_length;
		this.packet_id = packet_id;
		this.fragment_offset = fragment_offset;
		this.time_to_live = time_to_live;
		this.next_proto_id = next_proto_id;
		this.hdr_checksum = hdr_checksum;
		this.src_addr = src_addr;
		this.dst_addr = dst_addr;
	}
	
	public void swapAllEndian() {
		total_length = Utils.swap(total_length);
		packet_id = Utils.swap(packet_id);
		fragment_offset = Utils.swap(fragment_offset);
		hdr_checksum = Utils.swap(hdr_checksum);
		src_addr = Utils.swap(src_addr);
		dst_addr = Utils.swap(dst_addr);
	}

	public byte getVersion_ihl() {
		return version_ihl;
	}

	public byte getType_of_service() {
		return type_of_service;
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

	public void setVersion_ihl(byte version_ihl) {
		this.version_ihl = version_ihl;
	}

	public void setType_of_service(byte type_of_service) {
		this.type_of_service = type_of_service;
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