package chess;

import java.awt.Color;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JTextArea;

import chess.ChessBoard;
import chess.Tile;
import chess.Piece;
import chess.ChessBoardGUI.MoveHistory;
import chess.ServerGUI;
import chess.CreateAccountData;
import chess.LoginData;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

public class ChessServer extends AbstractServer {
	private JTextArea log;
	private JLabel status;
	private ChessBoard board;
	private ArrayList<ConnectionToClient> clients = new ArrayList<>();
	private Integer playersNum = 0;
	private boolean allConnected = false;
	private CreateAccountData createData;
	private Database database = new Database();
	private LoginData loginData;
	
	public ChessServer(int port, int timeout) {
		super(port);

		//Set the timeout value to the passed timeout value.
	    this.setTimeout(timeout);
	    
	    //Set the log and status objects to the current objects in the ServerGUI class.
	    setLog(ServerGUI.getLog());
	    setStatus(ServerGUI.getStatus());
	}

	@Override
	protected void handleMessageFromClient(Object arg0, ConnectionToClient arg1) {
		if(arg0 instanceof Integer) {
			System.out.println("Client has valid username. Registering as player.");
			clients.add(arg1);

			if(allConnected == false)
				playersNum++;
			System.out.println(playersNum);
			
			if(playersNum <= 2) {
				try {
					arg1.sendToClient(playersNum);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			
			if(playersNum == 2 && allConnected == false) {
				allConnected = true;
				for(ConnectionToClient c : this.clients) {
					try {
						c.sendToClient(board);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		if(arg0 instanceof ChessBoard) {
			sendToAllClients((ChessBoard) arg0);
		}
		if(arg0 instanceof Tile) {
			sendToAllClients((Tile) arg0);
		}
		if(arg0 instanceof Piece) {
			sendToAllClients((Piece) arg0);
		}
		if(arg0 instanceof MoveHistory) {
			sendToAllClients((MoveHistory) arg0);
		}
		if(arg0 instanceof CreateAccountData) {
			  createData = (CreateAccountData) arg0;
			  System.out.println("CreateData has been passed to server");
			  try {
				database.executeDML(createData, createData.getUser(), createData.getPass());
			} catch (SQLException e) {
				e.printStackTrace();
			}

			  try {
				  System.out.println("Sending updated createData to client: " + arg1.toString());
				  System.out.println(createData);
				arg1.sendToClient(createData);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}	  
		else if(arg0 instanceof LoginData) {
			System.out.println("LoginData has been passed to server");
			loginData = (LoginData) arg0;
			try {
				database.query(loginData, loginData.getUser(), loginData.getPass());
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
	
			try {
				System.out.println("Sending updated logindata to client: " + arg1.toString());
				System.out.println(loginData);
				arg1.sendToClient(loginData);
			} catch (IOException e) {
				e.printStackTrace();
			}
		 }
		else if(arg0 instanceof Boolean) {
			if(playersNum > 0)
				playersNum--;
			
			System.out.println("playersNum: " + playersNum);
		}
	}
	
	@Override
	public void serverStarted() {
		//Set the status and log to the current state of the ServerGUI objects.
		setStatus(this.status);
		setLog(this.log);
		
		//Update the status label to notify the user that the server is listening.
	    status.setText("Listening");
	    status.setForeground(Color.GREEN);
	    
	    
	    //Append a user notification to the Server Log.
	    log.append("Server Started\n");
	    this.board = ChessBoard.initializeStartBoard();
	}
	
	//If the server has been stopped update the log and status 
	public void serverStopped()
	{
		log.append("Server Stopped Accepting New Clients - "
				+ "Press Listen to Start Accepting New Clients\n");
		status.setText("Stopped");
		status.setForeground(Color.RED);
	}

	//If the server has been closed update the log and status
	protected void serverClosed()
	{
		log.append("Server and All Current Clients are Closed - Press Listen to Restart\n");
		status.setText("Close");
		status.setForeground(Color.RED);
	}
	
	//If there is a listening exception error print the error and update the log and status
	protected void listeningException(Throwable exception) 
	{
		log.append("Listening Exception: " + exception);
		exception.printStackTrace();

		log.append("Press Listen to Restart Server\n");
		status.setText("Exception Occurred when Listening");
		status.setForeground(Color.RED);
	}
	
	@Override
	public void clientConnected(ConnectionToClient client) {
		//Set the log to the current state of the ServerGUI log object.
		setLog(this.log);

		//Append the connected client's ID to the server log.
		log.append("Client" + client.getId() + " Connected\n");
	}
	
	//Sets the JTextArea object to the current state of the ServerGUI JTextArea object.
	public void setLog(JTextArea log) {
		this.log = ServerGUI.getLog();
	}
	  
	//Sets the JLabel object to the current state of the ServerGUI JLabel object.
	public void setStatus(JLabel status) {
		this.status = ServerGUI.getStatus();
	}
}
