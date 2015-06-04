import java.io.IOException;

public class ReceivePoller {
	
	UnsafeAccess ua;
	PacketInspector pi;
	long packet_all;
	long packet_all_size;
	long packet_interval;
	long packet_interval_size;

	public ReceivePoller(UnsafeAccess ua) {
		this.ua = ua;
		pi = new PacketInspector();
		packet_all = 0;
		packet_all_size = 0;
		packet_interval = 0;
		packet_interval_size = 0;
	}
	
	public long getPacketAll() {
		return packet_all;
	}
	
	public long getPacketAllSize() {
		return packet_all_size;
	}
	
	public long getPacketInterval() {
		return packet_interval;
	}
	
	public long getPacketIntervalSize() {
		return packet_interval_size;
	}
	
	public void resetInterval() {
		packet_interval = 0;
		packet_interval_size = 0;
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
			packet_all += packet_count; 
			packet_interval += packet_count;
			
			mem_pointer += ua.getOffset();

			//remember to free packets sometime
			if (packet_count > 0) {
				//System.out.println("JAVA: Parsing " + packet_count + " packets!");
				
				for (int i = 0; i < packet_count; i++) {
					//Packet p = new Packet();
					
					long new_pointer = mem_pointer + (2*i*ua.longSize());
					ua.setCurrentPointer(new_pointer);
					long mbuf = ua.getLong();
					long packet = ua.getLong();
					
					Ipv4Packet p = new Ipv4Packet(mbuf, packet);
					
					packet_all_size += p.getTotalLength(); // plus ethernet header?
					packet_interval_size += p.getTotalLength();
		
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
