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

public class WhitePlayer extends Player implements Serializable{
	private static final long serialVersionUID = 1L;

	public WhitePlayer(final ChessBoard board, final Collection<Move> whiteLegalMoves,
                       final Collection<Move> blackLegalMoves) {
        super(board, whiteLegalMoves, blackLegalMoves);
    }

    @Override
    public Collection<Piece> getLivePieces() {
        return this.board.getWhitePieces();
    }

    @Override
    public PieceAlliances getAlliance() {
        return PieceAlliances.WHITE;
    }

    @Override
    public Player getOpponent() {
        return this.board.blackPlayer();
    }

    @Override
    protected Collection<Move> findCastleMoves(final Collection<Move> playerLegals,
                                                    final Collection<Move> opponentLegals) {
        final List<Move> kingCastles = new ArrayList<>();

        if(this.playerKing.isFirstMove() && !this.inCheck()){
            if(!this.board.getTile(61).isOccupied() && !this.board.getTile(62).isOccupied()){
                final Tile rookTile = this.board.getTile(63);

                if(rookTile.isOccupied() && rookTile.getPiece().isFirstMove()){
                    if(Player.findAttacksOnPlayer(61, opponentLegals).isEmpty() &&
                            Player.findAttacksOnPlayer(62, opponentLegals).isEmpty() &&
                            rookTile.getPiece().getPieceClass().isRook()) {
                        kingCastles.add(new Move.KingCastle(this.board, this.playerKing, 62,
                                                            (Rook) rookTile.getPiece(), rookTile.getTileCoord(),
                                                            61));
                    }
                }
            }
            if(!this.board.getTile(59).isOccupied() &&
                    !this.board.getTile(58).isOccupied() &&
                    !this.board.getTile(57).isOccupied()){
                final Tile rookTile = this.board.getTile(56);

                if(rookTile.isOccupied() && rookTile.getPiece().isFirstMove() &&
                    Player.findAttacksOnPlayer(58, opponentLegals).isEmpty() &&
                    Player.findAttacksOnPlayer(59, opponentLegals).isEmpty() &&
                    rookTile.getPiece().getPieceClass().isRook()){
                    kingCastles.add(new Move.QueenCastle(this.board, this.playerKing, 58,
                                                        (Rook) rookTile.getPiece(), rookTile.getTileCoord(),
                                                        59));
                }
            }
        }
        return kingCastles;
    }
}
