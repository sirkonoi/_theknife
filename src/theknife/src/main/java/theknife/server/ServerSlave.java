package theknife.server;

import java.io.*;
import java.net.*;
import java.sql.SQLException;

import theknife.client.Message;
import theknife.server.models.Utente;
import theknife.server.UsersManager;

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
        }catch(IOException ie) {
            System.out.println("Creazione slave fallita");
        }
    }

    public void run() {
        while(true) {
            try {
                Message request = (Message) in.readObject();
                Object[] datiResult;
                Message result;
                if(request.getOp().equals("login")) {
                    String username = (String)request.getDati()[0];
                    String psw = (String)request.getDati()[1];
                    Utente user = usersManager.login(username, psw);
                    if(user!= null) {
                        datiResult = new Object[]{"OK"};
                        result = new Message("OK", datiResult);
                    }
                    else {
                        result = new Message("ERROR", null);
                    }
                  out.writeObject(result);
                }
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
