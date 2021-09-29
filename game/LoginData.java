/*
 * David Black
 * Lab 7 Out: LoginData.java
 * Software Engineering T-Th 4:05pm
 * Dr. Mark Smith
 */

package chess;

import java.awt.Window;
import java.io.IOException;
import java.io.Serializable;

public class LoginData implements Serializable{
	private static final long serialVersionUID = 1L;
	//Class variable declarations.
	private String username;
	private String password;
	private final Integer i = 1;
	private int userId;
	private boolean valid;
	
	//Constructor. Accepts a username and password from LoginControl.
	public LoginData(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public String getUser() {
		return username;
	}

	public void setUser(String username) {
		this.username = username;
	}
	
	public String getPass() {
		return password;
	}

	public void setPass(String password) {
		this.password = password;
	}
	
	public void setValid(boolean valid) {
		System.out.println("setValid: " + valid);
		this.valid = valid;
	}
	
	public boolean getValid() {
		return this.valid;
	}
	
	public int getId() {
		return this.userId;
	}
	
	public void setId(int id) {
		this.userId = id;
	}
	
	/*
	 * Called from handleMessageFromServer in ChatClient using a processed LoginData object.
	 * 
	 * This method checks to see if the current instance of LoginData has a 'valid' field 
	 * set to true or false. This was set in DatabaseFile server-side after the original 
	 * LoginData object was passed to the server during a login attempt.
	 * 
	 * If valid is true, kill the current Login window and open ContactPanel.
	 * If valid is false, call the static error method of LoginPanel.
	 */
	public void notifyInvalid() {
		if(this.valid) {
			//killWindow();
			try {
				LoginPanel.getSubmit().setEnabled(false);
				LoginPanel.getEnter().setText("Login Successful. Waiting for another player...");
				ChessClient.getClient().sendToServer(i);
			}
			catch(IOException e) {
				e.printStackTrace();
			}
		}
		else {
			LoginPanel.error();
		}
	}
	
	//Kills all instances of the current window.
	public void killWindow() {
		Window[] windows = LoginPanel.getWindows();
		
		for(Window window : windows)
			window.dispose();
	}
}
