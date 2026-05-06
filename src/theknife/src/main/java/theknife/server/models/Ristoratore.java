package theknife.server.models;

import java.util.Date;

public class Ristoratore extends Utente {
    
    public Ristoratore(int id, String nome, String cognome, String username, Password psw, Date data_nascita, String domicilio) {
        super(id, nome, cognome, username, psw, data_nascita, domicilio);
        this.ruolo = "ristoratore";
    }
}
