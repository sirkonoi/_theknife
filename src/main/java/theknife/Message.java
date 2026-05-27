package theknife;

import java.io.Serializable;

public class Message implements Serializable {
    private static final long serialVersionUID = 1;
    String op;
    Object[] dati;

    public Message(String op, Object[] dati) {
        this.op = op;
        this.dati = dati;
    }

    public String getOp() { return op; }
    public void setOp(String op) { this.op = op; }
    
    public Object[] getDati() { return this.dati; }
    public void setDati(Object[] dati) { this.dati = dati;}
}
