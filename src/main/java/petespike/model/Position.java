package petespike.model;

/**
 * Class representing a position on the board with a row and column.
 */
public class Position {
    private final int row;
    private final int col;
    
    /**
     * Constructs a new position.
     * 
     * @param row the row of the position
     * @param col the column of the position
     */
    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }
    
    /** 
     * Returns the row of the position.
     * 
     * @return the row of the position
     */
    public int getRow() {
        return this.row;
    }
    
    /** 
     * Returns the column of the position.
     * 
     * @return the column of the position
     */
    public int getCol() {
        return this.col;
    }

    /** 
     * Checks if this position is equal to another position.
     * 
     * @param obj the object to compare this position to
     * @return true if the positions are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Position position = (Position) obj;
        return row == position.row && col == position.col;
    }

    /** 
     * Returns the hash code of the position.
     * 
     * @return the hash code of the position
     */
    @Override
    public int hashCode() {
        return 31 * row + col;
    }

    /** 
     * Returns a string representation of the position.
     * 
     * @return the string representation of the position
     */
    @Override
    public String toString() {
        return "(" + row + ", " + col + ")";
    }
}
