import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Profile extends JFrame {
	User user;
	Landing landing;
	Restaurant restaurant;
	private JPanel contentPane;

	/**
	 * public static void main(String[] args) { EventQueue.invokeLater(new
	 * Runnable() { public void run() { try { Profile frame = new Profile();
	 * frame.setVisible(true); } catch (Exception e) { e.printStackTrace(); } } });
	 * }
	 */
	/**
	 * Create the frame.
	 */
	public Profile(User u, Landing l, Restaurant r) {
		user = u;
		landing = l;
		restaurant = r;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(500, 100, 450, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblHead = new JLabel("");
		lblHead.setFont(new Font("Dialog", Font.BOLD, 16));
		lblHead.setBounds(32, 68, 206, 37);
		contentPane.add(lblHead);
		lblHead.setText("Welcome " + user.name);

		JLabel lblPhNo = new JLabel("");
		lblPhNo.setBounds(32, 132, 296, 15);
		contentPane.add(lblPhNo);
		lblPhNo.setText("Phone number: " + user.phone_num);

		JLabel lblAddress = new JLabel("");
		lblAddress.setVerticalAlignment(SwingConstants.TOP);
		lblAddress.setBounds(32, 178, 296, 140);
		contentPane.add(lblAddress);
		lblAddress.setText("Address: " + user.address);
		
		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (landing ==  null)
				{
					Landing l = new Landing (user, "", "");
					l.setVisible(true);
				}
				else 
				{
					RestPage rp = new RestPage (user, restaurant, landing, 0, "");
					rp.setVisible(true);
				}
				dispose ();
			}
		});
		btnBack.setBounds(32, 12, 67, 25);
		contentPane.add(btnBack);
	}
}
