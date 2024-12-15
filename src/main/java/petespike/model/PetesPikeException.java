package petespike.model;

/**
 * Exception class used for reporting errors in the PetesPike game logic.
 */
public class PetesPikeException extends Exception {
    /**
     * Constructs a new PetesPikeException with the given error message.
     * 
     * @param message the error message
     */
    public PetesPikeException(String message) {
        super(message);
    } 
}
