
// File: src/pieces/Queen.java
package pieces;

import game.Board;
import game.Player;

public class Queen extends Piece {
    public Queen(Player player, int row, int col) {
        super(player, row, col);
    }

    @Override
    public boolean isValidMove(int toRow, int toCol, Board board) {
        if (!board.isValidPosition(toRow, toCol)) return false;
        if (toRow == row && toCol == col) return false;

        // Queen moves like rook or bishop
        boolean isRookMove = (toRow == row || toCol == col);
        boolean isBishopMove = (Math.abs(toRow - row) == Math.abs(toCol - col));

        if (!isRookMove && !isBishopMove) return false;

        // Check if path is clear
        if (!isPathClear(toRow, toCol, board)) return false;

        // Check destination
        Piece targetPiece = board.getPiece(toRow, toCol);
        return targetPiece == null || targetPiece.getPlayer() != player;
    }

    @Override
    public String getSymbol() {
        return player == Player.WHITE ? "♕" : "♛";
    }

    @Override
    public int getValue() {
        return 9;
    }
}
