import java.util.ArrayList;
import java.util.List;

/*
 * Frees given packets from native memory via DPDK library
 */

// How to spell free-er is the real question here
public class PacketFreeer {
	
	private List<Packet> list;
	UnsafeAccess ua;
	long past_freed;
	
	int free_burst;
	
	private static final int DEFAULT_FREE_BURST = 32;
	private static final long NANO_SECOND = 1000000000;
	private static final int SHORT_SIZE = 2;
	
	public PacketFreeer() {
		list = new ArrayList<Packet>();
		ua = new UnsafeAccess();
		past_freed = System.nanoTime();
		free_burst = DEFAULT_FREE_BURST;
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
		return (System.nanoTime() - past_freed) >= NANO_SECOND;
	}
	
	// packet added to free list and checks made for
	// timeout period and list size
	public void freePacket(Packet p) {
		list.add(p);
		if (list.size() >= free_burst || isTimedOut()) {
			freeBurst();
			past_freed = System.nanoTime();
		}
	}
	
	// frees burst of packets via dpdk library
	private void freeBurst() {
		int num = 0;
		if (list.size() > free_burst) {
			num = free_burst;
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
		
		DpdkAccess.dpdk_free_packets(pointer);
		
		ua.freeMemory(pointer);
	}

}
