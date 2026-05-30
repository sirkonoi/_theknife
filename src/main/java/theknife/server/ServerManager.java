/**
 * Studente: Mattia Rotteri
 * Matricola: 762508
 * Sede: Varese
 */
package theknife.server;

import java.io.IOException;
import java.net.*;

/**
 * Classe ServerManager.
 * Gestisce la connessione con i client.
 * Inizializza il {@link ServerSocket} sulla porta 6767 e il {@link DBManager},
 * rimanendo in ascolto di richieste, per cui istanzia un {@link ServerSlave}.
 *  */
public class ServerManager {
    /**
     * La porta standard per la connessione socket.
     */
    public static final int PORT = 6767;
     
    /**
     * Il socket del server.
     */
    ServerSocket server;

    /**
     * Il database.
     */
    DBManager db;

    /**
     * Costruttore predefinito.
     * Utilizzato per i test durante lo sviluppo dell'app.     
     */
    public ServerManager() {
        try {
            this.db = new DBManager();
            server = new ServerSocket(PORT);  
            System.out.println("Server: connessione riuscita. PORTA("+ PORT+")");  
        }catch(IOException ie) {
            System.out.println("Server: connessione fallita...");
            ie.printStackTrace(); 
            System.exit(1);         
        }
    }

    /**
     * Costruttore di ServerManager.
     * Inizializza il DBManager con le opportune credenziali e apre il ServerSocket 
     *
     * @param url URL per la connessione al DB.
     * @param user Username dabatase.
     * @param psw La password dell'utente del database.
     */
    public ServerManager(String url, String user, String psw) {
        try {
            this.db = new DBManager(url, user, psw);
            server = new ServerSocket(PORT);
            System.out.println("Server: connessione riuscita. PORTA("+ PORT+")");  
        } catch(IOException ie) {
            System.out.println("Server: connessione fallita...");
        }
    }    

    /**
     * Avvia il ciclo infinito di ascolto del server.
     * Per ogni connessione client accettata, crea un nuovo thread slave {@link ServerSlave}.
     */
    public void exec()  {
        Socket socket = null;
        while(true) {
            try {
                socket = server.accept();
                System.out.println("Il client " + socket + " si e' connesso.");
                new ServerSlave(socket, db).start();
            }catch(IOException e) {
                try {
                    System.out.println("Il client " + socket + " si e' disconnesso.");
                    socket.close();
                } catch (IOException e1) {}
            }
        }
    }

    /**
     * Restituisce l'oggetto ServerSocket.
     * Viene usato in ServerGUI per lo spegnimento del server.
     * 
     * @return {@link ServerSocket} del server.
     */
    public ServerSocket getServer() { return this.server; }

    /**
    * Il metodo Main.
    */
    public static void main(String[] args) throws IOException {
        new ServerManager().exec();
    }
}
