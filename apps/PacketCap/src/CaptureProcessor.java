import java.util.List;

/*
 * packet capture processor to be put into affinity threads
 */

public class CaptureProcessor extends PacketProcessor {
	
	public CaptureProcessor(PacketSender ps, PacketFreeer pf, ReceivePoller rp) {
		super(ps, pf, rp);
	}

	private boolean inspect(Packet currentPacket) {
		pf.get(0).freePacket(currentPacket);
		return true;
	}

	@Override
	public void run() {
		while (true) {
			List<Packet> packets = rp.get(0).getBurst();
			for (Packet p : packets) {
				inspect(p);
			}
		}
	}
	
}

