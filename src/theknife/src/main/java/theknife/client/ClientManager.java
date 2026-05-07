package theknife.client;

import java.io.*;
import java.net.*;

import theknife.server.ServerManager;

public class ClientManager {
    
    Socket client;
    ObjectOutputStream out;
    ObjectInputStream in;

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

    public Message send(Message message) throws IOException, ClassNotFoundException {
        out.writeObject(message);
        return (Message) in.readObject();
    }
}
