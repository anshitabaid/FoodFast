import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Image;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JComboBox;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTable;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Landing extends JFrame {

	private JPanel contentPane;
	private JTable table;
	private JTextField tfName;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Landing frame = new Landing();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

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

		DefaultTableModel tableModel =  new DefaultTableModel(data, columnNames);
		tableModel.fireTableDataChanged();
		return tableModel;

	}

	/**
	 * Create the frame.
	 */
	public Landing() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(500, 100, 450, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		JButton btnSearch = new JButton("");
		btnSearch.setBounds(215, 91, 40, 40);
		contentPane.add(btnSearch);
		// adding icon to searchbutton
		try {
			Image img = ImageIO.read(getClass().getResource("resources/search.png"));
			Image resizedImage = img.getScaledInstance(btnSearch.getWidth(), btnSearch.getHeight(),
					java.awt.Image.SCALE_SMOOTH);
			btnSearch.setIcon(new ImageIcon(resizedImage));
		} catch (Exception ex) {
			System.out.println(ex);
		}

		JLabel lblCuisine = new JLabel("Search by Cuisine");
		lblCuisine.setBounds(262, 35, 126, 15);
		contentPane.add(lblCuisine);

		JLabel lblSearch = new JLabel("Search by name");
		lblSearch.setBounds(12, 35, 113, 15);
		contentPane.add(lblSearch);

		tfName = new JTextField();
		tfName.setBounds(12, 52, 204, 19);
		contentPane.add(tfName);
		tfName.setColumns(10);

		JComboBox<String> cuisineList = new JComboBox<String>();
		cuisineList.setBounds(262, 49, 156, 24);
		contentPane.add(cuisineList);
		// populate combobox
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "SYSTEM", "16181618");
			Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = stmt.executeQuery("SELECT c_name FROM cuisine");
			while (rs.next()) {
				cuisineList.addItem(rs.getString("c_name"));
			}
			// sets first entry to null
			cuisineList.setSelectedIndex(-1);

		} catch (Exception e) {
			e.printStackTrace();
		}
		// prints list of all restaurants
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "SYSTEM", "16181618");
			Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			try {
				ResultSet rs = stmt.executeQuery("SELECT r_name FROM restaurant");
				table = new JTable(buildTableModel(rs));

				table.setBounds(12, 144, 426, 284);
				contentPane.add(table);

			} catch (Exception e) {
				e.printStackTrace();
			}

			// perform filter
			btnSearch.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					try {
						Class.forName("oracle.jdbc.driver.OracleDriver");
						Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "SYSTEM",
								"16181618");
						Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
								ResultSet.CONCUR_READ_ONLY);
						String query="";
						String name = tfName.getText();
						String cuisine = String.valueOf(cuisineList.getSelectedItem());
						if (name!= null)
							query = "Select r_name from restaurant where upper(r_name) like '" + name.toUpperCase() + "'";
						try {
							ResultSet rs = stmt.executeQuery(query);

							//table = new JTable(buildTableModel(rs));
							table.repaint();

						} catch (Exception e) {
							e.printStackTrace();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});

			table.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					JTable jTable = (JTable) e.getSource();
					int row = jTable.getSelectedRow();
					int column = jTable.getSelectedColumn();
					// extract text of clicked cell
					String valueInCell = (String) jTable.getValueAt(row, column);
					// pass to restaurant page
					RestPage rp = new RestPage(valueInCell);
					rp.setVisible(true);
					dispose();

				}
			});

			con.commit();
			con.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
