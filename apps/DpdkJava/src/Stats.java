import java.util.List;
import java.util.TimerTask;
import java.util.Timer;
import java.io.OutputStream;
import java.io.PrintStream;
import javax.swing.*;

/*
 * Class to collect all stats together and updates data in console or gui
 * at given time period
 */

public class Stats implements Runnable {
	
	private static final long SECOND = 1000;
	private List<ReceivePoller> receivers = null;
	private List<PacketSender> transmitters = null;
	private long repeat_delay = 0;
	private long received_packet_total = 0;
	private long received_packet_total_size = 0;
	private long transmitted_packet_total = 0;
	private long transmitted_packet_total_size = 0;
	boolean gui = false;
	
	private JTextField receive_all_count = null;
	private JTextField receive_all_size = null;
	private JTextField receive_gap_count = null;
	private JTextField receive_gap_size = null;
	private JTextField send_all_count = null;
	private JTextField send_all_size = null;
	private JTextField send_gap_count = null;
	private JTextField send_gap_size = null;
	
	public void setReceivers(List<ReceivePoller> list) {
		this.receivers = list;
	}
	
	public void setTransmitters(List<PacketSender> list) {
		this.transmitters = list;
	}
	
	public Stats(List<ReceivePoller> receivers, List<PacketSender> transmitters, boolean gui) {
		this.receivers = receivers;
		this.transmitters = transmitters;
		this.repeat_delay = SECOND;
		this.gui = gui;
		if (gui) {
			initGui();
		}
	}
	
	public Stats(List<ReceivePoller> receivers, List<PacketSender> transmitters, long repeat_delay, boolean gui) {
		this.receivers = receivers;
		this.transmitters = transmitters;
		this.repeat_delay = repeat_delay;
		this.gui = gui;
		if (gui) {
			initGui();
		}
	}
	
	public Stats(boolean gui) {
		this.repeat_delay = SECOND;
		this.gui = gui;
		if (gui) {
			initGui();
		}
	}
	
	public void initGui() {
		GuiFrame frame = new GuiFrame();
		receive_all_count = frame.getReceive_all_count();
		receive_all_size = frame.getReceive_all_size();
		receive_gap_count = frame.getReceive_gap_count();
		receive_gap_size = frame.getReceive_gap_size();
		send_all_count = frame.getSend_all_count();
		send_all_size = frame.getSend_all_size();
		send_gap_count = frame.getSend_gap_count();
		send_gap_size = frame.getSend_gap_size();
		OutputStream old = System.out;
		PrintStream printStream = new PrintStream(new MultiStream(old, new GuiOutputStream(frame.getConsole())));
        System.setOut(printStream);
        System.setErr(printStream);
	}

	@Override
	public void run() {
		Timer timer = new Timer();
		timer.schedule(new CollectStats(), repeat_delay, repeat_delay);  
	}
	
	private class CollectStats extends TimerTask {

		@Override
		public void run() {
			long transmitted_packet_interval = 0;
			long transmitted_Packet_interval_size = 0;
			long received_packet_interval = 0;
			long received_packet_interval_size = 0;
			
			for (PacketSender ps : transmitters) {
				transmitted_packet_interval += ps.getPacketInterval();
				transmitted_Packet_interval_size += ps.getPacketIntervalSize();
				ps.resetInterval();
			}
			
			for (ReceivePoller rp : receivers) {
				received_packet_interval += rp.getPacketInterval();
				received_packet_interval_size += rp.getPacketIntervalSize();
				rp.resetInterval();
			}
			
			received_packet_total += received_packet_interval;
			received_packet_total_size += received_packet_interval_size;
			transmitted_packet_total += transmitted_packet_interval;
			transmitted_packet_total_size += transmitted_Packet_interval_size;
			
			if (gui) {
				receive_all_count.setText(String.valueOf(received_packet_total));
				receive_all_size.setText(String.valueOf(received_packet_total_size));
				receive_gap_count.setText(String.valueOf(received_packet_interval));
				receive_gap_size.setText(String.valueOf(received_packet_interval_size));
				send_all_count.setText(String.valueOf(transmitted_packet_total));
				send_all_size.setText(String.valueOf(transmitted_packet_total_size));
				send_gap_count.setText(String.valueOf(transmitted_packet_interval));
				send_gap_size.setText(String.valueOf(transmitted_Packet_interval_size));
			} else {
				System.out.println();
				System.out.println("TRANSMITTED INTERVAL COUNT/SIZE: " + transmitted_packet_interval + " / " + transmitted_Packet_interval_size);
				System.out.println("TRANSMITTED TOTAL COUNT/SIZE: " + transmitted_packet_total + " / " + transmitted_packet_total_size);
				System.out.println("RECEIVED INTERVAL COUNT/SIZE: " + received_packet_interval + " / " + received_packet_interval_size);
				System.out.println("RECEIVED TOTAL COUNT/SIZE: " + received_packet_total + " / " + received_packet_total_size);
				System.out.println();
			}

		}
		
	}

}
