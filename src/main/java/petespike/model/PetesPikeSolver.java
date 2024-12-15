package petespike.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import backtracker.Backtracker;
import backtracker.Configuration;


 /**
 * The class Petes pike solver implements configuration< petes pike solver>
 */ 
public class PetesPikeSolver implements Configuration<PetesPikeSolver> {
    private static final Set<String> visited = new HashSet<>();
    private PetesPike petesPike;
    private List<Move> moves;

    /** 
     *
     * Petes pike solver
     *
     * @param petesPike  the petes pike. 
     * @return public
     */
    public PetesPikeSolver(PetesPike petesPike) { 
        this.petesPike = petesPike;
        this.moves = new ArrayList<>();
    }

    private PetesPikeSolver(PetesPike petesPike, List<Move> moves) {
        this.petesPike = petesPike;
        this.moves = new ArrayList<>(moves);
    }


    /** 
     *
     * Gets the moves
     *
     * @return the moves
     */
    public List<Move> getMoves() {return new ArrayList<>(moves);} 

    @Override
    public Collection<PetesPikeSolver> getSuccessors() {
        List<PetesPikeSolver> successors = new ArrayList<>();
        try {
            List<Move> possibleMoves = petesPike.getPossibleMoves();
            for (Move move : possibleMoves) {
                try {
                    PetesPike newGame = new PetesPike(petesPike);
                    newGame.makeMove(move);
                    
                    String boardHash = hashBoard(newGame);
                    if (!visited.contains(boardHash)) {
                        visited.add(boardHash);
                        List<Move> newMoves = new ArrayList<>(this.moves);
                        newMoves.add(move);
                        successors.add(new PetesPikeSolver(newGame, newMoves));
                    }
                } catch (PetesPikeException ignored) {}
            }
        } catch (PetesPikeException ignored) {}
        return successors;
    }

    /** 
     *
     * Is valid
     *
     * @return boolean
     */
    @Override
    public boolean isValid() { 
        try {
            boolean noMoves = petesPike.getPossibleMoves().isEmpty();
            boolean noWon = petesPike.getGameState() != GameState.WON;
            return !(noMoves && noWon);
        } catch (PetesPikeException e) {
            return false;
        }
    }

    /** 
     *
     * Is goal
     *
     * @return boolean
     */
    @Override
    public boolean isGoal() { 
        return petesPike.getGameState() == GameState.WON;
    }


    /** 
     *
     * Solve
     *
     * @param petesPike  the petes pike. 
     * @return PetesPikeSolver
     */
    public static PetesPikeSolver solve(PetesPike petesPike) { 
        Backtracker<PetesPikeSolver> backtracker = new Backtracker<>(false);
        return backtracker.solve(new PetesPikeSolver(petesPike));
    }

    /** 
     *
     * Solve
     *
     * @param petesPike  the petes pike. 
     * @param debug  the debug. 
     * @return PetesPikeSolver
     */
    public static PetesPikeSolver solve(PetesPike petesPike, boolean debug) { 
        Backtracker<PetesPikeSolver> backtracker = new Backtracker<>(debug);
        PetesPikeSolver pps = new PetesPikeSolver(petesPike);
        PetesPikeSolver solution = backtracker.solve(pps);
        return solution;
    }

    private String hashBoard(PetesPike game) throws PetesPikeException {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < game.getRows(); row++) {
            for (int col = 0; col < game.getCols(); col++) {
                sb.append(game.getSymbolAt(new Position(row, col)));
            }
        }
        return sb.toString();
    }

    public static void clearVisitedStates() {
        visited.clear();
    }

}
