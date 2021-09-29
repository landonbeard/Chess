package chess;

import chess.ChessBoard;
import chess.Tile;
import chess.Piece;
import chess.ChessBoardGUI;
import chess.ChessBoardGUI.MoveHistory;
import chess.CreateAccountData;
import chess.LoginData;
import ocsf.client.AbstractClient;

public class ChessClient extends AbstractClient {
	private ChessBoard board;
	protected ChessBoardGUI chesstable;
	protected ChessBoardGUI chesstable2;
	private boolean allConnected;
	private static ChessClient client;
	private int tileID;
	private Tile startTile;
	private Piece movedPiece;
	private MoveHistory moveHistory;
	private boolean isWhite;
	private boolean isBlack;
	
	public ChessClient(String host, int port) {
		super(host, port);
	}

	public void setClient(ChessClient client) {
		ChessClient.client = client;
	}
	
	public static ChessClient getClient() {
		return client;
	}
	
	public void setSourceTile(Tile sourceTile) {
		this.startTile = sourceTile;
	}
	
	public void setMovedPiece(Piece humanMovedPiece) {
		this.movedPiece = humanMovedPiece;
	}
	
	public void setBoard(ChessBoard board) {
		this.board = board;
	}
	
	public boolean isWhite() {
		return isWhite;
	}
	
	public boolean isBlack() {
		return isBlack;
	}
	
	public void setMoveLog(MoveHistory moveLog) {
		this.moveHistory = moveLog;
	}
	
	public void setTile(int tileID) {
		this.tileID = tileID;
	}
	
	@Override
	protected void handleMessageFromServer(Object arg0) {
		System.out.println("Handling message from server");
		if(arg0 instanceof ChessBoard && allConnected == true) {
			setBoard((ChessBoard) arg0);
			Thread update = new Thread(new Runnable() {

				@Override
				public void run() {
					chesstable.updateBoard(board, tileID, startTile, movedPiece, moveHistory);
				}
			});
			update.start();
		}
		if(arg0 instanceof ChessBoard && allConnected == false) {
			allConnected = true;
			setBoard((ChessBoard) arg0);
			Thread setup = new Thread(new Runnable() {

				@Override
				public void run() {
					chesstable = new ChessBoardGUI(board, client, isWhite, isBlack);
				}
			});
			setup.start();
		}
		if(arg0 instanceof Tile) {
			setSourceTile((Tile) arg0);
			System.out.println(startTile);
		}
		if(arg0 instanceof Piece) {
			setMovedPiece((Piece) arg0);
			System.out.println(movedPiece);
		}
		if(arg0 instanceof MoveHistory) {
			setMoveLog((MoveHistory) arg0);
		}
		if(arg0 instanceof Integer) {
			if((Integer) arg0 == 1) {
				this.isWhite = true;
				this.isBlack = false;
			}
			if((Integer) arg0 == 2) {
				this.isWhite = false;
				this.isBlack = true;
			}
		}
		
		if(arg0 instanceof LoginData) {
			System.out.println("Login valid: " + ((LoginData) arg0).getValid());
			((LoginData) arg0).notifyInvalid();
		}
		else if(arg0 instanceof CreateAccountData) {
			System.out.println("Create valid: " + ((CreateAccountData) arg0).getValid());
			((CreateAccountData) arg0).notifyInvalid();
		}
	}
}
