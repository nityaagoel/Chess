// File: src/gui/ChessSquare.java
package gui;

import pieces.Piece;
import javax.swing.*;
import java.awt.*;

/**
 * Represents a single square on the chess board
 */
public class ChessSquare extends JButton {
    private int row, col;
    private Piece piece;
    private boolean isSelected;
    private boolean isValidMove;
    private Color baseColor;

    private static final Color LIGHT_SQUARE = new Color(240, 217, 181);
    private static final Color DARK_SQUARE = new Color(181, 136, 99);
    private static final Color SELECTED_COLOR = new Color(255, 255, 0);
    private static final Color VALID_MOVE_COLOR = new Color(144, 238, 144);

    public ChessSquare(int row, int col) {
        this.row = row;
        this.col = col;
        this.isSelected = false;
        this.isValidMove = false;

        // Set base color (alternating pattern)
        baseColor = ((row + col) % 2 == 0) ? LIGHT_SQUARE : DARK_SQUARE;

        setPreferredSize(new Dimension(80, 80));
        setFont(new Font("Arial Unicode MS", Font.PLAIN, 40));
        setBorder(BorderFactory.createLineBorder(Color.GRAY));
        setFocusPainted(false);

        updateAppearance();
    }

    public void updatePiece(Piece piece) {
        this.piece = piece;
        setText(piece != null ? piece.getSymbol() : "");
        updateAppearance();
    }

    public void setSelected(boolean selected) {
        this.isSelected = selected;
        updateAppearance();
    }

    public void setValidMove(boolean validMove) {
        this.isValidMove = validMove;
        updateAppearance();
    }

    private void updateAppearance() {
        Color backgroundColor = baseColor;

        if (isSelected) {
            backgroundColor = SELECTED_COLOR;
        } else if (isValidMove) {
            backgroundColor = VALID_MOVE_COLOR;
        }

        setBackground(backgroundColor);

        // Set text color based on piece
        if (piece != null) {
            setForeground(piece.getPlayer() == game.Player.WHITE ? Color.BLACK : Color.BLACK);
        }
    }

    // Getters
    public int getRow() { return row; }
    public int getCol() { return col; }
    public Piece getPiece() { return piece; }
}