package view;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import db.Database;
import db.Ingredients;
import db.Product;
import db.ProductMap;

/**
 * A tab in the user interface which handles the creating of
 * pallets.
 */
public class ProductionPane extends BasicPane{
	private static final long serialVersionUID = 1L;
	
	DefaultTableModel ingrTableModel;
	ProductMap pr;
	Ingredients in;
	Database db;
	DefaultListModel productListModel;
	JList productList;
	JTextField quantityText;
	int oldQuantity;
	
	/**
	 * Creates a ProductionPane.
	 * 
	 * @param db The database.
	 * @param pr the map over all products.
	 * @param in A list of all ingredients.
	 */
	public ProductionPane(ProductMap pr, Ingredients in, Database db) {
		this.in = in;
		this.pr = pr;
		this.db = db;
		oldQuantity = 1;
		setUpPane();
	}
	
	/**
	 * Creates a list of products. Overrides the superclass method.
	 * 
	 * @return A panel.
	 */
	public JComponent createLeftPanel() {
		productListModel = new DefaultListModel();
		productList = new JList(productListModel);
		productList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		productList.setPrototypeCellValue("123456789012");
		productList.setFixedCellWidth(200);
		Set<String> productNames = pr.keySet();
		for(String s : productNames)
			productListModel.addElement(s);
		productList.addListSelectionListener(new ListFocusListener());
		return new JScrollPane(productList);
		
		
	}
	
	/**
	 * Creates a button for creating pallets and a label to show
	 * potential messages. Overrides the superclass method.
	 * 
	 * @return A panel.
	 */
	public JComponent createBottomPanel() {
		JButton[] buttons = new JButton[1];
		buttons[0] = new JButton("Create pallet");
		return new ButtonAndMessagePanel(buttons, messageLabel,
				new ActionHandler());	
	}
	
	/**
	 * Creates a textfield for input of quantity of pallets.
	 * Overrides the superclass method.
	 * 
	 * @return A panel.
	 */
	public JComponent createTopPanel() {		
		JLabel p1 = new JLabel("Quantity: ");
		quantityText = new JTextField(5);
		quantityText.setText("1");
		quantityText.addKeyListener(new QuantityKeyListener());
		JPanel p = new JPanel();
		p.add(p1);
		p.add(quantityText);
		return p;
	}
	
	/**
	 * Creates a table showing all ingredients needed to make
	 * the pallet(s). Overrides the superclass method.
	 * 
	 * @return A panel.
	 */
	public JComponent createMiddlePanel() {
		JTable table = new JTable();
		ingrTableModel = new DefaultTableModel( null, 
				new String [] {"Ingredient","Required", "Available"});
		table.setModel(ingrTableModel);
		table.setEnabled(false);
		JScrollPane scrollPane = new JScrollPane();
		for(int i = 0; i < table.getColumnCount(); i++)
			table.getColumnModel().getColumn(i).setResizable(false);
		 scrollPane.setViewportView(table);
		 return scrollPane;
	}
	
	/**
	 * A class which listens to keys.
	 */
	class QuantityKeyListener implements KeyListener{

		/**
		 * Not used.
		 */
		@Override
		public void keyPressed(KeyEvent e) {}
		
		/**
		 * Used when the user changes the quantity textfield.
		 * Updates the column in the ingredient table showing amounts
		 * needed for the given quantity.
		 * 
		 * @param e
		 *            The event object (not used).
		 */
		@Override
		public void keyReleased(KeyEvent e) {
			messageLabel.setText("");
			int quantity = getQuantity();
			if(quantity > 1000){
				quantityText.setText("1000");
				quantity = 1000;
			}
			for(int i = 0; i < ingrTableModel.getRowCount(); i++){
				long newValue = (Long) ingrTableModel.getValueAt(i, 1);
				newValue *= quantity;
				newValue /= oldQuantity;
				ingrTableModel.setValueAt(newValue , i, 1);
			}
			messageLabel.setText("");
			for(int i = 0; i < ingrTableModel.getRowCount(); i++)
				if(((Long) ingrTableModel.getValueAt(i, 1))>
					((Long) ingrTableModel.getValueAt(i, 2)))
					messageLabel.setText("Not enough ingredients!");
			
			oldQuantity = quantity;
		}

		/**
		 * Not used.
		 */
		@Override
		public void keyTyped(KeyEvent e) {}
		
	}
	
	/**
	 * Listens to button clicks.
	 */
	class ActionHandler implements ActionListener{
		
		/**
		 * Creates a number of pallets and updates the amount
		 * of ingredients available.
		 */
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(db.createPallet((String) productList.getSelectedValue(), oldQuantity))
				messageLabel.setText(oldQuantity + " pallets successfully created!");
			else
				messageLabel.setText("Pallets could not be created!");
			updateList();
		}
		
	}
	
	/**
	 * Listens for new selection in the product list.
	 */
	class ListFocusListener implements ListSelectionListener{

		/**
		 * Updates the list of ingredients.
		 */
		@Override
		public void valueChanged(ListSelectionEvent e) {
			messageLabel.setText("");
			updateList();
		}		
	}
	
	private int getQuantity(){
		try{
			return Integer.valueOf(quantityText.getText());
		} catch (NumberFormatException nfe) {
			return 1;
		}
	}

	private void updateList() {
		Product p = pr.getProduct((String)productList.getSelectedValue());
		for(int i = ingrTableModel.getRowCount()-1; i >=0; i--)
			ingrTableModel.removeRow(0);
		ArrayList<String> ingredientNames = p.getIngredients();
		ArrayList<Integer> neededAmount = p.getQuantities();
		int quantity = getQuantity();
		for(int i = 0; i < ingredientNames.size(); i++){
			Object[] row = new Object[3];
			String name = ingredientNames.get(i);
			long needed = neededAmount.get(i)*quantity*54;
			long available = in.getAmount(name);
			row[0] = name;
			row[1] = needed;
			row[2] = available;
			ingrTableModel.addRow(row);
			if(needed > available)
				messageLabel.setText(messageLabel.getText() + " Not enough ingredients!");
		}
		
	}
	

}
