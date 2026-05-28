/**
 * Studente: Mattia Rotteri
 * Matricola: 762508
 * Sede: Varese
 */
package theknife.server.models;

import java.io.Serializable;

/**
 * Classe Preferito.
 * Rappresenta l'associazione tra un utente e un ristorante salvato come preferito.
 * Implementa l'interfaccia {@link Serializable}
 */
public class Preferito implements Serializable {

    private static final long serialVersionUID = 1L;
    
    /**
     * L'id univoco dell'utente che ha salvato il preferito.
     */
    int idUtente;

    /**
     * L'id univoco del ristorante salvato tra i preferiti.
     */
    int idRistorante;

    /**
     * Il nome del ristorante.
     */
    String nomeRistorante;

    /**
     * Costruttore della classe Preferito.
     *
     * @param idUtente L'id dell'utente.
     * @param idRistorante L'id del ristorante.
     * @param nomeRistorante Il nome del ristorante.
     */
    public Preferito(int idUtente, int idRistorante, String nomeRistorante) {
        this.idUtente = idUtente;
        this.idRistorante = idRistorante;
        this.nomeRistorante = nomeRistorante;
    }

/**
     * Restituisce l'id dell'utente.
     *
     * @return L'id dell'utente.
     */
    public int getUtente() { return idUtente; }

    /**
     * Imposta l'id dell'utente.
     *
     * @param id Il nuovo id da assegnare.
     */
    public void setUtente(int id) { idUtente = id; }
    
    /**
     * Restituisce l'id del ristorante.
     *
     * @return L'id del ristorante.
     */
    public int getRistorante() { return idRistorante; }

    /**
     * Imposta l'id del ristorante.
     *
     * @param id Il nuovo id da assegnare.
     */
    public void setRistorante(int id) { idRistorante = id; }

    /**
     * Restituisce il nome del ristorante.
     *
     * @return La stringa del nome del ristorante.
     */
    public String getNomeRistorante() { return nomeRistorante; }

    /**
     * Imposta il nome del ristorante.
     *
     * @param nome Il nuovo nome da assegnare.
     */
    public void setNomeRistorante(String nome) { nomeRistorante = nome; }
}
