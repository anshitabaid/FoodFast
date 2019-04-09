import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.sql.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JDialog;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;
import javax.swing.AbstractAction;
import javax.swing.Action;

public class Home extends JFrame {

	private JPanel contentPane;
	private JTextField txtPhno;
	private JPasswordField txtPassword;
	protected User user;
	static Home frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new Home();
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
	public Home() {
		getContentPane().setLayout(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(500, 100, 450, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(12, 24, 426, 464);
		contentPane.add(panel);
		panel.setLayout(null);

		JLabel lblMessage = new JLabel("");
		lblMessage.setHorizontalAlignment(SwingConstants.CENTER);
		lblMessage.setBounds(103, 212, 237, 15);
		panel.add(lblMessage);

		txtPassword = new JPasswordField();
		txtPassword.setBounds(154, 105, 117, 19);
		panel.add(txtPassword);

		JLabel lblPhno = new JLabel("Phone Number");
		lblPhno.setHorizontalAlignment(SwingConstants.LEFT);
		lblPhno.setBounds(154, 12, 158, 25);
		panel.add(lblPhno);

		txtPhno = new JTextField();
		txtPhno.setBounds(154, 49, 114, 19);
		panel.add(txtPhno);
		txtPhno.setColumns(10);

		JButton btnSearch = new JButton("Login");

		btnSearch.setBounds(154, 171, 117, 25);
		panel.add(btnSearch);

		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(154, 76, 133, 17);
		panel.add(lblPassword);

		JButton btnSignUp = new JButton("Sign Up");
		btnSignUp.setBounds(154, 239, 117, 25);
		panel.add(btnSignUp);
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					String phno = txtPhno.getText();
					String password = String.valueOf(txtPassword.getPassword());
					// Get driver class
					Class.forName("oracle.jdbc.driver.OracleDriver");
					// step2 create the connection object
					Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "SYSTEM",
							"16181618");
					// create statement
					Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
					try {
						ResultSet rs = stmt.executeQuery(
								"SELECT * FROM users where phone_num = " + phno + " and password = '" + password + "'");
						if (!rs.next()) {
							lblMessage.setText("Incorrect credentials");
							/*
							JDialog d = new JDialog(frame, "dialog Box");

							// create a label
							JLabel l = new JLabel("Incorrect Credentials!");
							d.getContentPane().add(l);
							d.setBounds(625, 150, 200, 200);
							d.setVisible(true);
							*/
						} else {
							// reset to first row
							rs.beforeFirst();
							while (rs.next()) {
								// set instance
								user = new User(rs.getString("phone_num"), rs.getString("name"),
										rs.getString("address"));
								Landing l = new Landing();
								l.setVisible(true);
								dispose();
								// setVisible (false);
							}
						}
					} catch (SQLException e) {
						lblMessage.setText("Incorrect format");
					}

					con.commit();
					con.close();

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

	}
}