
public class ObjectPacket {
	
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
	
	public ObjectPacket() {
		
	}
	
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public int getIhl() {
		return ihl;
	}
	public void setIhl(int ihl) {
		this.ihl = ihl;
	}
	public int getDscp() {
		return dscp;
	}
	public void setDscp(int dscp) {
		this.dscp = dscp;
	}
	public int getEcn() {
		return ecn;
	}
	public void setEcn(int ecn) {
		this.ecn = ecn;
	}
	public int getTotal_length() {
		return total_length;
	}
	public void setTotal_length(int total_length) {
		this.total_length = total_length;
	}
	public int getPacket_id() {
		return packet_id;
	}
	public void setPacket_id(int packet_id) {
		this.packet_id = packet_id;
	}
	public int getFragment_offset() {
		return fragment_offset;
	}
	public void setFragment_offset(int fragment_offset) {
		this.fragment_offset = fragment_offset;
	}
	public int getTime_to_live() {
		return time_to_live;
	}
	public void setTime_to_live(int time_to_live) {
		this.time_to_live = time_to_live;
	}
	public int getNext_proto_id() {
		return next_proto_id;
	}
	public void setNext_proto_id(int next_proto_id) {
		this.next_proto_id = next_proto_id;
	}
	public int getHdr_checksum() {
		return hdr_checksum;
	}
	public void setHdr_checksum(int hdr_checksum) {
		this.hdr_checksum = hdr_checksum;
	}
	public long getSrc_addr() {
		return src_addr;
	}
	public void setSrc_addr(long src_addr) {
		this.src_addr = src_addr;
	}
	public long getDst_addr() {
		return dst_addr;
	}
	public void setDst_addr(long dst_addr) {
		this.dst_addr = dst_addr;
	}

}
