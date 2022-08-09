package app;

import java.io.*;
import java.net.Socket;


/**
 * Old & Unused Sender
 * Used before GUI, in CLI
 */
public class ClientSender extends Thread{

    private final PrintStream sender;
    private final BufferedReader scanner;

    ClientSender(Socket socket) throws IOException {
        this.sender = new PrintStream(socket.getOutputStream());
        this.scanner = new BufferedReader(new InputStreamReader(System.in));
    }

    public void run() {
        try {
            String message = "";
            while (!message.equals("exit")) {
                message = scanner.readLine();
                sender.println(message);
            }
        } catch (IOException e) {
            System.err.println("Error while reading from console:" + e);
        }
    }

}
