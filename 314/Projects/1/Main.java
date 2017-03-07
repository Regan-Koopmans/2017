/*

    CLASS       : Main
    AUTHOR      : Regan Koopmans
    DESCRIPTION : Defines an entry-point into the graphical program

 */
import bao.*;

import java.util.Optional;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.*;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.Observer;
import java.util.Observable;

import java.util.concurrent.atomic.AtomicInteger;

public class Main extends Application implements Observer {
    public static BaoGame bg = null;
    public static Hole [][] array = null;
    private static Thread gameThread = null;
    public static void main(String [] args) {
        bg = new BaoGame();
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
                    System.out.println("NUM : " + num);
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

                    if (num > 1 || num < 6) {
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

                    // PROBLEM STARTS HERE

                    while (bg.returnPlayers().get(0).turnDone) {
                        try {
                            Thread.currentThread().sleep(1000);
                            System.out.println("P1");
                        } catch (Exception e) {

                        }
                    }

                    System.out.println("Turn is done, updating board");
                    while (!bg.returnPlayers().get(1).turnDone) {
                        try {
                            Thread.currentThread().sleep(500);
                            System.out.println("P2");
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    }
                    System.out.println("Turn is done, updating board");
                    try {
                        Thread.currentThread().sleep(500);
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                    updateBoard(array);
                }
            }
        };
        // Dynamically create buttons
        array = new Hole[4][8];
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 8; y++) {
                array[x][y] = new Hole(x,y);
                array[x][y].setText("0");
                array[x][y].getStyleClass().add("hole");
                array[x][y].setUserData(y);
                array[x][y].setOnAction(event);
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
                System.out.println("NEW");
            }
        });

        // Add all elements to central pane.

        root.add(newGameButton, 0,0);
        for (int x = 0; x < 4; ++x) {
            for (int y = 0; y < 8; ++y) {
                root.add(array[x][y], 5+2*y, 5+2*x);
            }
        }

        // Create thread to run game in the backend

        gameThread = new Thread(new Runnable() {
            public void run() {
                bg.start(true, false);
            }
        });
        gameThread.start();

        //root.add(bankPlayerOne,0,10);
        //root.add(bankPlayerTwo,60,10);

        Scene scene = new Scene(root,700,500);
        scene.getStylesheets().add("bao/styles/main.css");
        mainStage.setScene(scene);
        mainStage.setResizable(false);
        mainStage.sizeToScene();
        mainStage.show();
        updateBoard(array);
    }

    public void updateBoard(Hole [][] array) {
        int [][] board = bg.board.getBoard();
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 8; y++) {
                array[x][y].setText(Integer.toString(board[x][y]));
            }
        }
    }

    public void update(Observable o, Object ob) {

    }
}
