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
	
	public boolean inspectPacket() {
		System.out.println("JAVA: Inspecting packet from " + Utils.intToIp(p.getSrc_addr()));
		/*if (blacklist.contains(p.getSrc_addr())) {
			pf.freePacket(p);
			return false;
		} else {
			ps.sendPacket(p);
			return true;
		}*/
		pf.freePacket(p);
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
