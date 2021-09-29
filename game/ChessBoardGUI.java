package chess;

import chess.ChessBoard;
import chess.BoardOperations;
import chess.Move;
import chess.Tile;
import chess.Piece;
import chess.MoveInProgress;
import chess.ChessClient;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

public class ChessBoardGUI {
    private final JFrame gameFrame;
    private MoveHistoryGUI moveHistoryPanel;
    private DeadPiecesGUI deadPiecesPanel;
    private BoardPanel boardPanel;
    private MoveHistory moveHistory;
    private ChessBoard chessboard;
    private ChessClient client;
    private Tile sourceTile;
    private Tile destinationTile;
    private Piece movedPiece;
    private BoardOrientations orientation;
    private boolean highlightLegalMoves;
    private boolean isWhite;
    private boolean isBlack;
    private boolean yourTurn;
    private final static Dimension OUTER_FRAME_DIMENSION = new Dimension(600, 600);
    private final static Dimension BOARD_PANEL_DIMENSION = new Dimension(400, 350);
    private final static Dimension TILE_PANEL_DIMENSION = new Dimension(10, 10);
    private static String gifPath = "pieces/";

    public ChessBoardGUI(ChessBoard board, ChessClient client, boolean isWhite, boolean isBlack){
        this.client = client;
        this.gameFrame = new JFrame();
        this.gameFrame.setLayout(new BorderLayout());
        final JMenuBar tableMenuBar = menuBar();
        this.gameFrame.setJMenuBar(tableMenuBar);
        this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
        this.chessboard = board;
        this.moveHistoryPanel = new MoveHistoryGUI();
        this.deadPiecesPanel = new DeadPiecesGUI();
        this.isWhite = isWhite;
        this.isBlack = isBlack;
        
        if(isBlack) {
        	yourTurn = false;
        	gameFrame.setTitle("Your Alliance is Black. It is your opponent's turn.");
        	orientation = BoardOrientations.BLACKORIENT;
        }
        if(isWhite) {
        	yourTurn = true;
        	gameFrame.setTitle("Your Alliance is White. It is your turn");
        	orientation = BoardOrientations.WHITEORIENT;
        }
        
        this.boardPanel = new BoardPanel();
        this.moveHistory = new MoveHistory();
        this.highlightLegalMoves = true;
        this.gameFrame.add(this.deadPiecesPanel, BorderLayout.WEST);
        this.gameFrame.add(this.moveHistoryPanel, BorderLayout.EAST);
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);

