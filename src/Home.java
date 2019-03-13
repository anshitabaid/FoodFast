import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.sql.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;

public class Home extends JFrame {

	private JPanel contentPane;
	private JTextField txtPhno;
	private JPasswordField txtPassword;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Home frame = new Home();
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
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(12, 24, 426, 271);
		contentPane.add(panel);
		panel.setLayout(null);

		JLabel lblPhno = new JLabel("Phone Number");
		lblPhno.setHorizontalAlignment(SwingConstants.TRAILING);
		lblPhno.setBounds(154, 12, 158, 25);
		panel.add(lblPhno);

		txtPassword = new JPasswordField();
		txtPassword.setBounds(154, 105, 117, 19);
		panel.add(txtPassword);

		txtPhno = new JTextField();
		txtPhno.setBounds(154, 49, 114, 19);
		panel.add(txtPhno);
		txtPhno.setColumns(10);

		JButton btnSearch = new JButton("Login");

		btnSearch.setBounds(154, 171, 117, 25);
		panel.add(btnSearch);

		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(154, 107, 133, 17);
		panel.add(lblPassword);
		
		JLabel lblData = new JLabel("");
		lblData.setBounds(79, 214, 297, 25);
		panel.add(lblData);
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
					Statement stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery("SELECT * FROM users where phone_num = "+phno);
					while (rs.next ()) {
						System.out.println ("Not empty");
						System.out.println (rs.getString("name"));
						//lblData.setText(rs.getString("phone_num") + " " + rs.getString ("name"));
					}
					

					con.commit();
					con.close();

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

}}
