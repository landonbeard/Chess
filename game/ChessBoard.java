package chess;

import chess.PieceAlliances;
import chess.BlackPlayer;
import chess.Player;
import chess.WhitePlayer;

import java.io.Serializable;
import java.util.*;

public final class ChessBoard implements Serializable{
	private static final long serialVersionUID = 1L;
	
	//Class variable initializations.
    private final List<Tile> boardTiles;
    private final Collection<Piece> white;
    private final Collection<Piece> black;
    private final WhitePlayer whitePlayer;
    private final BlackPlayer blackPlayer;
    private final Player currentPlayer;
    private final Pawn enPassant;

    private ChessBoard(final Assembler assembler){
        this.boardTiles = createBoardTiles(assembler);
        this.white = getLivePieces(this.boardTiles, PieceAlliances.WHITE);
        this.black = getLivePieces(this.boardTiles, PieceAlliances.BLACK);
        this.enPassant = assembler.enPassant;

        final Collection<Move> whiteLegalMoves = findLegalMoves(this.white);
        final Collection<Move> blackLegalMoves = findLegalMoves(this.black);

        this.whitePlayer = new WhitePlayer(this, whiteLegalMoves, blackLegalMoves);
        this.blackPlayer = new BlackPlayer(this, whiteLegalMoves, blackLegalMoves);
        this.currentPlayer = assembler.nextMove.choosePlayer(this.whitePlayer, this.blackPlayer);
    }

    @Override
    public String toString(){
        final StringBuilder sb = new StringBuilder();

        for(int i = 0; i < BoardOperations.NUM_TILES; i++){
            final String tileString = this.boardTiles.get(i).toString();
            sb.append(String.format("%3s", tileString));

            if((i + 1) % BoardOperations.PER_ROW == 0){
                sb.append("\n");
            }
        }

        return sb.toString();
    }

    public Player whitePlayer(){
        return this.whitePlayer;
    }

    public Player blackPlayer(){
        return this.blackPlayer;
    }

    public Player currentPlayer(){
        return this.currentPlayer;
    }

    public Collection<Piece> getBlackPieces(){
        return this.black;
    }

    public Collection<Piece> getWhitePieces(){
        return this.white;
    }

    private Collection<Move> findLegalMoves(Collection<Piece> alliance) {
        final List<Move> legalMoves = new ArrayList<>();

        for(final Piece piece : alliance){
            legalMoves.addAll(piece.findAllLegalMoves(this));
        }

        return legalMoves;
    }

    private static Collection<Piece> getLivePieces(final List<Tile> boardTiles, final PieceAlliances alliance) {
        final List<Piece> livePieces = new ArrayList<>();

        for(final Tile tile : boardTiles){
            if(tile.isOccupied()){
                final Piece piece = tile.getPiece();

                if(piece.getPieceAlliance() == alliance){
                    livePieces.add(piece);
                }
            }
        }
        return livePieces;
    }

    public Tile getTile(final int tileCoordinate){
        return boardTiles.get(tileCoordinate);
    }

    private static List<Tile> createBoardTiles(final Assembler assembler){
        final List<Tile> tiles = new ArrayList<Tile>();

        for(int i = 0; i < BoardOperations.NUM_TILES; i++){
            tiles.add(Tile.createTile(i, assembler.currentBoard.get(i)));
        }

        return tiles;
    }

