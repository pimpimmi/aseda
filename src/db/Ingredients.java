package db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class Ingredients {
	private HashMap<String, Integer> ingredients;

	public Ingredients() {
		ingredients = new HashMap<String, Integer>();
	}

	public void setAmounts(ResultSet amounts) {
		try {
			amounts.first();
			do {
				ingredients.put(amounts.getString(1), amounts.getInt(2));
			} while (amounts.next());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int getAmount(String mName){
		return ingredients.get(mName);
	}

	public boolean checkAvailable(ArrayList<String> ing, ArrayList<Integer> amn) {
		for (int i = 0; i < ing.size(); i++) {
			if (ingredients.get(ing.get(i)) < amn.get(i))
				return false;
		}
		return true;
	}
}
