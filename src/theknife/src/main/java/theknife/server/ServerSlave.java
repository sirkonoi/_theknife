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
    PreferitiManager preferitiManager;

    public ServerSlave(Socket socket) {
        try {
            this.socket = socket;
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            db = new DBManager();
            usersManager = new UsersManager(db);
            restaurantManager = new RestaurantManager(db);
            recensioniManager = new RecensioneManager(db);
            preferitiManager = new PreferitiManager(db);
            System.out.println("Creazione slave riuscita.");
        } catch (IOException ie) {
            System.out.println("Creazione slave fallita.");
        }
    }

    public void run() {
        try {
            while (true) {
                Message request = (Message) in.readObject();
                Message result = null;

                if (request.getOp().equals("login")) {
                    String username = (String) request.getDati()[0];
                    String psw = (String) request.getDati()[1];
                    Utente user = usersManager.login(username, psw);
                    result = user != null ? new Message("OK", new Object[] { user }) : new Message("ERROR", new Object[] { "Login fallito..." });
                } 
                else if (request.getOp().equals("register")) {
                    boolean reg = usersManager.register(new Object[] { (String) request.getDati()[0], (String) request.getDati()[1], (String) request.getDati()[2], (String) request.getDati()[3], (Date) request.getDati()[4], (String) request.getDati()[5], (String) request.getDati()[6] });
                    result = reg ? new Message("OK", new Object[] { usersManager.login((String) request.getDati()[2], Password.decrypt((String) request.getDati()[3])) }) : new Message("ERROR", new Object[] { "Registrazione fallita..." });
                }

                else if (request.getOp().equals("guest")) {
                    result = new Message("OK_GUEST", new Object[] { new Guest((String) request.getDati()[0]) });
                }

                else if (request.getOp().equals("filtra")) {
                    Object[] filtri = request.getDati();
                    List<Ristorante> lista = restaurantManager.filtra(filtri);
                    result = new Message("OK", new Object[] { lista });
                }

                else if (request.getOp().equals("cercaFromNome")) {
                    String nome = (String) request.getDati()[0];
                    Ristorante ris = restaurantManager.cercaRistorante(nome);
                    if (ris == null) {
                        result = new Message("ERROR", new Object[] { "ricerca fallita..." });
                    }
                    result = new Message("OK", new Object[] { ris });
                }

                else if (request.getOp().equals("cercaFromId")) {
                    int id = (int) request.getDati()[0];
                    Ristorante ris = restaurantManager.cercaRistorante(id);
                    if (ris == null) {
                        result = new Message("ERROR", new Object[] { "ricerca fallita..." });
                    }
                    result = new Message("OK", new Object[] { ris });
                }                

                else if(request.getOp().equals("getTipoRistorante")) {
                    int id = (int) request.getDati()[0];
                    List<String> tipo = restaurantManager.getTipo(id);
                    result = new Message("OK", new Object[] { tipo });
                }

                else if (request.getOp().equals("getAllTipi")) {
                    List<String> tipi = restaurantManager.getAllTipi();
                    result = new Message("OK", new Object[] { tipi });
                }

                else if (request.getOp().equals("getPreferitiUtente")) {
                    int idUtente = (int) request.getDati()[0];
                    List<Ristorante> lista = preferitiManager.getPreferitiUtente(idUtente);
                    result = new Message("OK", new Object[] { lista });
                }   

                else if (request.getOp().equals("addPreferiti")) {
                    int idUtente = (int) request.getDati()[0];
                    int idRistorante = (int) request.getDati()[1];
                    String nomeRistorante = (String) request.getDati()[2];
                    boolean add = preferitiManager.addPreferiti(new Preferito(idUtente, idRistorante, nomeRistorante));
                    if(add) {result= new Message("OK", new Object[] {});}
                    else { result = new Message("ERROR", new Object[] { "add preferiti fallito..." });}
                } 

                else if (request.getOp().equals("removePreferiti")) {
                    int idUtente    = (int) request.getDati()[0];
                    int idRistorante = (int) request.getDati()[1];
                    boolean ok = preferitiManager.removePreferiti(new Preferito(idUtente, idRistorante, ""));
                    result = ok ? new Message("OK", new Object[]{}) : new Message("ERROR", new Object[]{"Rimozione fallita"});
                } 
                
                else if (request.getOp().equals("recensioniRistorante")) {
                    int id = (int) request.getDati()[0];
                    List<Recensione> lista = recensioniManager.getRecensioniRistorante(id);
                    result = new Message("OK", new Object[] { lista });
                }                
                
                else if (request.getOp().equals("infoRecensioni")) {
                    int id = (int) request.getDati()[0];
                    double[] info = recensioniManager.getInfoRecensioni(id);
                    result = new Message("OK", new Object[] { info });
                }                 
                
                if (request.getOp().equals("rispondiRecensione")) {
                    int id = (int) request.getDati()[0];
                    String testo = (String) request.getDati()[1];
                    boolean ris = recensioniManager.addRisposta(id, testo);
                    if(ris) { result = new Message("OK", new Object[] {});}
                    else { result = new Message("ERROR", new Object[]{"Impossibile rispondere"});}
                }

                if (request.getOp().equals("aggiungiRecensione")) {
                    Recensione rec = (Recensione) request.getDati()[0];
                    boolean ris = recensioniManager.addRecensione(rec);
                    if(ris) { result = new Message("OK", new Object[] {});}
                    else { result = new Message("ERROR", new Object[]{"Aggiunta fallita"});}
                }  

                if (request.getOp().equals("eliminaRecensione")) {
                    int id = (int) request.getDati()[0];
                    boolean ris = recensioniManager.deleteRecensione(id);
                    if(ris) { result = new Message("OK", new Object[] {});}
                    else { result = new Message("ERROR", new Object[]{"Rimozione fallita"});}
                }                
                
                

                out.writeObject(result);
                out.flush();
            }
        } catch (ClassNotFoundException | IOException | SQLException e) {
            System.out.println("Terminazione client: " + socket);
        } finally {
            try {
                System.out.println("Chiudo gli streams e il socket.....");
                    out.close();
                    in.close();
                    socket.close();
                System.out.println("OK: risorse liberate.");
            } catch (IOException e) {
                System.out.println("Rilascio fallito.....");
            }
        }
    }
}