package db;

import java.util.ArrayList;
import java.util.HashMap;

public class PalletMap {
	public ArrayList<Pallet> palls;

	public PalletMap() {
		palls = new ArrayList<Pallet>();
	}


	public void add(int pNbr, String type, String pDate, String pTime,
			String fDate, String lDate) {
		 palls.add(new Pallet(pNbr, type, pDate, pTime, fDate, lDate));
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
