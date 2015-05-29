import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


public class PacketInspector {
	
	Packet p;
	PacketSender ps;
	PacketFreeer pf;
	Set<Long> blacklist;
	
	public PacketInspector(Packet p) throws IOException {
		this.p = p;
		ps = new PacketSender();
		pf = new PacketFreeer();
		blacklist = new HashSet<Long>();
		readBlacklist();
		printBlacklist();
	}
	
	public PacketInspector() throws IOException {
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
		if (blacklist.contains(p.getSrc_addr())) {
			pf.freePacket(p);
			return false;
		} else {
			ps.sendPacket(p);
			return true;
		}
	}
	
	private void readBlacklist() throws IOException {
		File file = new File("blacklist.txt");
		FileReader fileReader = new FileReader(file);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		String line;
		while ((line = bufferedReader.readLine()) != null) {
			blacklist.add(Utils.IpToInt(line));
		}
		fileReader.close();
	}
	
	private void printBlacklist() {
		System.out.println("Blacklist contains:");
		for (Long l : blacklist) {
			System.out.println(Utils.intToIp(l));
		}
	}
	
}
