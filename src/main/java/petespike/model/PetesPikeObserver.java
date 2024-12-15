package petespike.model;

/**
 * Interface for observing piece movements in the game.
 */
public interface PetesPikeObserver {
    /**
     * Called when a piece is moved.
     * 
     * @param from the original position of the piece
     * @param to the new position of the piece
     */
    void pieceMoved(Position from, Position to);
}
