package db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Set;

public class ProductMap {
	private HashMap<String, Product> prod;

	public ProductMap(Database db) {
		prod = new HashMap<String, Product>();
	}


	public Set<String> keySet() {
		return prod.keySet();
	}
	
	public Product getProduct(String product) {
		return prod.get(product);
	}


	public void populate(ResultSet productSet) {
		for (int i = 0; i < productSet.; i++) {
			
		}
		productSet.getArray(productSet.)
		
	}
 

	


}
