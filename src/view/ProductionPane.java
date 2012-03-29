package view;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import db.Product;
import db.ProductMap;


public class ProductionPane extends BasicPane{
	
	DefaultTableModel ingrTableModel;
	ProductMap pr;
	DefaultListModel listModel;
	JList list;
	JTextField quantityText;
	int id = 1;

	public ProductionPane(ProductMap pr) {
		this.pr = pr;
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
		JScrollPane simulationScrollPane = new JScrollPane();
		for(int i = 0; i < table.getColumnCount(); i++)
			table.getColumnModel().getColumn(i).setResizable(false);
		 simulationScrollPane.setViewportView(table);
		 return simulationScrollPane;
	}
	
	class ActionHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			
		}
		
	}
	
	class ListFocusListener implements ListSelectionListener{

		@Override
		public void valueChanged(ListSelectionEvent e) {
			Product p = pr.getProduct((String)list.getSelectedValue());
			for(int i = 0; i < ingrTableModel.getRowCount(); i++)
				ingrTableModel.removeRow(0);
			ArrayList<String> ingredients = p.getIngredients();
			ArrayList<Integer> neededAmount = p.getQuantities();
			int quantity = Integer.valueOf(quantityText.getText());
			for(int i = 0; i < ingredients.size(); i++){
				String[] row = new String[3];
				row[0] = ingredients.get(i);
				row[1] = String.valueOf(neededAmount.get(i)*quantity);
				row[2] = "";
				ingrTableModel.addRow(row);
			}
				
		}		
	}
	

}
