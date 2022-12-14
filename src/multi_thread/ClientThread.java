package multi_thread;

import java.io.*;
import java.net.*;

public class ClientThread extends Thread {

    private final Socket clientSocket;

    ClientThread(Socket s) {
        this.clientSocket = s;
    }

    /**
     * receives a request from client then sends an echo to the client
     **/
    public void run() {
        try {
            BufferedReader socIn;
            socIn = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            PrintStream socOut = new PrintStream(clientSocket.getOutputStream());

            while (true) {
                String line = socIn.readLine();
                socOut.println("clientName" + " : " + line);
            }
        } catch (Exception e) {
            System.err.println("Error in EchoServer:" + e);
        }
    }

}


