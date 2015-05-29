import java.util.ArrayList;
import java.util.List;


// How to spell free-er is the real question here
public class PacketFreeer {
	
	private List<Packet> list;
	UnsafeAccess ua;
	
	public PacketFreeer() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		list = new ArrayList<Packet>();
		ua = new UnsafeAccess();
	}
	
	public void freePacket(Packet p) {
		list.add(p);
		//TODO time delay on this as well - also in packet sender
		if (list.size() >= 256) {
			freeBurst(256);
		}
	}
	
	//TODO: make private and uncomment
	public void freeBurst(int num) {
		long memory_needed = (num * ua.longSize()) + 2;
		long pointer = ua.allocateMemory(memory_needed);
		ua.setCurrentPointer(pointer);
		
		ua.putShort(num);
		/*
		for (int i = 0; i < num; i++) {
			ua.putLong(list.get(i).getMbuf_pointer());
		}
		list.subList(0, num).clear();*/
		
		DpdkAccess.dpdk_free_packets(pointer);
		
		//TODO: free memory
	}

}
