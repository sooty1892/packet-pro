import java.util.ArrayList;
import java.util.List;


public class PacketSender {
	
	private List<Packet> list;
	UnsafeAccess ua;
	
	public PacketSender() {
		list = new ArrayList<Packet>();
		ua = new UnsafeAccess();
	}
	
	public void sendPacket(Packet p) {
		list.add(p);
		if (list.size() >= 5) {
			sendBurst(5);
		}
	}
	
	private void sendBurst(int num) {
		long memory_needed = (num * ua.longSize()) + 2;
		long pointer = ua.allocateMemory(memory_needed);
		ua.setCurrentPointer(pointer);
		
		ua.putShort(num);
		
		for (int i = 0; i < num; i++) {
			ua.putLong(list.get(i).getMbuf_pointer());
		}
		
		/*ua.setCurrentPointer(pointer+2);
		for (int i = 0; i < num; i++) {
			System.out.println("CHECK: " + Long.toHexString(ua.getLong()));
		}*/
		
		list.subList(0, num).clear();
		
		DpdkAccess.dpdk_send_packets(pointer);
		
		ua.freeMemory(pointer);
	}

}
