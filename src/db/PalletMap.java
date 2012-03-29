package db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class PalletMap {
	private HashMap<Integer, Pallet> palls;

	public PalletMap() {
		palls = new HashMap<Integer, Pallet>();
	}


	public void add(int pNbr, Pallet p) {
		 palls.put(pNbr, p);
	}

	public void remove(int id) {
		palls.remove(id);
	}



	public boolean block(int id) {
		if (palls.get(id).getBlocked() == true) {
			palls.get(id).setBlocked(1);
			return true;
		} else {
			return false;
		}
	}

	public boolean unblock(int id) {
		if (palls.get(id).getBlocked() == true) {
			palls.get(id).setBlocked(1);
			return true;
		} else {
			return false;
		}
	}


	public void populate(ResultSet info) {
		palls.clear();
		try {
			info.first();
			int pNbr, blocked;
			String pName, pDate, pTime;
			do {
					pNbr = info.getInt(1);
					pName = info.getString(2);
					pDate = info.getString(3);
					pTime = info.getString(4);
					blocked = info.getInt(5);
					add(pNbr, new Pallet(pNbr, pName, pDate, pTime, blocked));
			} while (info.next());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
