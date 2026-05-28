/**
 * Studente: Mattia Rotteri
 * Matricola: 762508
 * Sede: Varese
 */
package theknife.server.models;

import java.io.Serializable;
import java.util.Date;

/**
 * Classe Utente.
 * Gestisce i dati personali dell'utente e funge da classe base per ruoli specializzati.
 */
public class Utente implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /**
     * L'id univoco dell'utente.
     */
    protected int id;

    /**
     * Il nome dell'utente.
     */
    protected String nome;

    /**
     * Il cognome dell'utente.
     */
    protected String cognome;

    /**
     * L'username dell'utente.
     */
    protected String username;

    /**
     * La data di nascita dell'utente.
     */
    protected Date data_nascita;

    /**
     * La password in forma cifrata.
     */
    protected Password psw;

    /**
     * L'indirizzo di domicilio dell'utente.
     */
    protected String domicilio;

    /**
     * Il ruolo dell'utente.
     */
    protected String ruolo;

    /**
     * Costruttore completo della classe Utente.
     *
     * @param id L'id dell'utente.
     * @param nome Il nome dell'utente.
     * @param cognome Il cognome dell'utente.
     * @param username L'username dell'utente.
     * @param psw L'oggetto {@link Password} contenente la password cifrata.
     * @param data_nascita La data di nascita dell'utente.
     * @param domicilio L'indirizzo di domicilio.
     */
    public Utente(int id, String nome, String cognome, String username, Password psw, Date data_nascita, String domicilio) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.username = username;
        this.psw = psw;
        this.data_nascita = data_nascita;
        this.domicilio = domicilio;
        this.ruolo = "utente";
    }

    /**
     * Restituisce l'id dell'utente.
     *
     * @return L'id dell'utente.
     */
    public int getId() { return id; }

    /**
     * Aggiorna l'id dell'utente.
     *
     * @param id Il nuovo id da assegnare.
     */
    public void setId(int id) { this.id = id; }

    /**
     * Restituisce il nome dell'utente.
     *
     * @return La stringa del nome.
     */
    public String getNome() { return nome; }

    /**
     * Aggiorna il nome dell'utente.
     *
     * @param nome Il nuovo nome da impostare.
     */
    public void setNome(String nome) { this.nome = nome; }

    /**
     * Restituisce il cognome dell'utente.
     *
     * @return La stringa del cognome.
     */
    public String getCognome() { return cognome; }

    /**
     * Aggiorna il cognome dell'utente.
     *
     * @param cognome Il nuovo cognome da impostare.
     */
    public void setCognome(String cognome) { this.cognome = cognome; }

    /**
     * Restituisce l'username dell'utente.
     *
     * @return La stringa dell'username.
     */
    public String getUsername() { return username; }

    /**
     * Aggiorna l'username.
     *
     * @param username Il nuovo username da assegnare.
     */
    public void setUsername(String username) { this.username = username; }

    /**
     * Restituisce la password cifrata associata all'utente.
     *
     * @return {@link Password}.
     */
    public Password getPsw() { return psw; }

    /**
     * Aggiorna la password associata.
     *
     * @param psw {@link Password} da impostare.
     */
    public void setPsw(Password psw) { this.psw = psw; }

    /**
     * Restituisce la data di nascita dell'utente.
     *
     * @return {@link Date} di nascita.
     */
    public Date getDataNascita() { return data_nascita; }

    /**
     * Aggiorna la data di nascita dell'utente.
     *
     * @param data_nascita La nuova data da associare.
     */
    public void setDataNascita(Date data_nascita) { this.data_nascita = data_nascita; }

    /**
     * Restituisce l'indirizzo di domicilio.
     *
     * @return La stringa dell'indirizzo.
     */
    public String getDomicilio() { return domicilio; }

    /**
     * Aggiorna l'indirizzo di domicilio dell'utente.
     *
     * @param domicilio Il nuovo indirizzo da associare.
     */
    public void setDomicilio(String domicilio) { this.domicilio = domicilio; }

    /**
     * Restituisce la stringa del ruolo.
     *
     * @return La stringa del ruolo.
     */
    public String getRuolo() { return ruolo; }

    /**
     * Modifica il ruolo dell'utente.
     *
     * @param ruolo La nuova stringa di ruolo.
     */
    public void setRuolo(String ruolo) { this.ruolo = ruolo; }
}