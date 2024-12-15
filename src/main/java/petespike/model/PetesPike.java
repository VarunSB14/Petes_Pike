package petespike.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


 /**
 * Main class implementing the games logic for Pete's Pike.
 * 
 * Handles the state of the game, board configuration, moves, and interaction with observers.
 */ 
public class PetesPike {
    /** Symbol representing the mountaintop on the board. */
    public static final char MOUNTAINTOP_SYMBOL = 'T';

    /** Symbol representing an empty cell on the board. */
    public static final char EMPTY_SYMBOL = '-';

    /** Symbol representing Pete on the board. */
    public static final char PETE_SYMBOL = 'P';

    /** Set of symbols representing goats on the board. */
    public static final Set<Character> GOAT_SYMBOLS = Set.of('0', '1', '2', '3', '4', '5', '6', '7', '8');

    private int moveCount;
    public GameState gameState;
    private char[][] board;
    public Position mountainTop;
    private PetesPikeObserver observer;

    /**
     * Constructs a new PetesPike game from a given puzzle file.
     * 
     * @param filename the path to the puzzle file
     * @throws IOException if the file cannot be read
     * @throws PetesPikeException if the board configuration is invalid
     */
    public PetesPike(String filename) throws IOException, PetesPikeException { 
        printBoard(filename);
        this.gameState = GameState.NEW;
        this.moveCount = 0;
    }

