package chess;

import chess.Move;
import chess.Piece;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

public class DeadPiecesGUI extends JPanel {
	private static final long serialVersionUID = 1L;
	private final JPanel north;
    private final JPanel south;

    private static final Dimension DEAD_PIECES_DIMENSION = new Dimension(40, 80);
    private static final Color PANEL_COLOR = Color.decode("0xFDFE6");
    private static final EtchedBorder PANEL_BORDER = new EtchedBorder(EtchedBorder.RAISED);

    public DeadPiecesGUI(){
        super(new BorderLayout());
        setBackground(PANEL_COLOR);
        setBorder(PANEL_BORDER);
        this.north = new JPanel(new GridLayout(8, 2));
        this.south = new JPanel(new GridLayout(8, 2));
        this.north.setBackground(PANEL_COLOR);
        this.south.setBackground(PANEL_COLOR);

        this.add(this.north, BorderLayout.NORTH);
        this.add(this.south, BorderLayout.SOUTH);
        setPreferredSize(DEAD_PIECES_DIMENSION);
    }

    public void redo(final ChessBoardGUI.MoveHistory moveHistory){
        this.south.removeAll();
        this.north.removeAll();

        final List<Piece> deadWhitePieces = new ArrayList<>();
        final List<Piece> deadBlackPieces = new ArrayList<>();

        for(final Move move : moveHistory.getMoves()){
            if(move.isAttack()){
                final Piece deadPiece = move.getAttackedPiece();

                if(deadPiece.getPieceAlliance().isWhite())
                    deadWhitePieces.add(deadPiece);
                else if(deadPiece.getPieceAlliance().isBlack())
                    deadBlackPieces.add(deadPiece);
                else
                    throw new RuntimeException("You've reached the twilight zone");
            }
        }

        Collections.sort(deadWhitePieces, new Comparator<Piece>() {
            @Override
            public int compare(Piece o1, Piece o2) {
                return Integer.compare(o1.getPieceValue(), o2.getPieceValue());
            }
        });

        Collections.sort(deadBlackPieces, new Comparator<Piece>() {
            @Override
            public int compare(Piece o1, Piece o2) {
                return Integer.compare(o1.getPieceValue(), o2.getPieceValue());
            }
        });

        for(final Piece deadPiece : deadWhitePieces){
            try{
                final BufferedImage image = ImageIO.read(new File("pieces/" +
                        deadPiece.getPieceAlliance().toString().substring(0, 1) +
                        "" + deadPiece.toString() + ".gif"));
                final ImageIcon icon = new ImageIcon(image);
                final JLabel iLabel = new JLabel(new ImageIcon(icon.getImage().getScaledInstance
                        (icon.getIconWidth() - 15, icon.getIconWidth() - 15, Image.SCALE_SMOOTH)));
                this.south.add(iLabel);
            }catch(final IOException e){
                e.printStackTrace();
            }
        }

        for(final Piece deadPiece : deadBlackPieces){
            try{
                final BufferedImage image = ImageIO.read(new File("pieces/" +
                        deadPiece.getPieceAlliance().toString().substring(0, 1) +
                        "" + deadPiece.toString() + ".gif"));
                final ImageIcon icon = new ImageIcon(image);
                final JLabel iLabel = new JLabel(new ImageIcon(icon.getImage().getScaledInstance
                        (icon.getIconWidth() - 15, icon.getIconWidth() - 15, Image.SCALE_SMOOTH)));
                this.north.add(iLabel);
            }catch(final IOException e){
                e.printStackTrace();
            }
        }
        validate();

    }
}
