package chess;

import chess.PieceAlliances;
import chess.ChessBoard;
import chess.Move;
import chess.Tile;
import chess.Piece;
import chess.Rook;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BlackPlayer extends Player implements Serializable{
	private static final long serialVersionUID = 1L;

	public BlackPlayer(final ChessBoard board, final Collection<Move> whiteLegalMoves,
                       final Collection<Move> blackLegalMoves) {
        super(board, blackLegalMoves, whiteLegalMoves);
    }

    @Override
    public Collection<Piece> getLivePieces() {
        return this.board.getBlackPieces();
    }

    @Override
    public PieceAlliances getAlliance() {
        return PieceAlliances.BLACK;
    }

    @Override
    public Player getOpponent() {
        return this.board.whitePlayer();
    }

    @Override
    protected Collection<Move> findCastleMoves(final Collection<Move> playerLegals,
                                                    final Collection<Move> opponentLegals) {
        final List<Move> kingCastles = new ArrayList<>();

        if(this.playerKing.isFirstMove() && !this.inCheck()){
            if(!this.board.getTile(5).isOccupied() && !this.board.getTile(6).isOccupied()){
                final Tile rookTile = this.board.getTile(7);

                if(rookTile.isOccupied() && rookTile.getPiece().isFirstMove()){
                    if(Player.findAttacksOnPlayer(5, opponentLegals).isEmpty() &&
                        Player.findAttacksOnPlayer(6, opponentLegals).isEmpty() &&
                        rookTile.getPiece().getPieceClass().isRook()){
                        kingCastles.add(new Move.KingCastle(this.board, this.playerKing, 6,
                                (Rook) rookTile.getPiece(), rookTile.getTileCoord(),
                                5));
                    }
                }
            }
            if(!this.board.getTile(1).isOccupied() &&
                    !this.board.getTile(2).isOccupied() &&
                    !this.board.getTile(3).isOccupied()){
                final Tile rookTile = this.board.getTile(0);

                if(rookTile.isOccupied() && rookTile.getPiece().isFirstMove() &&
                    Player.findAttacksOnPlayer(2, opponentLegals).isEmpty() &&
                    Player.findAttacksOnPlayer(3, opponentLegals).isEmpty() &&
                    rookTile.getPiece().getPieceClass().isRook()){
                    kingCastles.add(new Move.QueenCastle(this.board, this.playerKing, 2,
                            (Rook) rookTile.getPiece(), rookTile.getTileCoord(),
                            3));
                }
            }
        }
        return kingCastles;
    }
}
