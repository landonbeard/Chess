package chess;

import chess.PieceAlliances;
import chess.ChessBoard;
import chess.Move;
import chess.King;
import chess.Piece;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class Player implements Serializable{
	private static final long serialVersionUID = 1L;
	protected final ChessBoard board;
    protected final King playerKing;
    protected final Collection<Move> legalMoves;
    private final boolean inCheck;

    Player(final ChessBoard board, final Collection<Move> legalMoves,
           final Collection<Move> opponentMoves){
        this.board = board;
        this.playerKing = determineIfKing();
        this.inCheck = !Player.findAttacksOnPlayer(this.playerKing.getPiecePosition(), opponentMoves).isEmpty();
        
        List<Move> temp = new ArrayList<>();
        temp.addAll(legalMoves);
        temp.addAll(findCastleMoves(legalMoves, opponentMoves));

        this.legalMoves = temp;
    }

    public King getPlayerKing(){
        return this.playerKing;
    }

    public Collection<Move> getLegalMoves(){
        return this.legalMoves;
    }

    protected static Collection<Move> findAttacksOnPlayer(int piecePosition, Collection<Move> opponentMoves){
        final List<Move> attackMoves = new ArrayList<>();

        for(final Move move: opponentMoves){
            if(piecePosition == move.getMoveDestinationCoordinate())
                attackMoves.add(move);
        }

        return attackMoves;
    }

    private King determineIfKing() {
        for(final Piece piece : getLivePieces()){
            if(piece.getPieceClass().isKing()){
                return (King)piece;
            }
        }
        throw new RuntimeException("Invalid state");
    }

    public boolean isMoveLegal(final Move move){
        return this.legalMoves.contains(move);
    }

    public boolean inCheck(){
        return this.inCheck;
    }

    public boolean inCheckMate(){
        return this.inCheck && !hasEscapeMoves();
    }

    protected boolean hasEscapeMoves() {
        for(final Move move : this.legalMoves){
            final MoveInProgress transition = makeMove(move);

            if(transition.getMoveOutcome().isDone()){
                return true;
            }
        }

        return false;
    }

    public boolean inStaleMate(){
        return !this.inCheck && !hasEscapeMoves();
    }

    public boolean isCastled(){
        return false;
    }

    public MoveInProgress makeMove(final Move move){
        if(!isMoveLegal(move)){
            return new MoveInProgress(this.board, move, MoveOutcome.ILLEGAL_MOVE);
        }

        final ChessBoard changingBoard = move.executeMove();

        final Collection<Move> kingAttacks = Player.findAttacksOnPlayer(changingBoard.currentPlayer().getOpponent().getPlayerKing().getPiecePosition(),
                changingBoard.currentPlayer().getLegalMoves());

        if(!kingAttacks.isEmpty()){
            return new MoveInProgress(this.board, move, MoveOutcome.LEAVES_PLAYER_IN_CHECK);
        }

        return new MoveInProgress(changingBoard, move, MoveOutcome.DONE);
    }

    public abstract Collection<Piece> getLivePieces();
    public abstract PieceAlliances getAlliance();
    public abstract Player getOpponent();
    protected abstract Collection<Move> findCastleMoves(Collection<Move> playerLegals, Collection<Move> opponentLegals);
}
