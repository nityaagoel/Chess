// File: src/ai/ChessAI.java
package ai;

import game.Board;
import game.Move;
import game.Player;
import pieces.Piece;
import java.util.List;
import java.util.Random;

/**
 * Chess AI implementation using Minimax algorithm with Alpha-Beta pruning
 */
public class ChessAI {
    private static final int MAX_DEPTH = 4;
    private Player aiPlayer;
    private Random random;

    public ChessAI(Player aiPlayer) {
        this.aiPlayer = aiPlayer;
        this.random = new Random();
    }

    /**
     * Get the best move for the AI using Minimax algorithm
     */
    public Move getBestMove(Board board) {
        List<Move> validMoves = board.getAllValidMoves(aiPlayer);
        if (validMoves.isEmpty()) {
            return null;
        }

        Move bestMove = null;
        int bestScore = Integer.MIN_VALUE;

        for (Move move : validMoves) {
            // Make the move temporarily
            BoardState state = makeTemporaryMove(board, move);

            // Evaluate using minimax
            int score = minimax(board, MAX_DEPTH - 1, Integer.MIN_VALUE,
                    Integer.MAX_VALUE, false);

            // Undo the move
            undoTemporaryMove(board, move, state);

            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }

        return bestMove;
    }

    /**
     * Minimax algorithm with Alpha-Beta pruning
     */
    private int minimax(Board board, int depth, int alpha, int beta, boolean maximizing) {
        if (depth == 0 || board.isGameOver()) {
            return evaluatePosition(board);
        }

        Player currentPlayer = maximizing ? aiPlayer : aiPlayer.getOpponent();
        List<Move> moves = board.getAllValidMoves(currentPlayer);

        if (maximizing) {
            int maxEval = Integer.MIN_VALUE;
            for (Move move : moves) {
                BoardState state = makeTemporaryMove(board, move);
                int eval = minimax(board, depth - 1, alpha, beta, false);
                undoTemporaryMove(board, move, state);

                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);
                if (beta <= alpha) {
                    break; // Alpha-Beta pruning
                }
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (Move move : moves) {
                BoardState state = makeTemporaryMove(board, move);
                int eval = minimax(board, depth - 1, alpha, beta, true);
                undoTemporaryMove(board, move, state);

                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);
                if (beta <= alpha) {
                    break; // Alpha-Beta pruning
                }
            }
            return minEval;
        }
    }

    /**
     * Evaluate the current board position
     */
    private int evaluatePosition(Board board) {
        int score = 0;

        // Material evaluation
        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                Piece piece = board.getPiece(row, col);
                if (piece != null) {
                    int pieceValue = piece.getValue();
                    if (piece.getPlayer() == aiPlayer) {
                        score += pieceValue;
                    } else {
                        score -= pieceValue;
                    }

                    // Position bonuses
                    score += getPositionBonus(piece, row, col);
                }
            }
        }

        // Mobility evaluation
        int aiMoves = board.getAllValidMoves(aiPlayer).size();
        int opponentMoves = board.getAllValidMoves(aiPlayer.getOpponent()).size();
        score += (aiMoves - opponentMoves) * 2;

        // King safety
        if (board.isKingInCheck(aiPlayer)) {
            score -= 50;
        }
        if (board.isKingInCheck(aiPlayer.getOpponent())) {
            score += 50;
        }

        return score;
    }

    /**
     * Get position bonus for piece placement
     */
    private int getPositionBonus(Piece piece, int row, int col) {
        int bonus = 0;

        // Center control bonus
        if (row >= 2 && row <= 5 && col >= 2 && col <= 5) {
            bonus += 5;
        }
        if (row >= 3 && row <= 4 && col >= 3 && col <= 4) {
            bonus += 10; // Central squares
        }

        // Apply bonus based on player
        return piece.getPlayer() == aiPlayer ? bonus : -bonus;
    }

    /**
     * Make a temporary move for evaluation
     */
    private BoardState makeTemporaryMove(Board board, Move move) {
        Piece movingPiece = board.getPiece(move.getFromRow(), move.getFromCol());
        Piece capturedPiece = board.getPiece(move.getToRow(), move.getToCol());

        // Store original state
        BoardState state = new BoardState();
        state.movingPiece = movingPiece;
        state.capturedPiece = capturedPiece;
        state.originalRow = movingPiece.getRow();
        state.originalCol = movingPiece.getCol();
        state.hadMoved = movingPiece.hasMoved();

        // Make the move
        board.getBoardArray()[move.getToRow()][move.getToCol()] = movingPiece;
        board.getBoardArray()[move.getFromRow()][move.getFromCol()] = null;
        movingPiece.setPosition(move.getToRow(), move.getToCol());
        movingPiece.setMoved(true);

        return state;
    }

    /**
     * Undo a temporary move
     */
    private void undoTemporaryMove(Board board, Move move, BoardState state) {
        // Restore the move
        board.getBoardArray()[state.originalRow][state.originalCol] = state.movingPiece;
        board.getBoardArray()[move.getToRow()][move.getToCol()] = state.capturedPiece;
        state.movingPiece.setPosition(state.originalRow, state.originalCol);
        state.movingPiece.setMoved(state.hadMoved);
    }

    /**
     * Inner class to store board state for temporary moves
     */
    private static class BoardState {
        Piece movingPiece;
        Piece capturedPiece;
        int originalRow;
        int originalCol;
        boolean hadMoved;
    }
}