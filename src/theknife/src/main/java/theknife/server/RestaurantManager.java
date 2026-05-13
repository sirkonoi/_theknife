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

    public void insert(Ristorante newRistorante) throws SQLException {
        
        Object[] values = {newRistorante.getNome(), newRistorante.getLuogo().getId(), newRistorante.getFasciaPrezzo(), newRistorante.isDelivery(), newRistorante.isPrenotazioneOnline(), newRistorante.getRistoratore()};
        db.insert(values, columns, "ristorante");
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

    public List<Ristorante> cercaVicini(String indirizzo, int raggio) throws SQLException, IOException {
        double[] coordinate = Luogo.getLatitudineLongitudine(indirizzo);
        if (coordinate == null) return new ArrayList<>();

        double lat = coordinate[0];
        double lon = coordinate[1];

        String query = "SELECT ris.*, luogo.nazione, luogo.citta, luogo.indirizzo, luogo.latitudine, luogo.longitudine FROM ristorante ris JOIN luogo ON ris.luogo = luogo.id WHERE (6371 * acos(cos(radians(?)) * cos(radians(luogo.latitudine)) * cos(radians(luogo.longitudine) - radians(?)) + sin(radians(?)) * sin(radians(luogo.latitudine)))) <= ? LIMIT 30";

        List<Ristorante> lista = new ArrayList<>();
        try (PreparedStatement statement = db.connection.prepareStatement(query)) {
            statement.setDouble(1, lat);
            statement.setDouble(2, lon);
            statement.setDouble(3, lat);
            statement.setDouble(4, raggio);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Luogo luogo = new Luogo( rs.getString("indirizzo"), rs.getString("citta"), rs.getString("nazione"), rs.getDouble("latitudine"), rs.getDouble("longitudine"));
                lista.add(new Ristorante(rs.getInt("id"), rs.getString("nome"), luogo, rs.getInt("fascia_prezzo"), rs.getBoolean("delivery"), rs.getBoolean("prenotazione_online"), rs.getInt("ristoratore")
                ));
            }
        }
        return lista;
    }  
    
    //RIGUARDA
    public List<String> getAllTipi() {
        String query = "SELECT DISTINCT tipo FROM tipocucina";
        List<String> tipi = new ArrayList<>();
        try(PreparedStatement statement = db.connection.prepareStatement(query)) {
            try(ResultSet rs = statement.executeQuery()) {
                while(rs.next()) {
                    tipi.add(rs.getString("tipo"));
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