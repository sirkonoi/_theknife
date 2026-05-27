package theknife.server.models;

import java.io.Serializable;
import java.util.Date;

public class Utente implements Serializable {
    private static final long serialVersionUID = 1L;
    
    protected int id;
    protected String nome, cognome, username;
    protected Date data_nascita;
    protected Password psw;
    protected String domicilio, ruolo;

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

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCognome() { return cognome; }
    public void setCognome(String cognome) { this.cognome = cognome; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public Password getPsw() { return psw; }
    public void setPsw(Password psw) { this.psw = psw; }

    public Date getDataNascita() { return data_nascita; }
    public void setDataNascita(Date data_nascita) { this.data_nascita = data_nascita; }

    public String getDomicilio() { return domicilio; }
    public void setDomicilio(String domicilio) { this.domicilio = domicilio; }

    public String getRuolo() { return ruolo; }
    public void setRuolo(String ruolo) { this.ruolo = ruolo; }
}