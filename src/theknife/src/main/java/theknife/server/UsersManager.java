package theknife.server;

import java.sql.*;

import javax.naming.spi.DirStateFactory.Result;

import theknife.server.models.*;

public class UsersManager {

    DBManager db;
    String[] columns = {"nome", "cognome", "username", "password", "data_nascita", "domicilio", "ruolo"};

    public UsersManager(DBManager db) {
        this.db = db;
    }
    
    public boolean register(Object[] values) throws SQLException {
        if(!userExists((String)values[2])) {
            db.insert(values, columns, "utente");
            return true;
        }
        return false;
    }

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

    public boolean userExists( String username) throws SQLException {
        try(PreparedStatement statement = db.connection.prepareStatement("SELECT 1 FROM utente WHERE username =  ?")) {
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();
            return rs.next();
        }
    }

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
