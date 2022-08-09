package app;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
/**
 * Classe de lecture des messages du serveur
 * @author gaelle & mathis
 *
 */
public class ClientReader extends Thread{

    private final BufferedReader reader;
    private final HTMLEditorKit kit;
    private final Document doc;
    
    /**
     * Constructeur du lecteur du serveur
     * @param socket : socket de communication au serveur
     * @param kit : gestion de la vue
     * @param doc : affichage des infos pour la vue
     * @throws IOException : si buffer de lecture a echou�
     */
    ClientReader(Socket socket, HTMLEditorKit kit, Document doc) throws IOException {
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.kit = kit;
        this.doc = doc;
    }
    
    /**
     * Ex�cution du processus de lecture 
     * r�ception du message du serveur 
     * affichage dans le document et restitution dans le html
     */
    public void run() {
        String messageFromServer;
        while (true) {
            try {
                messageFromServer = reader.readLine();
                if (messageFromServer.equals("disconnected from channel")) break;
                String html = "<div class=\"left\"><span>" + messageFromServer + "</span></div>";
                HTMLDocument htmlDocument = (HTMLDocument) doc;
                try {
                    kit.insertHTML(htmlDocument, doc.getLength(), html, 0, 0, null);
                } catch (BadLocationException | IOException ignored) { }
            } catch (IOException e) {
                System.err.println("Error while reading from server : " + e);
                break;
            }
        }
    }

}
