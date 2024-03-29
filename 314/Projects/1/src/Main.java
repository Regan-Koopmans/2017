import bao.*;
import bao.player.*;

import java.util.Optional;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.*;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.Observer;
import java.util.Observable;

import javafx.application.Platform;

import java.util.concurrent.atomic.AtomicInteger;

/**
* Defines a graphical entry-point to the Bao game.
* @author Regan Koopmans
*/
public class Main extends Application implements Observer {
    
    /** The game instance that the front-end interfaces with. */
    public static BaoGame bg = null;

    /** The array of Holes (buttons) that represent the board state.*/
    public static Hole [][] array = null;
    
    /** A thread to run the game independently of the application. */
    private static Thread gameThread = null;
    
    boolean hasWon = false;

    final double MAX_FONT_SIZE = 30.0;

    public static void main(String [] args) {
        Main m = new Main();
        m.init(args);
    }

    public void init(String [] args) {
        bg = new BaoGame();
        bg.addObserver(this);
        launch(args);
        gameThread.interrupt();
    }

    private int MARGIN = 10;
    private int BOARD_LEN = 150;
    public void start(Stage mainStage) {
        mainStage.setTitle("Bao");
        GridPane root = new GridPane();
        root.setStyle("-fx-background-color: orange");
        root.setHgap(10);
        root.setVgap(10);

        Button player_1_bank = new Button("22");
        player_1_bank.getStyleClass().add("bank");
        Button player_2_bank = new Button("22");
        player_2_bank.getStyleClass().add("bank");

        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Play Confirmation");
                alert.setHeaderText("Play Confirmation");
                alert.setContentText("Are you sure you want to select this"+
                                     " location?");
                int num = 0;
                Object obj = event.getSource();
                if (obj instanceof Hole) {
                    num = Integer.parseInt(((Hole) obj).getUserData().
                                           toString());
                }
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    if (bg.returnPlayers().get(0).turnType == TurnType.CAPTURE) {
                        bg.returnPlayers().get(0).seedLocation =
                            new AtomicInteger(num);
                    } else {
                        bg.returnPlayers().get(0).takasaLocation =
                            new AtomicInteger(num);
                    }

                    // Direction dialog, which is used to confirm a direction
                    // from the user.

                    if (num > 1 && num < 6) {
                        Alert directDialog = new Alert(AlertType.CONFIRMATION);
                        directDialog.setTitle("Choose a direction.");
                        directDialog.setHeaderText("Choose direction.");
                        directDialog.setContentText("Please select a direction"+
                                                    " to sow in:");
                        ButtonType buttonLeft = new ButtonType("Left");
                        ButtonType buttonRight = new ButtonType("Right");

                        directDialog.getButtonTypes().setAll(buttonLeft,
                                                             buttonRight);
                        Direction selectedDirection;
                        Optional<ButtonType> chosen = directDialog.showAndWait();
                        if (chosen.get() == buttonLeft) {
                            selectedDirection = Direction.LEFT;
                        }
                        else {
                            selectedDirection = Direction.RIGHT;
                        }
                        bg.returnPlayers().get(0).direction = selectedDirection;
                    }

                    int player_1_num_seeds = Integer.parseInt(player_1_bank.getText());
                    if (player_1_num_seeds > 0) {
                        player_1_bank.setText(Integer.toString(player_1_num_seeds-1));
                    }
                    int player_2_num_seeds = Integer.parseInt(player_2_bank.getText());
                    if (player_2_num_seeds > 0) {
                        player_2_bank.setText(Integer.toString(player_2_num_seeds-1));
                    }
                }
            }
        };

        // Dynamically create buttons

        array = new Hole[4][8];
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 8; y++) {
                array[x][y] = new Hole(x,y);
                array[x][y].setText("0");
                if (x > 1) {
                    array[x][y].getStyleClass().add("playable");
                    array[x][y].setOnAction(event);
                }
                else {
                    array[x][y].getStyleClass().add("hole");
                }
                if (x == 2 && y == 4 || x == 1 && y == 3) {
                   array[x][y].getStyleClass().add("house"); 
                }
                array[x][y].setUserData(y);
            }
        }
        Button bankPlayerOne = new Button();
        bankPlayerOne.getStyleClass().add("hole");
        Button bankPlayerTwo = new Button();
        bankPlayerTwo.getStyleClass().add("hole");

        Button newGameButton = new Button();
        newGameButton.setText("New Game");
        newGameButton.setTranslateX(MARGIN + 100);
        newGameButton.setTranslateY(MARGIN + 20);
        newGameButton.getStyleClass().add("start");

        // Add behaviors to UI Elements

        newGameButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                Alert newGameDialog = new Alert(AlertType.CONFIRMATION);
                newGameDialog.setTitle("New Game");
                newGameDialog.setHeaderText("Create a New Game");
                newGameDialog.setContentText("Choose game type:");

                ButtonType btnHvA = new ButtonType("Human vs AI");
                ButtonType btnAvA = new ButtonType("AI vs AI");
                ButtonType cancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
                newGameDialog.getButtonTypes().setAll(btnHvA, btnAvA, cancel);
                Optional<ButtonType> result = newGameDialog.showAndWait();
                final boolean p1_isHuman;
                final boolean p2_isHuman = false;
                if (result.get() == btnHvA) {
                    System.out.println("HUMAN VS AI");
                    p1_isHuman = true;
                } else if (result.get() == btnAvA) {
                    System.out.println("AI VS AI");
                    p1_isHuman = false;
                } else {
                    p1_isHuman = true;
                }
                if (result.get() != cancel) {
                    bg.stop();
                    bg = new BaoGame();
                    try {
                        gameThread.join();
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                    gameThread = new Thread(new Runnable() {
                        public void run() {
                        bg.start(p1_isHuman, p2_isHuman);
                    }
                    });
                    gameThread.start();
                    player_1_bank.setText("22");
                    player_2_bank.setText("22");
                    updateBoard(array);
                }
            }
        });

        // Add all elements to central pane.

        newGameButton.getStyleClass().add("new");
        root.add(newGameButton, 0, 0);

        for (int x = 0; x < 4; ++x) {
            for (int y = 0; y < 8; ++y) {
                root.add(array[x][y], 5+2*y, 5+2*x);
            }
        }
        root.add(player_1_bank, 20, 15);
        root.add(player_2_bank, 20, 3);

        // Create thread to run game in the backend

        gameThread = new Thread(new Runnable() {
            public void run() {
                bg.start(true, false);
            }
        });
        gameThread.start();

        Thread updateThread = new Thread(new Runnable() {
            public void run() {
                Runnable update = new Runnable() {
                    @Override public void run() {
                        updateBoard(array);
                    }
                };
                while(true) {
                    try {
                        Thread.currentThread().sleep(400);
                        Platform.runLater(update);
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }
            }  
        });
        updateThread.start();

        Scene scene = new Scene(root,750,500);

        scene.getStylesheets().add("bao/main.css");
        mainStage.setScene(scene);
        mainStage.setResizable(false);
        mainStage.sizeToScene();
        mainStage.show();
        updateBoard(array);
    }

    /** 
    * Function that updates the button array to represent
    * the current state of the board.
    * @param array The array of Holes to be updated.
    */
    public void updateBoard(Hole [][] array) {
        int [][] board = bg.board.getBoard();
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 8; y++) {
                array[x][y].setText(Integer.toString(board[x][y]));
            }
        }
    }

    /**
    * Observer function that reacts when the observed
    * (the game) notifies that someone has won the game.
    */
    public void update(Observable o, Object ob) {
        try {
           Platform.runLater(new Runnable() {
            @Override 
            public void run() {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Win");
                alert.setHeaderText("Winner!");
                alert.setContentText((String)ob + " has won.");
                alert.showAndWait();
            }
        });
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
