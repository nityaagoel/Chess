// File: src/gui/ChessGUI.java
package gui;

import game.Board;
import game.Player;
import game.Move;
import pieces.Piece;
import exceptions.InvalidMoveException;
import ai.ChessAI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Main GUI class for the chess game
 */
public class ChessGUI extends JFrame {
    private Board board;
    private ChessSquare[][] squares;
    private ChessSquare selectedSquare;
    private JLabel statusLabel;
    private JButton newGameButton;
    private JButton aiModeButton;
    private ChessAI ai;
    private boolean aiMode;
    private boolean aiThinking;

    private static final Color LIGHT_SQUARE = new Color(240, 217, 181);
    private static final Color DARK_SQUARE = new Color(181, 136, 99);
    private static final Color SELECTED_COLOR = new Color(255, 255, 0, 128);
    private static final Color VALID_MOVE_COLOR = new Color(0, 255, 0, 128);

    public ChessGUI() {
        initializeComponents();
        setupLayout();
        newGame();
        setVisible(true);
    }

    private void initializeComponents() {
        setTitle("Chess Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        board = new Board();
        squares = new ChessSquare[8][8];
        ai = new ChessAI(Player.BLACK);
        aiMode = false;
        aiThinking = false;

        // Create chess squares
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                squares[row][col] = new ChessSquare(row, col);
                squares[row][col].addActionListener(new SquareClickListener());
            }
        }

        // Create control components
        statusLabel = new JLabel("White's turn", JLabel.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));

        newGameButton = new JButton("New Game");
        newGameButton.addActionListener(e -> newGame());

        aiModeButton = new JButton("Play vs AI");
        aiModeButton.addActionListener(e -> toggleAIMode());
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Chess board panel
        JPanel boardPanel = new JPanel(new GridLayout(8, 8));
        boardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                boardPanel.add(squares[row][col]);
            }
        }

        // Control panel
        JPanel controlPanel = new JPanel(new FlowLayout());
        controlPanel.add(newGameButton);
        controlPanel.add(aiModeButton);

        // Status panel
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.add(statusLabel, BorderLayout.CENTER);
        statusPanel.add(controlPanel, BorderLayout.SOUTH);

        add(boardPanel, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    private void newGame() {
        board = new Board();
        selectedSquare = null;
        aiThinking = false;
        updateBoard();
        updateStatus();
    }

    private void toggleAIMode() {
        aiMode = !aiMode;
        aiModeButton.setText(aiMode ? "Play vs Human" : "Play vs AI");
        newGame();
    }

    private void updateBoard() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                squares[row][col].updatePiece(board.getPiece(row, col));
                squares[row][col].setSelected(false);
                squares[row][col].setValidMove(false);
            }
        }

        if (selectedSquare != null) {
            selectedSquare.setSelected(true);
            highlightValidMoves();
        }

        repaint();
    }

    private void highlightValidMoves() {
        if (selectedSquare == null) return;

        Piece piece = board.getPiece(selectedSquare.getRow(), selectedSquare.getCol());
        if (piece == null || piece.getPlayer() != board.getCurrentPlayer()) return;

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                try {
                    if (piece.isValidMove(row, col, board)) {
                        // Check if move doesn't put king in check
                        Piece capturedPiece = board.getPiece(row, col);
                        board.getBoardArray()[row][col] = piece;
                        board.getBoardArray()[selectedSquare.getRow()][selectedSquare.getCol()] = null;
                        piece.setPosition(row, col);

                        boolean wouldBeInCheck = board.isKingInCheck(board.getCurrentPlayer());

                        // Undo the temporary move
                        board.getBoardArray()[selectedSquare.getRow()][selectedSquare.getCol()] = piece;
                        board.getBoardArray()[row][col] = capturedPiece;
                        piece.setPosition(selectedSquare.getRow(), selectedSquare.getCol());

                        if (!wouldBeInCheck) {
                            squares[row][col].setValidMove(true);
                        }
                    }
                } catch (Exception e) {
                    // Invalid move, continue
                }
            }
        }
    }

    private void updateStatus() {
        if (board.isGameOver()) {
            if (board.isKingInCheck(board.getCurrentPlayer())) {
                String winner = (board.getCurrentPlayer() == Player.WHITE) ? "Black" : "White";
                statusLabel.setText("Checkmate! " + winner + " wins!");
            } else {
                statusLabel.setText("Stalemate! It's a draw!");
            }
        } else if (aiThinking) {
            statusLabel.setText("AI is thinking...");
        } else {
            String currentPlayer = (board.getCurrentPlayer() == Player.WHITE) ? "White" : "Black";
            if (board.isKingInCheck(board.getCurrentPlayer())) {
                statusLabel.setText(currentPlayer + "'s turn - Check!");
            } else {
                statusLabel.setText(currentPlayer + "'s turn");
            }
        }
    }

    private void makeAIMove() {
        if (!aiMode || board.getCurrentPlayer() != Player.BLACK || board.isGameOver()) {
            return;
        }

        aiThinking = true;
        updateStatus();

        SwingWorker<Move, Void> aiWorker = new SwingWorker<Move, Void>() {
            @Override
            protected Move doInBackground() throws Exception {
                return ai.getBestMove(board);
            }

            @Override
            protected void done() {
                try {
                    Move aiMove = get();
                    if (aiMove != null) {
                        board.makeMove(aiMove.getFromRow(), aiMove.getFromCol(),
                                aiMove.getToRow(), aiMove.getToCol());
                    }
                } catch (Exception e) {
                    System.err.println("AI move error: " + e.getMessage());
                } finally {
                    aiThinking = false;
                    selectedSquare = null;
                    updateBoard();
                    updateStatus();
                }
            }
        };

        aiWorker.execute();
    }

    private class SquareClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (aiThinking || board.isGameOver()) return;

            ChessSquare clickedSquare = (ChessSquare) e.getSource();

            if (selectedSquare == null) {
                // Select a piece
                Piece piece = board.getPiece(clickedSquare.getRow(), clickedSquare.getCol());
                if (piece != null && piece.getPlayer() == board.getCurrentPlayer()) {
                    selectedSquare = clickedSquare;
                    updateBoard();
                }
            } else if (selectedSquare == clickedSquare) {
                // Deselect
                selectedSquare = null;
                updateBoard();
            } else {
                // Attempt to make a move
                try {
                    board.makeMove(selectedSquare.getRow(), selectedSquare.getCol(),
                            clickedSquare.getRow(), clickedSquare.getCol());
                    selectedSquare = null;
                    updateBoard();
                    updateStatus();

                    // Make AI move if in AI mode
                    SwingUtilities.invokeLater(() -> makeAIMove());

                } catch (InvalidMoveException ex) {
                    // Invalid move, try selecting new piece
                    Piece piece = board.getPiece(clickedSquare.getRow(), clickedSquare.getCol());
                    if (piece != null && piece.getPlayer() == board.getCurrentPlayer()) {
                        selectedSquare = clickedSquare;
                        updateBoard();
                    } else {
                        selectedSquare = null;
                        updateBoard();
                        JOptionPane.showMessageDialog(ChessGUI.this, ex.getMessage(),
                                "Invalid Move", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        }
    }
}

