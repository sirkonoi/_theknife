package theknife.server;

import java.sql.*;

import theknife.server.models.Password;

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

    public boolean login(String username, String psw) throws SQLException {
        if(!userExists(username)) {return false;}
        try(PreparedStatement statement = db.connection.prepareStatement("SELECT password FROM utente WHERE username = ?")) {
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();
            if(rs.next()) {
                String en_psw = rs.getString("password");
                return psw.equals(Password.decrypt(en_psw));
            }
        }
        return false;
    }

    public boolean userExists( String username) throws SQLException {
        try(PreparedStatement statement = db.connection.prepareStatement("SELECT 1 FROM utente WHERE username =  ?")) {
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();
            return rs.next();
        }
    }
}
