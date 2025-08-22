// File: src/pieces/Piece.java
package pieces;

import game.Board;
import game.Player;

/**
 * Abstract base class for all chess pieces
 */
public abstract class Piece {
    protected Player player;
    protected int row;
    protected int col;
    protected boolean hasMoved;

    public Piece(Player player, int row, int col) {
        this.player = player;
        this.row = row;
        this.col = col;
        this.hasMoved = false;
    }

    /**
     * Abstract method to be implemented by each piece type
     * Determines if a move to the specified position is valid
     */
    public abstract boolean isValidMove(int toRow, int toCol, Board board);

    /**
     * Get the symbol representing this piece
     */
    public abstract String getSymbol();

    /**
     * Get the value of this piece (for AI evaluation)
     */
    public abstract int getValue();

    /**
     * Check if path is clear between two positions (for sliding pieces)
     */
    protected boolean isPathClear(int toRow, int toCol, Board board) {
        int rowDir = Integer.compare(toRow, row);
        int colDir = Integer.compare(toCol, col);

        int currentRow = row + rowDir;
        int currentCol = col + colDir;

        while (currentRow != toRow || currentCol != toCol) {
            if (board.getPiece(currentRow, currentCol) != null) {
                return false;
            }
            currentRow += rowDir;
            currentCol += colDir;
        }
        return true;
    }

    // Getters and setters
    public Player getPlayer() { return player; }
    public int getRow() { return row; }
    public int getCol() { return col; }
    public boolean hasMoved() { return hasMoved; }
    public void setMoved(boolean moved) { this.hasMoved = moved; }
    public void setPosition(int row, int col) { this.row = row; this.col = col; }
}






