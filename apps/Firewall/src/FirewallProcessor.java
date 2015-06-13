import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
 * firewall processor to be put into affinity threads
 */

public class FirewallProcessor extends PacketProcessor {
	
	Set<Long> blacklist;
	
	public FirewallProcessor(PacketSender ps, PacketFreeer pf, ReceivePoller rp) {
		super(ps, pf, rp);
		blacklist = new HashSet<Long>();
		readBlacklist();
		//printBlacklist();
	}

	private boolean inspect(Packet currentPacket) {
		int version = currentPacket.whichIP();

		if (version == 4) {
			Ipv4Packet cp = (Ipv4Packet)currentPacket;
			if (blacklist.contains(cp.getSrcAddr())) {
				pf.get(0).freePacket(currentPacket);
			} else {
				ps.get(0).sendPacket(currentPacket);
				return true;
			}
		} else if (version == 6) {
			//System.out.println("Not handling IPv6 - skipping and freeing");
			pf.get(0).freePacket(currentPacket);
		} else {
			//System.out.println("Packet received where version isn't 4 or 6 - skipping and freeing");
			pf.get(0).freePacket(currentPacket);
		}
		return false;
	}
	
	private void readBlacklist() {
		File file = new File("blacklist.txt");
		FileReader fileReader;
		try {
			fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line;
			try {
				while ((line = bufferedReader.readLine()) != null) {
					blacklist.add(Utils.IpToInt(line));
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try {
				fileReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unused")
	private void printBlacklist() {
		System.out.println("Blacklist contains:");
		for (Long l : blacklist) {
			System.out.println(Utils.intToIp(l));
		}
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
