import java.util.ArrayList;
import java.util.List;

/*
 * Abstract class to for packet processors to be created off
 */

public abstract class PacketProcessor extends CoreThread {
	
	List<PacketSender> ps;
	List<PacketFreeer> pf;
	List<ReceivePoller> rp;
	
	public PacketProcessor(PacketSender ps, PacketFreeer pf, ReceivePoller rp) {
		this.ps = new ArrayList<PacketSender>();
		this.pf = new ArrayList<PacketFreeer>();
		this.rp = new ArrayList<ReceivePoller>();
		this.ps.add(ps);
		this.pf.add(pf);
		this.rp.add(rp);
	}
	
	public PacketProcessor() {
		this.ps = new ArrayList<PacketSender>();
		this.pf = new ArrayList<PacketFreeer>();
		this.rp = new ArrayList<ReceivePoller>();
	}
	
	public PacketProcessor(List<PacketSender> ps, List<PacketFreeer> pf, List<ReceivePoller> rp) {
		this.ps = ps;
		this.pf = pf;
		this.rp = rp;
	}
	
	public void addPacketSender(PacketSender ps) {
		if (!this.ps.contains(ps)) {
			this.ps.add(ps);
		}
	}
	
	public void addPacketFreeer(PacketFreeer pf) {
		if (!this.pf.contains(pf)) {
			this.pf.add(pf);
		}
	}
	
	public void setReceivePoller(ReceivePoller rp) {
		if (!this.rp.contains(rp)) {
			this.rp.add(rp);
		}
	}
	
	public List<PacketSender> getPacketSenders() {
		return ps;
	}
	
	public List<PacketFreeer> getPacketFreeers() {
		return pf;
	}
	
	public List<ReceivePoller> getReceivePollers() {
		return rp;
	}
	
}
