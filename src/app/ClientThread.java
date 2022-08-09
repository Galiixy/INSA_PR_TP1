package app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Classe de processus d'une instance client c�t� serveur
 * @author gaelle & mathis
 *
 */
public class ClientThread extends Thread {

    private final Socket socket;
    private final Server server;
    private final PrintStream sender;
    private final BufferedReader reader;

    private String clientName;
    private String currentChannel;

    /**
     * Constructeur du processus Client
     * @param socket : Socket de communication au serveur
     * @param server
     * @throws IOException : Si le buffer de lecture du client n'a pas abouti
     */
    ClientThread(Socket socket, Server server) throws IOException {
        this.socket = socket;
        this.server = server;
        this.sender = new PrintStream(socket.getOutputStream());
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        clientName = "";
    }

    /**
     * Execution de l'instance avec reception des commandes de la part du client et ex�cution de ces services
     */
    public void run() {
        try {

            server.addClientInServer(this);
            setClientName();

            String channelChoice = "";
            while(!channelChoice.equals("exit")) {
                channelChoice = reader.readLine();
                if (channelChoice.equals("create")) {
                    createChannel();
                } else if (channelChoice.equals("join")) {
                    if (joinChannel()) {
                        String messageFromClient = "";
                        while (!messageFromClient.equals("exit")) {
                            messageFromClient = reader.readLine();
                            if (!messageFromClient.equals("exit") && !messageFromClient.equals(""))
                                server.sendInChannel(clientName + " : " + messageFromClient, currentChannel, this);
                        }
                        sendMessage("disconnected from channel");
                        server.removeClientFromChannel(this, currentChannel);
                    }
                }
            }

            // Client is disconnected
            server.removeClientFromServer(this);
            socket.close();

        } catch (Exception e) {
            server.log("Error in ClientThread - number : " + this.getName() + " - error : " + e);
        }
    }

    /**
     * Envoi du message au client
     * @param message
     */
    public void sendMessage(String message) {
        sender.println(message);
    }
    
    /**
     * Mettre le nom du client : reception cote client puis affectation c�t� serveur
     * @throws IOException
     */
    public void setClientName() throws IOException {
        clientName = reader.readLine();
        server.setClientName(this, clientName);
    }
    
    /**
     * Creation du channel 
     * R�cuperation du nom aupr�s du client puis affectation c�t� serveur
     * @throws IOException
     */
    public void createChannel() throws IOException {
        String channelName = reader.readLine();
        server.createChannel(channelName);
    }
    
    /**
     * Joindre un channel
     * @throws IOException
     */
    public boolean joinChannel() throws IOException {
        ArrayList<String> channels = server.listChannels();
        sender.println(channels);

        currentChannel = reader.readLine();

        if (!currentChannel.equals("back")) {
            ArrayList<String> history = server.addClientInChannel(this, currentChannel);
            for (String message : history) {
                sendMessage(message);
            }
            return true;
        }
        return false;
    }

}


