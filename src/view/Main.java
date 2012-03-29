package view;
import db.*;



public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Database db = new Database();
		PalletMap pa = new PalletMap(db);
		ProductMap pr = new ProductMap(db);
		new PalletView(db, pa, pr);
	}

}
