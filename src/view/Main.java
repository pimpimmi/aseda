package view;
import db.*;



public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		;
		PalletMap pa = new PalletMap();
		ProductMap pr = new ProductMap();
		Database db = new Database(pa, pr);
		new PalletView(db, pa, pr);
	}

}
