/*
  * David Black
 * Lab 7 Out: LoginControl.java
 * Software Engineering T-Th 4:05pm
 * Dr. Mark Smith
 */

package chess;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.Serializable;

public class LoginControl implements ActionListener, Serializable {
	private static final long serialVersionUID = 1L;
	//Class variable declarations
	private LoginPanel loginPanel;
	private LoginData loginData;
	private String username;
	private String pw;
	
	//Constructor. Takes the current instance of LoginPanel as an argument.
	public LoginControl(LoginPanel loginPanel) {
		this.loginPanel = loginPanel;
	}
	
	//Event handler.
	public void actionPerformed(ActionEvent e) {
		/*
		 * If Submit, gets the current contents of the username and password fields and
		 * passes them to a new instance of LoginData. A new chatClient is created,
		 * passing host ip and port number. The chatClient object then sends the new loginData
		 * object to the server where the username/password will be verified.
		 * 
		 * If Cancel, call a new instance of InitialPanel and kill the current window.
		 */
		if(e.getActionCommand().equals("Submit")) {
			this.username = loginPanel.getNameField();
			this.pw = loginPanel.getPasswordField();

			loginData = new LoginData(username, pw);
			
			try {
				System.out.println("Sending LoginData to server");
				ChessClient.getClient().sendToServer(loginData);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		else if(e.getActionCommand().equals("Cancel")) {
			try {
				ChessClient.getClient().sendToServer(true);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			new InitialPanel();
    		loginPanel.dispose();
		}
	}
}
