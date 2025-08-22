// File: src/exceptions/InvalidMoveException.java
package exceptions;

/**
 * Exception thrown when an invalid chess move is attempted
 */
public class InvalidMoveException extends Exception {
    public InvalidMoveException(String message) {
        super(message);
    }

    public InvalidMoveException(String message, Throwable cause) {
        super(message, cause);
    }
}
