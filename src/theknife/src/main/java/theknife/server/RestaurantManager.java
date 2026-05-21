package theknife.server;

import java.io.IOException;
import java.sql.*;
import java.util.*;
import theknife.server.models.*;

public class RestaurantManager {

    String[] columns = {"nome", "luogo", "fascia_prezzo", "delivery", "prenotazione_online", "ristoratore"};
    DBManager db;

    public RestaurantManager(DBManager db) {
        this.db = db;
    }

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

    public List<Ristorante> getRestaurantsList() throws SQLException, IOException {
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
            else if (prezzo.equals("50-100€")) {
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

    public Ristorante cercaRistorante(int id) {
        String query = "SELECT ris.*, luogo.nazione, luogo.citta, luogo.indirizzo, luogo.latitudine, luogo.longitudine FROM ristorante ris JOIN luogo ON ris.luogo = luogo.id  WHERE id = ?";
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

    public Luogo findLuogo(int id) throws SQLException, IOException {
        PreparedStatement statement = db.connection.prepareStatement("SELECT * FROM luogo WHERE id = ?");
        statement.setInt(1, id);
        ResultSet rs = statement.executeQuery();
        if(rs.next()) {
            return new Luogo (rs.getString("indirizzo"), rs.getString("citta"), rs.getString("nazione"));
        }
        return null;
    }

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