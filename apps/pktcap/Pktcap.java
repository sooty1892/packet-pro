import java.nio.ByteBuffer;
import java.lang.reflect.Field;

import sun.misc.Unsafe;

public class Pktcap implements Runnable {

	private static ByteBuffer buffer;
	
	public static void main(String[] args) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, InstantiationException {
		System.out.println("JAVA: Starting Java app");
	    buffer = ByteBuffer.allocateDirect(8*1024);
	    //(new Thread(new Pktcap())).start();
	    //(new Thread(new PacketInspector(buffer))).start();
	    if (DpdkAccess.setup_and_conf(buffer) < 0) {
	    	System.out.println("JAVA: Error in DPDK setup ");
	       return;
	    }
	    //System.out.println("JAVA: Starting recieve queue polling");
	    /*while (true) {
	    	buffer = ByteBuffer.allocateDirect(8*1024);
	    	int count = DpdkAccess.get_packets(buffer);
	            if (count > 0) {
	                    System.out.println("JAVA: Received " + count + " packets");
	            }
	    }*/
	
	    byte version_ihl = buffer.get();
	    byte type_of_service = buffer.get();
	    short total_length = buffer.getShort();
	    short packet_id = buffer.getShort();
	    short fragment_offset = buffer.getShort();
	    byte time_to_live = buffer.get();
	    byte next_proto_id = buffer.get();
	    short hdr_checksum = buffer.getShort();
	    int src_addr = buffer.getInt();
	    int dst_addr = buffer.getInt();

	    /*System.out.println((int)version_ihl);
	    System.out.println((int)type_of_service);
	    System.out.println((int)Utils.swap(total_length));
	    System.out.println((int)Utils.swap(packet_id));
	    System.out.println((int)Utils.swap(fragment_offset));
	    System.out.println((int)time_to_live);
	    System.out.println((int)next_proto_id);
	    System.out.println((int)Utils.swap(hdr_checksum));
	    System.out.println((int)Utils.swap(src_addr));
	    System.out.println((int)Utils.swap(dst_addr));*/
	    

        Field f = Unsafe.class.getDeclaredField("theUnsafe");
        f.setAccessible(true);
        Unsafe unsafe = (Unsafe)f.get(null);
       
        Packet p = (Packet)unsafe.allocateInstance(Packet.class);
        
	}
	
	
	
	public void run() {
		if (DpdkAccess.setup_and_conf(buffer) < 0) {
			System.out.println("JAVA: Error in DPDK setup ");
			return;
		}
	}

}
