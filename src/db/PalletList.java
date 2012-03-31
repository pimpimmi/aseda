package db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Holds a list of Pallets.
 */
public class PalletList {
	public ArrayList<Pallet> palls;

	/**
	 * Creates a new list of Pallets.
	 */
	public PalletList() {
		palls = new ArrayList<Pallet>();
	}

	/**
	 * Changes the block status for a list of pallet.
	 * 
	 * @param pNbr Which pallets to be changed.
	 * @param setting True if the pallets are to be blocked,
	 * false otherwise.
	 * @return The list of pallets. Value is -1 if no change has
	 * been done.
	 */
	public int[] setBlock(int[] pNbr, boolean setting) {
		for(int i = 0; i < pNbr.length; i++){
			Pallet p = palls.get(pNbr[i]);
			if (p.getBlocked() != setting && p.getDelivered() == false)
				p.setBlocked(setting);
			else
				pNbr[i] = -1;
		}
		return pNbr;
	}
	
	/**
	 * Populates the list with Pallet objects.
	 * 
	 * @param info A set of results from the database.
	 */
	public void populate(ResultSet info) {
		palls.clear();
		try {
			if (info.first()) {
				boolean blocked, delivered;
				int pNbr;
				String pName, pDate, pTime;
				do {
					pNbr = info.getInt(1);
					pName = info.getString(2);
					String temp = info.getString(3);
					pDate = temp.substring(0,10);
					pTime = temp.substring(10,16);
					blocked = info.getBoolean(4);
					if (info.getDate(5) == null)
						delivered = false;
					else
						delivered = true;
					palls.add(new Pallet(pNbr, pName, pDate, pTime,
							blocked, delivered));
				} while (info.next());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
