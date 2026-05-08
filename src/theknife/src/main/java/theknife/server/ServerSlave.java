package theknife.server;

import java.io.*;
import java.net.*;
import java.sql.Date;
import java.sql.SQLException;

import theknife.server.models.*;
import theknife.Message;

public class ServerSlave extends Thread {

    Socket socket;
    ObjectOutputStream out;
    ObjectInputStream in;
    UsersManager usersManager;
    
    public ServerSlave(Socket socket) {
        try {
            this.socket = socket;
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            usersManager = new UsersManager(new DBManager());
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
                out.writeObject(result);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
}
