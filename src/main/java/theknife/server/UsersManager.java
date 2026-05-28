/**
 * Studente: Mattia Rotteri
 * Matricola: 762508
 * Sede: Varese
 */
package theknife.server;

import java.sql.*;
import theknife.server.models.*;

/**
 * Classe UsersManager.
 * Gestisce la logica e interazioni con il database relative
 * alla registrazione e login degli utenti. 
 */
public class UsersManager {

    /**
     * Il database.
     */
    DBManager db;

    String[] columns = {"nome", "cognome", "username", "password", "data_nascita", "domicilio", "ruolo"};

    /**
     * Costruttore della classe UsersManager.
     *
     * @param db {@link DBManager} database.
     */
    public UsersManager(DBManager db) {
        this.db = db;
    }
   
    /**
     * Esegue la registrazione di un nuovo account.
     *
     * @param values Un array di oggetti contenente i dati dell'utente.
     * @return true se la registrazione avviene con successo, false se l'username è già presente.
     * @throws SQLException
     */
    public boolean register(Object[] values) throws SQLException {
        if(!userExists((String)values[2])) {
            db.insert(values, columns, "utente");
            return true;
        }
        return false;
    }

    /**
     * Esegue il login di un utente.
     *
     * @param username L'username dell'utente.
     * @param psw La password in chiaro dell'utente.
     * @return {@link Utente} (o {@link Ristoratore}) in caso di successo, null altrimenti.
     * @throws SQLException
     */
    public Utente login(String username, String psw) throws SQLException {
        if(userExists(username)) {
            try(PreparedStatement statement = db.connection.prepareStatement("SELECT * FROM utente WHERE username = ?")) {
                statement.setString(1, username);
                ResultSet rs = statement.executeQuery();
                if(rs.next()) {
                    String decr_psw = Password.decrypt(rs.getString("password"));
                    if(decr_psw.equals(psw)) {
                        if(rs.getString("ruolo").equals("utente")) {
                            return new Utente(rs.getInt("id"), rs.getString("nome"), rs.getString("cognome"), rs.getString("username"), new Password(rs.getString("password")), rs.getDate("data_nascita"), rs.getString("domicilio"));
                        }
                        else {
                            return new Ristoratore(rs.getInt("id"), rs.getString("nome"), rs.getString("cognome"), rs.getString("username"), new Password(rs.getString("password")), rs.getDate("data_nascita"), rs.getString("domicilio"));
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * Verifica se un utente e' gia' registrato all'interno dell'app tramite username.
     *
     * @param username Username dell'utente.
     * @return true se lo username esiste già all'interno della tabella utente, false altrimenti.
     * @throws SQLException
     */
    public boolean userExists( String username) throws SQLException {
        try(PreparedStatement statement = db.connection.prepareStatement("SELECT 1 FROM utente WHERE username =  ?")) {
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();
            return rs.next();
        }
    }

    /**
     * Restituisce un Utente trovato tramite id.
     * 
     * @param id L'id dell'utente da cercare.
     * @return Un'istanza di {@link Utente} o {@link Ristoratore} se presente, null altrimenti.
     */
    public Utente getUserFromID(int id) {
        String query = "SELECT * FROM utente WHERE id = ?";
        try(PreparedStatement statement = db.connection.prepareStatement(query)) {
            statement.setInt(1, id);
            try(ResultSet rs = statement.executeQuery()) {
                if(rs.next()) {
                    if(rs.getString("ruolo").equals("ristoratore")) {
                        return new Ristoratore(rs.getInt("id"), rs.getString("nome"), rs.getString("cognome"), rs.getString("username"), new Password(rs.getString("password")), rs.getDate("data_nascita"), rs.getString("domicilio"));
                    }
                    return new Utente(rs.getInt("id"), rs.getString("nome"), rs.getString("cognome"), rs.getString("username"), new Password(rs.getString("password")), rs.getDate("data_nascita"), rs.getString("domicilio"));
                }
            }
        }catch(SQLException e) { e.printStackTrace(); }
        return null;
    }
}
