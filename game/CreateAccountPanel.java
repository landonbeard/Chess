/*
 * David Black
 * Lab 7 Out: CreateAccountPanel.java
 * Software Engineering T-Th 4:05pm
 * Dr. Mark Smith
 */

package chess;

import java.awt.Color;
import java.io.Serializable;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

public class CreateAccountPanel extends JFrame implements Serializable{
	private static final long serialVersionUID = 1L;
	//Class variable declarations.
	private JPanel panel1;
	private static JLabel enter;
	private JLabel requirements;
	private JLabel nameLabel;
	private JLabel pwLabel;
	private JLabel verifyLabel;
	private static JTextField name;
	private static JPasswordField password;
	private static JPasswordField verify;
	private static JButton submit;
	private JButton cancel;
	private String username;
	private String pw;
	private String check;
	
	//Constructor.
	public CreateAccountPanel() {
		
		//Set title and default window close operation.
		this.setTitle("Chat Client");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		//Create and display all GUI components.
		SpringLayout l1 = new SpringLayout();
		panel1 = new JPanel(l1);
		
		enter = new JLabel("Enter a username and password");
		requirements = new JLabel("Password must be at least 6 characters");
		nameLabel = new JLabel("Name: ");
		pwLabel = new JLabel("Password: ");
		verifyLabel = new JLabel("Verify Password: ");
		
		name = new JTextField(16);
		password = new JPasswordField(16);
		verify = new JPasswordField(16);
		password.setEchoChar('\u25CF');
		verify.setEchoChar('\u25CF');
		
		
		submit = new JButton("Submit");
		cancel = new JButton("Cancel");
		
		/*
		 * If either Submit or Cancel is pressed, call a new instance of CreateAccountControl,
		 * passing the current instance of CreateAccountPanel as an argument.
		 */
		submit.addActionListener(new CreateAccountControl(this));
		cancel.addActionListener(new CreateAccountControl(this));

		/*
		 * This is an example of how to use SpringLayout. SpringLayout requires more detailed
		 * inputs, but it frees you from the grid constraints of other layouts and basically
		 * allows you to position anything anywhere. Positions are all relative to panel edges
		 * and other objects.
		 */
		l1.putConstraint(SpringLayout.NORTH, enter, 20, SpringLayout.NORTH, panel1);
		l1.putConstraint(SpringLayout.HORIZONTAL_CENTER, enter, 0, SpringLayout.HORIZONTAL_CENTER, panel1);
		l1.putConstraint(SpringLayout.NORTH, requirements, 5, SpringLayout.SOUTH, enter);
		l1.putConstraint(SpringLayout.HORIZONTAL_CENTER, requirements, 0, SpringLayout.HORIZONTAL_CENTER, panel1);
		l1.putConstraint(SpringLayout.NORTH, nameLabel, 12, SpringLayout.SOUTH, requirements);
		l1.putConstraint(SpringLayout.HORIZONTAL_CENTER, nameLabel, -60, SpringLayout.HORIZONTAL_CENTER, panel1);
		l1.putConstraint(SpringLayout.NORTH, pwLabel, 8, SpringLayout.SOUTH, nameLabel);
		l1.putConstraint(SpringLayout.HORIZONTAL_CENTER, pwLabel, -73, SpringLayout.HORIZONTAL_CENTER, panel1);
		l1.putConstraint(SpringLayout.NORTH, verifyLabel, 8, SpringLayout.SOUTH, pwLabel);
		l1.putConstraint(SpringLayout.HORIZONTAL_CENTER, verifyLabel, -91, SpringLayout.HORIZONTAL_CENTER, panel1);
		l1.putConstraint(SpringLayout.NORTH, submit, 50, SpringLayout.SOUTH, verifyLabel);
		l1.putConstraint(SpringLayout.HORIZONTAL_CENTER, submit, -50, SpringLayout.HORIZONTAL_CENTER, panel1);
		l1.putConstraint(SpringLayout.NORTH, cancel, 50, SpringLayout.SOUTH, verifyLabel);
		l1.putConstraint(SpringLayout.HORIZONTAL_CENTER, cancel, 50, SpringLayout.HORIZONTAL_CENTER, panel1);
		
		l1.putConstraint(SpringLayout.NORTH, name, 10, SpringLayout.SOUTH, requirements);
		l1.putConstraint(SpringLayout.HORIZONTAL_CENTER, name, 50, SpringLayout.HORIZONTAL_CENTER, panel1);
		l1.putConstraint(SpringLayout.NORTH, password, 5, SpringLayout.SOUTH, name);
		l1.putConstraint(SpringLayout.HORIZONTAL_CENTER, password, 50, SpringLayout.HORIZONTAL_CENTER, panel1);
		l1.putConstraint(SpringLayout.NORTH, verify, 5, SpringLayout.SOUTH, password);
		l1.putConstraint(SpringLayout.HORIZONTAL_CENTER, verify, 50, SpringLayout.HORIZONTAL_CENTER, panel1);
		
		panel1.add(enter);
		panel1.add(requirements);
		panel1.add(nameLabel);
		panel1.add(name);
		panel1.add(pwLabel);
		panel1.add(password);
		panel1.add(verifyLabel);
		panel1.add(verify);
		panel1.add(submit);
		panel1.add(cancel);
		
		this.add(panel1);
		this.setSize(350, 300);
		this.setVisible(true);
	}
	
	public void setCreatedUsername() {
		this.username = name.getText();
	}
	
	public void setCreatedPassword() {
		this.pw = String.valueOf(password.getPassword());
	}
	
	public void setVerify() {
		this.check = String.valueOf(verify.getPassword());
	}
	
	public String getCreatedUsername() {
		return this.username;
	}
	
	public String getCreatedPassword() {
		return this.pw;
	}
	
	public String getVerifiedPassword() {
		return this.check;
	}
	
	public static JLabel getEnter(){
		return enter;
	}
	
	public JTextField getNameField() {
		return name;
	}
	
	public JPasswordField getPassword() {
		return password;
	}
	
	public JPasswordField getVerify() {
		return verify;
	}
	
	public static JButton getSubmit() {
		return submit;
	}
	
	//This is called from notifyInvalid() if account data was found to be invalid.
	public static void error() {
		enter.setText("Username/password invalid. Please reenter.");
		enter.setForeground(Color.RED);
		name.setText("");
		password.setText("");
		verify.setText("");
	}
}
