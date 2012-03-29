package db;

import java.util.HashMap;

public class PalletMap {
	private HashMap<Integer, Pallet> palls;

	public PalletMap() {
		palls = new HashMap<Integer, Pallet>();
	}


	public void add(int pNbr, String type, String pDate, String pTime) {
		 palls.put(pNbr, new Pallet(pNbr, type, pDate, pTime));
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

	public boolean unBlock(int id) {
		if (palls.get(id).getBlocked() == true) {
			palls.get(id).setBlocked(1);
			return true;
		} else {
			return false;
		}
	}

}
