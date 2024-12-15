package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import petespike.model.PetesPike;
import petespike.model.PetesPikeException;
import petespike.model.Position;
import petespike.model.Direction;
import petespike.model.Move;

import java.io.IOException;

public class PetesPikeTest {

    @Test
    public void testLoadBoard() throws IOException, PetesPikeException {
        PetesPike game = new PetesPike("data/petes_pike_5_5_4_0.txt");

        assertEquals(5, game.getRows());
        assertEquals(5, game.getCols());
        assertEquals(PetesPike.MOUNTAINTOP_SYMBOL, game.getSymbolAt(new Position(2, 2)));
        assertEquals(PetesPike.PETE_SYMBOL, game.getSymbolAt(new Position(3, 2)));
    }

    @Test
    public void testMoveCount() throws IOException, PetesPikeException {
        PetesPike game = new PetesPike("data/petes_pike_5_5_4_0.txt");
        assertEquals(0, game.getMoveCount());
    }

    @Test
    public void testMakeMove() throws IOException, PetesPikeException {
        PetesPike game = new PetesPike("data/petes_pike_5_5_4_0.txt");
        Position goatPosition = new Position(0, 1);
        Move move = new Move(goatPosition, Direction.RIGHT);

        game.makeMove(move);
        assertEquals('0', game.getSymbolAt(goatPosition));
        assertEquals('1', game.getSymbolAt(new Position(0, 2)));
        assertEquals(1, game.getMoveCount());
    }

    @Test
    public void testInvalidMove() throws IOException, PetesPikeException {
        PetesPike game = new PetesPike("data/petes_pike_5_5_4_0.txt");
        Position emptyPosition = new Position(0, 0);
        Move move = new Move(emptyPosition, Direction.UP);

        assertThrows(PetesPikeException.class, () -> game.makeMove(move));
    }

    @Test
    public void testPossibleMoves() throws IOException, PetesPikeException {
        PetesPike game = new PetesPike("data/petes_pike_5_5_4_0.txt");

        assertFalse(game.getPossibleMoves().isEmpty());
    }

    @Test
    public void testGetSymbolAt() throws IOException, PetesPikeException {
        PetesPike game = new PetesPike("data/petes_pike_5_5_4_0.txt");

        assertEquals('P', game.getSymbolAt(new Position(3, 2))); // Pete's position
        assertEquals('T', game.getSymbolAt(new Position(2, 2))); // Mountaintop
        assertEquals('1', game.getSymbolAt(new Position(0, 2))); // Goat
    }

}

