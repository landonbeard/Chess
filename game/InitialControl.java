/*
 * David Black
 * Lab 7 Out: InitialControl.java
 * Software Engineering T-Th 4:05pm
 * Dr. Mark Smith
 */

package chess;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InitialControl implements ActionListener{
	//Class variable declarations.
	private InitialPanel initPanel;
	
	//Constructor. Accepts an InitialPanel object from InitialPanel.
	public InitialControl(InitialPanel initPanel) {
		this.initPanel = initPanel;	
	}
	
	//Event Handler.
	public void actionPerformed(ActionEvent e) {
		//Get which button was clicked.
		String action = e.getActionCommand();
		
		/*
		 * If Login, call a new instance of LoginPanel and kill the current window.
		 * If Create, call a new instance of CreateAccountPanel and kill the current window.
		 */
		if(action.equals("Login")) {
			new LoginPanel();
			this.initPanel.dispose();
		}
		else if(action.equals("Create")) {
			new CreateAccountPanel();
			this.initPanel.dispose();
		}
	}
}
