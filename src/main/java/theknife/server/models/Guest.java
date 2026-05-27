package theknife.server.models;

import java.io.Serializable;

public class Guest implements Serializable {
    private static final long serialVersionUID = 1L;    
    private String nome;
    private String domicilio;
    private String ruolo;

    public Guest(String domicilio) {
        nome = "Guest";
        this.domicilio = domicilio;
        ruolo = "guest";
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getDomicilio() { return domicilio; }
    public void setDomicilio(String domicilio) { this.domicilio = domicilio; }

    public String getRuolo() { return ruolo; }
    public void setRuolo(String ruolo) { this.ruolo = ruolo; }    
}
