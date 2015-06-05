import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReceivePoller {
	
	UnsafeAccess ua;
	long packet_all;
	long packet_all_size;
	long packet_interval;
	long packet_interval_size;
	int port_id;
	int queue_id;
	
	int get_size;
	
	private static final int DEFAULT_GET_SIZE = 32;

	public ReceivePoller(int port_id, int queue_id) {
		ua = new UnsafeAccess();
		packet_all = 0;
		packet_all_size = 0;
		packet_interval = 0;
		packet_interval_size = 0;
		get_size = DEFAULT_GET_SIZE;
		this.port_id = port_id;
		this.queue_id = queue_id;
	}
	
	public int getGetSize() {
		return get_size;
	}
	
	public void setGetSize(int get_size) {
		this.get_size = get_size;
	}
	
	public List<Packet> getBurst() {
		List<Packet> packets = new ArrayList<Packet>();
		
		// each packet sends back mbuf and packet_header pointer -> therefore 2
		int memory_size = ((Long.SIZE / Byte.SIZE) * get_size * 2) + Short.SIZE;
		long mem_pointer = ua.allocateMemory(memory_size);
		long orig = mem_pointer;

		DpdkAccess.dpdk_receive_burst(mem_pointer);
		
		ua.setCurrentPointer(mem_pointer);
		
		int packet_count = ua.getShort();
		packet_all += packet_count; 
		packet_interval += packet_count;
		
		mem_pointer += ua.getOffset();
		
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
				
				packets.add(p);
			}
		}
		ua.freeMemory(orig);
		return packets;
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
	
}