    /**
     * Constructs a deep copy of an existing PetesPike game.
     * 
     * @param other the PetesPike object to copy
     */
    public PetesPike(PetesPike other) {
        this.moveCount = other.moveCount;
        this.gameState = other.gameState;
        this.mountainTop = other.mountainTop;

        int rows = other.getRows();
        int cols = other.getCols();
        this.board = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                this.board[i][j] = other.board[i][j];
            }
        }
        this.observer = null; // Do not copy observer
    }

    /**
     * Registers an observer to be notified of moves in the game.
     * 
     * @param observer the observer to register
     */
    public void registerObserver(PetesPikeObserver observer) {
        this.observer = observer;
    }

    /**
     * Notifies the registered observer about a move.
     * 
     * @param from the position from which the piece was moved
     * @param to the position to which the piece was moved
     */
    private void notifyObserver(Position from, Position to) {
        if (observer != null) {
            observer.pieceMoved(from, to);
        }
    }

    /**
     * Prints the board to the console based on the configuration in the puzzle file.
     * 
     * @param filename the puzzle file
     * @throws IOException if the file cannot be read
     * @throws PetesPikeException if the board configuration is invalid
     */
    private void printBoard(String filename) throws IOException, PetesPikeException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String[] dimensions = reader.readLine().split(" ");
            int rows = Integer.parseInt(dimensions[0]);
            int cols = Integer.parseInt(dimensions[1]);

            board = new char[rows][cols];
            for (int i = 0; i < rows; i++) {
                board[i] = reader.readLine().toCharArray();
                for (int j = 0; j < cols; j++) {
                    if (board[i][j] == MOUNTAINTOP_SYMBOL) {
                        mountainTop = new Position(i, j);
                    }
                }
            }
        } catch (Exception e) {
            throw new PetesPikeException("Error loading board: " + e.getMessage());
        }
    }

    /**
     * Returns the current number of moves made in the game.
     * 
     * @return the number of moves
     */
    public int getMoveCount() {
        return this.moveCount;
    }

    /**
     * Returns the number of rows in the game board.
     * 
     * @return the number of rows
     */
    public int getRows() {
        return this.board.length;
    }

    /**
     * Returns the number of columns in the game board.
     * 
     * @return the number of columns
     */
    public int getCols() {
        return this.board[0].length;
    }

    /**
     * Returns the current state of the game.
     * 
     * @return the game state
     */
    public GameState getGameState() {
        return this.gameState;
    }

    /**
     * Returns the symbol at a specified position on the board.
     * 
     * @param position the position to check
     * @return the symbol at that position
     * @throws PetesPikeException if the position is invalid
     */
    public char getSymbolAt(Position position) throws PetesPikeException {
        if (position.getRow() < 0 || position.getRow() >= getRows() || position.getCol() < 0 || position.getCol() >= getCols()) {
            throw new PetesPikeException("Invalid position: " + position);
        }
        return board[position.getRow()][position.getCol()];
    }

    /**
     * Returns the position of the mountaintop on the board.
     * 
     * @return the mountaintop position
     */
    public Position getMountainTop() {
        return this.mountainTop;
    }

    /**
     * Makes a move for a piece on the board.
     * 
     * @param move the move to make
     * @throws PetesPikeException if the move is invalid
     */
    public void makeMove(Move move) throws PetesPikeException {
        Position from = move.getPosition();
        Direction direction = move.getDirection();

        char currentSymbol = getSymbolAt(from);
        if (currentSymbol == EMPTY_SYMBOL || currentSymbol == MOUNTAINTOP_SYMBOL) {
            throw new PetesPikeException("There is no piece at that position");
        }

        int currentRow = from.getRow();
        int currentCol = from.getCol();
        
        int targetRow = currentRow;
        int targetCol = currentCol;

        while (true) {
            switch (direction) {
                case UP -> currentRow--;
                case DOWN -> currentRow++;
                case LEFT -> currentCol--;
                case RIGHT -> currentCol++;
            }

            if (!isWithinBounds(currentRow, currentCol)) {
                throw new PetesPikeException("No piece to stop the move in the given direction.");
            }
            
            char targetSymbol = board[currentRow][currentCol];
            if (targetSymbol != EMPTY_SYMBOL) {
                if (currentSymbol == PETE_SYMBOL && targetSymbol == MOUNTAINTOP_SYMBOL) {
                    board[from.getRow()][from.getCol()] = EMPTY_SYMBOL;
                    board[currentRow][currentCol] = PETE_SYMBOL;
                    notifyObserver(from, new Position(currentRow, currentCol));
                    moveCount++;
                    gameState = GameState.WON;
                    return;
                }

                break;
            }
            targetRow = currentRow;
            targetCol = currentCol;
        }

        board[from.getRow()][from.getCol()] = EMPTY_SYMBOL;
        board[targetRow][targetCol] = currentSymbol;

        notifyObserver(from, new Position(targetRow, targetCol));
        moveCount++;
        gameState = GameState.IN_PROGRESS;
    }

    /**
     * Returns a list of all possible moves for the current game state.
     * 
     * @return list of possible moves
     * @throws PetesPikeException if there is an error retrieving the possible moves
     */
    public List<Move> getPossibleMoves() throws PetesPikeException {
        List<Move> possibleMoves = new ArrayList<>();

        for (int row = 0; row < getRows(); row++) {
            for (int col = 0; col < getCols(); col++) {
                char currentSymbol = board[row][col];
                if (currentSymbol == EMPTY_SYMBOL || currentSymbol == MOUNTAINTOP_SYMBOL) {
                    continue;
                }

                Position currentPosition = new Position(row, col);

                for (Direction direction : Direction.values()) {
                    if(isValidMove(currentPosition, direction)) {
                        possibleMoves.add(new Move(currentPosition, direction));
                    }
                }
            }
        }
        return possibleMoves;
    }

    private boolean isValidMove(Position position, Direction direction) {
        int targetRow = position.getRow();
        int targetCol = position.getCol();

        switch (direction) {
            case UP -> targetRow--;
            case DOWN -> targetRow++;
            case LEFT -> targetCol--;
            case RIGHT -> targetCol++;
        }

        if (!isWithinBounds(targetRow, targetCol)) {
            return false;
        }

        while (targetRow >= 0 && targetRow < getRows() && targetCol >= 0 && targetCol < getCols()) {
            char targetSymbol = board[targetRow][targetCol];
            if (targetSymbol != EMPTY_SYMBOL) {
                return true;
            }
            
            switch (direction) {
                case UP -> targetRow--;
                case DOWN -> targetRow++;
                case LEFT -> targetCol--;
                case RIGHT -> targetCol++;
            }
        }
        return false;
    }

    private boolean isWithinBounds(int row, int col) {
        return row >= 0 && row < getRows() && col >= 0 && col < getCols();
    }
}



