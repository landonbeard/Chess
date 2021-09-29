package chess;

import java.io.Serializable;
import java.util.*;

/*
The BoardOperations class is used to define certain methods universal to the game's programming. It technically
has a constructor but is never instantiated: only its methods are used.

1. initializeColumn

This method is used to initialize the boolean constant class arrays. It takes a column number as an argument, passed to
it by one of the boolean array initializations. The boolean array will then have a 'true' value placed in it every
eighth element from the number passed to it during one of the array initializations. These arrays are used in other
classes to determine whether a piece is in the first, second, seventh, or eighth columns, in which case certain move
restrictions must necessarily apply (a piece can't be on the first column, move left, and end up on the other side
of the board, for example.)

2. isValidCoord

Takes a destination coordinate of a piece as an argument, and checks to make sure it is no less than 0 and no more than
63. Since there are only 64 tiles on a chess board, this checks to ensure that a coordinate is indeed a valid
coordinate. A piece can't move to -1 or 65, for example, because these tiles would be out-of-bounds on the board.
 */
public class BoardOperations implements Serializable{
	private static final long serialVersionUID = 1L;
	/*
    Boolean constant class arrays. These are used to check whether or not a piece is in the first, second, seventh,
    or eighth columns. Each array is initialized by calling the initColumn method, passing in a 0, 1, 6, or 7,
    respectively. The initializeColumn method places a true value in every eighth element of each array, starting from the
    initial column number. The arrays will then contain values corresponding to the following maps:

    COLUMN_1:

    10000000
    10000000
    10000000
    10000000
    10000000
    10000000
    10000000
    10000000

    COLUMN_2:

    01000000
    01000000
    01000000
    01000000
    01000000
    01000000
    01000000
    01000000

    COLUMN_7:

    00000010
    00000010
    00000010
    00000010
    00000010
    00000010
    00000010
    00000010

    COLUMN_8:

    00000001
    00000001
    00000001
    00000001
    00000001
    00000001
    00000001
    00000001
     */
    public static final boolean[] COLUMN_1 = initializeColumn(0);
    public static final boolean[] COLUMN_2 = initializeColumn(1);
    public static final boolean[] COLUMN_7 = initializeColumn(6);
    public static final boolean[] COLUMN_8 = initializeColumn(7);

    public static final boolean[] ROW_1 = initializeRow(56);
    public static final boolean[] ROW_2 = initializeRow(48);
    public static final boolean[] ROW_3 = initializeRow(40);
    public static final boolean[] ROW_4 = initializeRow(32);
    public static final boolean[] ROW_5 = initializeRow(24);
    public static final boolean[] ROW_6 = initializeRow(16);
    public static final boolean[] ROW_7 = initializeRow(8);
    public static final boolean[] ROW_8 = initializeRow(0);

    public static final List<String> NOTATION_LIST = getNotation();
    public static final Map<String, Integer> COORDINATE_MAP = getCoordinateMap();

    private static boolean[] initializeRow(int i) {
        final boolean[] row = new boolean[NUM_TILES];

        do{
            row[i] = true;
            i++;
        }while(i % PER_ROW != 0);

        return row;
    }

    private static Map<String, Integer> getCoordinateMap(){
        final Map<String, Integer> positionToCoordinate = new HashMap<>();

        for(int i = 0; i < NUM_TILES; i++){
            positionToCoordinate.put(NOTATION_LIST.get(i), i);
        }

        return positionToCoordinate;
    }

    private static List<String> getNotation(){
        String[] temp = new String[]{"a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8",
                "a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7",
                "a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6",
                "a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5",
                "a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4",
                "a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3",
                "a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2",
                "a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1"};
        List<String> notation = new ArrayList<String>(Arrays.asList(temp));

        return notation;
    }

    //Constants containing the total number of board tiles and the number of tiles per row.
    public static final int NUM_TILES = 64;
    public static final int PER_ROW = 8;

    //Constructor. Throws an exception if initialized.
    private BoardOperations(){
        throw new RuntimeException("Cannot be instantiated.");
    }

    //This method initializes the boolean constant class arrays. It's overall function was described previously.
    private static boolean[] initializeColumn(int colNum) {
        //Create a new array which will be returned to initialize a boolean constant class array.
        final boolean[] column = new boolean[NUM_TILES];

        //While the column number is less than the number of tiles (64)....
        do{
            //Place a true value in the current array element.
            column[colNum] = true;

            //Increment the column number by the PER_ROW constant (by 8).
            colNum += PER_ROW;
        }while(colNum < NUM_TILES);

        //Return the boolean array.
        return column;
    }

    //This method checks to see whether a piece destination coordinate is within valid board bounds.
    public static boolean isCoordinateValid(int candidateDestinationCoord) {
        return candidateDestinationCoord >= 0 && candidateDestinationCoord < NUM_TILES;
    }

    public static int getCoordinate(final String position){
        return COORDINATE_MAP.get(position);
    }

    public static String getPiecePosition(final int coordinate){
        return NOTATION_LIST.get(coordinate);
    }
}
