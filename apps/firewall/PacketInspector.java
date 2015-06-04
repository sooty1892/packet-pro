import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


public class PacketInspector {
	
	Packet p;
	PacketSender ps;
	PacketFreeer pf;
	Set<Long> blacklist;
	
	public PacketInspector(Packet p) {
		this.p = p;
		ps = new PacketSender();
		pf = new PacketFreeer();
		blacklist = new HashSet<Long>();
		readBlacklist();
		printBlacklist();
	}
	
	public PacketInspector() {
		p = null;
		ps = new PacketSender();
		pf = new PacketFreeer();
		blacklist = new HashSet<Long>();
		readBlacklist();
		printBlacklist();
	}
	
	public Packet getPacket() {
		return p;
	}
	
	public void setPacket(Packet p) {
		this.p = p;
	}
	
	private boolean inspect() {
		int version = p.whichIP();
		assert(version == 4 || version == 6);
		
		if (version == 4) {
			Ipv4Packet cp = (Ipv4Packet)p;
			if (blacklist.contains(cp.getSrcAddr())) {
				pf.freePacket(p);
				return false;
			} else {
				//ps.sendPacket(p);
				pf.freePacket(p);
				return true;
			}
		} else {
			//Ipv6Packet cp = (Ipv6Packet)p;
			//System.out.println("NOT HANDLING IPv6 ATM");
			pf.freePacket(p);
			return false;
		}
		
	}
	
	public boolean inspectCurrentPacket() {
		return inspect();
	}
	
	public boolean inspectNewPacket(Packet p) {
		this.p = p;
		return inspect();
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
