import java.net.Socket;
import java.net.ServerSocket;
import java.util.Vector;
import java.util.ArrayList;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.StringBuilder;



public class Server implements Runnable {

    private Socket socket = null;
    private ServerSocket server = null;
    private DataInputStream streamIn = null;
    private DataOutputStream streamOut = null;
    private Thread thread = null;

    public static void fatalError(String msg) {
        System.out.println(msg);
        System.out.println("Exiting.");
        System.exit(1);
    }

    public String decorate(String message, Decorate decorator) {
      switch (decorator) {
        case RED : return "\u001B[31m" + message + "\u001B[0m";
        default  : return message;
      }
    }

    public void clearScreen() throws Exception {
      streamOut.writeUTF("\u001B[2J");
      streamOut.flush();
    }

    public void newAppointment() throws Exception {
      streamOut.writeUTF("Enter appointment name : ");
      streamOut.flush();
      String line = streamIn.readLine();
      if (true) {
        String msg = "An appointment named \"" + line + "\" already exists." + "\n";
        streamOut.writeUTF(msg);
        streamOut.flush();
      }
    }

    public void printFile(String file) throws Exception {
      try {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line = br.readLine();
        while (line != null) {

          sb.append(line + "\n");
          line = br.readLine();
        }
        streamOut.writeUTF(sb.toString());
        //streamOut.flush();
      } catch (Exception e) { System.out.println(e); }
    }

    public void handle(String line) throws Exception {
      switch(line) {
        case "help"    :  printFile("Help.txt");                          break;
        case "new"     :  newAppointment();                               break;
        case "license" :  printFile("License.txt");                       break;
        case "clear"   :  clearScreen();                                  break;
        default: System.out.println("Didn't understand that boss.");      break;
      }
    }

    public Server(int port) {
      try {
          System.out.println("Binding to port " + port);
          server = new ServerSocket(port);
          System.out.println("Server started: " + server);
          start();
        } catch (Exception e) {
          System.out.println(e);
        }
      }

      public void run() {
        while (thread != null) {
          try {
            System.out.println("Waiting for client");
            socket = server.accept();
            open();
            clearScreen();
            printFile("Splash.txt");
            boolean done = false;
            while(!done) {
              try {
                streamOut.writeUTF(decorate("> ", Decorate.RED));
                streamOut.flush();
                String line = streamIn.readLine();
                handle(line);
                done = line.equals(".bye");
              } catch (Exception e) { done = true; }
            }
            close();
        } catch (Exception e) {
          fatalError("Could not establish server socket on port ");
        }
      }
    }


    public void start() {
      if (thread == null) {
        thread = new Thread(this);
        thread.start();
      }
    }

    public void open() {
      try {
        streamIn = new DataInputStream(
          new BufferedInputStream(socket.getInputStream()));
        streamOut = new DataOutputStream(socket.getOutputStream());
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
