import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.JToggleButton;
import javax.swing.JComboBox;
import javax.swing.JTable;

public class RestPage extends JFrame {

	private JPanel contentPane;
	private Restaurant restaurant;
	User user;
	private JTable table;
	int isVeg;
	String sortBy;

	public static DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {

		ResultSetMetaData metaData = rs.getMetaData();

		// names of columns
		Vector<String> columnNames = new Vector<String>();
		int columnCount = metaData.getColumnCount();
		for (int column = 1; column <= columnCount; column++) {
			columnNames.add(metaData.getColumnName(column));
		}

		// data of the table
		Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		while (rs.next()) {
			Vector<Object> vector = new Vector<Object>();
			for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
				vector.add(rs.getObject(columnIndex));
			}
			data.add(vector);
		}

		DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);
		tableModel.fireTableDataChanged();
		return tableModel;

	}

	/**
	 * Launch the application.
	 */
	/*
	 * public static void main(String[] args) { EventQueue.invokeLater(new
	 * Runnable() { public void run() { try { RestPage frame = new RestPage();
	 * frame.setVisible(true); } catch (Exception e) { e.printStackTrace(); } } });
	 * }
	 */

	/**
	 * Create the frame.
	 */
	public RestPage (User u, Restaurant r, int v, String s){
		user = u;
		isVeg = v;
		sortBy = s;
		restaurant = r;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(500, 100, 450, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JCheckBox chkVeg = new JCheckBox("Veg only");
		chkVeg.setBounds(34, 63, 129, 23);
		contentPane.add(chkVeg);
		
		JLabel lblFilter = new JLabel("Sort By");
		lblFilter.setBounds(220, 39, 58, 15);
		contentPane.add(lblFilter);
		
		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Landing l = new Landing (user, "", "");
				l.setVisible(true);
				dispose();
			}
		});
		btnBack.setBounds(24, 34, 117, 25);
		contentPane.add(btnBack);
		

		String attr [] = {"Price", "Popularity"};
		JComboBox<String> cbFilter = new JComboBox(attr);
		cbFilter.setBounds(220, 62, 143, 24);
		cbFilter.setSelectedIndex(-1);
		contentPane.add(cbFilter);
		
		table = new JTable();
		table.setBounds(55, 187, 331, 254);
		contentPane.add(table);
		
		JButton btnApply = new JButton("Apply");
		btnApply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int tempIsVeg=0;
				String tempSort= cbFilter.getItemAt(cbFilter.getSelectedIndex());
				if (tempSort == null) tempSort = "";
				if (chkVeg.isSelected())
					tempIsVeg = 1;
				RestPage rpnew = new RestPage (user, restaurant, tempIsVeg, tempSort);
				rpnew.setVisible(true);
				dispose();
			}
		});
		btnApply.setBounds(161, 119, 117, 25);
		contentPane.add(btnApply);
		String query="select d_name, price from dish where r_id = " + restaurant.id;
		if (isVeg == 1 && !sortBy.equals ("")) {
			if (sortBy.equals ("Price"))
				query = "select d_name, price from dish where r_id =" +restaurant.id  + " and isVeg = 1 order by " + sortBy;
			//@TODO
			//implement order by popularity
			}
		else 
			if (isVeg == 1)
				query = "select d_name, price from dish where r_id =" +restaurant.id  + " and isVeg = 1";
			else if( !sortBy.equals (""))
				query = "select d_name, price from dish where r_id =" +restaurant.id  + " order by " + sortBy;
		System.out.println (query);
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "SYSTEM", "16181618");
			Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = stmt.executeQuery(query);
			// build table
			table = new JTable(buildTableModel(rs));
			table.setBounds(12, 157, 426, 284);
			contentPane.add(table);
			con.commit();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
	}
}