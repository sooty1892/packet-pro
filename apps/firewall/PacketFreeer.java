import java.util.ArrayList;
import java.util.List;


// How to spell free-er is the real question here
public class PacketFreeer {
	
	private List<Packet> list;
	UnsafeAccess ua;
	
	public PacketFreeer() {
		list = new ArrayList<Packet>();
		ua = new UnsafeAccess();
	}
	
	public void freePacket(Packet p) {
		list.add(p);
		//TODO time delay on this as well - also in packet sender
		if (list.size() >= 5) {
			freeBurst(5);
		}
	}
	
	private void freeBurst(int num) {
		long memory_needed = (num * ua.longSize()) + 2;
		long pointer = ua.allocateMemory(memory_needed);
		ua.setCurrentPointer(pointer);
		
		ua.putShort(num);
		
		System.out.println();

		for (int i = 0; i < num; i++) {
			System.out.println(Long.toHexString(list.get(i).getMbuf_pointer()));
			ua.putLong(list.get(i).getMbuf_pointer());
		}

		ua.setCurrentPointer(pointer+2);
		for (int i = 0; i < num; i++) {
			System.out.println("CHECK: " + Long.toHexString(ua.getLong()));
		}

		list.subList(0, num).clear();
		
		DpdkAccess.dpdk_free_packets(pointer);
		
		//TODO: free memory
	}

}
