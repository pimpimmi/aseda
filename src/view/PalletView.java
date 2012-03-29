package view;


import javax.swing.*;
import javax.swing.event.*;

import db.Database;
import db.Ingredients;
import db.PalletMap;
import db.ProductMap;

import java.awt.*;
import java.awt.event.*;

/**
 * MovieGUI is the user interface to the movie database. It sets up the main
 * window and connects to the database.
 */
public class PalletView {
	
	Database da;
	
	/**
	 * tabbedPane is the contents of the window. It consists of two panes, User
	 * login and Book tickets.
	 */
	private JTabbedPane tabbedPane;

	/**
	 * Create a GUI object and connect to the database.
	 * 
	 * @param db
	 *            The database.
	 */


	public PalletView(Database da, PalletMap pa, ProductMap pr, Ingredients in) {
		JFrame frame = new JFrame("Krusty Pallet Program");
		tabbedPane = new JTabbedPane();

		ProductionPane productionPane = new ProductionPane(pr, in);
		tabbedPane.addTab("Production", null, productionPane,
				"Produce pallets");

		SearchBlockPane bookingPane = new SearchBlockPane(pa);
		tabbedPane.addTab("Search/Block", null, bookingPane, "Find and block pallets");

		tabbedPane.setSelectedIndex(0);

		frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);

		tabbedPane.addChangeListener(new ChangeHandler());
		frame.addWindowListener(new WindowHandler());

		frame.setSize(700,400);
		frame.setVisible(true);
		
		da.openConnection("db69", "shamoona");

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
			//da.closeConnection();
			System.exit(0);
		}
	}
}
