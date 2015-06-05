public class AffinityThread extends Thread {
	
	private boolean worked;
	private int avail;
	private PacketProcessor pp = null;
	private PacketSender ps = null;
	private PacketFreeer pf = null;
	private ReceivePoller rp = null;

	public AffinityThread(Runnable r, int core, int avail) {
		super(r);
		worked = DpdkAccess.set_thread_affinity(core, avail);
		this.avail = avail;
	}
	
	public AffinityThread(Runnable r, PacketProcessor pp, int core, int avail) {
		super(r);
		worked = DpdkAccess.set_thread_affinity(core, avail);
		this.avail = avail;
		this.pp = pp;
	}
	
	public void setPacketProcessor(PacketProcessor pp) {
		this.pp = pp;
	}
	
	public void setPacketSender(PacketSender ps) {
		this.ps = ps;
	}
	
	public void setPacketFreeer(PacketFreeer pf) {
		this.pf = pf;
	}
	
	public void setReveivePoller(ReceivePoller rp) {
		this.rp = rp;
	}
	
	public boolean setNewAffinity(int core) {
		worked = DpdkAccess.set_thread_affinity(core, avail);
		return worked;
	}
	
	public boolean ifWorked() {
		return worked;
	}
	
	@Override
	public void start() {
		if (pp == null) {
			System.out.println("THREAD NOT STARTED - no pp");
		} else {
			super.start();
		}
	}

}