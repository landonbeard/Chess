/*
 * David Black
 * Lab 7 Out: CreateAccountData.java
 * Software Engineering T-Th 4:05pm
 * Dr. Mark Smith
 */

package chess;

import java.awt.Window;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;

public class CreateAccountData implements Serializable{
	private static final long serialVersionUID = 1L;
	private String username;
	private String password;
	private Integer i = 1;
	private boolean valid;
	Connection con;
	
	public CreateAccountData(String username, String password) {
		setUser(username);
		setPass(password);
	}
	
	//Getter for the current username.
	public String getUser() {
		return username;
	}

	//Setter for the current username.
	public void setUser(String username) {
		this.username = username;
	}
		
	//Getter for the current password.
	public String getPass() {
		return password;
	}

	//Setter for the current password.
	public void setPass(String password) {
		this.password = password;
	}
	
	public void setValid(boolean valid) {
		this.valid = valid;
	}
	
	public boolean getValid() {
		return this.valid;
	}
	
	/*
	 * Called from handleMessageFromServer in ChatClient using a processed CreateAccountData object.
	 * 
	 * This method checks to see if the current CreateAccountData object has a 'valid' field 
	 * set to true or false. This was set in DatabaseFile server-side after the original 
	 * CreateAccountData object was passed to the server during a create attempt.
	 * 
	 * If valid is true, kill the current Login window and open ContactPanel.
	 * If valid is false, call the static error method of CreateAccountPanel.
	 */
	public void notifyInvalid() {
		if(this.valid) {
			//killWindow();

			try {
				CreateAccountPanel.getSubmit().setEnabled(false);
				CreateAccountPanel.getEnter().setText("Account created. Waiting for another player...");
				ChessClient.getClient().sendToServer(i);
			}
			catch(IOException e) {
				e.printStackTrace();
			}
		}
		else {
			CreateAccountPanel.error();
		}
	}
	
	//Kills all instances of the current window.
	public void killWindow() {
		Window[] windows = CreateAccountPanel.getWindows();
		
		for(Window window : windows)
			window.dispose();
	}
}
