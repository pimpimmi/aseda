package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Database {

	private Connection conn;
	private PalletMap pa;
	private ProductMap pr;
	
	private DateFormat dateFormat;
	private DateFormat timeFormat;

	public Database(PalletMap pa, ProductMap pr) {
		conn = null;
		this.pr = pr;
		this.pa = pa;

		// for (int i = 0; i < 10; i++) {
		// Object[] o = new Object[5];
		// o[0] = i;
		// o[1] = "2001-01-01";
		// o[2] = "23:23";
		// o[3] = "yes"; // blocked?
		// if (Math.random() > 0.5)
		// o[4] = false;
		// else
		// o[4] = true; // delivered?
		// pr[i] = o;
		//
		// Object[][] p = new Object[3][3];
		//
		// p[0][0] = "Ingr1";
		// p[0][1] = 100;
		// p[0][2] = 10;
		// p[1][0] = "Ingr2" + String.valueOf(i + 1);
		// p[1][1] = 100;
		// p[1][2] = 7;
		// p[2][0] = "Ingr3" + String.valueOf(i + 1);
		// p[2][1] = 100;
		// p[2][2] = 37;
		//
		// pa.put("Cookie" + String.valueOf(i), p);
		//
		// }
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

	public ProductMap searchResult(Object[] criteria) {
		return pr;
	}

	public boolean createPallet(int pNbr, String type, String fDate, String lDate) {
		try {
			String add = "insert into Pallets(pNbr, type, pDate, pTime, blocked) values (?, ?, ?, ?, ?, ?)";
			PreparedStatement ps = conn.prepareStatement(add);
			
			dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    	timeFormat = new SimpleDateFormat("HH:mm");

	    	Date date = new Date();
	    	Calendar cal = Calendar.getInstance();
	    	String currentTime = timeFormat.format(cal.getTime());
	    	String currentDate = dateFormat.format(date);
	    	
			ps.setLong(1, pNbr);
			ps.setString(2, type);
			ps.setString(3, currentDate);
			ps.setString(4, currentTime);	
			ps.setLong(5, 0);
			ps.executeUpdate();
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
			return pa.unBlock(id);
		}
	}

	public String[] getProductList() {
		return (String[]) pr.keySet().toArray();
	}

	public Product getProductInfo(String product) {
		return pr.getProduct(product);
	}

	public void createProduct(String product, int amount) {

	}

}
