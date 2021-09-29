package chess;

import java.io.Serializable;

import chess.ChessBoard;
import chess.Move;

public class MoveInProgress implements Serializable{

	private static final long serialVersionUID = 1L;
	private final ChessBoard inProgress;
    private final MoveOutcome moveOutcome;

    public MoveInProgress(final ChessBoard board, final Move move, final MoveOutcome moveOutcome) {
        this.inProgress = board;
        this.moveOutcome = moveOutcome;
    }

    public MoveOutcome getMoveOutcome(){
        return this.moveOutcome;
    }

    public ChessBoard getBoardAfterMove(){
        return this.inProgress; 
    }
}
