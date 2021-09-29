package chess;

import java.io.Serializable;

import chess.Pawn;
import chess.Piece;
import chess.Rook;

public abstract class Move implements Serializable{
	private static final long serialVersionUID = 1L;
	protected final ChessBoard board;
    protected final Piece movedPiece;
    protected final int destinationCoord;
    protected final boolean isFirstMove;

    public static final Move INVALID_MOVE = new IllegalMove();

    private Move(final ChessBoard board, final Piece pieceMoved, final int destinationCoord){
        this.board = board;
        this.movedPiece = pieceMoved;
        this.destinationCoord = destinationCoord;
        this.isFirstMove = pieceMoved.isFirstMove();
    }


    private Move(final ChessBoard board, final int destinationCoord){
        this.board = board;
        this.destinationCoord = destinationCoord;
        this.movedPiece = null;
        this.isFirstMove = false;
    }

    @Override
    public boolean equals(final Object other){
        if(this == other)
            return true;

        if(!(other instanceof Move))
            return false;

        final Move otherMove = (Move) other;

        return getCurrentPieceCoordinate() == otherMove.getCurrentPieceCoordinate() &&
                getMoveDestinationCoordinate() == otherMove.getMoveDestinationCoordinate() &&
                getMovedPiece().equals(otherMove.getMovedPiece());
    }

    public ChessBoard getChessboard(){
        return this.board;
    }

    public int getCurrentPieceCoordinate(){
        return this.movedPiece.getPiecePosition();
    }

    public int getMoveDestinationCoordinate(){
        return this.destinationCoord;
    }

    public Piece getMovedPiece(){
        return this.movedPiece;
    }

    public boolean isAttack(){
        return false; 
    }

    public boolean isCastle(){
        return false; 
    }

    public Piece getAttackedPiece(){
        return null;
    }

    public ChessBoard executeMove() {
        final ChessBoard.Assembler builder = new ChessBoard.Assembler();

        for(final Piece piece : this.board.currentPlayer().getLivePieces()){
            if(!this.movedPiece.equals(piece)){
                builder.setPiece(piece);
            }
        }

        for(final Piece piece : this.board.currentPlayer().getOpponent().getLivePieces()){
            builder.setPiece(piece);
        }

        builder.setPiece(this.movedPiece.movePiece(this));
        builder.setNextMove(this.board.currentPlayer().getOpponent().getAlliance());

        return builder.assemble();
    }

    public static final class MajorMove extends Move{
		private static final long serialVersionUID = 1L;

		public MajorMove(final ChessBoard board, final Piece piece, final int destinationCoord) {
            super(board, piece, destinationCoord);
        }

        @Override
        public boolean equals(final Object other){
            return this == other || other instanceof MajorMove && super.equals(other);
        }

        @Override
        public String toString(){
            return movedPiece.getPieceClass().toString() + BoardOperations.getPiecePosition(this.destinationCoord);
        }
    }

    public static class MajorAttackMove extends AttackMove{
		private static final long serialVersionUID = 1L;

		public MajorAttackMove(final ChessBoard board, final Piece pieceMoved,
                               final int destinationCoord, final Piece pieceAttacked){
            super(board, pieceMoved, destinationCoord, pieceAttacked);
        }

        @Override
        public boolean equals(final Object other){
            return this == other || other instanceof MajorAttackMove && super.equals(other);
        }

        @Override
        public String toString(){
            return movedPiece.getPieceClass() + BoardOperations.getPiecePosition(this.destinationCoord);
        }
    }

    public static class AttackMove extends Move{
		private static final long serialVersionUID = 1L;
		final Piece attackedPiece;

        public AttackMove(final ChessBoard board, final Piece piece,
                   final int destinationCoord, final Piece attackedPiece) {
            super(board, piece, destinationCoord);

            this.attackedPiece = attackedPiece;
        }
       
        @Override
        public boolean equals(final Object other){
            if(this == other)
                return true;

            if(!(other instanceof AttackMove))
                return false;

            final AttackMove otherAttack = (AttackMove) other;

            return super.equals(otherAttack) && getAttackedPiece().equals(otherAttack.getAttackedPiece());
        }

        @Override
        public boolean isAttack(){
            return true;
        }

        @Override
        public Piece getAttackedPiece(){
            return this.attackedPiece;
        }
    }

