package view;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
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

import db.ProductMap;


public class ProductionPane extends BasicPane{
	
	ProductMap pr;
	DefaultListModel listModel;
	JTextField quantity;
	JTable table;
	int id = 1;

	public ProductionPane(ProductMap pr) {
		super();
		this.pr = pr;
	}
	
	
	public JComponent createLeftPanel() {
		listModel = new DefaultListModel();
		JList list = new JList(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setPrototypeCellValue("123456789012");
		list.setFixedCellWidth(200);
// TODO	Set<String> productNames = pr.keySet();
//		for(String s : productNames)
//			listModel.addElement(s);
		listModel.addElement("hej");
		listModel.addElement("hej2");
		
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

		quantity = new JTextField(5);
		
		JPanel p = new JPanel();
		p.add(p1);
		p.add(quantity);
		
		return p;
	}
	
	public JComponent createMiddlePanel() {
		table = new JTable();
		table.setModel(new DefaultTableModel( null, new String [] {"Ingredient","Required", "Available"} ));
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
			
		}		
	}
	

}
