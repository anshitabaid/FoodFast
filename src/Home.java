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

public class Home extends JFrame {

	private JPanel contentPane;
	private JTextField txtUID;
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
		
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setBounds(154, 12, 158, 25);
		panel.add(lblUsername);
		
		txtPassword = new JPasswordField();
		txtPassword.setBounds(154, 105, 117, 19);
		panel.add(txtPassword);
		
		txtUID = new JTextField();
		txtUID.setBounds(154, 49, 114, 19);
		panel.add(txtUID);
		txtUID.setColumns(10);
		
			JButton btnSearch = new JButton("Login");
			
					btnSearch.setBounds(154, 171, 117, 25);
					panel.add(btnSearch);
					
					JLabel lblPassword = new JLabel("Password");
					lblPassword.setBounds(154, 107, 133, 17);
					panel.add(lblPassword);
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					String uid = txtUID.getText();
					String password = txtPassword.getText ();
					//Get driver class
					Class.forName("oracle.jdbc.driver.OracleDriver");
					//step2 create the connection object			
			        Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "SYSTEM", "16181618");
			        //create statement
			        Statement stmt = con.createStatement();
			        ResultSet rs = stmt.executeQuery("SELECT * FROM users where uid = '" );
			        
			        con.commit();
			        con.close();  
			        
			        	
				} catch (Exception e) {
					e.printStackTrace();
				}			
		        
			}
		});
		
	
	}
}
