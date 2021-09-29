package chess;

import chess.Piece;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public abstract class Tile implements Serializable{
	private static final long serialVersionUID = 1L;

	//This variable stores a tile coordinate
    private final int coordinate;

    //This Map stores the board's tiles.
    private static final Map<Integer, EmptyTile> EMPTY_TILES = createAllPossibleEmptyTiles();

    //This function fills and returns the previous Map with 64 integers, 0-63, each integer representing a tile.
    private static Map<Integer,EmptyTile> createAllPossibleEmptyTiles() {
        final Map<Integer, EmptyTile> emptyTileMap = new HashMap<>();

        for(int i = 0; i < BoardOperations.NUM_TILES; i++)
            emptyTileMap.put(i, new EmptyTile(i));

        return emptyTileMap;
    }

    /*
    This method takes a coordinate and piece as arguments, and checks to see whether or not the value of the
    piece is null (if a tile is unoccupied). If the value of piece is not null, the OccupiedTile subclass
    constructor is called, passing the coordinate and piece values. Else, the coordinate of the empty tile
    is returned.
     */
    public static Tile createTile(final int coordinate, final Piece piece){
        return piece != null ? new OccupiedTile(coordinate, piece) : EMPTY_TILES.get(coordinate);
    }

    //Constructor. This is passed the coordinate of a tile from one of the subclasses.
    private Tile(int coordinate){
        this.coordinate = coordinate;
    }

    /*
    Abstract class which is overridden by either the subclass EmptyTile or OccupiedTile.
    Returns a true or false value, depending on whether or not a tile is currently occupied.
     */
    public abstract boolean isOccupied();

    /*
    Abstract class which is overridden by either the subclass EmptyTile or OccupiedTile.
    Returns a Piece value, which will determine the kind of piece currently occupying a tile.
     */
    public abstract Piece getPiece();

    public int getTileCoord(){
        return this.coordinate;
    }

    //Static subclass of the abstract Tile class. Its constructor takes a coordinate as an argument.
    public static final class EmptyTile extends Tile{
		private static final long serialVersionUID = 1L;

		EmptyTile(final int coordinate){
            super(coordinate);
        }

        @Override
        public String toString(){
            return "-";
        }

        //For an empty tile, the isOccupied value always returns false.
        @Override
        public boolean isOccupied(){
            return false;
        }

        //For an empty tile, the piece value is always null.
        @Override
        public Piece getPiece(){
            return null;
        }
    }

    //Static subclass of the abstract Tile class. Its constructor takes a coordinate and piece as arguments.
    public static final class OccupiedTile extends Tile{
		private static final long serialVersionUID = 1L;
		//Class variable used to store a passed piece value.
        private final Piece onTile;

        OccupiedTile(final int coordinate, final Piece onTile){
            super(coordinate);

            this.onTile = onTile;
        }

        @Override
        public String toString(){
            return getPiece().getPieceAlliance().isBlack() ?
                    getPiece().toString().toLowerCase() : getPiece().toString();
        }

        //For an occupied tile, isOccupied always returns true.
        @Override
        public boolean isOccupied(){
            return true;
        }

        //For an occupied tile, the piece value is always the stored in the class variable.
        @Override
        public Piece getPiece(){
            return this.onTile;
        }
    }
}
