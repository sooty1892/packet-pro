
public class ObjectPacket {
	
	private long version;
	private long ihl;
	private long dscp;
	private long ecn;
	private long total_length;
	private long packet_id;
	private long fragment_offset;
	private long time_to_live;
	private long next_proto_id;
	private long hdr_checksum;
	private long src_addr;
	private long dst_addr;
	
	public ObjectPacket() {
		
	}
	
	public long getVersion() {
		return version;
	}
	public void setVersion(long version) {
		this.version = version;
	}
	public long getIhl() {
		return ihl;
	}
	public void setIhl(long ihl) {
		this.ihl = ihl;
	}
	public long getDscp() {
		return dscp;
	}
	public void setDscp(long dscp) {
		this.dscp = dscp;
	}
	public long getEcn() {
		return ecn;
	}
	public void setEcn(long ecn) {
		this.ecn = ecn;
	}
	public long getTotal_length() {
		return total_length;
	}
	public void setTotal_length(long total_length) {
		this.total_length = total_length;
	}
	public long getPacket_id() {
		return packet_id;
	}
	public void setPacket_id(long packet_id) {
		this.packet_id = packet_id;
	}
	public long getFragment_offset() {
		return fragment_offset;
	}
	public void setFragment_offset(long fragment_offset) {
		this.fragment_offset = fragment_offset;
	}
	public long getTime_to_live() {
		return time_to_live;
	}
	public void setTime_to_live(long time_to_live) {
		this.time_to_live = time_to_live;
	}
	public long getNext_proto_id() {
		return next_proto_id;
	}
	public void setNext_proto_id(long next_proto_id) {
		this.next_proto_id = next_proto_id;
	}
	public long getHdr_checksum() {
		return hdr_checksum;
	}
	public void setHdr_checksum(long hdr_checksum) {
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
