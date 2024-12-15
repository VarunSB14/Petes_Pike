package petespike.model;

public class Move {
    public final Position position;
    public final Direction direction;
    
    /**
     * 
     * @param gameState
     * @param direction
     */
    public Move(Position position, Direction direction) {
        this.position = position;
        this.direction = direction;
    }
    
    /** 
     * Gets the position of the piece being moved.
     * 
     * @return Position of the piece
     */
    public Position getPosition() {
        return this.position;
    }
    
    /** 
     * Gets the direction of the move.
     * 
     * @return Direction of the move
     */
    public Direction getDirection() {
        return this.direction;
    }
    
    /** 
     * Provides a string representation of the move.
     * 
     * @return String representation of the move
     */
    @Override
    public String toString() {
        return "Move [position=" + position + ", direction=" + direction + "]";
    }
}
