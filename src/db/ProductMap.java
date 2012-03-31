package db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;


/**
 * Holds a map of Products.
 */
public class ProductMap {
	private HashMap<String, Product> prod;

	/**
	 * Creates a new ProductMap object.
	 */
	public ProductMap() {
		prod = new HashMap<String, Product>();
	}
	
	/**
	 * Returns a list of all the keys in the map.
	 * 
	 * @return A set of keys.
	 */
	public Set<String> keySet() {
		return prod.keySet();
	}
	
	/**
	 * Returns a product object.
	 * @param product The key to the product.
	 * 
	 * @return A product.
	 */
	public Product getProduct(String product) {
		return prod.get(product);
	}

	
	public void populate(ResultSet info) {
		try {
			info.first();
			String currProd = "";
			ArrayList<String> ingredients = new ArrayList<String>();
			ArrayList<Integer> quantities = new ArrayList<Integer>();
			do {
				if (!currProd.equals(info.getString(1))) {
					if (currProd != "") {
						prod.put(currProd, new Product(currProd, ingredients,
								quantities));
					}
					currProd = info.getString(1);
					ingredients = new ArrayList<String>();
					quantities = new ArrayList<Integer>();
				}
				ingredients.add(info.getString(2));
				quantities.add(info.getInt(3));
			} while (info.next());
			prod.put(currProd, new Product(currProd, ingredients,
					quantities));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
