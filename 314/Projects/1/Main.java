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

public class Main extends Application {
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
    @Override
    public void handle(ActionEvent event) {
      Alert alert = new Alert(AlertType.CONFIRMATION);
      alert.setTitle("Play Confirmation");
      alert.setHeaderText("Play Confirmation");
      alert.setContentText("Are you sure you want to select this location?");

      int num = 0;

      Object obj = event.getSource();
      if (obj instanceof Hole) {
        num = Integer.parseInt(((Hole) obj).getUserData().toString());
      }

      Optional<ButtonType> result = alert.showAndWait();
      if (result.get() == ButtonType.OK){
          if (bg.returnPlayers().get(0).turnType == TurnType.CAPTURE) {
            bg.returnPlayers().get(0).seedLocation = new Integer(num);
          } else { bg.returnPlayers().get(0).takasaLocation = new Integer(num); }

          // Direction dialog
          if (num != 0 || num != 7) {
            Alert directionDialog = new Alert(AlertType.CONFIRMATION);
            directionDialog.setTitle("Choose a direction.");
            directionDialog.setHeaderText("Choose direction.");
            directionDialog.setContentText("Please select a direction to sow in:");

            ButtonType buttonLeft = new ButtonType("Left");
            ButtonType buttonRight = new ButtonType("Right");

            directionDialog.getButtonTypes().setAll(buttonLeft, buttonRight);
            Direction selectedDirection;

            Optional<ButtonType> chosen = directionDialog.showAndWait();
            if (chosen.get() == buttonLeft){ selectedDirection = Direction.LEFT; }
            else { selectedDirection = Direction.RIGHT; }
            bg.returnPlayers().get(0).direction = selectedDirection;
          }
          while (!bg.returnPlayers().get(0).turnDone) { System.out.println("P1"); }
          System.out.println("Turn is done, updating board");
          updateBoard(array);
          while (!bg.returnPlayers().get(1).turnDone) { System.out.println("P2"); }
          System.out.println("Turn is done, updating board");
          updateBoard(array);
        }
    }
  };
  // Declare UI Elemets
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
    for (int y = 0; y < 8; ++y) { root.add(array[x][y], 5+2*y, 5+2*x); }
  }

    gameThread = new Thread(new Runnable() {
      public void run() { bg.start(true, false); }
   });
    gameThread.start();

  //root.add(bankPlayerOne,0,10);
  //root.add(bankPlayerTwo,60,10);

  //.addAll(newGameButton, player1Choice, player2Choice, canvas);
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
}
