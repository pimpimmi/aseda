package db;

import java.util.ArrayList;

/**
 * Holds the Recipe for a product, which ingredient and
 * how much.
 */
public class Product {
	private String name;
	private ArrayList<String> ingredients;
	private ArrayList<Integer> quantities;

	/**
	 * Creates a Product object.
	 * 
	 * @param name Name of the product.
	 * @param ingredients List of all the ingredients.
	 * @param quantities List of the quantity for each ingredient.
	 */
	public Product(String name, ArrayList<String> ingredients,
			ArrayList<Integer> quantities) {
		this.name = name;
		this.ingredients = ingredients;
		this.quantities = quantities;
	}
	
	/**
	 * Returns the name of the product.
	 * @return The name of the product.
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Returns a list of all the ingredients.
	 * @return A list of all the ingredients.
	 */
	public ArrayList<String> getIngredients(){
		return ingredients;
	}

	/**
	 * Returns a list of quantity for each ingredient.
	 * @return A list of quantity for each ingredient.
	 */
	public ArrayList<Integer> getQuantities(){
		return quantities;
	}
}