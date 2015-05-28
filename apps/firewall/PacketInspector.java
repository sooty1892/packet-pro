
public class PacketInspector {
	
	Packet p;
	
	public PacketInspector(Packet p) {
		this.p = p;
	}
	
	public PacketInspector() {
		
	}
	
	public boolean inspectNewPacket(Packet newPacket) {
		p = newPacket;
		return false;
	}
	
}
