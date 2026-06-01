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

import theknife.server.models.Recensione;

/**
 * Classe RecensioneManager.
 * Gestisce la logica e le interazioni con il database relative alle recensioni.
 */
public class RecensioneManager {

    /**
     * Il database
     */
    DBManager db;

    /**
     * Costruttore della classe RecensioneManager.
     *
     * @param db {@link DBManager} condiviso per l'accesso al database.
     */
    public RecensioneManager(DBManager db) {
        this.db = db;
    }

    /**
     * Inserisce una nuova recensione nel database dopo aver verificato la non
     * esistenza
     * di un'altra recensione per quel ristorante da parte dell'utente.
     * 
     * @param recensione {@link Recensione} da registrare.
     * @return true se l'inserimento ha successo, false se la recensione esiste già
     *         o in caso di errore SQL.
     */
    public boolean addRecensione(Recensione recensione) {
        if (recensioneExists(recensione.getIdRistorante(), recensione.getIdRecensore()))
            return false;
        String query = "INSERT INTO recensione (ristorante, utente, stelle, testo, data) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = db.connection.prepareStatement(query)) {
            statement.setInt(1, recensione.getIdRistorante());
            statement.setInt(2, recensione.getIdRecensore());
            statement.setInt(3, recensione.getStelle());
            statement.setString(4, recensione.getTesto());
            statement.setDate(5, new java.sql.Date(recensione.getData().getTime()));
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Modifica il testo di una recensione esistente all'interno del database.
     *
     * @param idRec      L'id della recensione da modificare.
     * @param nuovoTesto Il nuovo testo da sovrascrivere.
     * @return true se l'aggiornamento ha successo, false in caso di errore SQL.
     */
    public boolean modifyRecensione(int idRec, String nuovoTesto) {
        String query = "UPDATE recensione SET testo = ? WHERE id = ?";
        try (PreparedStatement statement = db.connection.prepareStatement(query)) {
            statement.setString(1, nuovoTesto);
            statement.setInt(2, idRec);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Elimina una recensione dal database.
     *
     * @param id L'id della recensione da rimuovere.
     * @return true se l'eliminazione ha successo, false in caso di errore SQL.
     */
    public boolean deleteRecensione(int id) {
        String query = "DELETE FROM recensione WHERE id = ?";
        try (PreparedStatement statement = db.connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Estrae una specifica recensione ricercandola attraverso la coppia id-utente e
     * id-ristorante.
     *
     * @param idRistorante L'id del ristorante associato.
     * @param idRecensore  L'id dell'utente recensore.
     * @return {@link Recensione} se presente, null altrimenti o in caso di errore
     *         SQL.
     */
    public Recensione getRecensione(int idRistorante, int idRecensore) {
        String query = "SELECT * FROM recensione WHERE utente = ? AND ristorante = ?";

        try (PreparedStatement statement = db.connection.prepareStatement(query)) {
            statement.setInt(1, idRecensore);
            statement.setInt(2, idRistorante);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return new Recensione(rs.getInt("id"), rs.getInt("utente"), rs.getInt("ristorante"),
                            rs.getString("testo"), rs.getInt("stelle"), rs.getDate("data"), rs.getString("risposta"));
                }
            }
        } catch (SQLException e) {
        }
        return null;
    }

    /**
     * Restituisce la lista completa di tutte le recensioni associate a uno
     * specifico ristorante.
     *
     * @param idRistorante L'id del ristorante.
     * @return {@link List} contenente tutte le {@link Recensione}.
     */
    public List<Recensione> getRecensioniRistorante(int idRistorante) {
        List<Recensione> recensioni = new ArrayList<>();
        String query = "SELECT * FROM recensione WHERE ristorante = ?";
        try (PreparedStatement statement = db.connection.prepareStatement(query)) {
            statement.setInt(1, idRistorante);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    recensioni.add(new Recensione(rs.getInt("id"), rs.getInt("utente"), rs.getInt("ristorante"),
                            rs.getString("testo"), rs.getInt("stelle"), rs.getDate("data"), rs.getString("risposta")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recensioni;
    }

    /**
     * Restituisce la lista completa di tutte le recensioni scritte da uno specifico
     * utente.
     *
     * @param id L'id dell'utente.
     * @return {@link List} contenente tutte le {@link Recensione}.
     */
    public List<Recensione> getRecensioniUtente(int id) {
        List<Recensione> recensioni = new ArrayList<>();
        String query = "SELECT * FROM recensione WHERE utente = ?";
        try (PreparedStatement statement = db.connection.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    recensioni.add(new Recensione(rs.getInt("id"), rs.getInt("utente"), rs.getInt("ristorante"),
                            rs.getString("testo"), rs.getInt("stelle"), rs.getDate("data"), rs.getString("risposta")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recensioni;
    }

    /**
     * Calcola il numero di recensioni totali e il numero medio di stelle di un
     * ristorante.
     *
     * @param idRistorante L'id del ristorante.
     * @return Un array di double che ha come primo elemento la media delle stelle e
     *         secondo elemento il numero complessivo di recensioni.
     */
    public double[] getInfoRecensioni(int idRistorante) {
        List<Recensione> recensioni = getRecensioniRistorante(idRistorante);
        double tmp = 0.0;
        for (Recensione recensione : recensioni) {
            tmp += recensione.getStelle();
        }
        if (tmp == 0) {
            return new double[] { 0, recensioni.size() };
        }
        return new double[] { tmp / recensioni.size(), recensioni.size() };

    }

    /**
     * Registra la risposta inserita dal ristoratore a una recensione.
     *
     * @param idRec    Id della recensione.
     * @param risposta La stringa che rappresenta la risposta.
     * @return true se l'operazione ha successo, false in caso di errore SQL.
     */
    public boolean addRisposta(int idRec, String risposta) {
        String query = "UPDATE recensione SET risposta = ? WHERE id = ?";
        try (PreparedStatement statement = db.connection.prepareStatement(query)) {
            statement.setString(1, risposta);
            statement.setInt(2, idRec);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Modifica la risposta inserita dal ristoratore a una recensione.
     *
     * @param idRec         Id della recensione.
     * @param nuovaRisposta La stringa che rappresenta la nuova risposta.
     * @return true se l'operazione ha successo, false in caso di errore SQL.
     */
    public boolean modifyRisposta(int idRec, String nuovaRisposta) {
        String query = "UPDATE recensione SET risposta = ? WHERE id = ?";
        try (PreparedStatement statement = db.connection.prepareStatement(query)) {
            statement.setString(1, nuovaRisposta);
            statement.setInt(2, idRec);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Rimuove la risposta inserita dal ristoratore.
     *
     * @param idRec L'identificativo della recensione da cui eliminare la replica.
     * @return true se l'operazione ha successo, false in caso di errore SQL.
     */
    public boolean deleteRisposta(int idRec) {
        String query = "UPDATE recensione SET risposta = NULL WHERE id = ?";
        try (PreparedStatement statement = db.connection.prepareStatement(query)) {
            statement.setInt(1, idRec);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Verifica se un utente abbia già scritto o meno una recensione per un dato
     * ristorante.
     *
     * @param idRistorante L'id del ristorante.
     * @param idRecensore  L'id dell'utente.
     * @return true se l'utente ha già recensito il ristorante specificato, false
     *         altrimenti.
     */
    public boolean recensioneExists(int idRistorante, int idRecensore) {
        String query = "SELECT 1 FROM recensione WHERE utente = ? AND ristorante = ?";

        try {
            PreparedStatement statement = db.connection.prepareStatement(query);
            statement.setInt(1, idRecensore);
            statement.setInt(2, idRistorante);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            return false;
        }
    }
}
