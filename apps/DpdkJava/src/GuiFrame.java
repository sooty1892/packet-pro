import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.PrintStream;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;

public class GuiFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTextField receive_all;
	private JTextField receive_gap;
	private JTextField send_all;
	private JTextField send_gap;
	private JTextArea console;
	
	public JTextArea getConsole() {
		return console;
	}
	
	public JTextField getReceiveAll() {
		return receive_all;
	}
	
	public JTextField getReceiveGap() {
		return receive_gap;
	}
	
	public JTextField getSendAll() {
		return send_all;
	}
	
	public JTextField getSendGap() {
		return send_gap;
	}

	public GuiFrame() {
		setTitle("DPDK Info");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		
		console = new JTextArea();
		console.setEditable(false);
		console.setBounds(6, 6, 206, 266);
		getContentPane().add(console);
		
		receive_all = new JTextField();
		receive_all.setEditable(false);
		receive_all.setBounds(259, 26, 134, 28);
		getContentPane().add(receive_all);
		receive_all.setColumns(10);
		
		receive_gap = new JTextField();
		receive_gap.setEditable(false);
		receive_gap.setBounds(259, 86, 134, 28);
		getContentPane().add(receive_gap);
		receive_gap.setColumns(10);
		
		send_all = new JTextField();
		send_all.setEditable(false);
		send_all.setBounds(259, 144, 134, 28);
		getContentPane().add(send_all);
		send_all.setColumns(10);
		
		send_gap = new JTextField();
		send_gap.setEditable(false);
		send_gap.setBounds(259, 203, 134, 28);
		getContentPane().add(send_gap);
		send_gap.setColumns(10);
		
		setSize(700,700);
		setVisible(true);
		
	}
}
