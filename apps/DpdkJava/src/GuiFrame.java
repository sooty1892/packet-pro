import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.PrintStream;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;

public class GuiFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTextArea console;
	private JTextField receive_all_count;
	private JTextField receive_all_size;
	private JTextField receive_gap_count;
	private JTextField receive_gap_size;
	private JTextField send_all_count;
	private JTextField send_all_size;
	private JTextField send_gap_count;
	private JTextField send_gap_size;
	
	public JTextArea getConsole() {
		return console;
	}
	
	public JTextField getReceive_all_count() {
		return receive_all_count;
	}
	
	public JTextField getReceive_all_size() {
		return receive_all_size;
	}

	public JTextField getReceive_gap_count() {
		return receive_gap_count;
	}

	public JTextField getReceive_gap_size() {
		return receive_gap_size;
	}

	public JTextField getSend_all_count() {
		return send_all_count;
	}

	public JTextField getSend_all_size() {
		return send_all_size;
	}

	public JTextField getSend_gap_count() {
		return send_gap_count;
	}

	public JTextField getSend_gap_size() {
		return send_gap_size;
	}

	public GuiFrame() {
		setTitle("DPDK Info");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		
		console = new JTextArea();
		console.setEditable(false);
		
		JScrollPane scroll = new JScrollPane(console);
		scroll.setSize(680, 200);
		scroll.setLocation(10, 10);
		getContentPane().add(scroll);
		
		receive_all_count = new JTextField();
		receive_all_count.setEditable(false);
		receive_all_count.setBounds(170, 250, 150, 30);
		getContentPane().add(receive_all_count);
		receive_all_count.setColumns(10);
		
		receive_gap_count = new JTextField();
		receive_gap_count.setEditable(false);
		receive_gap_count.setBounds(170, 300, 150, 30);
		getContentPane().add(receive_gap_count);
		receive_gap_count.setColumns(10);
		
		send_all_count = new JTextField();
		send_all_count.setEditable(false);
		send_all_count.setBounds(170, 350, 150, 30);
		getContentPane().add(send_all_count);
		send_all_count.setColumns(10);
		
		send_gap_count = new JTextField();
		send_gap_count.setEditable(false);
		send_gap_count.setBounds(170, 400, 150, 30);
		getContentPane().add(send_gap_count);
		send_gap_count.setColumns(10);
		
		receive_all_size = new JTextField();
		receive_all_size.setEditable(false);
		receive_all_size.setColumns(10);
		receive_all_size.setBounds(540, 250, 150, 30);
		getContentPane().add(receive_all_size);
		
		receive_gap_size = new JTextField();
		receive_gap_size.setEditable(false);
		receive_gap_size.setColumns(10);
		receive_gap_size.setBounds(540, 300, 150, 30);
		getContentPane().add(receive_gap_size);
		
		send_all_size = new JTextField();
		send_all_size.setEditable(false);
		send_all_size.setColumns(10);
		send_all_size.setBounds(540, 350, 150, 30);
		getContentPane().add(send_all_size);
		
		send_gap_size = new JTextField();
		send_gap_size.setEditable(false);
		send_gap_size.setColumns(10);
		send_gap_size.setBounds(540, 400, 150, 30);
		getContentPane().add(send_gap_size);
		
		JLabel label1 = new JLabel("Receive All Count:");
		label1.setBounds(10, 250, 150, 30);
		getContentPane().add(label1);
		
		JLabel label2 = new JLabel("Receive Gap Count:");
		label2.setBounds(10, 300, 150, 30);
		getContentPane().add(label2);
		
		JLabel label3 = new JLabel("Send All Count:");
		label3.setBounds(10, 350, 150, 30);
		getContentPane().add(label3);
		
		JLabel label4 = new JLabel("Send Gap Count:");
		label4.setBounds(10, 400, 150, 30);
		getContentPane().add(label4);
		
		JLabel label5 = new JLabel("Receive All Size:");
		label5.setBounds(380, 250, 150, 30);
		getContentPane().add(label5);
		
		JLabel label6 = new JLabel("Receive Gap Size:");
		label6.setBounds(380, 300, 150, 30);
		getContentPane().add(label6);
		
		JLabel label7 = new JLabel("Send All Size:");
		label7.setBounds(380, 350, 150, 30);
		getContentPane().add(label7);
		
		JLabel label8 = new JLabel("Send Gap Size:");
		label8.setBounds(380, 400, 150, 30);
		getContentPane().add(label8);
		
		setSize(700,500);
		setVisible(true);
		
	}
}
