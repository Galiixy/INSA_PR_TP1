package app;

import com.google.gson.Gson;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.logging.Logger;
/**
 * Classe Serveur � lancer pour avoir acc�s au chat
 * @author gaelle & mathis
 */
public class Server {

    private final Logger logger = Logger.getAnonymousLogger();

    private final int port;
    private final HashMap<ClientThread, String> clients = new HashMap<>();
    private final HashMap<String, ArrayList<ClientThread>> channels = new HashMap<>();
    private final HashMap<String, ArrayList<String>> history = new HashMap<>();
    
    /**
     * Initialise le serveur avec le port
     * @param port : num�ro du port
     */
    public Server(int port) {
        this.port = port;
        loadHistory();
    }

    /**
     * Charge l'historique des conversations
     */
    @SuppressWarnings("unchecked")
    private void loadHistory() {
        try {
            String jsonHistory = Files.readString(Paths.get(".","src", "app", "history.txt"));
            HashMap<String, ArrayList<String>> historyFromJson = new Gson().fromJson(jsonHistory, HashMap.class);
            for (Map.Entry<String, ArrayList<String>> entry : historyFromJson.entrySet()) {
                createChannel(entry.getKey());
                for (String message : entry.getValue()) {
                    history.get(entry.getKey()).add(message);
                }
            }
        } catch (IOException e) {
            logger.info("INTERNAL SERVER ERROR");
        }
    }

    /**
     * Sauvegarde l'historique des conversations
     */
    private void saveHistory() {
        String jsonHistory = new Gson().toJson(history);
        try {
            Files.writeString(Paths.get(".","src", "app", "history.txt"), jsonHistory, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            logger.info("INTERNAL SERVER ERROR");
        }
    }
    
    /**
     * Instancie les logs avec un message
     * @param message : message d'information
     */
    public void log(String message) {
        logger.info(message);
    }

    /**
     * Main du serveur, v�rifie la pr�sence du param�tre port puis lance l'ex�cution du serveur
     * @param args avec args[0] = port
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java EchoServer <EchoServer port>");
            System.exit(1);
        }

        int port = Integer.parseInt(args[0]);

        Server server = new Server(port);
        server.run();
    }

    /**
     * M�thode d'ex�cution du serveur
     * Catch new connections and start new ClientThread for each
     */
    public void run() {
        logger.info("Server started on port : " + port);
        try {
            ServerSocket listenSocket = new ServerSocket(this.port);
            logger.info("Server ready...");
            while (true) {
                Socket clientSocket = listenSocket.accept();
                logger.info("New connexion from : " + clientSocket.getInetAddress());

                ClientThread clientThread = new ClientThread(clientSocket, this);
                clientThread.start();
            }
        } catch (IOException ignored) {        }
    }
    
    /**
     * Ajouter un nom de Client
     * Mise � jour de la liste clients
     * @param clientThread : processus client qui demande une affectation
     * @param clientName : nom du client
     */
    public void setClientName(ClientThread clientThread, String clientName) {
        clients.replace(clientThread, clientName);
    }
  
    /**
     * Ajout d'un client au Serveur, se produit lors de la connexion
     * Mise � jour de la liste clients
     * @param clientThread : processus client
     */
    public void addClientInServer(ClientThread clientThread) {
        clients.put(clientThread, "");
    }
    
    /**
     * Retrait d'un client au Serveur, se produit lors de la d�connexion
     * Mise � jour de la liste clients
     * @param clientThread : processus client
     */
    public void removeClientFromServer(ClientThread clientThread) {
        String clientName = clients.remove(clientThread);
        logger.info(clientThread.getName() + " - " + clientName + " left server");
    }

    /**
     * Nombre de clients connect�s au serveur
     * @return int : taille de la liste clients
     */
    public int connectedClient() {
        return clients.size();
    }
    
    /**
     * Envoi du message � tous les utilisateurs sauf � celui donn� en param�tre
     * @param message : message � transmettre
     * @param excludeClient : processus du client qui envoie le message
     */
    public void sendAll(String message, ClientThread excludeClient) {
        for (ClientThread clientThread : clients.keySet()) {
            if (clientThread != excludeClient) {
                clientThread.sendMessage(message);
            }
        }
    }

    /**
     * Envoi d'un message dans un channel sauf � celui donn� en param�tre
     * @param message : message � transmettre
     * @param channel : nom du channel qui l'identifie
     * @param excludeClient : processus du client qui envoie le message
     */
    public void sendInChannel(String message, String channel, ClientThread excludeClient) {
        history.get(channel).add(message);
        ArrayList<ClientThread> clientsInChannel = channels.get(channel);
        for (ClientThread clientThread : clientsInChannel) {
            if(clientThread != excludeClient) {
                clientThread.sendMessage(message);
            }
        }
        saveHistory();
    }

    /**
     * Cr�ation d'un channel et ajout de channel � la liste du serveurs
     * @param channelName : nom du channel qui l'identifie,
     * s'il existe d�j� dans la base il ne se passe rien
     */
    public void createChannel(String channelName) {
        if (!channels.containsKey(channelName)) {
            channels.put(channelName, new ArrayList<>());
            history.put(channelName, new ArrayList<>());
            logger.info("New channel created : " + channelName);
        }
    }
    
    /**
     * Liste des channels propos�s par les utilisateurs du serveurs, peut-�tre vide mais diff�rente de null
     * @return ArrayList<String>
     */
    public ArrayList<String> listChannels() {
        return new ArrayList<>(channels.keySet());
    }

   
    
    /**
     * Ajout d'un client sur le channel
     * @param clientThread : processus client
     * @param channel : nom du channel qui l'identifie
     * @return ArrayList Historique de discussion du channel
     * si le nom du channel correspond � aucun channel dans le serveur il ne se passe rien
     */
     public ArrayList<String> addClientInChannel(ClientThread clientThread, String channel) {
        if (channels.containsKey(channel)) {
            channels.get(channel).add(clientThread);
            logger.info(clientThread.getName() + " - " + clients.get(clientThread) + " join channel : " + channel);
            return history.get(channel);
        }
        return new ArrayList<>();
    }
    
    /**
     * Retirer un client du channel
     * @param clientThread : processus client
     * @param channel : nom du channel qui l'identifie
     * si le nom du channel correspond � aucun channel dans le serveur il ne se passe rien
     */
    public void removeClientFromChannel(ClientThread clientThread, String channel) {
        if (channels.containsKey(channel)) {
            channels.get(channel).remove(clientThread);
            logger.info(clientThread.getName() + " - " + clients.get(clientThread) + " left channel : " + channel);
        }
    }
    
    /**
     * Nombre de clients connect�s au channel
     * @param channel : nom du channel qui l'identifie
     * @return int : taille de la liste channel peut-�tre �gale � 0
     */
    public int connectedInChannel(String channel) {
        if (channels.containsKey(channel)) {
            return channels.get(channel).size();
        }
        return 0;
    }
    
}


