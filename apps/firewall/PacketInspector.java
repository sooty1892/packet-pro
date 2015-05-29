
public class PacketInspector {
	
	Packet p;
	
	public PacketInspector(Packet p) {
		this.p = p;
	}
	
	public PacketInspector() {
		
	}
	
	public Packet getPacket() {
		return p;
	}
	
	public void setPacket(Packet p) {
		this.p = p;
	}
	
	public boolean inspectPacket() {
		System.out.println("JAVA: Inspecting packet from " + Utils.intToIp(p.getSrc_addr()));
		return false;
	}
	
}
