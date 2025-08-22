// File: src/game/Player.java
package game;

/**
 * Enumeration for chess players
 */
public enum Player {
    WHITE, BLACK;

    public Player getOpponent() {
        return this == WHITE ? BLACK : WHITE;
    }
}


