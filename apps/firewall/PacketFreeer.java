import java.util.ArrayList;
import java.util.List;


// How to spell free-er is the real question here
public class PacketFreeer {
	
	private List<Packet> list;
	
	public PacketFreeer() {
		list = new ArrayList<Packet>();
	}
	
	public void freePacket(Packet p) {
		list.add(p);
		if (list.size() >= 256) {
			freeBurst();
		}
	}
	
	private void freeBurst() {
		
	}

}
