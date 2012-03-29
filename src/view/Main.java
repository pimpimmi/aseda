package view;

import db.*;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Ingredients in = new Ingredients();
		PalletMap pa = new PalletMap();
		ProductMap pr = new ProductMap();
		Database db = new Database(pa, pr, in);
		new PalletView(db, pa, pr, in);
	}

}
