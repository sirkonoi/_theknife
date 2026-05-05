package theknife.server;

import java.sql.*;

public class UsersManager {

    DBManager db;

    public UsersManager(DBManager db) {
        this.db = db;
    }
    
    public boolean register(Object[] values) throws SQLException {
        if(!userExists((String)values[2])) {
            db.insert(values, "Utente");
            return true;
        }
        return false;
    }

    public boolean userExists( String username) throws SQLException {
        try(PreparedStatement statement = db.connection.prepareStatement("SELECT 1 FROM Utente WHERE Username =  ?")) {
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();
            return rs.next();
        }
    }
}
