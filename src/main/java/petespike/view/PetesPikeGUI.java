package petespike.view;

import java.util.*;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import petespike.model.Direction;
import petespike.model.GameState;
import petespike.model.Move;
import petespike.model.PetesPike;
import petespike.model.PetesPikeSolver;
import petespike.model.PetesPikeException;
import petespike.model.PetesPikeObserver;
import petespike.model.Position;

/**
 * JavaFX-based graphical user interface (GUI) for Pete's Pike.
 * 
 * This class provides an interactive GUI for playing the game, including buttons
 * for movement, solving, and resetting the game, as well as visual feedback.
 */
public class PetesPikeGUI extends Application implements PetesPikeObserver {
    private PetesPike game;
    private GridPane gridPane;
    private Label statusLabel;
    private Label moveCountLabel;
    private Map<Position, Button> buttonMap = new HashMap<>();
    private Position selectedPosition;
    private TextField filenameField = new TextField();
    private HBox directionControls;
    private HBox commandControls;


    private final Image peteImage = new Image("file:project-team_01/images/pete.png");
    private final Image blueGoatImage = new Image("file:project-team_01/images/bluegoat.png");
    private final Image grayGoatImage = new Image("file:project-team_01/images/graygoat.png");
    private final Image greenGoatImage = new Image("file:project-team_01/images/greengoat.png");
    private final Image redGoatImage = new Image("file:project-team_01/images/redgoat.png");
    private final Image yellowGoatImage = new Image("file:project-team_01/images/yellowgoat.png");
    private final Image mountaintopImage = new Image("file:project-team_01/images/mountain.png");
    private final Image[] goatImages = {blueGoatImage, grayGoatImage, greenGoatImage, redGoatImage, yellowGoatImage};
    private final Random RNG = new Random();


    /**
     * Starts the JavaFX application, initializing the GUI layout and game state.
     * 
     * @param primaryStage the main application window
     */
    @Override
    public void start(Stage primaryStage) throws Exception { 
        game = new PetesPike("project-team_01/data/petes_pike_5_5_4_0.txt");
        game.registerObserver(this);

        // Main layout
        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(10));

        // Setup board in the center
        layout.setCenter(setupBoard());

        // Setup controls on the right
        VBox controls = new VBox(10, setupGameControls(), setupDirectionControls());
        controls.setPadding(new Insets(10));
        layout.setRight(controls);

        // Status and moves at the bottom
        HBox statusBar = new HBox(10);
        statusLabel = new Label("Start the game!");
        moveCountLabel = new Label("Moves: 0");
        statusBar.getChildren().addAll(statusLabel, moveCountLabel);
        statusBar.setAlignment(Pos.CENTER);
        layout.setBottom(statusBar);

        Scene scene = new Scene(layout, 800, 800);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Pete's Pike");
        primaryStage.show();

