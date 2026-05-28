package theknife;

import java.io.Serializable;

public class Message implements Serializable {
    
    private static final long serialVersionUID = 1;

    /**
     * La stringa contenente l'oeprazione
     */
    String op;

    /**
     * L'array di oggetti contenente i dati.
     */
    Object[] dati;

    /**
     * Costruttore della classe Message.
     *
     * @param op Operazione da eseguire.
     * @param dati L'array contenente i dati.
     */
    public Message(String op, Object[] dati) {
        this.op = op;
        this.dati = dati;
    }

    /**
     * Restituisce la stringa dell'operazione del messaggio.
     *
     * @return La stringa dell'operazione.
     */
    public String getOp() { return op; }

    /**
     * Imposta la stringa dell'operazione del messaggio.
     *
     * @param op La nuova stringa.
     */
    public void setOp(String op) { this.op = op; }
    
    /**
     * Restituisce l'array di oggetti allegato al messaggio.
     *
     * @return L'array {@link Object} contenente i dati.
     */
    public Object[] getDati() { return this.dati; }

    /**
     * Imposta l'array di dati allegato al messaggio.
     *
     * @param dati Il nuovo array di oggetti.
     */    
    public void setDati(Object[] dati) { this.dati = dati;}
}
