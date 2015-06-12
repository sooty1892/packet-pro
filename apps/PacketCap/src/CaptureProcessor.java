import java.util.List;

/*
 * packet capture processor to be put into affinity threads
 */

public class CaptureProcessor extends PacketProcessor {
	
	PacketFreeer pf_ind;
	ReceivePoller rp_ind;
	
	public CaptureProcessor(PacketSender ps, PacketFreeer pf, ReceivePoller rp) {
		super(ps, pf, rp);
		pf_ind = pf;
		rp_ind = rp;
	}

	private boolean inspect(Packet currentPacket) {
		pf_ind.freePacket(currentPacket);
		return true;
	}

	@Override
	public void run() {
		while (true) {
			List<Packet> packets = rp_ind.getBurst();
			for (Packet p : packets) {
				inspect(p);
			}
		}
	}
	
}

