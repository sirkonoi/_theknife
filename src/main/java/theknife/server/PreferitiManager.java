package theknife.server;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import theknife.server.models.Luogo;
import theknife.server.models.Preferito;
import theknife.server.models.Ristorante;

public class PreferitiManager {
    
    DBManager db;

    public PreferitiManager(DBManager db) {
        this.db = db;
    }

    public boolean addPreferiti(Preferito pref) {
        String query = "INSERT INTO preferiti VALUES (?, ?)";
        try(PreparedStatement statement = db.connection.prepareStatement(query)) {
            statement.setInt(1, pref.getUtente());
            statement.setInt(2, pref.getRistorante());
            statement.execute();
            return true;
        }catch(SQLException e) {return false;}
    }

    public boolean removePreferiti(Preferito pref) {
        String query = "DELETE FROM preferiti WHERE utente = ? AND ristorante = ?";
        try(PreparedStatement statement = db.connection.prepareStatement(query)) {
            statement.setInt(1, pref.getUtente());
            statement.setInt(2, pref.getRistorante());
            statement.executeUpdate();
            return true;
        }catch(SQLException e) {return false;}
    }

    public List<Ristorante> getPreferitiUtente(int id) throws SQLException {
        List<Ristorante> lista = new ArrayList<>();
        String query = "SELECT ris.*, luogo.nazione, luogo.citta, luogo.indirizzo, luogo.latitudine, luogo.longitudine FROM ristorante ris JOIN luogo ON ris.luogo = luogo.id JOIN preferiti ON ris.id = preferiti.ristorante WHERE preferiti.utente = ?";

        try(PreparedStatement statement = db.connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                Luogo luogo = new Luogo( rs.getString("indirizzo"), rs.getString("citta"), rs.getString("nazione"), rs.getDouble("latitudine"), rs.getDouble("longitudine"));
                Ristorante ris = new Ristorante(rs.getInt("id"), rs.getString("nome"), luogo, rs.getInt("fascia_prezzo"), rs.getBoolean("delivery"), rs.getBoolean("prenotazione_online"), rs.getInt("ristoratore")
            );
             lista.add(ris);
            }
        }
        return lista;        
    }
}
