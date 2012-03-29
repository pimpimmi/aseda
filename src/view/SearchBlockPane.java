package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import db.Database;
import db.Pallet;
import db.PalletMap;



public class SearchBlockPane extends BasicPane{

	
	private static int SEARCH_ID = 0, SEARCH_SORT = 1, SEARCH_FROM_DATE = 2, SEARCH_FROM_TIME = 3,
			SEARCH_TO_DATE = 4, SEARCH_TO_TIME = 5, SEARCH_SIZE = 6;
	
	PalletMap pa;
	Database db;
	
	DefaultTableModel tableModel;
	
	private JTextField[] searchFields;
	JButton[][] searchButtons;
	JTable table;
	
	DateFormat dateFormat;
	DateFormat timeFormat;
	
	public SearchBlockPane(PalletMap pa, Database db) {
		this.pa = pa;
		this.db = db;
		setUpPane();
	}

	
	
//	public JComponent createBottomPanel() {
//		JButton[] buttons = new JButton[2];
//		buttons[0] = new JButton("Block");
//		buttons[1] = new JButton("Unblock");
//		return new ButtonAndMessagePanel(buttons, messageLabel,
//				new ActionHandler());	
//	}
	
	public JComponent createLeftPanel() {
		String[] texts = new String[SEARCH_SIZE];
		texts[SEARCH_ID] = "Pallet id";
		texts[SEARCH_FROM_DATE] = "From date";
		texts[SEARCH_FROM_TIME] = "From time";
		texts[SEARCH_TO_DATE] = "To date";
		texts[SEARCH_TO_TIME] = "To time";
		texts[SEARCH_SORT] = "Product";
		
		dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    	timeFormat = new SimpleDateFormat("HH:mm");
    	
    	searchFields = new JTextField[SEARCH_SIZE];
    	searchFields[SEARCH_ID] = new JFormattedTextField(NumberFormat.getIntegerInstance());

    	Date date = new Date();
    	Calendar cal = Calendar.getInstance();
    	String currentTime = timeFormat.format(cal.getTime());
    	
		searchFields[SEARCH_TO_DATE] = new JTextField();
		searchFields[SEARCH_TO_DATE].setText(dateFormat.format(date));
//		searchFields[SEARCH_TO_DATE].addFocusListener(new DateHintListener(dateTemp));
		searchFields[SEARCH_TO_TIME] = new JTextField();
		searchFields[SEARCH_TO_TIME].setText(currentTime);
//		searchFields[SEARCH_TO_TIME].addFocusListener(new TimeHintListener(currentTime));
    	
		
		date.setTime(date.getTime()-604800000); // Current date minus one week
		searchFields[SEARCH_FROM_DATE] = new JTextField();
		searchFields[SEARCH_FROM_DATE].setText(dateFormat.format(date));
//		searchFields[SEARCH_FROM_DATE].addFocusListener(new DateHintListener(dateTemp));
		searchFields[SEARCH_FROM_TIME] = new JTextField();
		searchFields[SEARCH_FROM_TIME].setText(currentTime);
//		searchFields[SEARCH_FROM_TIME].addFocusListener(new TimeHintListener(currentTime));
		
		searchFields[SEARCH_SORT] = new JTextField(12);
			
		JPanel p1 = new InputPanel(texts, searchFields);

		searchButtons = new JButton[2][2];
		searchButtons[0][0] = new JButton("Search");
		searchButtons[0][1] = new JButton("Clear");
//		JPanel p2 = new JPanel();
//		p2.add(searchButtons[0]);
//		p2.add(searchButtons[1]);
		
//		JButton[] blockButtons = new JButton[2];
		searchButtons[1][0] = new JButton("Block");
		searchButtons[1][1] = new JButton("Unblock");
		ButtonAndMessagePanel p3 = new ButtonAndMessagePanel(searchButtons, messageLabel,
				new SearchActionHandler());	

		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		p.add(p1);
//		p.add(p2);
		p.add(p3);
		return p;
	}
	
	public JComponent createTopPanel() {
		tableModel = new DefaultTableModel( null, new String [] {"Pallet ID","Product", "Date", "Time","Blocked","Delivered"} );
		table = new JTable();
		table.setModel(tableModel);
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
	
	class SearchActionHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			String s = ((JButton) arg0.getSource()).getText();
			if(s.equals("Search"))
				searchForPallets();
			else if(s.equals("Clear"))
				clearSearch();
			else if(s.equals("Block"))
				blockPallet();
			else
				unblockPallet();
		}

		private void unblockPallet() {
			
		}

		private void blockPallet() {
			// TODO Auto-generated method stub
			
		}

		private void clearSearch() {
			// TODO Auto-generated method stub
			
		}

		private void searchForPallets() {
			String[] s = new String[6];
			for(int i = 0; i < 6; i++)
				s[i] = searchFields[i].getText();
			db.searchResult(s);
			
			for(Pallet p : pa.palls){
				
			}
		}
		
	}
	
//	class DateHintListener implements FocusListener{
//		
//		String date;
//		
//		public DateHintListener(String date) {
//			super();
//			this.date = date;
//		}
//        public void focusGained(FocusEvent e) {}
//
//        public void focusLost(FocusEvent e) {
//        	
//        	JTextField temp = ((JTextField) e.getComponent());
//            if (temp.getText().trim().equals("")) {
//                temp.setText(dateFormat.format(date));
//            }
//        }
//		
//	}
//	
//	class TimeHintListener implements FocusListener{
//
//		public void focusGained(FocusEvent arg0) {}
//		
//        public void focusLost(FocusEvent e) {
//        	
//        	Calendar cal = Calendar.getInstance();
//        	
//        	JTextField temp = ((JTextField) e.getComponent());
//            if (temp.getText().trim().equals("")) {
//                temp.setText(dateFormat.format(cal.getTime()));
//            }
//        }
//
//		
//	}
	
}
