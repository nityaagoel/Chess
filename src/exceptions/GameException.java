
// File: src/exceptions/GameException.java
package exceptions;

/**
 * General exception for chess game errors
 */
public class GameException extends Exception {
    public GameException(String message) {
        super(message);
    }

    public GameException(String message, Throwable cause) {
        super(message, cause);
    }
}