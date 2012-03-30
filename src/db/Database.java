package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Database {

	private Connection conn;
	private PalletMap pa;
	private ProductMap pr;
	private Ingredients in;

	private DateFormat dateFormat;
	private DateFormat timeFormat;

	public Database(PalletMap pall, ProductMap prod, Ingredients ingr) {
		openConnection("db69", "shamoona");
		pa = pall;
		pr = prod;
		in = ingr;
		updateAmounts();
		dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		timeFormat = new SimpleDateFormat("HH:mm");

		String pop = "select * from Recipes";
		try {
			PreparedStatement ps = conn.prepareStatement(pop);
			pr.populate(ps.executeQuery());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean openConnection(String userName, String password) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(
					"jdbc:mysql://puccini.cs.lth.se/" + userName, userName,
					password);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Close the connection to the database.
	 */
	public void closeConnection() {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
		}
		conn = null;
	}

	/**
	 * Check if the connection to the database has been established
	 * 
	 * @return true if the connection has been established
	 */
	public boolean isConnected() {
		return conn != null;
	}

	public void searchResult(String[] criteria) {
		ArrayList<String> fields = new ArrayList<String>();
		String get = "select * from Pallets";
		boolean crit = false;
		if (!(criteria[0].equals("") && criteria[1].equals("")
				&& criteria[2].equals("") && criteria[3].equals("")
				&& criteria[4].equals("") && criteria[5].equals(""))) {
			get += " where";
			crit = true;
		}
		if (!criteria[0].equals("")) {
			get += " pNbr = ? and";
			fields.add("pNbr");
		}
		if (!criteria[1].equals("")) {
			get += " pName = ? and";
			fields.add("pName");
		}
		if (!criteria[2].equals("")) {
			get += " pDate > ? and";
			fields.add("fpDate");
		}
		if (!criteria[3].equals("")) {
			get += " pTime > ? and";
			fields.add("fpTime");
		}
		if (!criteria[4].equals("")) {
			get += " pDate < ? and";
			fields.add("tpDate");
		}
		if (!criteria[5].equals("")) {
			get += " pTime < ? and";
			fields.add("tpTime");
		}
		if (crit)
			get = get.substring(0, get.length() - 3);
		System.out.println(get);

		try {
			PreparedStatement ps = conn.prepareStatement(get);
			System.out.println(fields.size());
			for (int i = 1; i <= fields.size(); i++) {
				String s = fields.get(i-1);
				if (s.equals("pNbr"))
					ps.setString(i, criteria[0]);
				if (s.equals("pName"))
					ps.setString(i, criteria[1]);
				try {
					if (s.equals("fpDate")) {
						System.out.println("fpDate: " + criteria[2]);
						Date date = dateFormat.parse(criteria[2]);
						System.out.println(date);
						long ms = date.getTime();
						java.sql.Date d = new java.sql.Date(ms);
						ps.setDate(i, d);
					}
					if (s.equals("tpDate")){
						System.out.println("tpDate: " + criteria[4]);
						Date date = dateFormat.parse(criteria[4]);
						System.out.println(date);
						long ms = date.getTime();
						java.sql.Date d = new java.sql.Date(ms);
						ps.setDate(i, d);
					}
					if (s.equals("fpTime")) {
						System.out.println("fpTime: " + criteria[3]);
						Date time = timeFormat.parse(criteria[3]);
						System.out.println(time);
						long ms = time.getTime();
						java.sql.Date d = new java.sql.Date(ms);
						ps.setDate(i, d);
					}
					if (s.equals("tpTime")){
						System.out.println("tpTime: " + criteria[5]);
						Date time = timeFormat.parse(criteria[5]);
						System.out.println(time);
						long ms = time.getTime();
						java.sql.Date d = new java.sql.Date(ms);
						ps.setDate(i, d);
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			pa.populate(ps.executeQuery());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateAmounts() {
		String get = "select * from Materials";
		try {
			PreparedStatement ps = conn.prepareStatement(get);
			in.setAmounts(ps.executeQuery());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean checkAmounts(String type) {
		String get = "select mName, amount from Recipes where mName = ?";
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(get);
			ps.setString(1, type);
			ResultSet rs = ps.executeQuery();
			ArrayList<String> ingredients = new ArrayList<String>();
			ArrayList<Integer> quantities = new ArrayList<Integer>();
			do {
				ingredients.add(rs.getString(1));
				quantities.add(rs.getInt(2));
			} while (rs.next());

			if (!in.checkAvailable(ingredients, quantities))
				return false;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

	public boolean subtractAmounts(String type) {
		String set = "update M set amountAvail -= amount "
				+ "from Materials as M natural join Recipes "
				+ "where pName = ?";
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(set);
			ps.setString(1, type);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

	public boolean createPallet(String type) {
		try {
			updateAmounts();
			if (checkAmounts(type)) {
				String add = "insert into Pallets(pNbr, pName, pDate, pTime, blocked) values (?, ?, ?, ?, ?)";
				PreparedStatement ps = conn.prepareStatement(add);

				Date date = new Date();
				Calendar cal = Calendar.getInstance();
				String currentTime = timeFormat.format(cal.getTime());
				String currentDate = dateFormat.format(date);

				ps.setInt(1, 0);
				ps.setString(2, type);
				ps.setString(3, currentDate);
				ps.setString(4, currentTime);
				ps.setBoolean(5, false);
				ps.executeUpdate();

				subtractAmounts(type);

				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;

	}

	public boolean block(int id) {
		if (Math.random() > 0.9)
			return false;
		else {
			return pa.block(id);
		}
	}

	public boolean unblock(int id) {
		if (Math.random() > 0.9)
			return false;
		else {
			return pa.unblock(id);
		}
	}

	public String[] getProductList() {
		return (String[]) pr.keySet().toArray();
	}

	public Product getProductInfo(String product) {
		return pr.getProduct(product);
	}

	// public void createProduct(String product, int amount) {
	//
	// }

}
