package theknife.server;

import java.io.IOException;
import java.sql.*;
import java.util.*;
import theknife.server.models.*;

public class RestaurantManager {

    String[] columns = {"nome", "luogo", "fascia_prezzo", "delivery", "prenotazione", "ristoratore"};
    DBManager db;

    public RestaurantManager(DBManager db) {
        this.db = db;
    }

    public void insert(Ristorante newRistorante) throws SQLException {
        
        Object[] values = {newRistorante.getNome(), newRistorante.getLuogo().getId(), newRistorante.getFasciaPrezzo(), newRistorante.isDelivery(), newRistorante.isPrenotazioneOnline(), newRistorante.getRistoratore()};
        db.insert(values, columns, "ristorante");
    }

    public List<Ristorante> getRestaurantsList() throws SQLException, IOException {
        List<Ristorante> lista = new ArrayList();

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
        Object[] values = {luogo.getNazione(), luogo.getCitta(), luogo.getCitta(), luogo.getLatitudine(), luogo.getLongitudine()};
        String[] lcolumns = {"nazione", "città", "indirizzo", "latitudine", "longitudine"};

        db.insert(values, lcolumns, "luogo");
        PreparedStatement statement = db.connection.prepareStatement("SELECT id FROM luogo WHERE latitudine = ? AND longitudine = ? RETURNING ID");
        statement.setDouble(1, luogo.getLatitudine());
        statement.setDouble(2, luogo.getLongitudine());
        ResultSet rs = statement.executeQuery();
        if(rs.next()) {
            int id = rs.getInt("id");
            luogo.setId(id);
            return id;
        }
        return -1;
    }    
}