/*
 * David Black
 * Lab 7 Out: CreateAccountControl.java
 * Software Engineering T-Th 4:05pm
 * Dr. Mark Smith
 */

package chess;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.Serializable;

public class CreateAccountControl implements ActionListener, Serializable{
	private static final long serialVersionUID = 1L;
	//Class variable declarations.
	private CreateAccountData createData;
	private transient CreateAccountPanel createPanel;
	
	//Constructor. Accepts the current Create Account window as an argument.
	public CreateAccountControl(CreateAccountPanel createPanel){
		this.createPanel = createPanel;
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Submit")) {
			createPanel.setCreatedUsername();
			createPanel.setCreatedPassword();
			createPanel.setVerify();
			
			if(createPanel.getVerifiedPassword().equals(createPanel.getCreatedPassword()) && 
					createPanel.getCreatedPassword().length() >= 6) {
				createData = new CreateAccountData(createPanel.getCreatedUsername(), 
						createPanel.getCreatedPassword());
				
				try {
					System.out.println("Sending CreateData to server");
					ChessClient.getClient().sendToServer(createData);
				}
				catch(IOException e1){
					e1.printStackTrace();
				}
			}
			else
				CreateAccountPanel.error();
		}
		else if(e.getActionCommand().equals("Cancel")) {
			try {
				ChessClient.getClient().sendToServer(true);

			}
			catch(IOException e2) {
				e2.printStackTrace();
			}
			new InitialPanel();
			createPanel.dispose();
		}
	}
}
