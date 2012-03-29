package db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class ProductMap {
	private HashMap<String, Product> prod;

	public ProductMap() {
		prod = new HashMap<String, Product>();
	}

	public Set<String> keySet() {
		return prod.keySet();
	}
	
	public void addProduct(String name, Product product){
		prod.put(name, product);
	}

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
				if (currProd != info.getString(0) || currProd == "") {
					if (currProd != "")
						addProduct(currProd, new Product(currProd, ingredients, quantities));
					currProd = info.getString(0);
					ingredients = new ArrayList<String>();
					quantities = new ArrayList<Integer>();
				}

				ingredients.add(info.getString(1));
				quantities.add(info.getInt(2));
			} while (info.next());

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
