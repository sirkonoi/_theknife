/**
 * Studente: Mattia Rotteri
 * Matricola: 762508
 * Sede: Varese
 */
package theknife.server.models;

import java.io.Serializable;
import java.util.Date;

/**
 * Classe Recesione.
 * Per la creazione di recensioni.
 * Gestisce il testo della recensione, il numero di stelle e l'eventuale risposta del ristoratore.
 */
public class Recensione implements Serializable {
    private static final long serialVersionUID = 1L;
    
/**
     * L'id univoco della recensione.
     */
    private int id;

    /**
     * L'id del recensore.
     */
    private int idRecensore;

    /**
     * L'id del ristorante recensito.
     */
    private int idRistorante;

    /**
     * Il testo della recensione.
     */
    private String testo;

    /**
     * Il numero di stelle.
     */
    private int stelle;

    private Date data;

    /**
     * La stringa contenente la risposta del ristoratore.
     */
    private String risposta;

    /**
     * Costruttore per istanziare una recensione priva di risposta iniziale.
     *
     * @param id L'id della recensione.
     * @param idRecensore  L'id dell'utente recensore.
     * @param idRistorante L'id del ristorante.
     * @param testo Il testo della recensione.
     * @param stelle Il numero di stelle.
     * @param data
     */
    public Recensione(int id, int idRecensore, int idRistorante, String testo, int stelle, Date data) {
        this.id = id;
        this.idRecensore = idRecensore;
        this.idRistorante = idRistorante;
        this.testo = testo;
        this.stelle = stelle;
        this.data = data;
        risposta = null;
    }

    /**
     * Costruttore per istanziare una recensione con risposta.
     *
     * @param id L'id della recensione.
     * @param idRecensore  L'id dell'utente recensore.
     * @param idRistorante L'id del ristorante.
     * @param testo Il testo della recensione.
     * @param stelle Il numero di stelle.
     * @param data
     * @param risposta La risposta.
     */
   public Recensione(int id, int idRecensore, int idRistorante, String testo, int stelle, Date data, String risposta) {
        this.id = id;    
        this.idRecensore = idRecensore;
        this.idRistorante = idRistorante;
        this.testo = testo;
        this.stelle = stelle;
        this.data = data;
        this.risposta = risposta;
    }    

    /**
     * Restituisce l'id della recensione.
     *
     * @return L'id della recensione.
     */
    public int getId() { return id; }

    /**
     * Aggiorna l'id della recensione.
     *
     * @param id Il nuovo id da assegnare.
     */
    public void setId(int id) { this.id = id; }

    /**
     * Restituisce l'id del recensore.
     *
     * @return L'id dell'utente.
     */
    public int getIdRecensore() { return idRecensore; }

    /**
     * Aggiorna l'id del recensore.
     *
     * @param idRecensore Il nuovo id da associare.
     */
    public void setIdRecensore(int idRecensore) { this.idRecensore = idRecensore; }

    /**
     * Restituisce l'id del ristorante recensito.
     *
     * @return L'id del ristorante.
     */
    public int getIdRistorante() { return idRistorante; }

    /**
     * Aggiorna l'id del ristorante recensito.
     *
     * @param idRistorante Il nuovo id da associare.
     */
    public void setIdRistorante(int idRistorante) { this.idRistorante = idRistorante; }  
    
    /**
     * Restituisce il testo della recensione.
     *
     * @return La stringa di testo.
     */
    public String getTesto() { return testo; }

    /**
     * Modifica il testo della recensione.
     *
     * @param testo Il nuovo testo.
     */
    public void setTesto(String testo) { this.testo = testo; }    

    /**
     * Restituisce il numero di stelle.
     *
     * @return Il valore della valutazione.
     */
    public int getStelle() { return stelle; }

    /**
     * Modifica il numero di stelle della recensione.
     *
     * @param stelle Il nuovo numero di stelle da applicare.
     */
    public void setStelle(int stelle) { this.stelle = stelle; }  
    
    /**
     * Restituisce la data della recensione.
     *
     * @return L'oggetto {@link Date}.
     */
    public Date getData() { return data; }

    /**
     * Aggiorna la data della recensione.
     *
     * @param data La nuova data da associare.
     */
    public void setData(Date data) { this.data = data; } 
    
    /**
     * Restituisce l'eventuale risposta inserita dal ristoratore.
     *
     * @return La stringa della risposta o null se non presente.
     */
    public String getRisposta() { return risposta; }

    /**
     * Imposta la risposta del ristoratore.
     *
     * @param risposta Il nuovo testo.
     */
    public void setRisposta(String risposta) { this.risposta = risposta; }  

}
