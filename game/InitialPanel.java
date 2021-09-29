/*
 * David Black
 * Lab 7 Out: InitialPanel.java
 * Software Engineering T-Th 4:05pm
 * Dr. Mark Smith
 */

package chess;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class InitialPanel extends JFrame{
	private static final long serialVersionUID = 1L;
	//Class variable declarations
	private JPanel panel1;
	private JButton login;
	private JButton create;
	private JLabel header;

	//Constructor.
	public InitialPanel() {
		//Set title and default window close operation.
		this.setTitle("Chat Client");
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
	    //Create and display all GUI components.
		panel1 = new JPanel(new GridBagLayout());
		
		header = new JLabel("Account Information");
		
		login = new JButton("Login");
		create = new JButton("Create");
		login.setActionCommand("Login");
		create.setActionCommand("Create");
		
		/*
		 * If either Login or Create is selected, call a new instance of InitialControl to handle events,
		 * passing the current instance of InitialPanel as an argument.
		 */
		login.addActionListener(new InitialControl(this));
		create.addActionListener(new InitialControl(this));
		
		/*
	     * This is an example of a reasonably efficient way to use GridBagLayout. Also see
	     * the addItem method below.
	     */
	    addItem(panel1, header, 0, 0, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);
	    addItem(panel1, login, 0, 3, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);
	    addItem(panel1, create, 0, 4, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);
	    
	    this.add(panel1, BorderLayout.NORTH);
	    this.setSize(500, 250);
	    this.setVisible(true);
	}
	
	private void addItem(JPanel p, JComponent c, int x, int y, int width, int height, int align, int fill) {
	    GridBagConstraints gc = new GridBagConstraints();
	    gc.gridx = x;
	    gc.gridy = y;
	    gc.gridwidth = width;
	    gc.gridheight = height;
	    gc.weightx = 100.0;
	    gc.weighty = 100.0;
	    gc.insets = new Insets(10, 10, 10, 10);
	    gc.anchor = align;
	    gc.fill = fill;
	    p.add(c, gc);
	  }
}
