package theknife.server;

import java.io.IOException;
import java.sql.*;
import java.util.*;
import theknife.server.models.*;

/**
 * Classe RestaurantManager.
 * Gestisce la logica e le interazioni con il database relative ai ristoranti.
 */
public class RestaurantManager {
  
    /**
     * Il database
     */
    DBManager db;

     /**
     * Costruttore della classe RestaurantManager.
     *
     * @param db {@link DBManager} condiviso per l'accesso al database.
     */
    public RestaurantManager(DBManager db) {
        this.db = db;
    }

    /**
     * Registra un nuovo ristorante eseguendo in modo sequenziale l'inserimento
     * del luogo, l'estrazione della chiave generata, la scrittura
     * del record del ristorante e il popolamento delle tabelle dei tipi di cucina.
     *
     * @param newRistorante {@link Ristorante} da inserire.
     * @param tipiRistorante Un array di stringhe contenente le tipologie di cucina del ristorante.
     * @throws SQLException Lanciata in caso di errore delle query.
     */
    public void addRistorante(Ristorante newRistorante, String[] tipiRistorante) throws SQLException {
        int idLuogo = insertLuogo(newRistorante.getLuogo());
        if (idLuogo == -1) throw new SQLException("Inserimento luogo fallito.");
        newRistorante.getLuogo().setId(idLuogo);

        String query = "INSERT INTO ristorante (nome, luogo, fascia_prezzo, delivery, prenotazione_online, ristoratore) VALUES (?, ?, ?, ?, ?, ?) RETURNING id";
        int idRistorante;
        try(PreparedStatement statement = db.connection.prepareStatement(query)) {
            statement.setString(1, newRistorante.getNome());
            statement.setInt(2, idLuogo);
            statement.setInt(3, newRistorante.getFasciaPrezzo());
            statement.setBoolean(4, newRistorante.isDelivery());
            statement.setBoolean(5, newRistorante.isPrenotazioneOnline());
            statement.setInt(6, newRistorante.getRistoratore());
            ResultSet rs = statement.executeQuery();
            if (!rs.next()) throw new SQLException("Inserimento ristorante fallito.");
            idRistorante = rs.getInt("id");
        }
        for(String tipo : tipiRistorante) {
        String queryTipo = "INSERT INTO tipocucina (tipo) VALUES (?)";
            try(PreparedStatement ps = db.connection.prepareStatement(queryTipo)) {
                ps.setString(1, tipo);
                ps.executeUpdate();
            }
            String queryTipoRis = "INSERT INTO tipo_cucina_ristorante (ristorante, tipo_cucina) VALUES (?, ?)";
            try(PreparedStatement ps = db.connection.prepareStatement(queryTipoRis)) {
                ps.setInt(1, idRistorante);
                ps.setString(2, tipo);
                ps.executeUpdate();
            }
        }
    }

    /**
     * Elimina un ristorante dal database in base al suo id.
     *
     * @param id Id del ristorante da eliminare.
     * @return true se l'eliminazione avviene con successo, false in caso di eccezione SQL.
     */
    public boolean deleteRistorante(int id) {
        String query = "DELETE FROM ristorante WHERE id = ?";
        
        try(PreparedStatement statement = db.connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
            return true;
        }catch(SQLException e) { return false; }
    }

