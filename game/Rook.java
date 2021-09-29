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

public class Rook extends Piece implements Serializable{
	private static final long serialVersionUID = 1L;
	
	//Array containing the bishop's offsets.
    private final static int[] MOVE_OFFSETS = {-8, -1, 1, 8};

    public Rook(final int piecePosition, final PieceAlliances pieceAlliance) {
        super(PieceClass.ROOK, piecePosition, pieceAlliance, true);
    }

    public Rook(final int piecePosition, final PieceAlliances pieceAlliance, final boolean isFirstMove){
        super(PieceClass.ROOK, piecePosition, pieceAlliance, isFirstMove);
    }

    @Override
    public Rook movePiece(Move move) {
        return new Rook(move.getMoveDestinationCoordinate(), move.getMovedPiece().getPieceAlliance());
    }

    @Override
    public String toString(){
        return PieceClass.ROOK.toString();
    }

    private static boolean isfirstColumn(final int currentPosition, final int candidateOffset){
        return BoardOperations.COLUMN_1[currentPosition] && candidateOffset == -1;
    }

    private static boolean isEighthColumn(final int currentPosition, final int candidateOffset){
        return BoardOperations.COLUMN_8[currentPosition] && candidateOffset == 1;
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
        for(final int possibleMove : MOVE_OFFSETS) {
            //Initially define the destination coordinate as the bishop's current position.
            int destinationCoord = this.piecePosition;

            //While the destination coordinate is within legal board bounds (between 0 and 64)...
            while (BoardOperations.isCoordinateValid(destinationCoord)) {

                if(isfirstColumn(destinationCoord, possibleMove) ||
                        isEighthColumn(destinationCoord, possibleMove)){
                    break;   //Break the loop if any of these methods return true
                }
                //Add the current offset to the bishop's current position.
                destinationCoord += possibleMove;

                //If the destination coordinate is within legal board bounds...
                if (BoardOperations.isCoordinateValid(destinationCoord)) {
                    //Store the tile of the destination coordinate.
                    final Tile destinationTile = board.getTile(destinationCoord);

                    //If the tile is not marked as occupied by the Tile class...
                    if (!destinationTile.isOccupied()) {
                        //Add the move to the list of legal moves as a non-attack move.
                        legalMoves.add(new Move.MajorMove(board, this, destinationCoord));
                    }
                    else {
                        //Otherwise, get the type and alliance of the piece at the destination.
                        final Piece destinationPiece = destinationTile.getPiece();
                        final PieceAlliances pieceAlliance = destinationPiece.getPieceAlliance();

                        //If the piece at the occupied tile's alliance differs...
                        if (this.pieceAlliance != pieceAlliance) {
                            //Add the move to the list of legal moves as an attack move.
                            legalMoves.add(new Move.MajorAttackMove(board, this, destinationCoord, destinationPiece));
                        }
                        break;
                    }
                }
            }
        }
        return legalMoves;
    }
}
