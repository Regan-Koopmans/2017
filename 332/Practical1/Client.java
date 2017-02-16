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

import java.net.Socket;

import java.util.Optional;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Client extends Application {

private static int PORT;
private static String ADDR;

private static Socket socket;
private static DataInputStream streamIn;
private static DataOutputStream streamOut;


  public static void openStreams() {
    streamIn = null;
    try {
      streamOut = new DataOutputStream(socket.getOutputStream());
    } catch (Exception e) {
      System.out.println(e);
    }
  }

public static void main(String [] args) {
  if (args.length < 1) {
      System.out.println("Address not provided, assuming 10.0.0.1:3000.");
      ADDR = "10.0.0.1";
      PORT = 3000;
  }
  else if (args.length < 2)
  {
    System.out.println("Port not provided, assuming 3000.");
    ADDR = args[0];
    PORT = 3000;
  }
  else {
      ADDR = args[0];
      PORT = Integer.parseInt(args[1]);
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

  TextField messageField = new TextField();

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

  /*try {
      Socket socket = new Socket(ADDR, PORT);
  } catch(Exception e) {
      System.out.println(e);
  }
  openStreams();
  String line = "";
  while (!line.equals(".bye")) {
    try {
      streamOut.writeUTF("hi");
      streamOut.flush();
    } catch (Exception e) {
      System.out.println(e);
    }
  }*/
}

}
