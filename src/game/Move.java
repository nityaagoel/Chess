// File: src/game/Move.java
package game;

/**
 * Represents a chess move
 */
public class Move {
    private int fromRow, fromCol, toRow, toCol;
    private int score; // For AI evaluation

    public Move(int fromRow, int fromCol, int toRow, int toCol) {
        this.fromRow = fromRow;
        this.fromCol = fromCol;
        this.toRow = toRow;
        this.toCol = toCol;
        this.score = 0;
    }

    // Getters and setters
    public int getFromRow() { return fromRow; }
    public int getFromCol() { return fromCol; }
    public int getToRow() { return toRow; }
    public int getToCol() { return toCol; }
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    @Override
    public String toString() {
        return String.format("(%d,%d) -> (%d,%d)", fromRow, fromCol, toRow, toCol);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Move move = (Move) obj;
        return fromRow == move.fromRow && fromCol == move.fromCol &&
                toRow == move.toRow && toCol == move.toCol;
    }
}
