// File: src/game/Board.java
package game;

import pieces.*;
import exceptions.InvalidMoveException;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the chess board and manages the game state
 */
public class Board {
    public static final int SIZE = 8;
    private Piece[][] board;
    private Player currentPlayer;
    private boolean gameOver;
    private King whiteKing;
    private King blackKing;

    public Board() {
        this.board = new Piece[SIZE][SIZE];
        this.currentPlayer = Player.WHITE;
        this.gameOver = false;
        initializeBoard();
    }

    /**
     * Initialize the chess board with pieces in starting positions
     */
    private void initializeBoard() {
        // Place white pieces
        board[7][0] = new Rook(Player.WHITE, 7, 0);
        board[7][1] = new Knight(Player.WHITE, 7, 1);
        board[7][2] = new Bishop(Player.WHITE, 7, 2);
        board[7][3] = new Queen(Player.WHITE, 7, 3);
        board[7][4] = whiteKing = new King(Player.WHITE, 7, 4);
        board[7][5] = new Bishop(Player.WHITE, 7, 5);
        board[7][6] = new Knight(Player.WHITE, 7, 6);
        board[7][7] = new Rook(Player.WHITE, 7, 7);

        for (int i = 0; i < SIZE; i++) {
            board[6][i] = new Pawn(Player.WHITE, 6, i);
        }

        // Place black pieces
        board[0][0] = new Rook(Player.BLACK, 0, 0);
        board[0][1] = new Knight(Player.BLACK, 0, 1);
        board[0][2] = new Bishop(Player.BLACK, 0, 2);
        board[0][3] = new Queen(Player.BLACK, 0, 3);
        board[0][4] = blackKing = new King(Player.BLACK, 0, 4);
        board[0][5] = new Bishop(Player.BLACK, 0, 5);
        board[0][6] = new Knight(Player.BLACK, 0, 6);
        board[0][7] = new Rook(Player.BLACK, 0, 7);

        for (int i = 0; i < SIZE; i++) {
            board[1][i] = new Pawn(Player.BLACK, 1, i);
        }
    }

    /**
     * Make a move on the board
     */
    public boolean makeMove(int fromRow, int fromCol, int toRow, int toCol) throws InvalidMoveException {
        if (!isValidPosition(fromRow, fromCol) || !isValidPosition(toRow, toCol)) {
            throw new InvalidMoveException("Invalid board position");
        }

        Piece piece = board[fromRow][fromCol];
        if (piece == null) {
            throw new InvalidMoveException("No piece at source position");
        }

        if (piece.getPlayer() != currentPlayer) {
            throw new InvalidMoveException("Not your piece");
        }

        if (!piece.isValidMove(toRow, toCol, this)) {
            throw new InvalidMoveException("Invalid move for this piece");
        }

        // Check if move would put own king in check
        Piece capturedPiece = board[toRow][toCol];
        board[toRow][toCol] = piece;
        board[fromRow][fromCol] = null;
        piece.setPosition(toRow, toCol);

        boolean wouldBeInCheck = isKingInCheck(currentPlayer);

        // Undo the move to check validity
        board[fromRow][fromCol] = piece;
        board[toRow][toCol] = capturedPiece;
        piece.setPosition(fromRow, fromCol);

        if (wouldBeInCheck) {
            throw new InvalidMoveException("Move would put king in check");
        }

        // Make the actual move
        board[toRow][toCol] = piece;
        board[fromRow][fromCol] = null;
        piece.setPosition(toRow, toCol);
        piece.setMoved(true);

        // Switch players
        currentPlayer = (currentPlayer == Player.WHITE) ? Player.BLACK : Player.WHITE;

        // Check for checkmate or stalemate
        checkGameEnd();

        return true;
    }

    /**
     * Check if position is valid on the board
     */
    public boolean isValidPosition(int row, int col) {
        return row >= 0 && row < SIZE && col >= 0 && col < SIZE;
    }

    /**
     * Get piece at specified position
     */
    public Piece getPiece(int row, int col) {
        if (isValidPosition(row, col)) {
            return board[row][col];
        }
        return null;
    }

    /**
     * Check if king is in check
     */
    public boolean isKingInCheck(Player player) {
        King king = (player == Player.WHITE) ? whiteKing : blackKing;
        int kingRow = king.getRow();
        int kingCol = king.getCol();

        // Check if any opponent piece can attack the king
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                Piece piece = board[row][col];
                if (piece != null && piece.getPlayer() != player) {
                    if (piece.isValidMove(kingRow, kingCol, this)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Get all valid moves for current player
     */
    public List<Move> getAllValidMoves(Player player) {
        List<Move> validMoves = new ArrayList<>();

        for (int fromRow = 0; fromRow < SIZE; fromRow++) {
            for (int fromCol = 0; fromCol < SIZE; fromCol++) {
                Piece piece = board[fromRow][fromCol];
                if (piece != null && piece.getPlayer() == player) {
                    for (int toRow = 0; toRow < SIZE; toRow++) {
                        for (int toCol = 0; toCol < SIZE; toCol++) {
                            try {
                                if (piece.isValidMove(toRow, toCol, this)) {
                                    // Check if move doesn't put king in check
                                    Piece capturedPiece = board[toRow][toCol];
                                    board[toRow][toCol] = piece;
                                    board[fromRow][fromCol] = null;
                                    piece.setPosition(toRow, toCol);

                                    boolean wouldBeInCheck = isKingInCheck(player);

                                    // Undo the move
                                    board[fromRow][fromCol] = piece;
                                    board[toRow][toCol] = capturedPiece;
                                    piece.setPosition(fromRow, fromCol);

                                    if (!wouldBeInCheck) {
                                        validMoves.add(new Move(fromRow, fromCol, toRow, toCol));
                                    }
                                }
                            } catch (Exception e) {
                                // Invalid move, continue
                            }
                        }
                    }
                }
            }
        }
        return validMoves;
    }

    /**
     * Check if game has ended (checkmate or stalemate)
     */
    private void checkGameEnd() {
        List<Move> validMoves = getAllValidMoves(currentPlayer);
        if (validMoves.isEmpty()) {
            gameOver = true;
            if (isKingInCheck(currentPlayer)) {
                System.out.println("Checkmate! " +
                        (currentPlayer == Player.WHITE ? "Black" : "White") + " wins!");
            } else {
                System.out.println("Stalemate! It's a draw!");
            }
        }
    }

    // Getters
    public Player getCurrentPlayer() { return currentPlayer; }
    public boolean isGameOver() { return gameOver; }
    public Piece[][] getBoardArray() { return board; }
}