/**
 * Studente: Mattia Rotteri
 * Matricola: 762508
 * Sede: Varese
 */
package theknife.server;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import theknife.server.models.Luogo;
import theknife.server.models.Preferito;
import theknife.server.models.Ristorante;

/**
 * Classe PreferitiManager.
 * Gestisce la logica e le interazioni con il database relative 
 * ai ristoranti preferiti degli utenti.
 */
public class PreferitiManager {
    
    /**
     * Il database
     */
    DBManager db;

    /**
     * Costruttore della classe PreferitiManager.
     *
     * @param db {@link DBManager} condiviso per l'accesso al database.
     */
    public PreferitiManager(DBManager db) {
        this.db = db;
    }

    /**
     * Aggiunge un ristorante tra i preferiti di un utente.
     *
     * @param pref {@link Preferito} contenente id utente e id ristorante.
     * @return true se l'inserimento avviene con successo, false in caso di eccezione SQL.
     */
    public boolean addPreferiti(Preferito pref) {
        String query = "INSERT INTO preferiti VALUES (?, ?)";
        try(PreparedStatement statement = db.connection.prepareStatement(query)) {
            statement.setInt(1, pref.getUtente());
            statement.setInt(2, pref.getRistorante());
            statement.execute();
            return true;
        }catch(SQLException e) {return false;}
    }

    /**
     * Rimuove un ristorante dall'elenco dei preferiti di un utente.
     *
     * @param pref {@link Preferito} contenente id utente e id ristorante della recensione da cancellare.
     * @return true se la rimozione avviene con successo, false in caso di eccezione SQL.
     */
    public boolean removePreferiti(Preferito pref) {
        String query = "DELETE FROM preferiti WHERE utente = ? AND ristorante = ?";
        try(PreparedStatement statement = db.connection.prepareStatement(query)) {
            statement.setInt(1, pref.getUtente());
            statement.setInt(2, pref.getRistorante());
            statement.executeUpdate();
            return true;
        }catch(SQLException e) {return false;}
    }

    /**
     * Restituisce l'elenco dei ristoranti preferiti associati a uno specifico utente.
     *
     * @param id L'id dell'utente.
     * @return {@link List} contenente gli oggetti {@link Ristorante} preferiti dell'utente.
     * @throws SQLException
     */
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