        updateBoard();
    }

    private HBox setupGameControls() {
        commandControls = new HBox(10);
        commandControls.setAlignment(Pos.CENTER);

        Button newGameButton = new Button("New Game");
        newGameButton.setOnAction(e -> loadNewGame());

        Button resetButton = new Button("Reset");
        resetButton.setOnAction(e -> resetGame());

        Button hintButton = new Button("Hint");
        hintButton.setOnAction(e -> hint());
        
        Button solveButton = new Button("Solve");
        solveButton.setOnAction(e -> solveGame());
        
        filenameField = new TextField();
        filenameField.setPromptText("Enter puzzle file path");
        
        commandControls.getChildren().addAll(newGameButton, resetButton, hintButton, solveButton, filenameField);
        return commandControls;
    }
             
    private HBox setupDirectionControls() {
        directionControls = new HBox(10);
        directionControls.setAlignment(Pos.CENTER);

        Button upButton = new Button("UP");
        upButton.setOnAction(e -> moveSelectedPiece(Direction.UP));

        Button downButton = new Button("DOWN");
        downButton.setOnAction(e -> moveSelectedPiece(Direction.DOWN));

        Button leftButton = new Button("LEFT");
        leftButton.setOnAction(e -> moveSelectedPiece(Direction.LEFT));

        Button rightButton = new Button("RIGHT");
        rightButton.setOnAction(e -> moveSelectedPiece(Direction.RIGHT));
        
        directionControls.getChildren().addAll(upButton, leftButton, rightButton, downButton);
        return directionControls;
    }

    private void disableGameControls() {
        for (Node node : directionControls.getChildren()) {
            if (node instanceof Button) {
                node.setDisable(true);
            }
        }

        for (Node node : commandControls.getChildren()) {
            if (node instanceof Button) {
                node.setDisable(true);
            }
        }

        filenameField.setDisable(true);
    }

    private void enableGameControls() {
        for (Node node : directionControls.getChildren()) {
            if (node instanceof Button) {
                node.setDisable(false);
            }
        }

        for (Node node : commandControls.getChildren()) {
            if (node instanceof Button) {
                node.setDisable(false);
            }
        }

        filenameField.setDisable(false);
    }

    private VBox setupBoard() {
        gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(5);
        gridPane.setVgap(5);

        return new VBox(gridPane);
    }

    private void updateBoard() {
        gridPane.getChildren().clear();
        buttonMap.clear();

        try {
            for (int row = 0; row < game.getRows(); row++) {
                for (int col = 0; col < game.getCols(); col++) {
                    Position position = new Position(row, col);
                    char symbol = game.getSymbolAt(position);

                    Button button = new Button();
                    button.setMinSize(50, 50);
                    button.setMaxSize(50, 50);
                    
                    if (symbol == PetesPike.EMPTY_SYMBOL) {
                        button.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
                    } else {
                        button.setGraphic(createImageView(getImageForSymbol(symbol)));
                    }

                    button.setOnAction(e -> handleCellClick(position, symbol));
                    gridPane.add(button, col, row);
                    buttonMap.put(position, button);
                }
            }
            moveCountLabel.setText("Moves: " + game.getMoveCount());
        } catch (PetesPikeException e) {
            statusLabel.setText("Error updating board: " + e.getMessage());
        }
    }

    private void handleCellClick(Position position, char symbol) {
        if (Character.isDigit(symbol) || symbol == PetesPike.PETE_SYMBOL) {
            selectedPosition = position;
            statusLabel.setText("Piece selected at " + position + ". Choose a direction.");
            statusLabel.setTextFill(Color.BLACK);

            buttonMap.forEach((pos, btn) -> btn.setStyle(null));
            Button selectedButton = buttonMap.get(position);
            if (selectedButton != null) {
                selectedButton.setStyle("-fx-border-color: blue; -fx-border-width: 2px;");
            }

        } else {
            statusLabel.setText("No movable piece at this position");
            statusLabel.setTextFill(Color.RED);
        }
    }

    private void resetGame() {
        try {
            game = new PetesPike("project-team_01/data/petes_pike_5_5_4_0.txt");
            game.registerObserver(this);
            PetesPikeSolver.clearVisitedStates();
            updateBoard();
            statusLabel.setText("Game Reset");
        } catch (Exception e) {
            statusLabel.setText("Error resetting game: " + e.getMessage());
        }
    }

    private void hint() {
        disableGameControls();
        new Thread(() -> {
            try {
                PetesPikeSolver.clearVisitedStates();
                PetesPikeSolver solution = PetesPikeSolver.solve(game, false);
                
                if (solution != null && !solution.getMoves().isEmpty()) {
                    Move firstMove = solution.getMoves().get(0);
                    Platform.runLater(() -> {
                        statusLabel.setText("Hint: Move piece at " + firstMove.getPosition() + " " + firstMove.getDirection());
                        enableGameControls();
                    });
                } else {
                    Platform.runLater(() -> {
                        statusLabel.setText("No valid moves available.");
                        enableGameControls();
                    });
                }
            } catch (Exception e) {
                Platform.runLater(() -> {
                    statusLabel.setText("Error solving game: " + e.getMessage());
                    enableGameControls();
                });
            }
        }).start();
    }

    private void solveGame() {
        disableGameControls();
        new Thread(() -> {
            try {
                PetesPikeSolver.clearVisitedStates();
                PetesPikeSolver solution = PetesPikeSolver.solve(game, false);
                
                if (solution != null) {
                    for (Move move : solution.getMoves()) {
                        Platform.runLater(() -> {
                            try {
                                game.makeMove(move);
                                updateBoard();
                                moveCountLabel.setText("Moves: " + game.getMoveCount());
                            } catch (PetesPikeException e) {
                                statusLabel.setText("Error applying move: " + e.getMessage());
                            }
                        });
                        Thread.sleep(300);
                    }
                    Platform.runLater(() -> statusLabel.setText("Congratulations! Puzzle solved."));
                } else {
                    Platform.runLater(() -> statusLabel.setText("No solution found."));
                }
            } catch (Exception e) {
                Platform.runLater(() -> statusLabel.setText("Error solving game: " + e.getMessage()));
            } finally {
                Platform.runLater(this::enableGameControls);
            }
        }).start();
    }

    private Image getImageForSymbol(char symbol) {
        switch (symbol) {
            case 'P': return peteImage;
            case 'T': return mountaintopImage;
            case '-': return null;
            default: return goatImages[RNG.nextInt(goatImages.length)];
        }
    }

    private void loadNewGame() {
        String filename = filenameField.getText();
        if (!filename.trim().isEmpty()) {
            try {
                game = new PetesPike(filename);
                game.registerObserver(this);
                PetesPikeSolver.clearVisitedStates();
                updateBoard();
                statusLabel.setText("New game loaded");
            } catch (Exception e) {
                statusLabel.setText("Error loading game: " + e.getMessage());
            }
        } else {
            statusLabel.setText("Enter a valid filename");
        }
    }

    @Override
    public void pieceMoved(Position from, Position to) {
        updateBoard();
    }

    private void moveSelectedPiece(Direction direction) {
        if (selectedPosition == null) {
            statusLabel.setText("No piece selected. Please select a piece first");
            statusLabel.setTextFill(Color.RED);
            return;
        }

        try {
            Move move = new Move(selectedPosition, direction);
            game.makeMove(move);

            updateBoard();
            moveCountLabel.setText("Moves: " + game.getMoveCount());

            if (game.getGameState() == GameState.WON) {
                statusLabel.setText("Congratulations! You scaled the mountain!");
                statusLabel.setTextFill(Color.GREEN);
            } else {
                statusLabel.setText("Piece moved successfully");
                statusLabel.setTextFill(Color.BLACK);
            }
            selectedPosition = null;

        } catch (PetesPikeException e) {
            statusLabel.setText("Invalid Move: " + e.getMessage());
            statusLabel.setTextFill(Color.RED);
        }
    }

    private ImageView createImageView(Image image) {
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);
        imageView.setPreserveRatio(true);
        return imageView;
    }

    public static void main(String[] args) {
        launch(args);
    }  
}