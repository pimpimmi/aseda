package db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A map of all the ingredients and the available amount.
 */
public class Ingredients {
	private HashMap<String, Integer> ingredients;

	/**
	 * Creates a new Ingredient object.
	 */
	public Ingredients() {
		ingredients = new HashMap<String, Integer>();
	}

	/**
	 * Updates the amounts available.
	 * 
	 * @param amounts The ingredients and amounts to be updated.
	 */
	public void updateAmounts(ResultSet amounts) {
		try {
			amounts.first();
			do {
				ingredients.put(amounts.getString(1), amounts.getInt(2));
			} while (amounts.next());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns the amount available for an ingredient.
	 * 
	 * @param mName Name of the ingredient.
	 * @return The amount available.
	 */
	public int getAmount(String mName){
		return ingredients.get(mName);
	}

	/**
	 * Checks if the amount is enough.
	 * 
	 * @param ing A list of ingredients required.
	 * @param amn A list of amount needed for one pallet.
	 * @param quantity The quantity of pallets.
	 * @return True if there is enough, false otherwise.
	 */
	public boolean checkAvailable(ArrayList<String> ing, ArrayList<Integer> amn, int quantity) {
		for (int i = 0; i < ing.size(); i++) {
			if (ingredients.get(ing.get(i)) < amn.get(i)*quantity)
				return false;
		}
		return true;
	}
}
