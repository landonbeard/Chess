/*
 * David Black
 * Lab 7 Out: LoginPanel.java
 * Software Engineering T-Th 4:05pm
 * Dr. Mark Smith
 */

package chess;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.Serializable;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginPanel extends JFrame implements Serializable{
	private static final long serialVersionUID = 1L;
	//Class variable declarations.
	private JPanel panel1;
	private JPanel panel2;
	private JPanel panel3;
	private static JLabel enter;
	private JLabel nameLabel;
	private JLabel pwLabel;
	private static JTextField nameField;
	private static JPasswordField pwField;
	private static JButton submit;
	private JButton cancel;
	
	//Constructor.
	public LoginPanel() {
		//Set title and default window close operation.
		this.setTitle("Chat Client");
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
	    //Create and display all GUI components.
	    panel1 = new JPanel(new GridBagLayout());
	    panel2 = new JPanel(new GridBagLayout());
	    panel3 = new JPanel(new GridBagLayout());
	    
	    enter = new JLabel("Enter a username and password to login:");
	    nameLabel = new JLabel("Username: ");
	    pwLabel = new JLabel("Password: ");
	    
	    nameField = new JTextField();
	    nameField.setColumns(16);
	    pwField = new JPasswordField();
	    pwField.setColumns(16);
	    pwField.setEchoChar('\u25CF');
	    
	    submit = new JButton("Submit");
	    cancel = new JButton("Cancel");
	    submit.setActionCommand("Submit");
	    cancel.setActionCommand("Cancel");
	    
	    /*
	     * If either Submit or Cancel are selected, call a new instance of LoginControl to
	     * handle events, passing the current instance of LoginPanel as an argument.
	     */
	    submit.addActionListener(new LoginControl(this));
	    cancel.addActionListener(new LoginControl(this));
	    
	    /*
	     * This is an example of a reasonably efficient way to use GridBagLayout. Also see
	     * the addItem method below.
	     */
	    addItem(panel1, enter, 0, 0, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);
	 
	    addItem(panel2, nameLabel, 0, 1, 1, 1, GridBagConstraints.EAST, GridBagConstraints.NONE);
	    addItem(panel2, pwLabel, 0, 2, 1, 1, GridBagConstraints.EAST, GridBagConstraints.NONE);
	    addItem(panel2, nameField, 1, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL);
	    addItem(panel2, pwField, 1, 2, 1, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL);
	    
	    addItem(panel3, submit, 0, 0, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);
	    addItem(panel3, cancel, 1, 0, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);
	    
	    this.add(panel1, BorderLayout.NORTH);
	    this.add(panel2, BorderLayout.CENTER);
	    this.add(panel3, BorderLayout.SOUTH);
	    this.setSize(400, 300);
	    this.setVisible(true);
	}
	
	//This method streamlines the GridBagLayout process.
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
	
	//Getter for the username field.
	public String getNameField() {
		return nameField.getText();
	}
	
	//Getter for the password field.
	public String getPasswordField() {
		return String.valueOf(pwField.getPassword());
	}
	
	public static JLabel getEnter() {
		return LoginPanel.enter;
	}
	
	public static JButton getSubmit() {
		return submit;
	}
	
	//This is called from notifyInvalid() if account data was found to be invalid.
	public static void error() {
		enter.setText("Username/password combination not found. Please reenter.");
		enter.setForeground(Color.RED);
		nameField.setText("");
		pwField.setText("");
	}
}