        this.gameFrame.setVisible(true);
        boardPanel.drawBoard(chessboard);
    }
    
    public void updateBoard(ChessBoard board, int tileID, Tile sourceTile, Piece movedPiece, 
    		MoveHistory moveHistory) {
    	
    	this.chessboard = board;
    	this.sourceTile = sourceTile;
    	this.movedPiece = movedPiece;
    	this.moveHistory = moveHistory;
    	
        destinationTile = chessboard.getTile(tileID);

        final Move move = Move.MoveFilter.checkMoveValidity(chessboard, sourceTile.getTileCoord(),
                                                        destinationTile.getTileCoord());
    
        final MoveInProgress inProgress = chessboard.currentPlayer().makeMove(move);
        if(inProgress.getMoveOutcome().isDone()){
            chessboard = inProgress.getBoardAfterMove();
            this.moveHistory.addMove(move);
        }

        this.sourceTile = null;
        this.destinationTile = null;
        this.movedPiece = null;
        client.setMovedPiece(null);
        client.setSourceTile(null);
        
        moveHistoryPanel.redo(chessboard, this.moveHistory);
        deadPiecesPanel.redo(this.moveHistory);
        boardPanel.drawBoard(chessboard);
        
        if(yourTurn) {
        	yourTurn = false;
        	
        	if(isBlack) {
        		if(chessboard.currentPlayer().inCheckMate())
        			gameFrame.setTitle("GAME OVER!!!!!! You win.");
        		else
        			gameFrame.setTitle("Your Alliance is Black. It is your opponent's turn.");
        	}
        	else if(isWhite) {
        		if(chessboard.currentPlayer().inCheckMate())
        			gameFrame.setTitle("GAME OVER!!!!!! You win.");
        		else
        			gameFrame.setTitle("Your Alliance is White. It is your opponent's turn.");
        	}
        }
        else if(!yourTurn) {
        	yourTurn = true;
        	
        	if(isBlack) {
        		if(chessboard.currentPlayer().inCheckMate())
        			gameFrame.setTitle("GAME OVER!!!!!! White wins.");
        		else if(chessboard.currentPlayer().inCheck())
        			gameFrame.setTitle("Your turn. Your King is in check.");
        		else
        			gameFrame.setTitle("Your Alliance is Black. It is your turn.");
        	}	
        	else if(isWhite) {
        		if(chessboard.currentPlayer().inCheckMate())
        			gameFrame.setTitle("GAME OVER!!!!!! Black wins.");
        		else if(chessboard.currentPlayer().inCheck())
        			gameFrame.setTitle("Your turn. Your King is in check.");
        		else
        			gameFrame.setTitle("Your Alliance is White. It is your turn.");
        	}
        		
        }
    }
    
    private JMenuBar menuBar(){
        final JMenuBar tableMenuBar = new JMenuBar();
        tableMenuBar.add(fileMenu());

        return tableMenuBar;
    }

    private JMenu fileMenu() {
        final JMenu fileMenu = new JMenu("File");
        final JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        fileMenu.add(exitMenuItem);

        return fileMenu;
    }
    
    public class BoardPanel extends JPanel{
		private static final long serialVersionUID = 1L;
		final List<TileGUI> boardTiles;

        public BoardPanel(){
            super(new GridLayout(8, 8));
            this.boardTiles = new ArrayList<>();

            for(int i = 0; i < BoardOperations.NUM_TILES; i++){
                final TileGUI tilePanel = new TileGUI(this, i);
                this.boardTiles.add(tilePanel);
                add(tilePanel);
            }
            setPreferredSize(BOARD_PANEL_DIMENSION);
            validate();
        }

        public void drawBoard(final ChessBoard board){
            removeAll();
            for(final TileGUI tilePanel : orientation.iterate(boardTiles)){
                tilePanel.drawTile(board);
                add(tilePanel);
            }
            validate();
            repaint();
        }
    }

    public enum BoardOrientations{
    	WHITEORIENT{
    		@Override
    		List<TileGUI> iterate(final List<TileGUI> boardTiles){
    			return boardTiles;
    		}
    	},
    	
    	BLACKORIENT{
    		@Override
    		List<TileGUI> iterate(final List<TileGUI> boardTiles){
    			List<TileGUI> blackBoard = new ArrayList<TileGUI>();
    			
    			for(int i = boardTiles.size() - 1; i >= 0; i--) {
    				blackBoard.add(boardTiles.get(i));
    			}
    			
    			return blackBoard;
    		}
    	};
    	
    	abstract List<TileGUI> iterate(final List<TileGUI> boardTiles);
    }
    
    private class TileGUI extends JPanel{
		private static final long serialVersionUID = 1L;
		
		private final int tileID;

        TileGUI(final BoardPanel boardPanel, final int tileID){
            super(new GridBagLayout());
            this.tileID = tileID;
            setPreferredSize(TILE_PANEL_DIMENSION);
            tileColor();
            tilePiece(chessboard);

            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(final MouseEvent e) {
                	if(yourTurn) {
                	    if (isRightMouseButton(e)) {
                            sourceTile = null;
                            destinationTile = null;
                            movedPiece = null;
                        }
                        else if(isLeftMouseButton(e)){
                            if(sourceTile == null){
                            	
                                sourceTile = chessboard.getTile(tileID);
                                movedPiece = sourceTile.getPiece();
                                
                                try {
    								client.sendToServer(sourceTile);
    								client.sendToServer(movedPiece);
    							} catch (IOException e1) {
    								e1.printStackTrace();
    							}
                                if(highlightLegalMoves){
                                	boardPanel.drawBoard(chessboard);
                                }
                                
                                if(movedPiece == null) {
                                	sourceTile = null;
                                	System.out.println("First click");
                                }    
                            }
                            else if(sourceTile != chessboard.getTile(tileID)){
                            	destinationTile = chessboard.getTile(tileID);
                            	final Move move = Move.MoveFilter.checkMoveValidity(chessboard, sourceTile.getTileCoord(),
                                        destinationTile.getTileCoord());

                            	final MoveInProgress transition = chessboard.currentPlayer().makeMove(move);

                            	if(!transition.getMoveOutcome().isDone()) {
                            		sourceTile = null;
                            		movedPiece = null;
                            		return;
                            	}
                            	if(transition.getMoveOutcome().isDone()){
                            		chessboard = transition.getBoardAfterMove();
                            		moveHistory.addMove(move);
                            	}
                            	client.setTile(tileID);
                            	try {
                            		client.sendToServer(moveHistory);
    								client.sendToServer(chessboard);
    							} catch (IOException e1) {
    								e1.printStackTrace();
    							}
                            }
                            else if(sourceTile == chessboard.getTile(tileID)) {
                            	sourceTile = null;
                            	movedPiece = null;
                            }
                        }
                		
                	}
                
                }

				@Override
				public void mouseEntered(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseExited(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mousePressed(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
            });

            validate();
        }

        private Collection<Move> legalMoves(final ChessBoard board){
            if(movedPiece != null && movedPiece.getPieceAlliance() == board.currentPlayer().getAlliance()){
                return movedPiece.findAllLegalMoves(board);
            }

            return Collections.emptyList();
        }
        
        private void highlightLegals(final ChessBoard board){
            if(highlightLegalMoves){
                for(final Move move : legalMoves(board)){
                    if(move.getMoveDestinationCoordinate() == this.tileID){
                        try {
                            add(new JLabel(new ImageIcon(ImageIO.read(new File("pieces/green_dot.png")))));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        public void drawTile(final ChessBoard board){
            tileColor();
            tilePiece(board);
            highlightLegals(chessboard);
            validate();
            repaint();
        }

        private void tilePiece(final ChessBoard board){
            this.removeAll();
            if(board.getTile(this.tileID).isOccupied()){
                try {
                    final BufferedImage image =
                            ImageIO.read(new File(gifPath + board.getTile(this.tileID).getPiece().getPieceAlliance().toString().substring(0, 1) +
                                    board.getTile(this.tileID).getPiece().toString() + ".gif"));
                    add(new JLabel(new ImageIcon(image)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
        private void tileColor() {
            if(BoardOperations.ROW_1[this.tileID] ||
                BoardOperations.ROW_3[this.tileID] ||
                BoardOperations.ROW_5[this.tileID] ||
                BoardOperations.ROW_7[this.tileID]){
                setBackground(this.tileID % 2 == 0 ? Color.DARK_GRAY : Color.LIGHT_GRAY);
            }
            else if(BoardOperations.ROW_2[this.tileID] ||
                    BoardOperations.ROW_4[this.tileID] ||
                    BoardOperations.ROW_6[this.tileID] ||
                    BoardOperations.ROW_8[this.tileID]){
                setBackground(this.tileID % 2 != 0 ? Color.DARK_GRAY : Color.LIGHT_GRAY);
            }
        }
    }
    
    public static class MoveHistory implements Serializable{
		private static final long serialVersionUID = 1L;
		private final List<Move> moves;

        MoveHistory(){
            this.moves = new ArrayList<>();
        }

        public List<Move> getMoves(){
            return this.moves;
        }

        public void addMove(final Move move){
            this.moves.add(move);
        }

        public int size(){
            return this.moves.size();
        }

        public void clear(){
            this.moves.clear();
        }

        public Move removeMove(int index){
            return this.moves.remove(index);
        }

        public boolean removeMove(final Move move){
            return this.moves.remove(move);
        }
    }
}
