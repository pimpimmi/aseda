package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Database {

	private Connection conn;
	private PalletList pa;
	private ProductMap pr;
	private Ingredients in;

	private DateFormat dateFormat;
	private DateFormat timeFormat;

	public Database(PalletList pall, ProductMap prod, Ingredients ingr) {
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
		String get = "select pNbr, pName, pDateTime, blocked, dDateTime from Pallets";
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
		if (!(criteria[2].equals("") && criteria[3].equals(""))) {
			if(criteria[2].equals(""))
				criteria[2] = "1000-01-01";
			if(criteria[3].equals(""))
				criteria[3] = "00:00:00";
			else
				criteria[3] += ":00";
			get += " pDateTime >= ? and";
			fields.add("fpDateTime");
		}
		if (!(criteria[4].equals("") && criteria[5].equals(""))) {
			if(criteria[4].equals(""))
				criteria[4] = "9999-12-31";
			if(criteria[5].equals(""))
				criteria[5] = "00:00:00";
			else
				criteria[5] += ":00";
			get += " pDateTime <= ? and";
			fields.add("tpDateTime");
		}
		if (crit)
			get = get.substring(0, get.length() - 3);
		try {
			PreparedStatement ps = conn.prepareStatement(get);
			for (int i = 1; i <= fields.size(); i++) {
				String s = fields.get(i-1);
				System.out.println(s + criteria[0]);
				if (s.equals("pNbr")){
					ps.setInt(i, Integer.valueOf(criteria[0]));
				}if (s.equals("pName"))
					ps.setString(i, criteria[1]);
				if (s.equals("fpDateTime"))
					ps.setString(i,criteria[2] + " " + criteria[3]);
				if (s.equals("tpDateTime"))
					ps.setString(i,criteria[4] + " " + criteria[5]);
			}
			System.out.println(ps.toString());
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

	public boolean checkAmounts(String type, int amount) {
		ArrayList<String> ingredients = pr.getProduct(type).getIngredients();
		ArrayList<Integer> quantities = pr.getProduct(type).getQuantities();
		if (!in.checkAvailable(ingredients, quantities, amount)) {
			return false;
		} else {
			return true;
		}

	}

	public boolean subtractAmounts(String type, int quantity) {
		PreparedStatement ps;
		try {
			String get1 = "update Materials m, Recipes r" +
					" set m.amountAvail = m.amountAvail - r.amount*54*" + String.valueOf(quantity) + 
					" where m.mName = r.mName and r.pName = ?";
			ps = conn.prepareStatement(get1);
			ps.setString(1, type);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		updateAmounts();
		return true;
	}

	public boolean createPallet(String type, int quantity) {
		try {
			if (checkAmounts(type, quantity)) {
				Date date = new Date();
				Calendar cal = Calendar.getInstance();
				String currentTime = timeFormat.format(cal.getTime());
				String currentDate = dateFormat.format(date);
				String add = "insert into Pallets(pName, pDateTime, blocked) values";
				for(int i = 0; i < quantity; i++){
					add += " (?,?,?),";
				}
				add = add.substring(0, add.length()-1);
				
				PreparedStatement ps = conn.prepareStatement(add);
				for(int i = 0; i < quantity; i++){
					ps.setString(1+i*3, type);
					ps.setString(2+i*3, currentDate + " " + currentTime + ":00");
					ps.setBoolean(3+i*3, false);
				}
				ps.executeUpdate();

				subtractAmounts(type, quantity);
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;

	}

	public int[] setBlock(int[] rowIds, boolean setting) {
		String block = "update Pallets set blocked = ? where dDateTime is NULL and (pNbr = ?";
		for(int i = 1;i<rowIds.length;i++)
			block += " or pNbr = ?";
		block += ")";
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(block);
			ps.setBoolean(1, setting);
			for(int i = 0; i<rowIds.length; i++)
				ps.setInt(2+i, pa.palls.get(rowIds[i]).getPNbr());
			if (rowIds.length != 0)
				ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pa.setBlock(rowIds, setting);
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
