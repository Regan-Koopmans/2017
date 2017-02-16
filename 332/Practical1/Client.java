/*

          CLASS       : Client
          AUTHOR      : Regan Koopmans
          DESCRIPTION : The client for the chat server.

 */

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.scene.input.KeyEvent;

import java.util.Optional;

public class Client extends Application {

private int PORT;
private int ADDR;

public static void main(String [] args) {
  if (args.length < 3) {
    System.out.println("Port not provided, assuming 3000.");
  }
  else if (args.length < 2)
  {
    System.out.println("Address not provided, assuming 10.0.0.1:3000.");
  }
  else {

  }
  launch(args);
}


public String getUsername() {
  TextInputDialog dialog = new TextInputDialog("user");
  dialog.setTitle("Chat Program Login");
  dialog.setHeaderText("Enter Desired Username");
  dialog.setContentText("Username :");
  Optional<String> desiredName = dialog.showAndWait();
  return desiredName.isPresent() ? desiredName.get() : "user";
}

public void start(Stage mainStage) {
  mainStage.setTitle("COS 332 - Chat Application");
  BorderPane root = new BorderPane();

  // Declare UI Elemets

  TextArea chatMain = new TextArea();
  chatMain.setEditable(false);
  chatMain.setText("Connecting...");


  Button sendButton = new Button();
  sendButton.setText("Send");


  TextField messageField = new TextField();

  // Add behaviors to UI Elements

  sendButton.setOnAction(new EventHandler<ActionEvent>() {
      public void handle(ActionEvent event) {

      }
    });

  // Add all elements to central pane.




  String username = getUsername();
  chatMain.setText(chatMain.getText() + "\nEntering chat with username : " + username);

  root.setBottom(messageField);
  root.setCenter(chatMain);
  Scene mainScene = new Scene(root, 700, 500);

  mainScene.setOnKeyPressed(new EventHandler <KeyEvent>() {
      public void handle(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
          if (!messageField.getText().equals("")) {
            chatMain.setText(chatMain.getText() + "\n" + username + " : "
                             + messageField.getText());
            messageField.setText("");
	  }
	}
      }
    });

  mainStage.setScene(mainScene);
  mainStage.show();
}

}
