package app;

import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
/**
 * G�re les services c�t� clients
 * @author gaelle & mathis
 *
 */
public class ClientManagement extends Thread {

    private final Socket socket;

    private final ClientIHM ihm = new ClientIHM(this, "Client");

    private final PrintStream sender;
    private final BufferedReader reader;

    private String clientName;
    private String currentChannelName;
    
    /**
     * Constructeur Client Management
     * instancie la lecture et l'envoi
     * @param socket : socket de connexion avec le serveur
     * @throws IOException
     */
    public ClientManagement(Socket socket) throws IOException {
        this.socket = socket;
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.sender = new PrintStream(socket.getOutputStream());
    }
    
    /**
     * lancement de l'IHM
     */
    public void run() {
        ihm.start();
    }
    
    /**
     * @return String nom du client
     */
    public String getClientName() {
        return clientName;
    }
    
    /**
     * Mettre le nom du client et envoi au serveur
     * @param clientName nom du client
     */
    public void setClientName(String clientName) {
        this.clientName = clientName;
        sender.println(clientName);
    }

    /**
     * Envoi au serveur de l'action du client
     * Si action est exit, fermeture de la socket
     * @param action
     */
    public void chooseAction(String action) {
        sender.println(action);
        if (action.equals("exit")) {
            try {socket.close();} catch (IOException ignored) {}
        }
    }
    
    /**
     * Cr�ation d'un channel aupr�s du serveur
     * @param channelName
     */
    public void createChannel(String channelName) {
        sender.println(channelName);
    }
    
    /**
     * R�cup�rer la liste des channels aupr�s du serveur
     * @return la liste String des channels
     */
    public String[] getChannels() {
        try {
            String channels = reader.readLine();
            channels = channels.substring(1, channels.length()-1);
            return channels.split(", ");
        } catch (IOException ignored) { }
        return new String[]{};
    }
    
    /**
     * R�cup�rer le nom du channel courant 
     * @return String nom du channel
     */
    public String getCurrentChannelName() {
        return currentChannelName;
    }

    /**
     * Rejoindre un channel du serveur
     * @param channelName nom du channel l'identifiant
     */
    public void joinChannel(String channelName) {
        currentChannelName = channelName;
        sender.println(channelName);
    }
    
    /**
     * Envoi d'un message dans le channel courant du serveur
     * @param message
     */
    public void sendMessageInChannel(String message) {
        sender.println(message);
    }
    
    /**
     * Lancement du Processus des informations du serveur
     * @param kit
     * @param doc
     */
    public void runReader(HTMLEditorKit kit, Document doc) {
        try {
            ClientReader clientReader = new ClientReader(socket, kit, doc);
            clientReader.start();
        } catch (IOException ignored) { }
    }

}
