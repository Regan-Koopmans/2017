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

    // function defining all the provided commands.

    public void handle(String line) throws Exception {
        switch(line.trim()) {
        case "help"    :
            printFile("Help.txt");
            break;
        case "new"     :
            newAppointment();
            break;
        case "edit"    :
            editAppoint();
            break;
        case "license" :
            printFile("License.txt");
            break;
        case "search"  :
            searchAppointments();
            break;
        case "delete"  :
            deleteAppointment();
            break;
        case "rm"      :
            deleteAppointment();
            break;
        case "clear"   :
            clearScreen();
            break;
        case "list"    :
            listAppointments();
            break;
        case "ls"      :
            listAppointments();
            break;
        case "exit"    :
            close();
            break;
        case "bye"     :
            close();
            break;
        case "save"     :
            writeAppointmentsToFile("main.appl");
            break;
        case "poweroff":
            killServer();
            break;
        default        :
            unknownCommand(line);
            break;
        }
    }

    public void editAppoint() throws Exception {
        streamOut.writeChars("Enter a phrase to search by: ");
        streamOut.flush();
        String searchString = streamIn.readLine();
        if (appointmentExists(searchString)) {
            Appointment editAppoint = getAppointment(searchString);
            streamOut.writeChars("You are now editing " + searchString +
                                 ". To leave any field as it previously was,"+
                                 " just press enter.\n");
            streamOut.flush();
            streamOut.writeChars("Edit name ["+searchString+"]: ");
            streamOut.flush();
            String name = streamIn.readLine();
            streamOut.writeChars("Edit participants ["+editAppoint.
                                 getParticipants()+"]: ");
            streamOut.flush();
            String part = streamIn.readLine();
            streamOut.writeChars("Edit description: \n["+editAppoint.getDesc()
                                 +"]\n\n");
            streamOut.flush();
            String desc = streamIn.readLine();
            streamOut.writeChars("Edit time ["+editAppoint.getTimeString()+"]: ");
            streamOut.flush();
            String time = streamIn.readLine();
            streamOut.writeChars("Enter the date of the appointment ["+
                                 editAppoint.getDateString()+"]: ");
            streamOut.flush();
            String date = streamIn.readLine();

            name = (name.equals("")) ? editAppoint.getName() : name;
            part = (part.equals("")) ? editAppoint.getParticipants() : part;
            desc = (desc.equals("")) ? editAppoint.getDesc() : desc;

            editAppoint.setName(name);
            if (!date.equals("")) {
                editAppoint.setDate(date);
            }
            if (!time.equals("")) {
                editAppoint.setTime(time);
            }
            editAppoint.setDesc(desc);

            streamOut.writeChars("Edit was successful. " + name + " was saved.\n");
            streamOut.flush();
        } else {
            streamOut.writeUTF(decorate("\nCould not find " +
                                        "any appointments by that search "+
                                        "string.\n",Decorate.RED));
            streamOut.flush();
        }
    }

    public Appointment getAppointment(String searchString) {
        for (Appointment a:appointments) {
            if (a.getName().equals(searchString)) {
                return a;
            }
        }
        return null;
    }

    public static void fatalError(String msg) {
        System.out.println(msg);
        System.out.println("Exiting.");
        System.exit(1);
    }

    public String decorate(String message, Decorate decorator) {
        switch (decorator) {
        case RED    :
            return "\u001B[31m" + message + "\u001B[0m";
        case GREEN  :
            return "\u001B[32m" + message + "\u001B[0m";
        default     :
            return message;
        }
    }

    public void clearScreen() throws Exception {
        streamOut.writeUTF("\u001B[2J");
        streamOut.flush();
    }

    public void newAppointment() throws Exception {
        streamOut.writeChars("Enter appointment name : ");
        streamOut.flush();
        String name = streamIn.readLine();
        if (appointmentExists(name)) {
            String msg = "An appointment named \"" + name + "\" already exists."
                         + "\n";
            streamOut.flush();
            streamOut.writeChars(msg);
            streamOut.flush();
        } else {
            streamOut.writeChars("People attending this appointment (comma separated list): ");
            streamOut.flush();
            String part = streamIn.readLine();

            streamOut.writeChars("Write a short description of this appointment: \n\n");
            streamOut.flush();
            String desc = streamIn.readLine();

            streamOut.writeChars("Enter the time of the appointment (hh:mm:ss) : ");
            streamOut.flush();
            String time = streamIn.readLine();

            streamOut.writeChars("Enter the date of the appointment (dd/mm/yyyy) : ");
            streamOut.flush();
            String date = streamIn.readLine();

            Appointment newApp = new Appointment();
            newApp.setName(name);
            newApp.setDate(date);
            newApp.setTime(time);
            newApp.setDesc(desc);
            newApp.setParticipants(part);
            appointments.add(newApp);
            String msg = "Appointment added. \n";
            streamOut.writeChars(msg);
            streamOut.flush();
        }
    }

    public boolean appointmentExists(String name) {
        for (Appointment a:appointments) {
            if (a.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public void deleteAppointment() throws Exception {
        streamOut.writeChars("Enter a phrase to search by: ");
        streamOut.flush();

        String searchString = streamIn.readLine();
        if (appointmentExists(searchString)) {
            for (int x = 0; x < appointments.size(); ++x) {
                if (appointments.get(x).getName().equals(searchString)) {
                    appointments.remove(x);
                    streamOut.writeUTF(decorate("\nDeleting record found by the name of \" "
                                                + searchString + "\"\n\n",Decorate.GREEN));
                    streamOut.flush();
                    return;
                }
            }
        } else {
            streamOut.writeUTF(decorate("Could not find " +
                                        "any appointments by that search string.\n",Decorate.RED));
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
            streamOut.flush();
            streamOut.writeChars(sb.toString());
            streamOut.flush();
        } catch (Exception e) {
            System.out.println("printFile() : " + e);
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
                    } catch (Exception e) {
                        done = true;
                    }
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
        writeAppointmentsToFile("main.appl");
    }

    public void searchAppointments() {

        ArrayList<Appointment> results = new ArrayList<Appointment>();
        try {
            streamOut.writeChars("Enter a phrase to search by: ");
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
                message = decorate("\n No appointments were found with this" +
                                   " search phrase.\n", Decorate.RED);
            }

            streamOut.writeUTF(message);
            streamOut.flush();

        } catch (Exception e) {
            System.out.println("searchAppointments: " + e);
        }
    }

    public void unknownCommand(String line) {
        if (!line.equals("")) {
            try {
                streamOut.writeChars("\nUnknown command  \"" + line
                                     + "\". Type \"help\" to get a list of commands.\n");
                streamOut.flush();
            } catch (Exception e) {
                System.out.println("unknownCommand() : " + e);
            }
        }
    }

    public void listAppointments() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n\tALL APPOINTMENTS\n\t --------------\n\n");
        for (Appointment a:appointments) {
            if (a != null) {
                sb.append(a.toPrettyString()+"\n");
            }
        }
        try {
            streamOut.flush();
            streamOut.writeChars(sb.toString());
            streamOut.flush();
        } catch (Exception e) {
            System.out.println("listAppointments() : " + e);
        }
    }

    public static void populateFromFile(String fname) {
        try {
            BufferedReader read = new BufferedReader(
                new FileReader(new File(fname)));
            String line = null;
            Appointment prospectiveAppointment = null;
            while ((line = read.readLine()) != null) {

                if (line.charAt(0) == '#') {
                    System.out.println("READING ONE");
                    if (prospectiveAppointment != null) {
                        appointments.add(prospectiveAppointment);
                    }
                    prospectiveAppointment = new Appointment();
                    line = line.replaceFirst("#","");
                    line = line.trim();
                    prospectiveAppointment.setName(line);
                }
                else {
                    String [] lineArray = line.split(":",2);
                    switch(lineArray[0]) {
                    case "date" :
                        ;
                        prospectiveAppointment.setDate(lineArray[1]);
                        break;
                    case "part" :
                        ;
                        prospectiveAppointment.setParticipants(lineArray[1]);
                        break;
                    case "desc" :
                        ;
                        prospectiveAppointment.setDesc(lineArray[1]);
                        break;
                    case "time" :
                        ;
                        prospectiveAppointment.setTime(lineArray[1]);
                        break;
                    }
                }
            }
            appointments.add(prospectiveAppointment);
        } catch (Exception e) {
            System.out.println("populate : " + e);
        }
    }


    public static void writeAppointmentsToFile(String fname) {
        System.out.println("Writing appointments to file.");
        try {
            PrintWriter write = new PrintWriter(fname, "UTF-8");
            for (Appointment a:appointments) {
                if (a != null) {
                    write.print(a.toString());
                }
            }
            write.close();
        } catch (Exception e) {
            System.out.println("writeAppointmentsToFile() : " + e);
        }
    }

    public static void main(String[] args) {
        populateFromFile("main.appl");
        System.out.println("Populated " + appointments.size()
                           + " appointments from file.");
        try {
            Server server = new Server(Integer.parseInt(args[0]));
        } catch(Exception e) {
            System.out.println(e);
        }
    }
}
