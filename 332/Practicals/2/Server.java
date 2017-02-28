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

import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;

public class Server implements Runnable {

    private Socket socket = null;
    private ServerSocket server = null;
    private DataInputStream streamIn = null;
    private DataOutputStream streamOut = null;
    private Thread thread = null;

    // This variable holds a list of all appointments
    // abstracted in "Appointment" data types.

    private static ArrayList<Appointment> appointments =
      new ArrayList<Appointment>();

    public static void fatalError(String msg) {
        System.out.println(msg);
        System.out.println("Exiting.");
        System.exit(1);
    }

    public String decorate(String message, Decorate decorator) {
      switch (decorator) {
        case RED    : return "\u001B[31m" + message + "\u001B[0m";
        case GREEN  : return "\u001B[32m" + message + "\u001B[0m";
        default     : return message;
      }
    }

    public void clearScreen() throws Exception {
      streamOut.writeUTF("\u001B[2J");
      streamOut.flush();
    }

    public void newAppointment() throws Exception {
      streamOut.writeUTF("Enter appointment name : ");
      streamOut.flush();
      String name = streamIn.readLine();
      if (appointmentExists(name)) {
        String msg = "An appointment named \"" + name + "\" already exists."
          + "\n";
        streamOut.writeUTF(msg);
        streamOut.flush();
      } else {
	streamOut.writeUTF("Write a short description of this appointment: \n");
	streamOut.flush();
	String desc = streamIn.readLine();

	streamOut.writeUTF("Enter the date of the appointment (dd/mm/yyyy) : ");
	streamOut.flush();
	String date = streamIn.readLine();


	Appointment newApp = new Appointment();
	newApp.setName(name);
	newApp.setDate(date);
	newApp.setDesc(desc);

	appointments.add(newApp);

        String msg = "Appointment added. \n";
        streamOut.writeUTF(msg);
        streamOut.flush();
      }
    }

    public boolean appointmentExists(String name) {
      for (Appointment a:appointments) {
        if (a.getName().equals(name)) { return true; }
      }
      return false;
    }

    public void deleteAppointment() throws Exception {
    	System.out.println("User tried to delete appointment.");
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
      } catch (Exception e) { System.out.println("printFile() : " + e); }
    }

    public void handle(String line) throws Exception {
      switch(line) {
        case "help"    :  printFile("Help.txt");                          break;
        case "new"     :  newAppointment();                               break;
        case "license" :  printFile("License.txt");                       break;
        case "search"  :  searchAppointments();                           break;
	case "delete"  :  deleteAppointment();				  break;
        case "clear"   :  clearScreen();                                  break;
        case "list"    :  listAppointments();                             break;
        case "killserv":  killServer();                                   break;
        default        :  unknownCommand(line);                           break;
      }
    }

    public void killServer() {
      if (appointments.size() > 0) {
        writeAppointmentsToFile("main.appl");
      }
      close();
      System.out.println("Server shutting down.");
      System.exit(0);
    }

    // In this function we bind to a port and start.

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
            System.out.println("IDLE");
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
        System.out.println("open() : " + e);
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
        System.out.println("open() : "  + e);
      }
    }

    public void searchAppointments() {

      ArrayList<Appointment> results = new ArrayList<Appointment>();
      try {
        streamOut.writeUTF("Enter a phrase to search by: ");
        streamOut.flush();
        String line = streamIn.readLine();
        for (Appointment a:appointments) {
          if (a.getName().equals(line)) {
            results.add(a);
          }
        }
        String message;
        if (results.size() > 1) {
          message = "More than one appointment. \n";
        } else if (results.size() == 1) {
          message =  decorate("\nOne appointment was found with this search phrase:\n\n", Decorate.GREEN) +
            results.get(0).toString() + "\n";
        } else {
          message = decorate("\n No appointments were found with this search phrase.\n", Decorate.RED);
        }

        streamOut.writeUTF(message);
        streamOut.flush();

      } catch (Exception e) { System.out.println("searchAppointments: " + e); }
    }

    public void unknownCommand(String line) {
      if (!line.equals("")) {
        try {
          streamOut.writeUTF("Unknown command  \"" + line
            + "\" . Type \"help\" to get a list of commands.\n");
          streamOut.flush();
        } catch (Exception e) { System.out.println("unknownCommand() : " + e); }
      }
    }

    public void listAppointments() {
      StringBuilder sb = new StringBuilder();
      sb.append("\n\tALL APPOINTMENTS\n");
      for (Appointment a:appointments) {
        if (a != null) {
          sb.append(a.toString()+"\n");
        }
      }
      try {
        streamOut.flush();
        streamOut.writeUTF(sb.toString());
        streamOut.flush();
      } catch (Exception e) { System.out.println("listAppointments() : " + e); }
    }

    public static void populateFromFile(String fname) {
      try {
        BufferedReader read = new BufferedReader(new FileReader(new File(fname)));
        String line = null;
        Appointment prospectiveAppointment = null;
        while ((line = read.readLine()) != null) {

          if (line.charAt(0) == '#') {
            if (prospectiveAppointment != null) {
              appointments.add(prospectiveAppointment);
            }
            prospectiveAppointment = new Appointment();
            line = line.replaceFirst("#","");
            line = line.trim();
            prospectiveAppointment.setName(line);
          }
          else {
            String [] lineArray = line.split(":");
            switch(lineArray[0]) {
              case "date" : ; prospectiveAppointment.setDate(lineArray[1]); break;
            }
          }
        }
        appointments.add(prospectiveAppointment);
      } catch (Exception e) { System.out.println(e); }
    }


    public static void writeAppointmentsToFile(String fname) {
      System.out.println("Writing appointments to file.");
      try {
        PrintWriter write = new PrintWriter(fname, "UTF-8");
        for (Appointment a:appointments) {
          if (a != null)
          {
            System.out.print(a.toString());
            write.print(a.toString());
          }
        }
        write.close();
      } catch (Exception e) { System.out.println("writeAppointmentsToFile() : " + e); }
    }

    public static void main(String[] args) {
      populateFromFile("main.appl");
      System.out.println("Populated " + appointments.size() + " appointments from file.");
      try {
        Server server = new Server(Integer.parseInt(args[0]));
      } catch(Exception e) {

        System.out.println(e);
      }
    }
}
