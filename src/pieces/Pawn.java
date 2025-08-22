// File: src/pieces/Pawn.java
package pieces;

import game.Board;
import game.Player;

public class Pawn extends Piece {
    public Pawn(Player player, int row, int col) {
        super(player, row, col);
    }

    @Override
    public boolean isValidMove(int toRow, int toCol, Board board) {
        if (!board.isValidPosition(toRow, toCol)) return false;

        int direction = (player == Player.WHITE) ? -1 : 1;
        int rowDiff = toRow - row;
        int colDiff = Math.abs(toCol - col);

        Piece targetPiece = board.getPiece(toRow, toCol);

        // Move forward
        if (colDiff == 0) {
            if (targetPiece != null) return false;

            // One square forward
            if (rowDiff == direction) return true;

            // Two squares forward from starting position
            if (!hasMoved && rowDiff == 2 * direction) return true;
        }
        // Diagonal capture
        else if (colDiff == 1 && rowDiff == direction) {
            return targetPiece != null && targetPiece.getPlayer() != player;
        }

        return false;
    }

    @Override
    public String getSymbol() {
        return player == Player.WHITE ? "♙" : "♟";
    }

    @Override
    public int getValue() {
        return 1;
    }
}
