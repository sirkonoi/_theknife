package theknife.server;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import theknife.server.models.Recensione;

public class RecensioneManager {
    

    DBManager db;
    
    public RecensioneManager(DBManager db) {
        this.db = db;
    }

    public boolean addRecensione(Recensione recensione) {
        if (recensioneExists(recensione.getIdRistorante(), recensione.getIdRecensore())) return false;
        String query = "INSERT INTO recensione (ristorante, utente, stelle, testo, data) VALUES (?, ?, ?, ?, ?)";
        try(PreparedStatement statement = db.connection.prepareStatement(query)) {
            statement.setInt(1, recensione.getIdRistorante());
            statement.setInt(2, recensione.getIdRecensore());
            statement.setInt(3, recensione.getStelle());
            statement.setString(4, recensione.getTesto());
            statement.setDate(5, new java.sql.Date(recensione.getData().getTime()));
            statement.executeUpdate();
            return true;
        } catch(SQLException e) { 
            e.printStackTrace();
            return false; 
        }
    }

    public boolean deleteRecensione(int id) {
        String query = "DELETE FROM recensione WHERE id = ?";
        try(PreparedStatement statement = db.connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
            return true;
        }catch(SQLException e) { return false; }
    }

    public Recensione getRecensione(int idRistorante, int idRecensore) {
        String query = "SELECT * FROM recensione WHERE utente = ? AND ristorante = ?";
        
        try(PreparedStatement statement = db.connection.prepareStatement(query)) {
            statement.setInt(1,idRecensore);
            statement.setInt(2, idRistorante);
            try(ResultSet rs = statement.executeQuery()) {
                if(rs.next()) {
                    return new Recensione(rs.getInt("id"), rs.getInt("utente"), rs.getInt("ristorante"), rs.getString("testo"), rs.getInt("stelle"), rs.getDate("data"), rs.getString("risposta"));
                }
            }
        }catch(SQLException e) {}
        return null;
    }

    public List<Recensione> getRecensioniRistorante(int idRistorante) {
        List<Recensione> recensioni = new ArrayList<>();
        String query = "SELECT * FROM recensione WHERE ristorante = ?"; 
        try(PreparedStatement statement = db.connection.prepareStatement(query)) {
            statement.setInt(1, idRistorante);
            try(ResultSet rs = statement.executeQuery()) {
                while(rs.next()) {
                    recensioni.add(new Recensione(rs.getInt("id"), rs.getInt("utente"), rs.getInt("ristorante"), rs.getString("testo"), rs.getInt("stelle"), rs.getDate("data"), rs.getString("risposta")));
                }
            }
        }catch(SQLException e) { e.printStackTrace(); }    
        return recensioni;           
    }

    public List<Recensione> getRecensioniUtente(int id) {
        List<Recensione> recensioni = new ArrayList<>();
        String query = "SELECT * FROM recensione WHERE utente = ?";
        try(PreparedStatement statement = db.connection.prepareStatement(query)) {
            statement.setInt(1, id);
            try(ResultSet rs = statement.executeQuery()) {
                while(rs.next()) {
                    recensioni.add(new Recensione(rs.getInt("id"), rs.getInt("utente"), rs.getInt("ristorante"), rs.getString("testo"), rs.getInt("stelle"), rs.getDate("data"), rs.getString("risposta")));
                }
            }
        }catch(SQLException e) { e.printStackTrace(); }  
        return recensioni;         
    }

    public double[] getInfoRecensioni(int idRistorante) {
        List<Recensione> recensioni = getRecensioniRistorante(idRistorante);
        double tmp = 0.0;
        for(Recensione recensione : recensioni) {
            tmp += recensione.getStelle();
        }
        if(tmp == 0) {
            return new double[]{0, recensioni.size()};
        }
        return new double[]{tmp/recensioni.size(), recensioni.size()};

    }


    public boolean addRisposta(int idRec, String risposta) {
        String query = "UPDATE recensione SET risposta = ? WHERE id = ?";
        try (PreparedStatement statement = db.connection.prepareStatement(query)) {
           statement.setString(1, risposta);
            statement.setInt(2, idRec);
            statement.executeUpdate();
            return true;
        } catch(SQLException e) { return false; }
    }
    
    public boolean deleteRisposta(int idRec) {
        String query = "UPDATE recensione SET risposta = NULL WHERE id = ?";
        try (PreparedStatement statement = db.connection.prepareStatement(query)) {
            statement.setInt(1, idRec);
            statement.executeUpdate();
            return true;
        } catch(SQLException e) { return false; }
    }

    public boolean recensioneExists(int idRistorante, int idRecensore) {
        String query = "SELECT 1 FROM recensione WHERE utente = ? AND ristorante = ?";
        
        try{PreparedStatement statement = db.connection.prepareStatement(query);
        statement.setInt(1, idRecensore);
        statement.setInt(2, idRistorante);
        try(ResultSet rs = statement.executeQuery()) {
            return rs.next();        
        }
        }catch(SQLException e) { return false; }
    }
}
