import java.util.ArrayList;
import java.util.List;


public class PacketSender {
	
	private List<Packet> list;
	
	public PacketSender() {
		list = new ArrayList<Packet>();
	}
	
	public void sendPacket(Packet p) {
		list.add(p);
		if (list.size() >= 256) {
			sendBurst();
		}
	}
	
	private void sendBurst() {
		
	}

}
