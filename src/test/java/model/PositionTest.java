package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;
import petespike.model.Position;

public class PositionTest {

    @Test
    public void testGetters() {
        Position position = new Position(2, 3);
        assertEquals(2, position.getRow());
        assertEquals(3, position.getCol());
    }

    @Test
    public void testToString() {
        Position position = new Position(3, 4);
        assertEquals("(3, 4)", position.toString());
    }

    @Test
    public void testEquals() {
        Position position1 = new Position(1, 1);
        Position position2 = new Position(1, 1);
        Position position3 = new Position(2, 2);

        assertEquals(position1, position2);
        assertNotEquals(position1, position3);
    }

    @Test
    public void testHashCode() {
        Position position1 = new Position(1, 1);
        Position position2 = new Position(1, 1);
        Position position3 = new Position(2, 2);

        assertEquals(position1.hashCode(), position2.hashCode());
        assertNotEquals(position1.hashCode(), position3.hashCode());
    }
}
