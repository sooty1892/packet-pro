import java.io.IOException;

public class ReceivePoller {
	
	UnsafeAccess ua;
	PacketInspector pi;
	long all_packet_count;
	//long mem_pointer;

	public ReceivePoller(UnsafeAccess ua) {
		this.ua = ua;
		pi = new PacketInspector();
		all_packet_count = 0;
	}
	
	public long getPacketCount() {
		return all_packet_count;
	}

	public void stop() {
		//ua.freeMemory(mem_pointer);
	}
	
	public void start() {
		boolean b = true;
		while (b) {
			int memory_size = ((Long.SIZE / Byte.SIZE) * 512 * 2) + 2;
			long mem_pointer = ua.allocateMemory(memory_size);
			long orig = mem_pointer;

			DpdkAccess.dpdk_receive_burst(mem_pointer);
			
			ua.setCurrentPointer(mem_pointer);
			
			int packet_count = ua.getShort();
			all_packet_count += packet_count; 
			
			mem_pointer += ua.getOffset();

			//remember to free packets sometime
			if (packet_count > 0) {
				System.out.println("JAVA: Parsing " + packet_count + " packets!");
				
				for (int i = 0; i < packet_count; i++) {
					//Packet p = new Packet();
					
					long new_pointer = mem_pointer + (2*i*ua.longSize());
					ua.setCurrentPointer(new_pointer);
					long mbuf = ua.getLong();
					long packet = ua.getLong();
					
					Ipv4Packet p = new Ipv4Packet(mbuf, packet);

					//System.out.println("JAVA: mbuf_pointer = " + p.getMbuf_pointer());
					//System.out.println("JAVA: packet_pointer = " + p.getPacket_pointer());
					
					/*ua.setCurrentPointer(new_pointer + 8);
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
					p.setSrc_addr(ua.getInt());
					p.setDst_addr(ua.getInt());*/
					
					//System.out.println(p.toString());

					System.out.println(p.toString());
			
					pi.inspectNewPacket(p);
					
					
					
					//TODO: TAKE THIS OUT!!!
					//Thread.sleep(1000);
				}
				System.out.println();

				//b = false;
			}
			//TODO: release memory?
			ua.freeMemory(orig);
		}

		System.out.println("OUT OF WHILE");
	}
	
}
