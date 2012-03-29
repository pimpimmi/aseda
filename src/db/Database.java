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
		if (!(criteria[0] == "" && criteria[1] == "" && criteria[2] == ""
				&& criteria[3] == "" && criteria[4] == "" && criteria[5] == ""))
			get += " where";
		if (criteria[0] != "") {
			get += " pNbr = ? and";
			fields.add("pNbr");
		}
		if (criteria[1] != "") {
			get += "pName = ? and";
			fields.add("pName");
		}
		if (criteria[2] != "") {
			get += "pDate > ?";
			fields.add("fpDate");
		}
		if (criteria[3] != "") {
			get += "pTime > ?";
			fields.add("fpTime");
		}
		if (criteria[4] != "") {
			get += "pDate < ?";
			fields.add("tpDate");
		}
		if (criteria[5] != "") {
			get += "pTime < ?";
			fields.add("tpTime");
		}
		get = get.substring(0, get.length()-3);
		try {
			PreparedStatement ps = conn.prepareStatement(get);
			for (int i = 0; i < fields.size(); i++) {
				String s = fields.remove(0);
				if (s == "pNbr" || s == "pName")
					ps.setString(i, s);
					try {
						if (s == "fpDate" || s == "tpDate")
						ps.setDate(i, (java.sql.Date) dateFormat.parse(s));
						if (s == "fpTime" || s == "tpTime")
							ps.setTime(i, (java.sql.Time) timeFormat.parse(s));
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

				dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				timeFormat = new SimpleDateFormat("HH:mm");

				Date date = new Date();
				Calendar cal = Calendar.getInstance();
				String currentTime = timeFormat.format(cal.getTime());
				String currentDate = dateFormat.format(date);

				ps.setInt(1, 0);
				ps.setString(2, type);
				ps.setString(3, currentDate);
				ps.setString(4, currentTime);
				ps.setInt(5, Pallet.UNBLOCKED);
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
