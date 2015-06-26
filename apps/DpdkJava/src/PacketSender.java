
/*
 * Class to aid in sending packets via certain port and queue
 */

public class PacketSender {
	
	UnsafeAccess ua;
	long packet_all;
	long packet_all_size;
	long packet_interval;
	long packet_interval_size;
	long past_sent;
	int port_id;
	int queue_id;
	
	int send_burst;
	
	int current_count;
	
	long start_pointer;
	long mbuf_pointer;
	
	private static final int DEFAULT_SEND_BURST = 32;
	private static final long MILLI_SECOND = 1000;
	private static final int SHORT_SIZE = 2;
	
	public PacketSender(int port_id, int queue_id) {
		ua = new UnsafeAccess();
		packet_all = 0;
		packet_all_size = 0;
		packet_interval = 0;
		packet_interval_size = 0;
		past_sent = System.currentTimeMillis();
		send_burst = DEFAULT_SEND_BURST;
		this.port_id = port_id;
		this.queue_id = queue_id;
		start_pointer = ua.allocateMemory((ua.longSize()*send_burst)+2);
		mbuf_pointer = start_pointer + 2;
	}
	
	public int getSendBurst() {
		return send_burst;
	}
	
	public void setSendBurst(int send_burst) {
		this.send_burst = send_burst;
	}
	
	// checks if the given time period has occurred since last packet sending
	// used so packets are held in memory for too long for no reason
	private boolean isTimedOut() {
		return (System.currentTimeMillis() - past_sent) >= MILLI_SECOND;
	}
	
	// packet added to sends list and checks made for
	// timeout period and list size
	public void sendPacket(Packet p) {
		ua.setCurrentPointer(mbuf_pointer + (ua.longSize() * current_count));
		ua.putLong(p.getMbuf_pointer());
		current_count += 1;
		if (current_count >= send_burst || isTimedOut()) {
			sendBurst();
			past_sent = System.currentTimeMillis();
		}
	}
	
	// send burst of packets and also frees them via dpdk library
	// also contains stats data collection
	private void sendBurst() {
		int num = 0;
		if (current_count > send_burst) {
			num = send_burst;
		} else {
			num = current_count;
		}

		ua.setCurrentPointer(start_pointer);
		
		ua.putShort(num);
		
		DpdkAccess.dpdk_send_packets(start_pointer, port_id, queue_id);
		
		current_count -= num;
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
