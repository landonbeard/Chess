package chess;

import chess.PieceAlliances;
import chess.ChessBoard;
import chess.Move;

import java.io.Serializable;
import java.util.Collection;

/*
The Piece class defines a chess piece, comprised of four components: a position on the board (a coordinate), 
an alliance (black or white), a class (type of piece), and a boolean operator indicating whether or not it is a 
piece's first move (necessary for things like pawn jumps and castling). Its constructor takes all of these as arguments.
 */
public abstract class Piece implements Serializable{
	private static final long serialVersionUID = 1L;
	
	//Class variables to store position and alliance.
    protected final int piecePosition;
    protected final PieceAlliances pieceAlliance;
    protected final boolean isFirstMove;
    protected final PieceClass pieceClass;

    //Constructor
    Piece(final PieceClass pieceClass, final int piecePosition, final PieceAlliances pieceAlliance, final boolean isFirstMove){
        this.pieceClass = pieceClass;
        this.piecePosition = piecePosition;
        this.pieceAlliance = pieceAlliance;
        this.isFirstMove = isFirstMove;
    }

    @Override
    public boolean equals(final Object other){
        if(this == other){
            return true;
        }

        if(!(other instanceof Piece)){
            return false;
        }

        final Piece otherPiece = (Piece)other;

        return piecePosition == otherPiece.getPiecePosition() && pieceClass == otherPiece.getPieceClass() &&
                pieceAlliance == otherPiece.getPieceAlliance() && isFirstMove == otherPiece.isFirstMove();
    }

    //This method returns a piece alliance.
    public PieceAlliances getPieceAlliance(){
        return this.pieceAlliance; 
    }

    public boolean isFirstMove(){
        return this.isFirstMove;
    }

    public int getPiecePosition(){
        return this.piecePosition;
    }

    public PieceClass getPieceClass(){
        return this.pieceClass;
    }

    public int getPieceValue(){
        return this.pieceClass.getPieceValue();
    }

    /*
    This method can is used in a subclass to calculate the legal moves for a given piece. Takes
    the current state of the board as an argument.
     */
    public abstract Collection<Move> findAllLegalMoves(final ChessBoard board);

    public abstract Piece movePiece(Move move);

    public enum PieceClass{

        PAWN("P", 100){
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() { return false; }
        },
        KNIGHT("N", 300){
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() { return false; }
        },
        BISHOP("B", 300){
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() { return false; }
        },
        ROOK("R", 500){
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() { return true; }
        },
        QUEEN("Q", 900){
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() { return false; }
        },
        KING("K", 10000){
            @Override
            public boolean isKing() {
                return true;
            }

            @Override
            public boolean isRook() { return false; }
        };

        private String pieceName;
        private int pieceValue;

        PieceClass(final String pieceName, final int pieceValue){
            this.pieceValue = pieceValue;
            this.pieceName = pieceName;
        }

        @Override
        public String toString(){
            return this.pieceName;
        }

        public int getPieceValue(){
            return this.pieceValue;
        }

        public abstract boolean isKing();

        public abstract boolean isRook();
    }
}
