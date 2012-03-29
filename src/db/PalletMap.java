package db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class PalletMap {
	private HashMap<Integer, Pallet> palls;

	public PalletMap(Database db) {
		palls = new HashMap<Integer, Pallet>();
	}


	public void add(int id, int type, int quantity, String customer,
			String fDate, String lDate) {
		palls.put(id, new Pallet(id, type, quantity, customer, fDate, lDate));
	}

	public void remove(int id) {
		palls.remove(id);
	}



	public boolean block(int id) {
		if (palls.get(id).getBlocked() == false) {
			palls.get(id).setBlocked(true);
			return true;
		} else {
			return false;
		}
	}

	public boolean unBlock(int id) {
		if (palls.get(id).getBlocked() == true) {
			palls.get(id).setBlocked(false);
			return true;
		} else {
			return false;
		}
	}

}
