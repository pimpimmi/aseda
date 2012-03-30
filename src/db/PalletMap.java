package db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PalletMap {
	public ArrayList<Pallet> palls;

	public PalletMap() {
		palls = new ArrayList<Pallet>();
	}

	public void add(Pallet p) {
		palls.add(p);
	}

	public boolean setBlock(int pNbr, boolean setting) {
		if (palls.get(pNbr).getBlocked() != setting) {
			palls.get(pNbr).setBlocked(setting);
			return true;
		} else {
			return false;
		}
	}

	
	public void populate(ResultSet info) {
		palls.clear();
		try {
			info.first();
			if (info.next()) {
				//TODO skippar den en?
				boolean blocked, delivered;
				int pNbr;
				String pName, pDate, pTime;
				do {
					pNbr = info.getInt(1);
					pName = info.getString(2);
					pDate = info.getString(3);
					pTime = info.getString(4);
					blocked = info.getBoolean(5);
					if (info.getDate(6) == null)
						delivered = false;
					else
						delivered = true;
					palls.add(pNbr, new Pallet(pNbr, pName, pDate, pTime,
							blocked, delivered));
				} while (info.next());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
