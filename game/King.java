package chess;

import chess.PieceAlliances;
import chess.ChessBoard;
import chess.BoardOperations;
import chess.Move;
import chess.Tile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class King extends Piece implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private final static int[] MOVE_OFFSETS = {-9, -8, -7, -1, 1, 7, 8, 9};

    public King(final int piecePosition, final PieceAlliances pieceAlliance) {
        super(PieceClass.KING, piecePosition, pieceAlliance, true);
    }

    public King(final int piecePosition, final PieceAlliances pieceAlliance, final boolean isFirstMove) {
        super(PieceClass.KING, piecePosition, pieceAlliance, isFirstMove);
    }

    @Override
    public King movePiece(Move move) {
        return new King(move.getMoveDestinationCoordinate(), move.getMovedPiece().getPieceAlliance());
    }

    @Override
    public String toString(){
        return PieceClass.KING.toString();
    }

    private static boolean isfirstColumn(final int currentPosition, final int candidateOffset){
        return BoardOperations.COLUMN_1[currentPosition] && ((candidateOffset == -9) || (candidateOffset == -1) ||
                (candidateOffset == 7));
    }

    private static boolean isEighthColumn(final int currentPosition, final int candidateOffset){
        return BoardOperations.COLUMN_8[currentPosition] && ((candidateOffset == -7) || (candidateOffset == 1) ||
                (candidateOffset == 9));
    }
    
    @Override
    public Collection<Move> findAllLegalMoves(ChessBoard board) {
        final List<Move> legalMoves = new ArrayList<>();
        int destinationCoord;

        for(final int possibleMove : MOVE_OFFSETS){
            destinationCoord = this.piecePosition + possibleMove;

            if(isfirstColumn(this.piecePosition, possibleMove) ||
                    isEighthColumn(this.piecePosition, possibleMove)){
                continue;   //Continue the loop if any of these methods return true
            }

            if(BoardOperations.isCoordinateValid(destinationCoord)){
                final Tile destinationTile = board.getTile(destinationCoord);

                if(!destinationTile.isOccupied()){
                    //Add the move to the list of legal moves as a non-attack move.
                    legalMoves.add(new Move.MajorMove(board, this, destinationCoord));
                }
                else{
                    //Otherwise, get the type and alliance of the piece at the destination.
                    final Piece destinationPiece = destinationTile.getPiece();
                    final PieceAlliances pieceAlliance = destinationPiece.getPieceAlliance();

                    //If the piece at the occupied tile's alliance differs...
                    if(this.pieceAlliance != pieceAlliance){
                        //Add the move to the list of legal moves as an attack move.
                        legalMoves.add(new Move.MajorAttackMove(board, this, destinationCoord, destinationPiece));
                    }
                }
            }
        }

        return legalMoves;
    }
}