    /**
     * Restituisce la lista di tutti i ristoranti.
     *
     * @return {@link List} contenente tutti i {@link Ristorante} presenti sul DB.
     * @throws SQLException Errori SQL.
     * @throws IOException Errori I/O.
     */
    public List<Ristorante> getListaRistoranti() throws SQLException, IOException {
        List<Ristorante> lista = new ArrayList<>();

        try(PreparedStatement statement = db.connection.prepareStatement("SELECT * FROM ristorante")) {
            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                Luogo luogo = findLuogo(rs.getInt("luogo"));
                Ristorante ris = new Ristorante(rs.getInt("id"), rs.getString("nome"), luogo, rs.getInt("fascia_prezzo"), rs.getBoolean("delivery"), rs.getBoolean("prenotazione_online"), rs.getInt("ristoratore")
            );
            lista.add(ris);
            }
        }
        return lista;
    }

    /**
     * Restituisce la lista dei ristoranti associati a uno specifico utente con ruolo di ristoratore.
     *
     * @param id L'id dell'utente.
     * @return {@link List} dei ristoranti dell'utente.
     * @throws IOException Errori di I/O.
     */
    public List<Ristorante> getRistorantiUtente(int id) throws IOException {
        List<Ristorante> lista = new ArrayList<>();
        String query = "SELECT ris.*, luogo.nazione, luogo.citta, luogo.indirizzo, luogo.latitudine, luogo.longitudine FROM ristorante ris JOIN luogo ON ris.luogo = luogo.id  WHERE ristoratore = ?";

        try(PreparedStatement statement = db.connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Luogo luogo = findLuogo(rs.getInt("luogo"));
                lista.add(new Ristorante(rs.getInt("id"), rs.getString("nome"), luogo, rs.getInt("fascia_prezzo"), rs.getBoolean("delivery"), rs.getBoolean("prenotazione_online"), rs.getInt("ristoratore")));             
            }
        }catch(SQLException e) {}
        return lista;
    }

    /**
     * Restituisce una lista di ristoranti filtrati sulla base di vari criteri come:
     * distanza, cucina, prezzo, delivery, prenotazione.
     *
     * @param filtri Un array di oggetti contenente nell'ordine: raggio (0), cucina (1), prezzo (2), delivery (3), prenotazione (4), indirizzo dell'utente (5).
     * @return {@link List} di {@link Ristorante} filtrati.
     * @throws SQLException
     * @throws IOException
     */
    public List<Ristorante> filtra(Object[] filtri) throws SQLException, IOException {
        double[] coordinate = Luogo.getLatitudineLongitudine((String) filtri[5]);
        if (coordinate == null)
            return new ArrayList<>();
        double lat = coordinate[0];
        double lon = coordinate[1];
        int raggio = (int) filtri[0];

        String query = "SELECT ris.*, luogo.nazione, luogo.citta, luogo.indirizzo, luogo.latitudine, luogo.longitudine FROM ristorante ris JOIN luogo ON ris.luogo = luogo.id WHERE (6371 * acos(cos(radians(?)) * cos(radians(luogo.latitudine)) * " +"cos(radians(luogo.longitudine) - radians(?)) + sin(radians(?)) * sin(radians(luogo.latitudine)))) <= ?";

        if (filtri[1] != null) {
            query += " AND ris.id IN (SELECT ristorante FROM tipo_cucina_ristorante WHERE tipo_cucina = ?)";
        }

        if (filtri[2] != null) {
            String prezzo = (String) filtri[2];
            if (prezzo.equals("< 20€")) {
                query += " AND ris.fascia_prezzo < 20";
            }
            else if (prezzo.equals("20-50€")) {
                query += " AND ris.fascia_prezzo BETWEEN 20 AND 50";
            }    
            else if (prezzo.equals("50-100€")) {
                query += " AND ris.fascia_prezzo BETWEEN 50 AND 100";
            }
            else if (prezzo.equals("> 100€")) {
                query += " AND ris.fascia_prezzo > 100";  
            }          
        }

        if (filtri[3] != null) {
            query += " AND ris.delivery = ?";
        }
        if (filtri[4] != null) {
            query += " AND ris.prenotazione_online = ?";
        }
        //query += " LIMIT 30";

        List<Ristorante> lista = new ArrayList<>();
        try (PreparedStatement ps = db.connection.prepareStatement(query)) {
            int i = 1;
            ps.setDouble(i++, lat);
            ps.setDouble(i++, lon);
            ps.setDouble(i++, lat);
            ps.setInt(i++, raggio);
            if (filtri[1] != null) {
                ps.setString(i++, (String) filtri[1]);
            }    
            if (filtri[3] != null) {
                ps.setBoolean(i++, (boolean) filtri[3]);
            }    
            if (filtri[4] != null) {
                ps.setBoolean(i++, (boolean) filtri[4]);
            }    

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Luogo luogo = new Luogo(rs.getString("indirizzo"), rs.getString("citta"), rs.getString("nazione"), rs.getDouble("latitudine"), rs.getDouble("longitudine"));
                lista.add(new Ristorante(rs.getInt("id"), rs.getString("nome"), luogo, rs.getInt("fascia_prezzo"), rs.getBoolean("delivery"), rs.getBoolean("prenotazione_online"), rs.getInt("ristoratore")));
            }
        }
        return lista;
    }

    /**
     * Cerca un ristorante in base al nome.
     * 
     * @param nome La stringa del nome da ricercare.
     * @return Un {@link Ristorante} corrispondente se trovato, null altrimenti.
     */
    public Ristorante cercaRistorante(String nome) {
        String query = "SELECT ris.*, luogo.nazione, luogo.citta, luogo.indirizzo, luogo.latitudine, luogo.longitudine FROM ristorante ris JOIN luogo ON ris.luogo = luogo.id  WHERE nome = ?";
        try(PreparedStatement statement = db.connection.prepareStatement(query)) {
            statement.setString(1, nome);
            try(ResultSet rs = statement.executeQuery()) {
                if(rs.next()) {
                Luogo luogo = new Luogo( rs.getString("indirizzo"), rs.getString("citta"), rs.getString("nazione"), rs.getDouble("latitudine"), rs.getDouble("longitudine"));
                return new Ristorante(rs.getInt("id"), rs.getString("nome"), luogo, rs.getInt("fascia_prezzo"), rs.getBoolean("delivery"), rs.getBoolean("prenotazione_online"), rs.getInt("ristoratore"));                
                }
            }
        }catch(SQLException e) {}
        return null;
    }

        /**
     * Cerca un ristorante in base all'id.
     * 
     * @param id Id del ristorante da ricercare.
     * @return Un {@link Ristorante} corrispondente se trovato, null altrimenti.
     */
    public Ristorante cercaRistorante(int id) {
        String query = "SELECT ris.*, luogo.nazione, luogo.citta, luogo.indirizzo, luogo.latitudine, luogo.longitudine FROM ristorante ris JOIN luogo ON ris.luogo = luogo.id  WHERE ris.id = ?";
        try(PreparedStatement statement = db.connection.prepareStatement(query)) {
            statement.setInt(1, id);
            try(ResultSet rs = statement.executeQuery()) {
                if(rs.next()) {
                Luogo luogo = new Luogo( rs.getString("indirizzo"), rs.getString("citta"), rs.getString("nazione"), rs.getDouble("latitudine"), rs.getDouble("longitudine"));
                return new Ristorante(rs.getInt("id"), rs.getString("nome"), luogo, rs.getInt("fascia_prezzo"), rs.getBoolean("delivery"), rs.getBoolean("prenotazione_online"), rs.getInt("ristoratore"));                
                }
            }
        }catch(SQLException e) {}
        return null;
    }    
    
    /**
     * Restituisce una lista di tutti i tipi di cucina disponibili sul DB.
     *
     * @return {@link List} di stringhe contenente i tipi di cucina.
     */
    public List<String> getAllTipi() {
        String query = "SELECT DISTINCT tipo FROM tipocucina";
        List<String> tipi = new ArrayList<>();
        try(PreparedStatement statement = db.connection.prepareStatement(query)) {
            try(ResultSet rs = statement.executeQuery()) {
                while(rs.next()) {
                    String tipo = rs.getString("tipo");
                    tipi.add(tipo);
                }
            }
        }catch(SQLException e) {System.out.println("Errore nel selezionare i tipi...");}
        return tipi;
    }

    /**
     * Dato l'id, restituisce la lista di tipi di cucina di uno specifico ristorante.
     * 
     * @param id L'iddel ristorante,
     * @return {@link List} di stringhe ovvero i tipi di cucina.
     */
    public List<String> getTipo(int id) {
        String query = "SELECT tipo_cucina FROM Ristorante JOIN tipo_cucina_ristorante ON id = ristorante WHERE id = ?";
        List<String> tipi = new ArrayList<>();
        try(PreparedStatement statement = db.connection.prepareStatement(query)) {
            statement.setInt(1, id);
            try(ResultSet rs = statement.executeQuery()) {
                while(rs.next()) {
                    String tipo = rs.getString("tipo_cucina");
                    tipi.add(tipo);    
                }            
            }
        }catch(SQLException e) {System.out.println("Errore nel selezionare i tipi...");}
        return tipi;
    }

    /**
     * Individua un luogo, a partire dall'id.
     * 
     * @param id L'id del luogo.
     * @return {@link Luogo} se presente, null altrimenti.
     * @throws SQLException 
     * @throws IOException
     */
    public Luogo findLuogo(int id) throws SQLException, IOException {
        PreparedStatement statement = db.connection.prepareStatement("SELECT * FROM luogo WHERE id = ?");
        statement.setInt(1, id);
        ResultSet rs = statement.executeQuery();
        if(rs.next()) {
            return new Luogo (rs.getString("indirizzo"), rs.getString("citta"), rs.getString("nazione"));
        }
        return null;
    }

    /**
     * Aggiungi un nuovo luogo nel DB.
     *
     * @param luogo {@link Luogo} da inserire.
     * @return Il valore della chiave primaria del luogo aggiunto, oppure -1 in caso di fallimento.
     * @throws SQLException
     */
    public int insertLuogo(Luogo luogo) throws SQLException {
        String query = "INSERT INTO luogo (nazione, citta, indirizzo, latitudine, longitudine) VALUES (?, ?, ?, ?, ?) RETURNING id";
        try(PreparedStatement statement = db.connection.prepareStatement(query)) {
            statement.setString(1, luogo.getNazione());
            statement.setString(2, luogo.getCitta());
            statement.setString(3, luogo.getIndirizzo());
            statement.setDouble(4, luogo.getLatitudine());
            statement.setDouble(5, luogo.getLongitudine());
            ResultSet rs = statement.executeQuery();
            if(rs.next()) {
                int id = rs.getInt("id");
                luogo.setId(id);
                return id;
            }
        }
        return -1;
    }  
     
}