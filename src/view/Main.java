package view;

import db.*;

public class Main {

	/**
	 * Main is the main class for the pallet production application. It creates
	 * an Ingredients, a PalletList and a ProductMap to be used by Database and
	 * PalletView.
	 */
	public static void main(String[] args) {
		Ingredients in = new Ingredients();
		PalletList pa = new PalletList();
		ProductMap pr = new ProductMap();
		Database db = new Database(pa, pr, in);
		new PalletView(db, pa, pr, in);
	}

}
