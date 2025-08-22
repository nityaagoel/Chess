// File: src/main/ChessGame.java
package main;

import gui.ChessGUI;
import javax.swing.SwingUtilities;

/**
 * Main class to start the Chess Game application
 */
public class ChessGame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ChessGUI();
        });
    }
}