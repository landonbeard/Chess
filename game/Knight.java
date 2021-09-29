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

import static chess.Move.*;

/*
This class defines the behavior of a knight. Each piece type maintains an array of offsets which are used to determine
the legal moves for a given piece from any given position on the board (or in the Tile HashMap, more accurately). Each
piece type inherits from the abstract Piece class, overriding the findAllLegalMoves method.
 */
public class Knight extends Piece implements Serializable{
	private static final long serialVersionUID = 1L;
	
	//Array containing the knight's offsets.
    private final static int[] MOVE_OFFSETS = {-17, -15, -10, -6, 6, 10, 15, 17};

    //Constructor. Takes position and alliance as arguments, passing them to the Piece constructor.
    public Knight(final int piecePosition, final PieceAlliances pieceAlliance) {
        super(PieceClass.KNIGHT, piecePosition, pieceAlliance, true);
    }

    public Knight(final int piecePosition, final PieceAlliances pieceAlliance, final boolean isFirstMove) {
        super(PieceClass.KNIGHT, piecePosition, pieceAlliance, isFirstMove);
    }

    @Override
    public Knight movePiece(Move move) {
        return new Knight(move.getMoveDestinationCoordinate(), move.getMovedPiece().getPieceAlliance());
    }

    @Override
    public String toString(){
        return PieceClass.KNIGHT.toString();
    }

    //Column exclusion methods used by the findLegalMoves method.
    private static boolean isfirstColumn(final int currentPosition, final int candidateOffset){
        return BoardOperations.COLUMN_1[currentPosition] && ((candidateOffset == -17) || (candidateOffset == -10) ||
                (candidateOffset == 6) || (candidateOffset == 15));
    }

    private static boolean isSecondColumn(final int currentPosition, final int candidateOffset){
        return BoardOperations.COLUMN_2[currentPosition] && ((candidateOffset == -10) || (candidateOffset == -6));
    }

    private static boolean isSeventhColumn(final int currentPosition, final int candidateOffset){
        return BoardOperations.COLUMN_7[currentPosition] && ((candidateOffset == -6) || (candidateOffset == 10));
    }

    private static boolean isEighthColumn(final int currentPosition, final int candidateOffset){
        return BoardOperations.COLUMN_8[currentPosition] && ((candidateOffset == -15) || (candidateOffset == -6) ||
                (candidateOffset == 10) || (candidateOffset == -17));
    }
    
    /*
    Overridden method from the Piece class, which calculates all possible legal moves from a piece's current position
    on the board. The method begins by creating a List of legal moves which will be used to store legal moves. It then
    iterates through a piece's list of offsets, adding the current piece's position coordinate to each offset: each
    destination value is then stored in the list of legal moves.

    Additionally, the class checks to see whether or not a move is within the board's bounds, or doesn't move off the
    board onto the other side of the board (moving left and ending up on the right side, for example, since Tile
    coordinate values are a HashMap). It does this by passing each destination coordinate to the column exclusion
    methods in the BoardOperations class, checking to see if any of them return true.

    The method then checks the Tile of each destination coordinate to see if that Tile is occupied by another piece.
    If the Tile is unoccupied, the move is added to the list of legal non-attack moves. If the Tile is occupied, the
    method gets the type and alliance of the piece occupying the destination tile. If the alliance of the occupying
    piece differs from the alliance of the current moving piece, the move is added to the list of legal moves as an
    attack move.
     */
    @Override
    public Collection<Move> findAllLegalMoves(ChessBoard board) {
        //ArrayList of moves used to store all possible legal moves.
        final List<Move> legalMoves = new ArrayList<>();

        //Iterate the constant class array of piece offsets.
        for(final int possibleMove : MOVE_OFFSETS){
            //Add the current coordinate of the piece to each offset, storing as a destination coordinate.
            final int destinationCoord = this.piecePosition + possibleMove;

            //If the destination coordinate is within legal board bounds (between 0 and 64)...
            if(BoardOperations.isCoordinateValid(destinationCoord)){
                //Check to see if the piece is in the first column and its destination would move it outside the board.
                if(isfirstColumn(this.piecePosition, possibleMove) ||
                        isSecondColumn(this.piecePosition, possibleMove) ||
                        isSeventhColumn(this.piecePosition, possibleMove) ||
                        isEighthColumn(this.piecePosition, possibleMove)){
                    continue;   //Continue the loop if any of these methods return true
                }

                //Store the tile of the destination coordinate.
                final Tile destinationTile = board.getTile(destinationCoord);

                //If the tile is not marked as occupied by the Tile class...
                if(!destinationTile.isOccupied()){
                    //Add the move to the list of legal moves as a non-attack move.
                    legalMoves.add(new MajorMove(board, this, destinationCoord));
                }
                else{
                    //Otherwise, get the type and alliance of the piece at the destination.
                    final Piece destinationPiece = destinationTile.getPiece();
                    final PieceAlliances pieceAlliance = destinationPiece.getPieceAlliance();

                    //If the piece at the occupied tile's alliance differs...
                    if(this.pieceAlliance != pieceAlliance){
                        //Add the move to the list of legal moves as an attack move.
                        legalMoves.add(new MajorAttackMove(board, this, destinationCoord, destinationPiece));
                    }
                }
            }
        }

        //Return the list of legal moves.
        return legalMoves;
    }
}
