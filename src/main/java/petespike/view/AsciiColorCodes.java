package petespike.view;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import petespike.model.PetesPike;

public class AsciiColorCodes {
    private static final Map<Character, String> goatColorMap = new HashMap<>();
    private static final Random RNG = new Random();
    
    /** Reset color for terminal output. */
    public static String RESET = "\u001b[0m";
    
    /** Predefined colors for terminal display. */
    public static String RED = "\u001b[38;5;9m";
    public static String BLUE = "\u001b[38;5;12m";
    public static String ORANGE = "\u001b[38;5;130m";
    public static String GREEN = "\u001b[38;5;28m";
    public static String YELLOW = "\u001b[38;5;11m";
    public static String MAGENTA = "\u001b[38;5;13m";
    public static String GOLD = "\u001b[38;5;220m";
    public static String PURPLE = "\u001b[38;5;5m";
    public static String LT_GRAY = "\u001b[38;5;7m";
    public static String CYAN = "\u001b[38;5;14m";
    private static final String[] GOAT_COLORS = {RED, BLUE, ORANGE, GREEN, YELLOW, MAGENTA, GOLD, PURPLE, LT_GRAY, CYAN};

    static {
        for (char goatSymbol : PetesPike.GOAT_SYMBOLS) {
            String color = GOAT_COLORS[RNG.nextInt(GOAT_COLORS.length)];
            goatColorMap.put(goatSymbol, color);
        }
    }

    /**
     * Gets the color associated with a goat symbol.
     * 
     * @param goatSymbol the symbol of the goat
     * @return the color associated with the goat symbol
     */
    public static String getGoatColor(char goatSymbol) {
        return goatColorMap.getOrDefault(goatSymbol, RESET);
    }
}
