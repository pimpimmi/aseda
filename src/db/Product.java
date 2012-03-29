package db;

import java.util.ArrayList;

public class Product {
	private String name;
	private ArrayList<String> ingredients;
	private ArrayList<Integer> quantities;

	public Product(String name, ArrayList<String> ingredients,
			ArrayList<Integer> quantities) {
		this.name = name;
		this.ingredients = ingredients;
		this.quantities = quantities;
	}

}