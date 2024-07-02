package hella;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.util.*;

public class Admin extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Admin frame = new Admin();
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	*/
	public Admin() {
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 796, 611);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon("D:\\dsa_project\\student.jpg"));
		lblNewLabel.setBounds(62, 79, 449, 413);
		contentPane.add(lblNewLabel);
		     
		JButton add_staff = new JButton("Add Task Page");
		add_staff.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				setVisible(false);
				new TaskManagementGUI().setVisible(true);
			}
		});
		add_staff.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		add_staff.setForeground(new Color(0, 0, 255));
		add_staff.setFont(new Font("Times New Roman", Font.BOLD, 16));
		add_staff.setBounds(562, 96, 171, 59);
		contentPane.add(add_staff);
		
		JButton add_recurring = new JButton("Add Recurring Task");
		add_recurring.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				new RecurringTasks().setVisible(true);
			}
		});
		add_recurring.setForeground(Color.BLUE);
		add_recurring.setFont(new Font("Times New Roman", Font.BOLD, 16));
		add_recurring.setBounds(562, 190, 171, 59);
		contentPane.add(add_recurring);
		
		JButton add_fare = new JButton("Logout");
		add_fare.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				new login().setVisible(true);
			}
		});
		add_fare.setForeground(Color.BLUE);
		add_fare.setFont(new Font("Times New Roman", Font.BOLD, 16));
		add_fare.setBounds(562, 288, 171, 59);
		contentPane.add(add_fare);
//		
//		JButton log_out = new JButton("Log out");
//		log_out.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseClicked(MouseEvent e) {
//				setVisible(false);
//				new login().setVisible(true);
//				 
//			}
//		});
//		log_out.setForeground(Color.BLUE);
//		log_out.setFont(new Font("Times New Roman", Font.BOLD, 16));
//		log_out.setBounds(562, 384, 171, 59);
//		contentPane.add(log_out);
//		
		JLabel lblWelcomeToAdmin = new JLabel("WELCOME TO STUDENT TASK MANAGEMENT SYSTEM");
		lblWelcomeToAdmin.setHorizontalAlignment(SwingConstants.CENTER);
		lblWelcomeToAdmin.setForeground(Color.BLUE);
		lblWelcomeToAdmin.setFont(new Font("Times New Roman", Font.BOLD, 21));
		lblWelcomeToAdmin.setBounds(80, 10, 639, 82);
		contentPane.add(lblWelcomeToAdmin);
	}
}
