
public class ReceivePoller {
	
	UnsafeAccess ua;
	PacketInspector pi;
	long all_packet_count;

	public ReceivePoller(UnsafeAccess ua) {
		this.ua = ua;
		pi = new PacketInspector();
		all_packet_count = 0;
	}
	
	public long getPacketCount() {
		return all_packet_count;
	}
	
	public void start() throws InterruptedException {
		boolean b = true;
		int memory_size = ((Long.SIZE / Byte.SIZE) * 512) + 2;
		while (b) {
			long mem_pointer = ua.allocateMemory(memory_size);
			
			DpdkAccess.dpdk_receive_burst(mem_pointer);
			
			ua.setCurrentPointer(mem_pointer);
			
			int packet_count = ua.getShort();
			all_packet_count += packet_count; 
			
			mem_pointer += ua.getOffset();

			//remember to free packets sometime
			if (packet_count > 0) {
				System.out.println("JAVA: Parsing " + packet_count + " packets!");
				
				for (int i = 0; i < packet_count; i++) {
					Packet p = new Packet();
					
					ua.setCurrentPointer(mem_pointer + (i*ua.longSize()));
					long ip_hdr_pointer = ua.getLong();
					
					ua.setCurrentPointer(ip_hdr_pointer);

					p.set_version_ihl(ua.getByte());
					p.set_dscp_ecn(ua.getByte());
					p.setTotal_length(ua.getShort());
					p.setPacket_id(ua.getShort());
					p.setFragment_offset(ua.getShort());
					p.setTime_to_live(ua.getByte());
					p.setNext_proto_id(ua.getByte());
					p.setHdr_checksum(ua.getShort());
					p.setSrc_addr(ua.getLong());
					p.setDst_addr(ua.getLong());
					
					//System.out.println(p.toString());
					
					pi.setPacket(p);
					pi.inspectPacket();
					
					
					System.out.println();
					System.out.println();
					//TODO: TAKE THIS OUT!!!
					Thread.sleep(1000);
				}

				b = false;
			}
		}
	}
	
}
