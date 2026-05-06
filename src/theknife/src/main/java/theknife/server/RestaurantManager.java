package theknife.server;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import theknife.server.models.*;

public class RestaurantManager {

    String[] columns = {"nome", "luogo", "fascia_prezzo", "delivery", "prenotazione", "ristoratore"};
    DBManager db;

    public RestaurantManager(DBManager db) {
        this.db = db;
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

    public void insert(Ristorante newRistorante) throws SQLException {

        Object[] values = {newRistorante.getNome(), newRistorante.getLuogo().getId(), newRistorante.getFasciaPrezzo(), newRistorante.isDelivery(), newRistorante.isPrenotazioneOnline(), newRistorante.getRistoratore()};
        db.insert(values, columns, "ristorante");
    }
}