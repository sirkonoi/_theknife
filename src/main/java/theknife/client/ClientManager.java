/**
 * Studente: Mattia Rotteri
 * Matricola: 762508
 * Sede: Varese
 */

package theknife.client;

import java.io.*;
import java.net.*;

import theknife.Message;
import theknife.server.ServerManager;

/**
 * Classe ClientManager.
 * Gestisce la connessione con il server.
 */
public class ClientManager {
    
    /**
     * Socket.
     */
    Socket client;

    /**
     * Lo stream di output.
     */
    ObjectOutputStream out;

    /**
     * Lo stream di input.
     */
    ObjectInputStream in;

    /**
     * Costruttore della classe ClientManager.
     * Tenta di stabilire una connessione con il server tramite la porta
     * e inizializza gli stream di oggetti.
     */
    public ClientManager() {
        InetAddress addr;
        try {
            addr = InetAddress.getByName(null);
            client = new Socket(addr, ServerManager.PORT);
            out = new ObjectOutputStream(client.getOutputStream());
            in = new ObjectInputStream(client.getInputStream());
            System.out.println("Client: connessione riuscita.");

        } catch (IOException e) {
            System.out.println("Client: connessione fallita.");
        }
    }

    /**
     * Invia un messaggio al server.
     *
     * @param message {@link Message} messaggio da inviare al server.
     * @return {@link Message} risposta ricevuta dal seerver.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public Message send(Message message) throws IOException, ClassNotFoundException {
        out.writeObject(message);
        return (Message) in.readObject();
    }
}
