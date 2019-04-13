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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
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
	String valueInCell;
	Landing landing;

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
	public RestPage(User u, Restaurant r, Landing l, int v, String s) {
		user = u;
		isVeg = v;
		sortBy = s;
		restaurant = r;
		landing = l;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(500, 100, 450, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JCheckBox chkVeg = new JCheckBox("Veg only");
		chkVeg.setBounds(24, 20, 129, 23);
		contentPane.add(chkVeg);

		JLabel lblFilter = new JLabel("Sort By");
		lblFilter.setBounds(220, 39, 58, 15);
		contentPane.add(lblFilter);

		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Landing l = new Landing(user, "", "");
				l.setVisible(true);
				dispose();
			}
		});
		btnBack.setBounds(24, 91, 117, 25);
		contentPane.add(btnBack);

		String attr[] = { "Price" };
		JComboBox<String> cbFilter = new JComboBox(attr);
		cbFilter.setBounds(220, 62, 143, 24);
		cbFilter.setSelectedIndex(-1);
		contentPane.add(cbFilter);

		JButton btnApply = new JButton("Apply");
		btnApply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int tempIsVeg = 0;
				String tempSort = cbFilter.getItemAt(cbFilter.getSelectedIndex());
				if (tempSort == null)
					tempSort = "";
				if (chkVeg.isSelected())
					tempIsVeg = 1;
				RestPage rpnew = new RestPage(user, restaurant, landing, tempIsVeg, tempSort);
				rpnew.setVisible(true);
				dispose();
			}
		});
		btnApply.setBounds(161, 119, 117, 25);
		contentPane.add(btnApply);
		String query = "select d_name, price from dish where r_id = " + restaurant.id;
		if (isVeg == 1 && !sortBy.equals("")) {
			if (sortBy.equals("Price"))
				query = "select d_name, price from dish where r_id =" + restaurant.id + " and isVeg = 1 order by "
						+ sortBy;
			// @TODO
			// implement order by popularity
		} else if (isVeg == 1)
			query = "select d_name, price from dish where r_id =" + restaurant.id + " and isVeg = 1";
		else if (!sortBy.equals(""))
			query = "select d_name, price from dish where r_id =" + restaurant.id + " order by " + sortBy;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "SYSTEM", "16181618");
			Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = stmt.executeQuery(query);
			// build table
			table = new JTable(buildTableModel(rs));
			table.setBounds(12, 157, 426, 144);
			contentPane.add(table);

			JButton btnRemove = new JButton("Remove seleced item");
			btnRemove.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (valueInCell!= null && landing.order.isEmpty()==false) {
						
						//check if exists in arraylist, if it does, delete it
						int i;
						for (i=0; i< landing.order.size(); i++)
							if (landing.order.get(i).d_name.compareToIgnoreCase(valueInCell)==0) 
								break;
						if (i < landing.order.size ())
						{
							if (landing.order.get(i).quantity == 1)
								landing.order.remove(i);
							else {
								Order otemp = landing.order.get(i);
								otemp.quantity--;
								landing.order.set(i, otemp);
							}
								
						}
						
					}
				}
			});
			btnRemove.setBounds(24, 337, 183, 25);
			contentPane.add(btnRemove);

			JButton btnAdd = new JButton("Add selected item");
			btnAdd.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (valueInCell != null) {
						Order otemp;
						if (landing.order.isEmpty()) {
							// add first item to arraylist
							try {
								Class.forName("oracle.jdbc.driver.OracleDriver");
								Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe",
										"SYSTEM", "16181618");
								Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
										ResultSet.CONCUR_READ_ONLY);
								ResultSet rs = stmt
										.executeQuery("select * from dish where d_name = '" + valueInCell + "'");
								if (rs.next()) {
									otemp = new Order(1, rs.getInt("d_id"), rs.getString("d_name"), 1,
											rs.getInt("price"));
									landing.order.add(otemp);
								}

								con.commit();
								con.close();
							} catch (Exception ex) {
								ex.printStackTrace();
							}

						} else {
							// check if it exists in arraylist
							// if it does then increase the quantity and total
							// else add to list
							boolean exists = false;
							for (int i = 0; i < landing.order.size(); i++) {
								if (landing.order.get(i).d_name.compareToIgnoreCase(valueInCell) == 0) {
									// exists
									otemp = landing.order.get(i);
									otemp.quantity += 1;
									otemp.total += otemp.perprice;
									landing.order.set(i, otemp);
									exists = true;
									break;
								}
							}
							if (!exists) {
								// find last index, increment and add new entry
								int lastIndex = landing.order.get(landing.order.size() - 1).slNo;
								try {
									Class.forName("oracle.jdbc.driver.OracleDriver");
									Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe",
											"SYSTEM", "16181618");
									Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
											ResultSet.CONCUR_READ_ONLY);
									ResultSet rs = stmt
											.executeQuery("select * from dish where d_name = '" + valueInCell + "'");
									if (rs.next()) {
										otemp = new Order(lastIndex + 1, rs.getInt("d_id"), rs.getString("d_name"), 1,

												rs.getInt("price"));
										landing.order.add(otemp);
									}
									con.commit();
									con.close();
								} catch (Exception ex) {
									ex.printStackTrace();
								}

							}
						}

					}
				}
			});
			btnAdd.setBounds(244, 337, 161, 25);
			contentPane.add(btnAdd);
			
			JButton btnCart = new JButton("Cart");
			btnCart.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Cart c = new Cart (user, landing, restaurant);
					c.setVisible (true);
					dispose ();
				}
			});
			btnCart.setBounds(161, 463, 117, 25);
			contentPane.add(btnCart);

			JButton btnProfile = new JButton("Profile");
			btnProfile.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					Profile p = new Profile (user, landing, restaurant);
					p.setVisible(true);
					dispose ();
				}
			});
			btnProfile.setBounds(301, 436, 117, 25);
			contentPane.add(btnProfile);
			con.commit();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JTable jTable = (JTable) e.getSource();
				int row = jTable.getSelectedRow();
				// extract text of clicked cell
				valueInCell = (String) jTable.getValueAt(row, 0);

				/**
				 * try { Class.forName("oracle.jdbc.driver.OracleDriver"); Connection con =
				 * DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "SYSTEM",
				 * "16181618"); Statement stmt =
				 * con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
				 * ResultSet.CONCUR_READ_ONLY); String query = "select * from restaurant where
				 * r_name = '" + valueInCell + "'"; ResultSet rs = stmt.executeQuery(query);
				 * Restaurant rst; if (rs.next()) { rst = (Restaurant)new
				 * Restaurant(rs.getInt("r_id"), valueInCell, rs.getString("address")); // pass
				 * to restaurant page RestPage rp = new RestPage (user, rst, 0, "");
				 * rp.setVisible(true); dispose(); } con.commit(); con.close();
				 * 
				 * } catch (Exception ex) { ex.printStackTrace(); }
				 */
			}
		});

	}
	/*

	public void printList(List<Order> l) {
		int i;
		Order o;
		for (i = 0; i < l.size(); i++) {
			o = l.get(i);
			System.out.println(
					o.slNo + " " + o.d_id + " " + o.d_name + " " + o.quantity + " " + o.perprice + " " + o.total);
		}
	}
	*/
}