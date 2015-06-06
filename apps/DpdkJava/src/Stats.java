import java.util.List;
import java.util.TimerTask;
import java.util.Timer;
import java.awt.*;
import java.awt.event.*;
import java.io.PrintStream;

import javax.swing.*;

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
	
	private JTextField receive_all = null;
	private JTextField receive_gap = null;
	private JTextField send_all = null;
	private JTextField send_gap = null;
	
	
	public Stats(List<ReceivePoller> receivers, List<PacketSender> transmitters, boolean gui) throws InterruptedException {
		this.receivers = receivers;
		this.transmitters = transmitters;
		this.repeat_delay = SECOND;
		this.gui = gui;
	}
	
	public Stats(List<ReceivePoller> receivers, List<PacketSender> transmitters, long repeat_delay, boolean gui) {
		this.receivers = receivers;
		this.transmitters = transmitters;
		this.repeat_delay = repeat_delay;
		this.gui = gui;
	}
	
	public void initGui() {
		GuiFrame frame = new GuiFrame();
		receive_all = frame.getReceiveAll();
		receive_gap = frame.getReceiveGap();
		send_all = frame.getSendAll();
		send_gap = frame.getSendGap();
		PrintStream printStream = new PrintStream(new GuiOutputStream(frame.getConsole()));
        System.setOut(printStream);
        System.setErr(printStream);
	}

	@Override
	public void run() {
		if (gui) {
			initGui();
		}
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
				receive_all.setText(received_packet_total + " / " + received_packet_total_size);
				receive_gap.setText(received_packet_interval + " / " + received_packet_interval_size);
				send_all.setText(transmitted_packet_total + " / " + transmitted_packet_total_size);
				send_gap.setText(transmitted_packet_interval + " / " + transmitted_Packet_interval_size);
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