    public static final class PawnMove extends Move{
		private static final long serialVersionUID = 1L;

		public PawnMove(final ChessBoard board, final Piece piece, final int destinationCoord) {
            super(board, piece, destinationCoord);
        }

        @Override
        public boolean equals(final Object other){
            return this == other || other instanceof PawnMove && super.equals(other);
        }

        @Override
        public String toString(){
            return BoardOperations.getPiecePosition(this.destinationCoord);
        }
    }

    public static class PawnAttackMove extends AttackMove{
		private static final long serialVersionUID = 1L;

		public PawnAttackMove(final ChessBoard board, final Piece piece, final int destinationCoord, final Piece attackedPiece) {
            super(board, piece, destinationCoord, attackedPiece);
        }

        @Override
        public boolean equals(final Object other){
            return this == other || other instanceof PawnAttackMove && super.equals(other);
        }

        @Override
        public String toString(){
            return BoardOperations.getPiecePosition(this.movedPiece.getPiecePosition()).substring(0, 1) + "x" +
                                                        BoardOperations.getPiecePosition(this.destinationCoord);
        }
    }

    public static final class EnPassant extends PawnAttackMove{
		private static final long serialVersionUID = 1L;

		public EnPassant(final ChessBoard board, final Piece piece, final int destinationCoord, final Piece attackedPiece) {
            super(board, piece, destinationCoord, attackedPiece);
        }

        @Override
        public boolean equals(final Object other){
            return this == other || other instanceof EnPassant && super.equals(other);
        }

        @Override
        public ChessBoard executeMove(){
            final ChessBoard.Assembler builder = new ChessBoard.Assembler();

            this.board.currentPlayer().getLivePieces().stream().filter(piece -> !this.movedPiece.equals(piece)).forEach(builder::setPiece);
            this.board.currentPlayer().getOpponent().getLivePieces().stream().filter(piece -> !piece.equals(this.getAttackedPiece())).forEach(builder::setPiece);

            builder.setPiece(this.movedPiece.movePiece(this));
            builder.setNextMove(this.board.currentPlayer().getOpponent().getAlliance());
            builder.setMoveInProgress(this);

            return builder.assemble();
        }
    }

    public static final class PawnJump extends Move{
		private static final long serialVersionUID = 1L;

		public PawnJump(final ChessBoard board, final Piece piece, final int destinationCoord) {
            super(board, piece, destinationCoord);
        }

        @Override
        public ChessBoard executeMove(){
            final ChessBoard.Assembler builder = new ChessBoard.Assembler();

            for(final Piece piece : this.board.currentPlayer().getLivePieces()){
                if(!this.movedPiece.equals(piece))
                    builder.setPiece(piece);
            }

            for(final Piece piece : this.board.currentPlayer().getOpponent().getLivePieces())
                builder.setPiece(piece);

            final Pawn movedPawn = (Pawn) this.movedPiece.movePiece(this);
            builder.setPiece(movedPawn);
            builder.setEnPassant(movedPawn);
            builder.setNextMove(this.board.currentPlayer().getOpponent().getAlliance());

            return builder.assemble();
        }

        @Override
        public String toString(){
            return BoardOperations.getPiecePosition(this.destinationCoord);
        }
    }

    public static class PawnPromotion extends Move{
		private static final long serialVersionUID = 1L;
		final Move promotionMove;
        final Pawn promotedPawn;

        public PawnPromotion(final Move promotionMove){
            super(promotionMove.getChessboard(), promotionMove.getMovedPiece(), promotionMove.getMoveDestinationCoordinate());
            this.promotionMove = promotionMove;
            this.promotedPawn = (Pawn) promotionMove.getMovedPiece();
        }

        @Override
        public ChessBoard executeMove(){
            final ChessBoard pawnPromotionBoard = this.promotionMove.executeMove();
            final ChessBoard.Assembler builder = new ChessBoard.Assembler();

            for(final Piece piece : pawnPromotionBoard.currentPlayer().getLivePieces()){
                if(!this.promotedPawn.equals(piece)){
                    builder.setPiece(piece);
                }
            }

            for(final Piece piece : pawnPromotionBoard.currentPlayer().getOpponent().getLivePieces()){
                builder.setPiece(piece);
            }

            builder.setPiece(this.promotedPawn.getPromotionPiece().movePiece(this));
            builder.setNextMove(pawnPromotionBoard.currentPlayer().getAlliance());

            return builder.assemble();
        }

