import java.io.DataInputStream;
import javafx.scene.control.TextArea;

public class ClientThread extends Thread {

  private DataInputStream in;
  private TextArea tx;

  public ClientThread(TextArea tx, DataInputStream in) {
    this.tx = tx;
    this.in = in;
  }

  public void run() {
    while (true) {
      try {
        Thread.sleep(500);
        while(in.available() >= 0) {
          appendToChat(tx, in.readUTF());
        }
      } catch (Exception e) {
        System.out.println(e);
      }
    }
  }

  void appendToChat(TextArea textArea, String message) {
    textArea.appendText("\n" + message);
  }
}
