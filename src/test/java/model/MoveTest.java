package model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import petespike.model.Direction;
import petespike.model.Move;
import petespike.model.Position;

public class MoveTest {

    @Test
    public void testGetters() {
        Position position = new Position(2, 3);
        Direction direction = Direction.UP;
        Move move = new Move(position, direction);

        assertEquals(position, move.getPosition());
        assertEquals(direction, move.getDirection());
    }
    
    @Test
    public void testToString() {
        Position position = new Position(1, 2);
        Direction direction = Direction.DOWN;
        Move move = new Move(position, direction);
        
        assertEquals("Move [position=(1, 2), direction=DOWN]", move.toString());
    }

    @Test
    public void testEquals() {
        
        //setup
        Position position1 = new Position(3, 4);
        Position position2 = new Position(2, 4);


        Move m1 = new Move(position1, Direction.UP);
        Move m2 = new Move (position2, Direction.DOWN);

        boolean expected = false;

        //invoke 
        boolean actual = m1.equals(m2);

        //analyze
        assertEquals(expected, actual);
    }

    @Test
    public void testHashCode() {
        
        //setup
        Position position2 = new Position(2, 4);

        Move m2 = new Move (position2, Direction.DOWN);

        int expected = 915349526;

        //invoke 
        int actual = m2.hashCode();

        //analyze
        assertEquals(expected, actual);
    }

}
