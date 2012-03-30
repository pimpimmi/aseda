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


public class ProductionPane extends BasicPane{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	DefaultTableModel ingrTableModel;
	ProductMap pr;
	Ingredients in;
	Database db;
	DefaultListModel listModel;
	JList list;
	JTextField quantityText;
	int oldQuantity;

	public ProductionPane(ProductMap pr, Ingredients in, Database db) {
		this.in = in;
		this.pr = pr;
		this.db = db;
		oldQuantity = 1;
		setUpPane();
	}
	
	
	public JComponent createLeftPanel() {
		listModel = new DefaultListModel();
		list = new JList(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setPrototypeCellValue("123456789012");
		list.setFixedCellWidth(200);
		Set<String> productNames = pr.keySet();
		for(String s : productNames)
			listModel.addElement(s);
		list.addListSelectionListener(new ListFocusListener());
		return new JScrollPane(list);
		
		
	}
	
	public JComponent createBottomPanel() {
		JButton[] buttons = new JButton[1];
		buttons[0] = new JButton("Create pallet");
		return new ButtonAndMessagePanel(buttons, messageLabel,
				new ActionHandler());	
	}
	
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
	
	public JComponent createMiddlePanel() {
		JTable table = new JTable();
		ingrTableModel = new DefaultTableModel( null, new String [] {"Ingredient","Required", "Available"});
		table.setModel(ingrTableModel);
		table.setEnabled(false);
		JScrollPane scrollPane = new JScrollPane();
		for(int i = 0; i < table.getColumnCount(); i++)
			table.getColumnModel().getColumn(i).setResizable(false);
		 scrollPane.setViewportView(table);
		 return scrollPane;
	}
	
	
	class QuantityKeyListener implements KeyListener{

		@Override
		public void keyPressed(KeyEvent e) {
			
		}

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
			oldQuantity = quantity;
		}

		@Override
		public void keyTyped(KeyEvent e) {
			
		}
		
	}
	
	class ActionHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			for(int i = 0; i < oldQuantity; i++)
				db.createPallet((String) list.getSelectedValue());
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
	
	class ListFocusListener implements ListSelectionListener{

		@Override
		public void valueChanged(ListSelectionEvent e) {
			updateList();
				
		}		
	}

	public void updateList() {
		messageLabel.setText("");
		Product p = pr.getProduct((String)list.getSelectedValue());
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
				messageLabel.setText("Not enough " + name + "!");
		}
		
	}
	

}
