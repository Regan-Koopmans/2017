import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.BufferedInputStream;
import java.util.ArrayList;
import java.net.Socket;

public class ServerThread extends Thread {

  public static ArrayList<Socket> connections = new ArrayList<Socket>();

  private Socket socket  = null;
  private Server server = null;
  private int id = -1;
  private DataInputStream streamIn = null;
  private DataOutputStream streamOut = null;

  public ServerThread(Server _server, Socket _socket) {
    server = _server;
    socket = _socket;
    id = socket.getPort();
    connections.add(_socket);
  }

  public void run() {
    String line;
    boolean cont = true;
    while (cont) {
      try {
        line = streamIn.readUTF();
        System.out.println(line);
        if (line.equals("###EXIT###")) {
          for (int x = 0; x < connections.size(); ++x) {
            if (connections.get(x) == socket) {
              connections.remove(x);
            }
          }
          close();
          cont = false;
        }
        else {
        for (Socket connection: connections) {
          streamOut = new DataOutputStream(connection.getOutputStream());
          streamOut.writeUTF(line);
          streamOut.flush();
          }
        }
      } catch (Exception e) {
        System.out.println(e);
      }
    }
  }

  public void open() throws Exception {
    streamIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

  }

  public void close() throws Exception {
    if (socket != null) { socket.close(); }
    if (streamIn != null) { streamIn.close(); }
  }
}
