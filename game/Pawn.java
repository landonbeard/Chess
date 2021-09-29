package chess;

import chess.PieceAlliances;
import chess.ChessBoard;
import chess.BoardOperations;
import chess.Move;
import chess.Piece;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Pawn extends Piece implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private final static int[] MOVE_OFFSETS = {8, 16, 7, 9};

    public Pawn(final int piecePosition, final PieceAlliances pieceAlliance) {
        super(PieceClass.PAWN, piecePosition, pieceAlliance, true);
    }

    public Pawn(final int piecePosition, final PieceAlliances pieceAlliance, final boolean isFirstMove) {
        super(PieceClass.PAWN, piecePosition, pieceAlliance, isFirstMove);
    }
    

    public Piece getPromotionPiece(){
        return new Queen(this.piecePosition, this.pieceAlliance, false);
    }

    @Override
    public Pawn movePiece(Move move) {
        return new Pawn(move.getMoveDestinationCoordinate(), move.getMovedPiece().getPieceAlliance());
    }

    @Override
    public String toString(){
        return PieceClass.PAWN.toString();
    }

    @Override
    public Collection<Move> findAllLegalMoves(ChessBoard board) {
        final List<Move> legalMoves = new ArrayList<>();

        for(final int possibleMove : MOVE_OFFSETS){

            int destinationCoord = this.piecePosition + (this.getPieceAlliance().getDirection() * possibleMove);

            if(!BoardOperations.isCoordinateValid(destinationCoord))
                continue;

            if(possibleMove == 8 && !board.getTile(destinationCoord).isOccupied())
            {
                if(this.pieceAlliance.isPawnPromotionSquare(destinationCoord))
                    legalMoves.add(new Move.PawnPromotion(new Move.PawnMove(board, this, destinationCoord)));
                else
                    legalMoves.add(new Move.PawnMove(board, this, destinationCoord));
            }
            else if(possibleMove == 16 && this.isFirstMove() &&
                    ((BoardOperations.ROW_7[this.piecePosition] && this.getPieceAlliance().isBlack()) ||
                    (BoardOperations.ROW_2[this.piecePosition] && this.getPieceAlliance().isWhite())))
            {
                final int behindDestination = this.piecePosition + (this.pieceAlliance.getDirection() * 8);

                if(!board.getTile(behindDestination).isOccupied() &&
                        !board.getTile(destinationCoord).isOccupied())
                    legalMoves.add(new Move.PawnJump(board, this, destinationCoord));
            }
            else if(possibleMove == 7 &&
                    !((BoardOperations.COLUMN_8[this.piecePosition] &&
                            this.pieceAlliance.isWhite()) ||
                            (BoardOperations.COLUMN_1[this.piecePosition] &&
                                    this.pieceAlliance.isBlack())))
            {
                if(board.getTile(destinationCoord).isOccupied())
                {
                    final Piece destinationPiece = board.getTile(destinationCoord).getPiece();

                    if(this.pieceAlliance != destinationPiece.getPieceAlliance())
                    {
                        if(this.pieceAlliance.isPawnPromotionSquare(destinationCoord))
                            legalMoves.add(new Move.PawnPromotion(new Move.PawnAttackMove(board, this,
                                    destinationCoord, destinationPiece)));
                        else
                            legalMoves.add(new Move.PawnAttackMove(board, this, destinationCoord,
                                    destinationPiece));
                    }
                }
                else if(board.getEnPassant() != null)
                {
                    if(board.getEnPassant().getPiecePosition() == (this.piecePosition +
                            (this.pieceAlliance.getOppositeDirection())))
                    {
                        final Piece destinationPiece = board.getEnPassant();

                        if(this.pieceAlliance != destinationPiece.getPieceAlliance())
                            legalMoves.add(new Move.EnPassant(board, this, destinationCoord,
                                    destinationPiece));
                    }
                }
            }
            else if(possibleMove == 9 &&
                    !((BoardOperations.COLUMN_8[this.piecePosition] &&
                            this.pieceAlliance.isBlack()) ||
                            (BoardOperations.COLUMN_1[this.piecePosition] &&
                                    this.pieceAlliance.isWhite())))
            {
                if(board.getTile(destinationCoord).isOccupied())
                {
                    final Piece destinationPiece = board.getTile(destinationCoord).getPiece();

                    if (this.pieceAlliance != destinationPiece.getPieceAlliance())
                    {
                        if(this.pieceAlliance.isPawnPromotionSquare(destinationCoord))
                            legalMoves.add(new Move.PawnPromotion(new Move.PawnAttackMove(board, this,
                                    destinationCoord, destinationPiece)));
                        else
                            legalMoves.add(new Move.PawnAttackMove(board, this, destinationCoord,
                                    destinationPiece));
                    }
                }
                else if(board.getEnPassant() != null)
                {
                    if(board.getEnPassant().getPiecePosition() == (this.piecePosition -
                            (this.pieceAlliance.getOppositeDirection())))
                    {
                        final Piece destinationPiece = board.getEnPassant();

                        if(this.pieceAlliance != destinationPiece.getPieceAlliance())
                            legalMoves.add(new Move.EnPassant(board, this, destinationCoord,
                                    destinationPiece));
                    }
                }
            }
        }

        return legalMoves;
    }
}
