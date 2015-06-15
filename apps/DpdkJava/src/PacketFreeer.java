
/*
 * Frees given packets from native memory via DPDK library
 */

// How to spell free-er is the real question here
public class PacketFreeer {
	
	UnsafeAccess ua;
	long past_freed;
	
	int free_burst;
	
	int current_count;
	
	long start_pointer;
	long mbuf_pointer;
	
	private static final int DEFAULT_FREE_BURST = 32;
	private static final long MILLI_SECOND = 1000;
	private static final int SHORT_SIZE = 2;
	
	public PacketFreeer() {
		ua = new UnsafeAccess();
		past_freed = System.currentTimeMillis();
		free_burst = DEFAULT_FREE_BURST;
		current_count = 0;
		start_pointer = ua.allocateMemory((ua.longSize()*free_burst)+2);
		mbuf_pointer = start_pointer + 2;
	}
	
	public int getFreeBurst() {
		return free_burst;
	}
	
	public void setFreeBurst(int free_burst) {
		this.free_burst = free_burst;
	}
	
	// checks if the given time period has occurred since last packet freeing
	// used so packets are held in memory for too long for no reason
	private boolean isTimedOut() {
		return (System.currentTimeMillis() - past_freed) >= MILLI_SECOND;
	}
	
	// packet added to free list and checks made for
	// timeout period and list size
	public void freePacket(Packet p) {
//		ua.setCurrentPointer(mbuf_pointer + (ua.longSize() * current_count));
//		ua.putLong(p.getMbuf_pointer());
//		current_count += 1;
//		if (current_count >= free_burst || isTimedOut()) {
//			freeBurst();
//			past_freed = System.currentTimeMillis();
//		}
		ua.setCurrentPointer(start_pointer);	
		ua.putShort(1);
		ua.putLong(p.getMbuf_pointer());
		DpdkAccess.dpdk_free_packets(start_pointer);
	}
	
	// frees burst of packets via dpdk library
	private void freeBurst() {
		int num = 0;
		if (current_count > free_burst) {
			num = free_burst;
		} else {
			num = current_count;
		}

		ua.setCurrentPointer(start_pointer);
		
		ua.putShort(num);
		
		DpdkAccess.dpdk_free_packets(start_pointer);
		
		current_count -= num;
		
	}

}
