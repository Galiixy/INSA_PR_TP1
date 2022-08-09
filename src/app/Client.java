package app;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Classe Client à lancer après avoir lancé le serveur
 * @author gaelle & mathis
 */
public class Client {

    private final String hostName;
    private final int port;
    
    /**
     * Constructeur du client
     * @param String hostName : adresse du serveur
     * @param int port : numéro de port du serveur
     */
    public Client(String hostName, int port) {
        this.hostName = hostName;
        this.port = port;
    }
    
    /**
     * Connexion au serveur puis lancement du processusclient management
     * @throw : UnknownHostException : Si l'adresse et/ou le port n'est pas correct 
     * 			IOException : Si la connexion n'est pas possible
     */
    public void connexion() {
        try {
            Socket socket = new Socket(hostName, port);
            new ClientManagement(socket).start();
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host : " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for "
                    + "the connection to : " + hostName);
            System.exit(1);
        }
    }
    
    /**
     * Main du client avec création d'un client avec les arguments puis lance la connexion
     * @param args avec en 1er argument l'adresse du serveur et en 2e le numero de port
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java EchoClient <EchoServer host> <EchoServer port>");
            System.exit(1);
        }
        new Client(args[0], Integer.parseInt(args[1])).connexion();
    }

}


