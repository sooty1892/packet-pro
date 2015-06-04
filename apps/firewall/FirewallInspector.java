import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


public class FirewallInspector extends PacketInspector {
	
	Set<Long> blacklist;
	
	public FirewallInspector(PacketSender ps, PacketFreeer pf) {
		super(ps, pf);
		blacklist = new HashSet<Long>();
		readBlacklist();
		printBlacklist();
	}
	
	@Override
	public boolean inspectCurrentPacket() {
		return inspect();
	}
	
	@Override
	public boolean inspectNewPacket(Packet p) {
		this.currentPacket = p;
		return inspect();
	}

	private boolean inspect() {
		int version = currentPacket.whichIP();
		assert(version == 4 || version == 6);
		
		if (version == 4) {
			Ipv4Packet cp = (Ipv4Packet)currentPacket;
			if (blacklist.contains(cp.getSrcAddr())) {
				pf.freePacket(currentPacket);
				return false;
			} else {
				//ps.sendPacket(p);
				pf.freePacket(currentPacket);
				return true;
			}
		} else {
			//Ipv6Packet cp = (Ipv6Packet)p;
			//System.out.println("NOT HANDLING IPv6 ATM");
			pf.freePacket(currentPacket);
			return false;
		}
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
				System.out.println("IO EXCEPTION");
				e1.printStackTrace();
			}
			try {
				fileReader.close();
			} catch (IOException e) {
				System.out.println("IO EXCEPTION");
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			System.out.println("FILE NOT FOUND EXCEPTION");
			e.printStackTrace();
		}
	}
	
	private void printBlacklist() {
		System.out.println("Blacklist contains:");
		for (Long l : blacklist) {
			System.out.println(Utils.intToIp(l));
		}
	}
	
}
