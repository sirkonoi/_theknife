/**
 * Studente: Mattia Rotteri
 * Matricola: 762508
 * Sede: Varese
 */
package theknife.server.models;

import java.io.Serializable;

/**
 * Classe Ristorante.
 * Memorizza le informazioni, la posizione , le opzioni di servizio e il proprietario di un ristorante.
 * Implementa {@link Serializable}
 */
public class Ristorante implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * L'id univoco del ristorante.
     */
    private int id;

    /**
     * Il nome del ristorante.
     */
    private String nome;

    /**
     * La posizione geografica del ristorante.
     */
    private Luogo luogo;

    /**
     * La fascia di prezzo del ristorante.
     */
    private int fascia_prezzo;

    /**
     * Indica se offre la consegna a domicilio.
     */
    private boolean delivery;

    /**
     * Indica se offre servizio prenotazione online.
     */
    private boolean prenotazione_online;

    /**
     * L'id univoco dell'utente ristoratore proprietario del ristorante.
     */
    private int ristoratore;

    /**
     * Costruttore della classe Ristorante.
     *
     * @param id L'id del ristorante.
     * @param nome Il nome del locale.
     * @param luogo {@link Luogo} contenente le coordinate e l'indirizzo.
     * @param fascia_prezzo La fascia di prezzo.
     * @param delivery La disponibilità di consegna a domicilio.
     * @param prenotazione_online La possibilità di prenotare online.
     * @param ristoratore L'id del ristoratore.
     */
    public Ristorante(int id, String nome, Luogo luogo, int fascia_prezzo, boolean delivery, boolean prenotazione_online, int ristoratore) {
        this.id = id;
        this.nome = nome;
        this.luogo = luogo;
        this.fascia_prezzo = fascia_prezzo;
        this.delivery = delivery;
        this.prenotazione_online = prenotazione_online;
        this.ristoratore = ristoratore;
    }

    /**
     * Restituisce l'id del ristorante.
     *
     * @return L'id del ristorante.
     */
    public int getId() { return id; }

    /**
     * Aggiorna l'id del ristorante.
     *
     * @param id Il nuovo id da assegnare.
     */
    public void setId(int id) { this.id = id; }

    /**
     * Restituisce il nome del ristorante.
     *
     * @return La stringa del nome.
     */
    public String getNome() { return nome; }

    /**
     * Aggiorna il nome del ristorante.
     *
     * @param nome Il nuovo nome da impostare.
     */
    public void setNome(String nome) { this.nome = nome; }
    
    /**
     * Restituisce il luogo del ristorante.
     *
     * @return {@link Luogo}.
     */
    public Luogo getLuogo() { return luogo; }

    /**
     * Aggiorna il luogo associato al ristorante.
     *
     * @param luogo Il nuovo  {@link Luogo} da associare.
     */
    public void setLuogo(Luogo luogo) { this.luogo = luogo; }

    /**
     * Restituisce la fascia di prezzo del ristorante.
     *
     * @return Il valore della fascia di prezzo.
     */
    public int getFasciaPrezzo() { return fascia_prezzo; }

    /**
     * Aggiorna il valore della fascia di prezzo.
     *
     * @param fascia_prezzo La nuova fascia di spesa da impostare.
     */
    public void setFasciaPrezzo(int fascia_prezzo) { this.fascia_prezzo = fascia_prezzo; }

    /**
     * Verifica se è attivo il servizio di consegna a domicilio.
     *
     * @return true se il servizio di delivery è supportato, false altrimenti.
     */
    public boolean isDelivery() { return delivery; }

    /**
     * Imposta la disponibilità del servizio di consegna a domicilio.
     *
     * @param delivery Booleano per il servizio di consegna.
     */
    public void setDelivery(boolean delivery) { this.delivery = delivery; }

    /**
     * Verifica se è supportata la prenotazione online.
     *
     * @return true se la prenotazione online è disponibile, false altrimenti.
     */
    public boolean isPrenotazioneOnline() { return prenotazione_online; }

    /**
     * Imposta la disponibilità delle prenotazioni online.
     *
     * @param prenotazione_online Booleano.
     */
    public void setPrenotazioneOnline(boolean prenotazione_online) { this.prenotazione_online = prenotazione_online; }

    /**
     * Restituisce l'id del proprietario.
     *
     * @return L'id dell'utente ristoratore.
     */
    public int getRistoratore() { return ristoratore; }

    /**
     * Modifica l'id del proprietario.
     *
     * @param ristoratore Il nuovo id del ristoratore.
     */
    public void setRistoratore(int ristoratore) { this.ristoratore = ristoratore; }
}