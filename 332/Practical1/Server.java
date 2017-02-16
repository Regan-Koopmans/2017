import java.net.Socket;
import java.net.ServerSocket;
import java.util.Vector;
import java.util.ArrayList;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.BufferedInputStream;

public class Server {

    private static Socket socket = null;
    private static ServerSocket serverSocket = null;
    private static DataInputStream streamIn = null;
    private static DataOutputStream streamOut = null;

    public static void fatalError(String msg) {
        System.out.println(msg);
        System.out.println("Exitiing.");
        System.exit(1);
    }

    public static void open() {
      try {
        streamIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
      }
      catch (Exception e) {
        System.out.println(e);
      }
    }

    public static void close() {
      try {
        if ( socket != null ) {
          socket.close();
        }
        if ( streamOut != null) {
          streamIn.close();
        }
      } catch (Exception e) {
        System.out.println(e);
      }
    }

    public static void main(String[] args) {
        int PORT = 3000;
        if (args.length < 1) {
            System.out.println("No port was given, assuming 3000.");
        }
        else {
            try {
                PORT = Integer.parseInt(args[0]);
            } catch (Exception e) {
                fatalError("You did not pass an integer port.");
            }
        }

        try {
            serverSocket = new ServerSocket(PORT);
            socket = serverSocket.accept();
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
            fatalError("Could not establish server socket on port " + PORT);
        }
    }
}
