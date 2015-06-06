/*
 * Abstract class to for packet processors to be created off
 */

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