        @Override
        public boolean isAttack(){
            return this.promotionMove.isFirstMove;
        }

        @Override
        public Piece getAttackedPiece(){
            return this.promotionMove.getAttackedPiece();
        }

        @Override
        public String toString(){
            return "";
        }

        @Override
        public boolean equals(final Object other){
            return this == other || other instanceof PawnPromotion && super.equals(other);
        }
    }

    static abstract class CastleMove extends Move{
		private static final long serialVersionUID = 1L;
		protected final Rook castleRook;
        protected final int castleRookStart;
        protected final int castleRookDest;

        public CastleMove(final ChessBoard board, final Piece piece, final int destinationCoord,
                            final Rook castleRook, final int castleRookStart,
                            final int castleRookDest) {
            super(board, piece, destinationCoord);
            this.castleRook = castleRook;
            this.castleRookStart = castleRookStart;
            this.castleRookDest = castleRookDest;
        }

        public Rook getCastleRook(){
            return this.castleRook;
        }

        @Override
        public boolean isCastle(){
            return true; 
        }

        @Override
        public ChessBoard executeMove(){
            final ChessBoard.Assembler builder = new ChessBoard.Assembler();

            for(final Piece piece : this.board.currentPlayer().getLivePieces()){
                if(!this.movedPiece.equals(piece) && !this.castleRook.equals(piece))
                    builder.setPiece(piece);
            }

            for(final Piece piece : this.board.currentPlayer().getOpponent().getLivePieces())
                builder.setPiece(piece);

            builder.setPiece(this.movedPiece.movePiece(this));
            builder.setPiece(new Rook(this.castleRookDest, this.board.currentPlayer().getAlliance()));
            builder.setNextMove(this.board.currentPlayer().getOpponent().getAlliance());

            return builder.assemble();
        }

        @Override
        public boolean equals(final Object other){
            if(this == other){
                return true;
            }
            if(!(other instanceof CastleMove)){
                return false;
            }
            final CastleMove otherCastleMove = (CastleMove) other;

            return super.equals(otherCastleMove) && this.castleRook.equals(otherCastleMove.getCastleRook());
        }
    }

    public static final class KingCastle extends CastleMove{
		private static final long serialVersionUID = 1L;

		public KingCastle(final ChessBoard board, final Piece piece, final int destinationCoord,
                          final Rook castleRook, final int castleRookStart, final int castleRookDest) {
            super(board, piece, destinationCoord, castleRook, castleRookStart, castleRookDest);
        }

        @Override
        public boolean equals(final Object other){
            return this == other || other instanceof KingCastle && super.equals(other);
        }

        @Override
        public String toString(){
            return "0-0";
        }
    }

    public static final class QueenCastle extends CastleMove{
		private static final long serialVersionUID = 1L;

		public QueenCastle(final ChessBoard board, final Piece piece, final int destinationCoord,
                           final Rook castleRook, final int castleRookStart, final int castleRookDest) {
            super(board, piece, destinationCoord, castleRook, castleRookStart, castleRookDest);
        }

        @Override
        public boolean equals(final Object other){
            return this == other || other instanceof QueenCastle && super.equals(other);
        }

        @Override
        public String toString(){
            return "0-0-0";
        }
    }

    public static final class IllegalMove extends Move{
		private static final long serialVersionUID = 1L;

		private IllegalMove(){
            super(null, -1);
        }

        @Override
        public int getCurrentPieceCoordinate(){
            return -1;
        }

        @Override
        public int getMoveDestinationCoordinate(){
            return -1;
        }

        @Override
        public ChessBoard executeMove(){
            throw new RuntimeException("INVALID MOVE!!! CANNOT EXECUTE!!!!");
        }

        @Override
        public String toString(){
            return "Invalid Move";
        }
    }

    public static class MoveFilter{

        private MoveFilter(){
            throw new RuntimeException("No instantiation");
        }

        public static Move checkMoveValidity(final ChessBoard board, final int currentCoord, final int destinationCoord) {
            for(final Move move: board.getAllLegalMoves()){
                if(move.getCurrentPieceCoordinate() == currentCoord && move.getMoveDestinationCoordinate() == destinationCoord) {
                    return move;
                }
            }
            return INVALID_MOVE;
        }
    }
}