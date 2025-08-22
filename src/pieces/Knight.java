// File: src/pieces/Knight.java
package pieces;

import game.Board;
import game.Player;

public class Knight extends Piece {
    public Knight(Player player, int row, int col) {
        super(player, row, col);
    }

    @Override
    public boolean isValidMove(int toRow, int toCol, Board board) {
        if (!board.isValidPosition(toRow, toCol)) return false;
        if (toRow == row && toCol == col) return false;

        int rowDiff = Math.abs(toRow - row);
        int colDiff = Math.abs(toCol - col);

        // Knight moves in L-shape: 2 squares in one direction, 1 in perpendicular
        if (!((rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2))) {
            return false;
        }

        // Check destination
        Piece targetPiece = board.getPiece(toRow, toCol);
        return targetPiece == null || targetPiece.getPlayer() != player;
    }

    @Override
    public String getSymbol() {
        return player == Player.WHITE ? "♘" : "♞";
    }

    @Override
    public int getValue() {
        return 3;
    }
}
