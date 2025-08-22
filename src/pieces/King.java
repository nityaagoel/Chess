// File: src/pieces/King.java
package pieces;

import game.Board;
import game.Player;

public class King extends Piece {
    public King(Player player, int row, int col) {
        super(player, row, col);
    }

    @Override
    public boolean isValidMove(int toRow, int toCol, Board board) {
        if (!board.isValidPosition(toRow, toCol)) return false;
        if (toRow == row && toCol == col) return false;

        int rowDiff = Math.abs(toRow - row);
        int colDiff = Math.abs(toCol - col);

        // King moves one square in any direction
        if (rowDiff > 1 || colDiff > 1) return false;

        // Check destination
        Piece targetPiece = board.getPiece(toRow, toCol);
        return targetPiece == null || targetPiece.getPlayer() != player;
    }

    @Override
    public String getSymbol() {
        return player == Player.WHITE ? "♔" : "♚";
    }

    @Override
    public int getValue() {
        return 1000; // King is invaluable
    }
}