import java.util.ArrayList;
import java.util.List;


public class PacketSender {
	
	private List<Packet> list;
	UnsafeAccess ua;
	long packet_all;
	long packet_all_size;
	long packet_interval;
	long packet_interval_size;
	long past_sent;
	int port_id;
	int queue_id;
	
	int send_burst;
	
	private static final int DEFAULT_SEND_BURST = 32;
	private static final long NANO_SECOND = 1000000000;
	private static final int SHORT_SIZE = 2;
	
	public PacketSender(int port_id, int queue_id) {
		list = new ArrayList<Packet>();
		ua = new UnsafeAccess();
		packet_all = 0;
		packet_all_size = 0;
		packet_interval = 0;
		packet_interval_size = 0;
		past_sent = System.nanoTime();
		send_burst = DEFAULT_SEND_BURST;
		this.port_id = port_id;
		this.queue_id = queue_id;
	}
	
	public int getSendBurst() {
		return send_burst;
	}
	
	public void setSendBurst(int send_burst) {
		this.send_burst = send_burst;
	}
	
	private boolean isTimedOut() {
		return (System.nanoTime() - past_sent) >= NANO_SECOND;
	}
	
	public void sendPacket(Packet p) {
		list.add(p);
		if (list.size() >= send_burst || isTimedOut()) {
			sendBurst();
			past_sent = System.nanoTime();
		}
	}
	
	private void sendBurst() {
		int num = 0;
		if (list.size() > send_burst) {
			num = send_burst;
		} else {
			num = list.size();
		}
		long memory_needed = (num * ua.longSize()) + SHORT_SIZE;
		long pointer = ua.allocateMemory(memory_needed);
		ua.setCurrentPointer(pointer);
		
		ua.putShort(num);
		
		for (int i = 0; i < num; i++) {
			ua.putLong(list.get(i).getMbuf_pointer());
		}
		
		list.subList(0, num).clear();
		
		DpdkAccess.dpdk_send_packets(pointer, port_id, queue_id);
		
		ua.freeMemory(pointer);
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
