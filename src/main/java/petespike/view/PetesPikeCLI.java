package petespike.view;

import petespike.model.*;

import java.io.IOException;
import java.util.*;


 /**
 * Command-line interface (CLI) for playing Pete's Pike.
 * 
 * This class provides interactive commands to play the game, including making moves,
 * resetting the game, getting hints, and solving the puzzle.
 */
public class PetesPikeCLI {
    /**
 * Provides a simple command-line-interface (CLI) for playing a game of 
 * PetesPike.
 */

    /**
     * The help command.
     */
    public static final String HELP = "help";

    /**
     * The quit command.
     */
    public static final String QUIT = "quit";

    /**
     * The move command.
     */
    public static final String MOVE = "move";

    /**
     * The reset command.
     */
    public static final String RESET = "reset";

     /**
     * The hint command.
     */
    public static final String HINT = "hint";

    /**
     * The board command.
     */
    public static final String BOARD = "board";

    /**
     * The new command.
     */
    public static final String NEW = "new";

    /**
     * The solve command.
     */
    public static final String SOLVE = "solve";

    /**
     * Variable for the Random function
     */
    public static final Random RNG = new Random();

    private static boolean controls = true;

    /**
     * Plays a game of PetesPike using a command line interface.
     * 
     * @param pp The PetesPike used to run the game.
     * @param filename the puzzle filename for resetting the game.
     * @throws PetesPikeException 
     * @throws IOException 
          * @throws InterruptedException 
          */
    public static void playPetesPike(PetesPike pp, String filename) throws PetesPikeException, IOException, InterruptedException { 

        boolean sentinel = true;
        Scanner scanner = new Scanner(System.in);
        printBoard(pp);
        while(sentinel) {
            if (!controls) {
                System.out.println("Waiting for process to complete...");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }
            
            System.out.print(">> ");
            String command = scanner.nextLine();
            String[] tokens = command.split(" ");
            
            boolean validCommand = false;
            if(tokens.length > 0) {
                switch(tokens[0]) {
                    case HELP:
                        help();
                        validCommand = true;
                        break;
                    case QUIT:
                        sentinel = quit(scanner);
                        validCommand = true;
                        break;
                    case MOVE:
                        if (tokens.length == 4) {
                            move(pp, tokens);
                        } else {
                            System.out.println("Usage: move <row> <col> <direction>");
                        }
                        validCommand = true;
                        break;
                    case RESET:
                        try {
                            pp = new PetesPike(filename); //Resets the game
                            System.out.println("Game reset");
                        } catch (PetesPikeException e) {
                            System.err.println("Reset failed" + e.getMessage());
                        }
                        validCommand = true;
                        break;
                    case HINT:
                        hint(pp);
                        validCommand = true;
                        break;
                    case BOARD:
                        printBoard(pp);
                        validCommand = true;
                        break;
                    case NEW:
                        if (tokens.length == 2) {
                            String newFileName = tokens[1];
                            try {
                                pp = new PetesPike(newFileName);
                                System.out.println("New game started with file: " + newFileName);
                                printBoard(pp);
                            } catch (PetesPikeException e) {
                                System.out.println("Failed to load new game: " + e.getMessage());
                            }
                        } else {
                            System.out.println("Usage: new <filename>");
                        }
                        validCommand = true;
                        break;
                    case SOLVE:
                        solveGame(pp);
                        validCommand = true;
                        break;
                    default:
                        System.out.println("Please enter a command. Type 'help', for guide.");
                }
            }

            if (!validCommand && tokens.length > 0) {
                System.out.println("Unknown Command. Type 'help' for guide");
            }

            System.out.println("Game State: " + pp.getGameState());
        }

        System.out.println("Good bye!");
        scanner.close();
    }
    /**
     * Prints a help message.
     */
    private static void help() { 

        System.out.println("Commands: ");
        System.out.println("  board - display current board");
        System.out.println("  help - this help menu");
        System.out.println("  reset - reset current puzzle to the start");
        System.out.println("  new <puzzle_filename> - start a new puzzle");
        System.out.println("  move <row> <col> <direction> - move the piece at "
            + "<row>, <col> where <direction> one of u(p), d(own), l(eft), r(ight)");
        System.out.println("  hint - get a valid move, if one exists");
        System.out.println(" solve - solved the current puzzle");
        System.out.println("  quit - quit the game");
    }

