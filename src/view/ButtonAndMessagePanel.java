package view;


import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

/**
 * A GUI panel which contains buttons and a message line.
 */
public class ButtonAndMessagePanel extends JPanel {
	private static final long serialVersionUID = 1;

	/**
	 * Create the component with the specified buttons and message line, and
	 * also an action listener for the buttons.
	 * 
	 * @param buttons
	 *            The array of buttons.
	 * @param messageLine
	 *            The message line.
	 * @param actHand
	 *            The action listener for the buttons.
	 */
	public ButtonAndMessagePanel(JButton[][] buttons, JLabel messageLine,
			ActionListener actHand) {
		setUp(buttons, messageLine, actHand);

	}
	
	public ButtonAndMessagePanel(JButton[] buttons, JLabel messageLine,
			ActionListener actHand) {
		JButton[][] b = new JButton[1][buttons.length];
		b[0] = buttons;
		setUp(b, messageLine, actHand);

	}
	
	
	public void setUp(JButton[][] buttons, JLabel messageLine,
			ActionListener actHand){
		setLayout(new GridLayout(2, 1));

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(buttons.length,buttons[0].length));
		for (int i = 0; i < buttons.length; i++) {
			for (int j = 0; j < buttons[i].length; j++){
				buttonPanel.add(buttons[i][j]);
				buttons[i][j].addActionListener(actHand);
			}
		}
		add(buttonPanel);

		add(messageLine);
	}
	
}
