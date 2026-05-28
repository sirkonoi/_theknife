/**
 * Studente: Mattia Rotteri
 * Matricola: 762508
 * Sede: Varese
 */
package theknife.server.models;

/**
 * Classe Ristoratore.
 * Estende la classe base {@link Utente} specializzandone il ruolo per la gestione di ristoranti.
 */
import java.util.Date;

public class Ristoratore extends Utente {
    
    /**
     * Costruttore della classe Ristoratore.
     *
     * @param id L'id univoco dell'utente.
     * @param nome Il nome del ristoratore.
     * @param cognome Il cognome del ristoratore.
     * @param username L'username del ristoratore.
     * @param psw La password in forma cifrata.
     * @param data_nascita La data di nascita del ristoratore.
     * @param domicilio L'indirizzo del ristoratore.
     */
    public Ristoratore(int id, String nome, String cognome, String username, Password psw, Date data_nascita, String domicilio) {
        super(id, nome, cognome, username, psw, data_nascita, domicilio);
        this.ruolo = "ristoratore";
    }
}
