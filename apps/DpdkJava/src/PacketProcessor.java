import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


public abstract class PacketProcessor extends CoreThread {
	
	PacketSender ps;
	PacketFreeer pf;
	ReceivePoller rp;
	
	public PacketProcessor(PacketSender ps, PacketFreeer pf, ReceivePoller rp) {
		this.ps = ps;
		this.pf = pf;
		this.rp = rp;
	}
	
	public void setPacketSender(PacketSender ps) {
		this.ps = ps;
	}
	
	public void setPacketFreeer(PacketFreeer pf) {
		this.pf = pf;
	}
	
	public void setReceivePoller(ReceivePoller rp) {
		this.rp = rp;
	}
	
	public PacketSender getPacketSender() {
		return ps;
	}
	
	public PacketFreeer getPacketFreeer() {
		return pf;
	}
	
	public ReceivePoller getReceivePoller() {
		return rp;
	}
	
}
