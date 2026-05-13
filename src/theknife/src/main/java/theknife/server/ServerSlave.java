package theknife.server;

import java.io.*;
import java.net.*;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import theknife.server.models.*;
import theknife.Message;

public class ServerSlave extends Thread {

    Socket socket;
    ObjectOutputStream out;
    ObjectInputStream in;
    UsersManager usersManager;
    RestaurantManager restaurantManager;
    DBManager db;
    RecensioneManager recensioniManager;
    
    public ServerSlave(Socket socket) {
        try {
            this.socket = socket;
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            db = new DBManager();
            usersManager = new UsersManager(db);
            restaurantManager = new RestaurantManager(db);
            recensioniManager = new RecensioneManager(db);
            System.out.println("Creazione slave riuscita.");
        }catch(IOException ie) {
            System.out.println("Creazione slave fallita.");
        }
    }

    public void run() {
        while(true) {
            try {
                Message request = (Message) in.readObject();
                Message result = null;
                if(request.getOp().equals("login")) {
                    String username = (String)request.getDati()[0];
                    String psw = (String)request.getDati()[1];
                    Utente user = usersManager.login(username, psw);
                    result = user != null ? new Message("OK", new Object[]{user}) : new Message("ERROR", new Object[] {"Login fallito..."});
                }
                else if(request.getOp().equals("register")) {
                    boolean reg = usersManager.register(new Object[]{(String) request.getDati()[0], (String) request.getDati()[1], (String) request.getDati()[2],(String) request.getDati()[3], (Date) request.getDati()[4], (String) request.getDati()[5], (String) request.getDati()[6]});
                    result = reg ? result = new Message("OK", new Object[]{usersManager.login((String) request.getDati()[2], Password.decrypt((String) request.getDati()[3]))}) : new Message("ERROR", new Object[]{"Registrazione fallita..."});

                }
                if(request.getOp().equals("guest")){
                    result = new Message("OK_GUEST", new Object[]{new Guest((String) request.getDati()[0], (String) request.getDati()[1])});
                }

                if(request.getOp().equals("cercaVicini")) {
                    String indirizzo = (String) request.getDati()[0];
                    int raggio = (int) request.getDati()[1];
                    List<Ristorante> lista = restaurantManager.cercaVicini(indirizzo, raggio);
                    result = new Message("OK", new Object[]{lista});
                } 

                if(request.getOp().equals("recensioniRistorante")) {
                    int id = (int) request.getDati()[0];
                    List<Recensione> lista = recensioniManager.getRecensioniRistorante(id);
                    result = new Message("OK", new Object[]{lista});
                }
                if(request.getOp().equals("infoRecensioni")) {
                    int id = (int) request.getDati()[0];
                    double[] info = recensioniManager.getInfoRecensioni(id);
                    result = new Message("OK", new Object[]{info});
                } 
                if(request.getOp().equals("getAllTipi")) {
                    List<String> tipi = restaurantManager.getAllTipi();
                    result = new Message("OK", new Object[]{tipi});
                }                                                 
                
                out.writeObject(result);
            } catch (ClassNotFoundException | IOException | SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
}
