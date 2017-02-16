import java.net.Socket;
import java.net.ServerSocket;
import java.util.Vector;
import java.util.ArrayList;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.BufferedInputStream;

public class Server {

    private Socket socket = null;
    private ServerSocket server = null;
    private DataInputStream streamIn = null;

    public static void fatalError(String msg) {
        System.out.println(msg);
        System.out.println("Exitiing.");
        System.exit(1);
    }

    public Server(int port) {
      try {
          System.out.println("Binding to port " + port);
          server = new ServerSocket(port);
          System.out.println("Server started: " + server);
          socket = server.accept();
          open();
          boolean done = false;
          while (!done) {
              try {
                String line = streamIn.readUTF();
                System.out.println(line);
                done = line.equals(".bye");
              } catch(Exception e) { System.out.println(e); }
          }

      } catch(Exception e) {
          fatalError("Could not establish server socket on port " + port);
      }
    }

    public void open() {
      try {
        streamIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
      }
      catch (Exception e) {
        System.out.println(e);
      }
    }

    public void close() {
      try {
        if ( socket != null ) {
          socket.close();
        }
        if ( streamIn != null) {
          streamIn.close();
        }
      } catch (Exception e) {
        System.out.println(e);
      }
    }

    public static void main(String[] args) {
      Server server = new Server(Integer.parseInt(args[0]));
    }
}
