/*

    CLASS       : Main
    AUTHOR      : Regan Koopmans
    DESCRIPTION : Defines a graphical user interface and main entry-point
                  into the program.

 */

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.*;
import java.util.Optional;

import bao.*;

public class Main extends Application {

public static BaoGame bg = null;
public static Hole [][] array = null;


public static void main(String [] args) {
  bg = new BaoGame();
  launch(args);
  //textGame();
}

public static void textGame() {
  bg.start(true, false);
}

private int MARGIN = 10;
private int BOARD_LEN = 150;

public void start(Stage mainStage) {
  mainStage.setTitle("Bao");
  GridPane root = new GridPane();
  root.setStyle("-fx-background-color: orange");

  root.setHgap(10);
  root.setVgap(10);


  // Declare UI Elemets


  array = new Hole[4][8];
  for (int x = 0; x < 4; x++) {
    for (int y = 0; y < 8; y++) {
      array[x][y] = new Hole(x,y);
      array[x][y].setText("0");
      array[x][y].getStyleClass().add("hole");

      array[x][y].setOnAction(new EventHandler<ActionEvent>() {
          public void handle(ActionEvent event) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Play Confirmation");
            alert.setHeaderText("Play Confirmation");
            alert.setContentText("Are you sure you want to select this location?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                System.out.println("ACCEPT");
            }
            updateBoard(array);
          }
      });
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


  ChoiceBox<String> player1Choice = new ChoiceBox<String>(
    FXCollections.observableArrayList("Human", "AI"));
  player1Choice.setTranslateX(MARGIN);
  player1Choice.setTranslateY(MARGIN + 10);

  ChoiceBox<String> player2Choice = new ChoiceBox<String>(
    FXCollections.observableArrayList("Human", "AI"));
  player2Choice.setTranslateX(MARGIN);
  player2Choice.setTranslateY(MARGIN + 50);


  Canvas canvas = new Canvas(300, 250);
  canvas.setTranslateX(MARGIN+100);
  canvas.setTranslateY(MARGIN+100);
  GraphicsContext gc = canvas.getGraphicsContext2D();
  drawBoard(gc);

  // Add behaviors to UI Elements

  newGameButton.setOnAction(new EventHandler<ActionEvent>() {
      public void handle(ActionEvent event) {
        if (player1Choice.getValue() == null || player2Choice.getValue() == null) {
          System.out.println("Not all parameters were set!");
          return;
	}
        Boolean player1Human = player1Choice.getValue() == "Human" ? true : false;
        Boolean player2Human = player2Choice.getValue() == "Human" ? true : false;
      }
    });

  // Add all elements to central pane.

  root.add(newGameButton, 0,0);

  for (int x = 0; x < 4; ++x) {
    for (int y = 0; y < 8; ++y) {
      root.add(array[x][y], 5+2*y, 5+2*x);
    }
  }

  //root.add(bankPlayerOne,0,10);
  //root.add(bankPlayerTwo,60,10);

  //.addAll(newGameButton, player1Choice, player2Choice, canvas);
  Scene scene = new Scene(root,700,500);
  scene.getStylesheets().add("bao/styles/main.css");
  mainStage.setScene(scene);
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

public void drawBoard(GraphicsContext gc) {
  gc.setFill(Color.GREEN);
  gc.setStroke(Color.BLACK);
  gc.setLineWidth(2);
  gc.strokeLine(10, 10, 10, 100);
  gc.strokeLine(BOARD_LEN, 10, BOARD_LEN, 100);
}
}
