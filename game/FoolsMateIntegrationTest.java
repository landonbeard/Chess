package chess;

import static org.junit.Assert.*;

import org.junit.Test;

public class FoolsMateIntegrationTest {
	private ChessBoard startBoard;
	private ChessBoard whiteKingPawnMove;
	private ChessBoard blackKingPawnMove;
	private ChessBoard whiteKingBishopMove;
	private ChessBoard blackQueenRookPawnMove;
	private ChessBoard whiteQueenMove;
	private ChessBoard blackQueenKnightPawnMove;
	private ChessBoard whiteQueenCheckmate;
	
	/*
	This test programmatically plays the game to a Fool's Mate, asserting True if checkmate has been reached.
	Tests functionality of the Board and Move classes, which in turn unify and utilize most other non-GUI classes.
	*/
	@Test
	public void test() {
		//Generate the starting board.
		this.startBoard = ChessBoard.initializeStartBoard();
		
		//King's Pawn opening
		this.whiteKingPawnMove = moveMaker(startBoard, 52, 36);
		    	
		//Black King's Pawn defense
		this.blackKingPawnMove = moveMaker(whiteKingPawnMove, 12, 28);
		    	
		//White King's Bishop center attack setup
		this.whiteKingBishopMove = moveMaker(blackKingPawnMove, 61, 34);
		    	
		//Black Queen's Rook Pawn attack setup
		this.blackQueenRookPawnMove = moveMaker(whiteKingBishopMove, 8, 16);
		    	
		//White Queen Fool's Mate setup
		this.whiteQueenMove = moveMaker(blackQueenRookPawnMove, 59, 45);
		    	
		//Black Queen's Knight Pawn attack setup
		this.blackQueenKnightPawnMove = moveMaker(whiteQueenMove, 9, 25);
		    	
		//White Queen checkmates Black King
		this.whiteQueenCheckmate = moveMaker(blackQueenKnightPawnMove, 45, 13);
		    	
		//This displays a String version of each board that was generated after every move.
		System.out.println(startBoard);
		System.out.println(whiteKingPawnMove);
		System.out.println(blackKingPawnMove);
		System.out.println(whiteKingBishopMove);
		System.out.println(blackQueenRookPawnMove);
		System.out.println(whiteQueenMove);
		System.out.println(blackQueenKnightPawnMove);
		System.out.println(whiteQueenCheckmate);
		    	
		//Checks to see if checkmate has been reached.
		assertTrue(whiteQueenCheckmate.currentPlayer().inCheckMate());
	}
	
	private ChessBoard moveMaker(ChessBoard oldBoard, int startCoord, int destinationCoord) {
		/*
		 * Create a new Move object, passing coordinates and a board object to the checkMoveValidity method.
		 * This class determines whether or not a particular move is legal or illegal, among other traits.
		 * If illegal, the move ultimately will not be made.
		 */
		Move move = Move.MoveFilter.checkMoveValidity(oldBoard, startCoord, destinationCoord);
		
		/*
		 * If the move was legal, it will be made in the makeMove method. The new board configuration is then
		 * stored in a MoveInProgress object. 
		 */
		MoveInProgress inProgress = oldBoard.currentPlayer().makeMove(move);
		
		//Get the new board configuration from the MoveInProgress object.
		ChessBoard newBoard = inProgress.getBoardAfterMove();
		
		//Return the new board object.
		return newBoard;
	}

}
