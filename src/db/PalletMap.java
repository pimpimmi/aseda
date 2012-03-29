package db;

import java.util.ArrayList;
import java.util.HashMap;

public class PalletMap {
	public ArrayList<Pallet> palls;

	public PalletMap() {
		palls = new ArrayList<Pallet>();
	}



	public void add(int pNbr, String type, String pDate, String pTime, boolean blocked, boolean delivered) {
		 palls.add(new Pallet(pNbr, type, pDate, pTime, blocked, delivered));
	}

	public boolean block(int id) {
		if (palls.get(id).getBlocked()) {
			palls.get(id).setBlocked(true);
			return true;
		} else {
			return false;
		}
	}

	public boolean unblock(int id) {
		if (!palls.get(id).getBlocked()) {
			palls.get(id).setBlocked(false);
			return true;
		} else {
			return false;
		}
	}

}
