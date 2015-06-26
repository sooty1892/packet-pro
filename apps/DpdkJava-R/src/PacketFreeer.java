
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
	
	public PacketFreeer() {
		ua = new UnsafeAccess();
		past_freed = System.currentTimeMillis();
		free_burst = DEFAULT_FREE_BURST;
		list = new PacketListCreate(free_burst);
	}
	
	public int getFreeBurst() {
		return free_burst;
	}
	
	// packet added to free list and checks made for
	// timeout period and list size
	public void freePacket(Packet p) {
		list.add(p);
		if (list.size() == free_burst) {
			freeBurst();
		}
	}
	
	// frees burst of packets via dpdk library
	private void freeBurst() {
		DpdkAccess.dpdk_free_packets(list.getNativePointer());
		list.reset();
		
	}

}
