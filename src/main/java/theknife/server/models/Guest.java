/**
 * Studente: Mattia Rotteri
 * Matricola: 762508
 * Sede: Varese
 */
package theknife.server.models;

import java.io.Serializable;

/**
 * Classe Guest.
 * Rappresenta l'utente non loggato, ospite.
 * Implementa l'interfaccia {@link Serializable}.
 */
public class Guest implements Serializable {

    private static final long serialVersionUID = 1L;    
    
    /**
     * Il nome predefinito dell'utente ospite.
     */
    private String nome;
    
    /**
     * L'indirizzo di domicilio per il filtraggio.
     */
    private String domicilio;
    
    /**
     * La stringa del ruolo.
     */
    private String ruolo;

    /**
     * Costruttore della classe Guest.
     * Inizializza il profilo anonimo impostando i valori predefiniti per il nome
     * e per il ruolo, e aggiunge il domicilio specificato.
     *
     * @param domicilio Domicilio di riferimento.
     */
    public Guest(String domicilio) {
        nome = "Guest";
        this.domicilio = domicilio;
        ruolo = "guest";
    }

    /**
     * Restituisce il nome associato al profilo guest.
     *
     * @return La stringa del nome.
     */
    public String getNome() { return nome; }
    
    /**
     * Modifica il nome associato al profilo guest.
     *
     * @param nome Il nuovo nome.
     */
    public void setNome(String nome) { this.nome = nome; }
    
    /**
     * Restituisce l'indirizzo di domicilio.
     *
     * @return La stringa del domicilio.
     */
    public String getDomicilio() { return domicilio; }
    
    /**
     * Aggiorna l'indirizzo di domicilio.
     *
     * @param domicilio Il nuovo indirizzo.
     */
    public void setDomicilio(String domicilio) { this.domicilio = domicilio; }

    /**
     * Restituisce la stringa identificativa del ruolo.
     *
     * @return La stringa del ruolo.
     */
    public String getRuolo() { return ruolo; }
    
    /**
     * Imposta la stringa del ruolo.
     *
     * @param ruolo La nuova stringa.
     */
    public void setRuolo(String ruolo) { this.ruolo = ruolo; }  
}