    /**
     * Prompts the user to ensure that they want to quit.
     * 
     * @param scanner The scanner used to read user input.
     * 
     * @return True if the game should continue, false if it should quit.
     */
    private static boolean quit(Scanner scanner) { 
        System.out.print("Are you sure (y/n): ");
        String response = scanner.nextLine();
        return !response.toLowerCase().equals("y");
    }

    
    /** 
     * @param pp
     */
    private static void hint(PetesPike pp) throws PetesPikeException { 
        PetesPikeSolver solution = PetesPikeSolver.solve(pp, false);
        if (solution != null && !solution.getMoves().isEmpty()) {
            Move hintMove = solution.getMoves().get(0);
            System.out.println("Hint: Move piece at " + hintMove.getPosition() + " " + hintMove.getDirection());
        } else {
            System.out.println("No hint available");
        }  
    }

    private static void solveGame(PetesPike pp) throws PetesPikeException, InterruptedException{
        PetesPikeSolver solution = PetesPikeSolver.solve(pp, false);
        if (solution != null) {
            System.out.println("Solution found. Applying moves:");
            for (Move move : solution.getMoves()) {
                System.out.println("Move: " + move);
                pp.makeMove(move);
                printBoard(pp);
                Thread.sleep(500);
            }
            System.out.println("Congratulations, you have scaled the mountain!");
        } else {
            System.out.println("Sorry, there is no solution available.");
        }
    }

    /**
     * Attempts to make a move on the PetesPike board.
     * 
     * @param pp The PetesPike used to control the game.
     * 
     * @param tokens The user's input for the move.
     * @throws PetesPikeException 
     */
    public static void move(PetesPike pp, String[] tokens) throws PetesPikeException {   
        try {
            int row = Integer.parseInt(tokens[1]);
            int col = Integer.parseInt(tokens[2]);
            Direction direction = switch (tokens[3].toLowerCase()) {
                case "u" -> Direction.UP;
                case "d" -> Direction.DOWN;
                case "l" -> Direction.LEFT;
                case "r" -> Direction.RIGHT;
                default -> throw new IllegalArgumentException("Invalid Direction: Use UP, DOWN, LEFT, or RIGHT");
            };
            pp.makeMove(new Move(new Position(row, col), direction));
            printBoard(pp);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (PetesPikeException e) {
            System.out.println("Move failed: " + e.getMessage());
        }   
    }
    

    /** 
     * @param game
     */
    public static void printBoard(PetesPike game) { 
        System.out.print(" ");
        for (int col = 0; col < game.getCols(); col++) {
            System.out.print(col + " ");
        }
        System.out.println();
        
        for (int row = 0; row < game.getRows(); row++) {
            System.out.print(row + " ");
            for (int col = 0; col < game.getCols(); col++) {
                try {
                    char symbol = game.getSymbolAt(new Position(row, col));
                    System.out.print(asciiSymbol(symbol) + " ");
                } catch (PetesPikeException e) {
                    System.out.println("? ");
                }
            }
            System.out.println();
        }
        System.out.println("Moves: " + game.getMoveCount());
    }

    
    /** 
     * @param symbol
     * @return String
     */
    public static String asciiSymbol(char symbol) { 
        if (PetesPike.GOAT_SYMBOLS.contains(symbol)) {
            return AsciiColorCodes.getGoatColor(symbol) + "G" + AsciiColorCodes.RESET;
        } else if (symbol == PetesPike.PETE_SYMBOL) {
            return AsciiColorCodes.RED + "P" + AsciiColorCodes.RESET;
        } else if (symbol == PetesPike.MOUNTAINTOP_SYMBOL) {
            return AsciiColorCodes.BLUE + "+" + AsciiColorCodes.RESET;
        } else if (symbol == PetesPike.EMPTY_SYMBOL) {
            return AsciiColorCodes.LT_GRAY + "-" + AsciiColorCodes.RESET;
        } else {
            return Character.toString(symbol);
        }
    }
 
    /**
     * Plays a game of PetesPike using a command line interface.
     * 
     * @param args Command line arguments. Not used.
          * @throws IOException 
               * @throws InterruptedException 
                    */
                   //@SuppressWarnings("static-access")
        public static void main(String[] args) throws IOException, InterruptedException { 
            String filename = "data/petes_pike_5_5_4_0.txt";
            try {
                PetesPike pp = new PetesPike(filename);
                System.out.println("Puzzle Filename: " + filename);
                playPetesPike(pp, filename);
            } catch (PetesPikeException e) {
                System.out.println("file doesn't exist");
            }
        }
}