    //This is the method, called from Main(), that initially creates the board. It's the first method called.
    public static ChessBoard initializeStartBoard(){
        //Create a new Builder object, which will create a new HashMap for the board.
        final Assembler assembler = new Assembler();

        /*
        Using the setPiece method, a piece of each color at each initial position is created. New Piece objects are
        passed as arguments, resulting in the constructor for each type being called.
         */
        assembler.setPiece(new Rook(0, PieceAlliances.BLACK));
        assembler.setPiece(new Knight(1, PieceAlliances.BLACK));
        assembler.setPiece(new Bishop(2, PieceAlliances.BLACK));
        assembler.setPiece(new Queen(3, PieceAlliances.BLACK));
        assembler.setPiece(new King(4, PieceAlliances.BLACK));
        assembler.setPiece(new Bishop(5, PieceAlliances.BLACK));
        assembler.setPiece(new Knight(6, PieceAlliances.BLACK));
        assembler.setPiece(new Rook(7, PieceAlliances.BLACK));
        assembler.setPiece(new Pawn(8, PieceAlliances.BLACK));
        assembler.setPiece(new Pawn(9, PieceAlliances.BLACK));
        assembler.setPiece(new Pawn(10, PieceAlliances.BLACK));
        assembler.setPiece(new Pawn(11, PieceAlliances.BLACK));
        assembler.setPiece(new Pawn(12, PieceAlliances.BLACK));
        assembler.setPiece(new Pawn(13, PieceAlliances.BLACK));
        assembler.setPiece(new Pawn(14, PieceAlliances.BLACK));
        assembler.setPiece(new Pawn(15, PieceAlliances.BLACK));

        assembler.setPiece(new Pawn(48, PieceAlliances.WHITE));
        assembler.setPiece(new Pawn(49, PieceAlliances.WHITE));
        assembler.setPiece(new Pawn(50, PieceAlliances.WHITE));
        assembler.setPiece(new Pawn(51, PieceAlliances.WHITE));
        assembler.setPiece(new Pawn(52, PieceAlliances.WHITE));
        assembler.setPiece(new Pawn(53, PieceAlliances.WHITE));
        assembler.setPiece(new Pawn(54, PieceAlliances.WHITE));
        assembler.setPiece(new Pawn(55, PieceAlliances.WHITE));
        assembler.setPiece(new Rook(56, PieceAlliances.WHITE));
        assembler.setPiece(new Knight(57, PieceAlliances.WHITE));
        assembler.setPiece(new Bishop(58, PieceAlliances.WHITE));
        assembler.setPiece(new Queen(59, PieceAlliances.WHITE));
        assembler.setPiece(new King(60, PieceAlliances.WHITE));
        assembler.setPiece(new Bishop(61, PieceAlliances.WHITE));
        assembler.setPiece(new Knight(62, PieceAlliances.WHITE));
        assembler.setPiece(new Rook(63, PieceAlliances.WHITE));

        //Set the first move to white (customary in chess) by calling the WHITE object in the Alliance enum.
        assembler.setNextMove(PieceAlliances.WHITE);

        //Return the new board via the Assembler method assemble().
        return assembler.assemble();

    }

    public Collection<Move> getAllLegalMoves(){
        List<Move> temp = new ArrayList<Move>();
        temp.addAll(this.whitePlayer.getLegalMoves());
        temp.addAll(this.blackPlayer.getLegalMoves());

        return temp;
    }

    public Pawn getEnPassant(){
        return this.enPassant;
    }

    public static class Assembler{

        Map<Integer, Piece> currentBoard;
        PieceAlliances nextMove;
        Pawn enPassant;
        Move inProgress;

        //When the Builder class is called, create a new instance of the current board as a HashMap.
        public Assembler(){
            this.currentBoard = new HashMap<>();
        }

        //Setter for the board pieces. Takes a piece as an argument.
        public Assembler setPiece(final Piece piece){
            //In the currentBoard HashMap, store the piece's position as the key, and the piece as the value.
            this.currentBoard.put(piece.getPiecePosition(), piece);

            //Return the Builder object.
            return this;
        }

        //Setter for which alliance gets the next move. Takes an Alliance argument (Alliance.WHITE or Alliance.BLACK)
        public Assembler setNextMove(final PieceAlliances nextMove){
            this.nextMove = nextMove;
            return this;
        }

        //Returns a new Board object.
        public ChessBoard assemble(){
            return new ChessBoard(this);
        }

        public void setEnPassant(Pawn enPassant) {
            this.enPassant = enPassant;
        }

        public Assembler setMoveInProgress(final Move inProgress) {
            this.inProgress = inProgress;

            return this;
        }
    }
}
