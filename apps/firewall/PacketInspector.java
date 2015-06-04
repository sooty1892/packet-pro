import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


public abstract class PacketInspector {
	
	Packet currentPacket;
	PacketSender ps;
	PacketFreeer pf;
	
	public PacketInspector(PacketSender ps, PacketFreeer pf) {
		this.ps = ps;
		this.pf = pf;
	}
	
	public Packet getCurrentPacket() {
		return currentPacket;
	}
	
	public void setCurrentPacket(Packet p) {
		this.currentPacket = p;
	}
	
	public abstract boolean inspectCurrentPacket();
	public abstract boolean inspectNewPacket(Packet newPacket);
	
}
