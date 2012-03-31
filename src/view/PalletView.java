package view;


import javax.swing.*;
import javax.swing.event.*;

import db.Database;
import db.Ingredients;
import db.PalletList;
import db.ProductMap;

import java.awt.*;
import java.awt.event.*;

/**
 * PalletView is the user interface of the application. 
 */
public class PalletView {
	

	private JTabbedPane tabbedPane;
	
	private Database db;

	/**
	 * Create a GUI object.
	 * 
	 * @param db The database.
	 * @param pa A list of pallets.
	 * @param pr the map over all products.
	 * @param in A list of all ingredients.
	 */
	public PalletView(Database db, PalletList pa, ProductMap pr, Ingredients in) {
		this.db = db;
		
		JFrame frame = new JFrame("Krusty Pallet Program");
		tabbedPane = new JTabbedPane();

		ProductionPane productionPane = new ProductionPane(pr, in, db);
		tabbedPane.addTab("Production", null, productionPane,
				"Produce pallets");

		SearchBlockPane bookingPane = new SearchBlockPane(pa, db);
		tabbedPane.addTab("Search/Block", null, bookingPane, "Find and block pallets");

		tabbedPane.setSelectedIndex(0);

		frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);

		tabbedPane.addChangeListener(new ChangeHandler());
		frame.addWindowListener(new WindowHandler());

		frame.setSize(800,500);
		frame.setVisible(true);
		

	}

	/**
	 * ChangeHandler is a listener class, called when the user switches panes.
	 */
	class ChangeHandler implements ChangeListener {
		/**
		 * Called when the user switches panes. The entry actions of the new
		 * pane are performed.
		 * 
		 * @param e
		 *            The change event (not used).
		 */
		public void stateChanged(ChangeEvent e) {
			BasicPane selectedPane = (BasicPane) tabbedPane
					.getSelectedComponent();
			selectedPane.entryActions();
		}
	}

	/**
	 * WindowHandler is a listener class, called when the user exits the
	 * application.
	 */
	class WindowHandler extends WindowAdapter {
		/**
		 * Called when the user exits the application. Closes the connection to
		 * the database.
		 * 
		 * @param e
		 *            The window event (not used).
		 */
		public void windowClosing(WindowEvent e) {
			db.closeConnection();
			System.exit(0);
		}
	}
}
