package theknife.server.models;

import java.io.Serializable;
import java.util.Date;

public class Recensione implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int id;
    private int idRecensore;
    private int idRistorante;
    private String testo;
    private int stelle;
    private Date data;
    private String risposta;

    public Recensione(int id, int idRecensore, int idRistorante, String testo, int stelle, Date data) {
        this.id = id;
        this.idRecensore = idRecensore;
        this.idRistorante = idRistorante;
        this.testo = testo;
        this.stelle = stelle;
        this.data = data;
        risposta = null;
    }

   public Recensione(int id, int idRecensore, int idRistorante, String testo, int stelle, Date data, String risposta) {
        this.id = id;    
        this.idRecensore = idRecensore;
        this.idRistorante = idRistorante;
        this.testo = testo;
        this.stelle = stelle;
        this.data = data;
        this.risposta = risposta;
    }    

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdRecensore() { return idRecensore; }
    public void setIdRecensore(int idRecensore) { this.idRecensore = idRecensore; }

    public int getIdRistorante() { return idRistorante; }
    public void setIdRistorante(int idRistorante) { this.idRistorante = idRistorante; }  
    
    public String getTesto() { return testo; }
    public void setTesto(String testo) { this.testo = testo; }    

    public int getStelle() { return stelle; }
    public void setStelle(int stelle) { this.stelle = stelle; }  
    
    public Date getData() { return data; }
    public void setData(Date data) { this.data = data; } 
    
    public String getRisposta() { return risposta; }
    public void setRisposta(String risposta) { this.risposta = risposta; }     

}
