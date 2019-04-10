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
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.JToggleButton;
import javax.swing.JComboBox;
import javax.swing.JTable;

public class RestPage extends JFrame {

	private JPanel contentPane;
	private String r_name;
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
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RestPage frame = new RestPage();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	*/

	/**
	 * Create the frame.
	 */
	public RestPage (User u, String r_name, int v, String s){
		user = u;
		isVeg = v;
		sortBy = s;
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
		JComboBox <String> cbFilter = new JComboBox<String>(attr);
		cbFilter.setBounds(220, 62, 143, 24);
		cbFilter.setSelectedIndex(-1);
		contentPane.add(cbFilter);
		
		table = new JTable();
		table.setBounds(55, 187, 331, 254);
		contentPane.add(table);
		
		JButton btnApply = new JButton("Apply");
		btnApply.setBounds(152, 139, 117, 25);
		contentPane.add(btnApply);
		
		//populate table with all dishes
		//if (isVeg == 0 && sortBy.equals("")) {
			//query = "select "
		}
		
	}
