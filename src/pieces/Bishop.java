// File: src/pieces/Bishop.java
package pieces;

import game.Board;
import game.Player;

public class Bishop extends Piece {
    public Bishop(Player player, int row, int col) {
        super(player, row, col);
    }

    @Override
    public boolean isValidMove(int toRow, int toCol, Board board) {
        if (!board.isValidPosition(toRow, toCol)) return false;
        if (toRow == row && toCol == col) return false;

        // Bishop moves diagonally
        if (Math.abs(toRow - row) != Math.abs(toCol - col)) return false;

        // Check if path is clear
        if (!isPathClear(toRow, toCol, board)) return false;

        // Check destination
        Piece targetPiece = board.getPiece(toRow, toCol);
        return targetPiece == null || targetPiece.getPlayer() != player;
    }

    @Override
    public String getSymbol() {
        return player == Player.WHITE ? "♗" : "♝";
    }

    @Override
    public int getValue() {
        return 3;
    }
}