package theknife.server.models;

import java.io.Serializable;

public class Preferito implements Serializable {

    private static final long serialVersionUID = 1L;

    int idUtente;
    int idRistorante;
    String nomeRistorante;

    public Preferito(int idUtente, int idRistorante, String nomeRistorante) {
        this.idUtente = idUtente;
        this.idRistorante = idRistorante;
        this.nomeRistorante = nomeRistorante;
    }

    public int getUtente() { return idUtente; }
    public void setUtente(int id) { idUtente = id; }
    
    public int getRistorante() { return idRistorante; }
    public void setRistorante(int id) { idRistorante = id; }

    public String getNomeRistorante() { return nomeRistorante; }
    public void setNomeRistorante(String nome) { nomeRistorante = nome; }
}
