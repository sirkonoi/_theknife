package theknife.server;

import java.io.IOException;
import java.net.*;

public class ServerManager {
    
    ServerSocket server;
    DBManager db; //implementa meglio: se uno usa il proprio server

    public static final int PORT = 6767;
    public ServerManager() {
        try {
            server = new ServerSocket(PORT);  
            System.out.println("Server: connessione riuscita.");  
        }catch(IOException ie) {
            System.out.println("Server: connessione fallita...");
        }
    }

    public void exec()  {
        Socket socket = null;
        while(true) {
            try {
                socket = server.accept();
                System.out.println("Il client " + socket + " si e' connesso.");
                new ServerSlave(socket).start();
            }catch(IOException e) {
                try {
                    System.out.println("Il client " + socket + " si e' disconnesso.");
                    socket.close();
                } catch (IOException e1) {}
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new ServerManager().exec();
    }
}
