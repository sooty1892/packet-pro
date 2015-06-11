import sun.misc.Unsafe;


public class DirectPacket {
	
	private long pointer;
	private Unsafe unsafe;
	
	public DirectPacket(long pointer, Unsafe unsafe) {
		this.pointer = pointer;
		this.unsafe = unsafe;
	}
	
	public long getVersion() {
		return unsafe.getInt(pointer);
	}
	public void setVersion(long version) {
		unsafe.putInt(pointer, (int)version);
	}
	public long getIhl() {
		return unsafe.getInt(pointer + 4);
	}
	public void setIhl(long ihl) {
		unsafe.putInt(pointer + 4, (int)ihl);
	}
	public long getDscp() {
		return unsafe.getInt(pointer + 8);
	}
	public void setDscp(long dscp) {
		unsafe.putInt(pointer + 8, (int)dscp);
	}
	public long getEcn() {
		return unsafe.getInt(pointer + 12);
	}
	public void setEcn(long ecn) {
		unsafe.putInt(pointer + 12, (int)ecn);
	}
	public long getTotal_length() {
		return unsafe.getInt(pointer + 16);
	}
	public void setTotal_length(long total_length) {
		unsafe.putInt(pointer + 16, (int)total_length);
	}
	public long getPacket_id() {
		return unsafe.getInt(pointer + 20);
	}
	public void setPacket_id(long packet_id) {
		unsafe.putInt(pointer + 20, (int)packet_id);
	}
	public long getFragment_offset() {
		return unsafe.getInt(pointer + 24);
	}
	public void setFragment_offset(long fragment_offset) {
		unsafe.putInt(pointer + 24, (int)fragment_offset);
	}
	public long getTime_to_live() {
		return unsafe.getInt(pointer + 28);
	}
	public void setTime_to_live(long time_to_live) {
		unsafe.putInt(pointer + 28, (int)time_to_live);
	}
	public long getNext_proto_id() {
		return unsafe.getInt(pointer + 32);
	}
	public void setNext_proto_id(long next_proto_id) {
		unsafe.putInt(pointer + 32, (int)next_proto_id);
	}
	public long getHdr_checksum() {
		return unsafe.getInt(pointer + 36);
	}
	public void setHdr_checksum(long hdr_checksum) {
		unsafe.putInt(pointer + 36, (int)hdr_checksum);
	}
	public long getSrc_addr() {
		return unsafe.getLong(pointer + 40);
	}
	public void setSrc_addr(long src_addr) {
		unsafe.putLong(pointer + 40, src_addr);
	}
	public long getDst_addr() {
		return unsafe.getLong(pointer + 48);
	}
	public void setDst_addr(long dst_addr) {
		unsafe.putLong(pointer + 48, dst_addr);
	}
	
}
