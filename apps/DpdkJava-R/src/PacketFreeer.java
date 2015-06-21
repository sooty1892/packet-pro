
/*
 * Frees given packets from native memory via DPDK library
 */

// How to spell free-er is the real question here
public class PacketFreeer {
	
	UnsafeAccess ua;
	long past_freed;
	
	int free_burst;
	
	private PacketListCreate list;
	
	private static final int DEFAULT_FREE_BURST = 16;
	//private static final long MILLI_SECOND = 1000;
	
	public PacketFreeer() {
		ua = new UnsafeAccess();
		past_freed = System.currentTimeMillis();
		free_burst = DEFAULT_FREE_BURST;
		list = new PacketListCreate(free_burst);
	}
	
	public int getFreeBurst() {
		return free_burst;
	}
	
	// checks if the given time period has occurred since last packet freeing
	// used so packets are held in memory for too long for no reason
	/*private boolean isTimedOut() {
		return (System.currentTimeMillis() - past_freed) >= MILLI_SECOND;
	}*/
	
	// packet added to free list and checks made for
	// timeout period and list size
	public void freePacket(Packet p) {
		list.add(p);
		//if (list.size() == free_burst || isTimedOut()) {
		if (list.size() == free_burst) {
			freeBurst();
			//past_freed = System.currentTimeMillis();
		}
	}
	
	// frees burst of packets via dpdk library
	private void freeBurst() {
		DpdkAccess.dpdk_free_packets(list.getNativePointer());
		list.reset();
		
	}

}
