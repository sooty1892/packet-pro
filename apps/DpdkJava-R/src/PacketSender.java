
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

	
	private PacketListCreate list;
	
	private static final int DEFAULT_SEND_BURST = 16;
	
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
		list = new PacketListCreate(send_burst);
	}
	
	public int getSendBurst() {
		return send_burst;
	}

	
	// packet added to sends list and checks made for
	// timeout period and list size
	public void sendPacket(Packet p) {
		list.add(p);
		if (list.size() == send_burst) {
			sendBurst();
		}
	}
	
	// send burst of packets and also frees them via dpdk library
	// also contains stats data collection
	private void sendBurst() {
		DpdkAccess.dpdk_send_packets(list.getNativePointer(), port_id, queue_id);
		list.reset();
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
