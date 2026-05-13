package theknife.server.models;

import java.io.Serializable;

public class Ristorante implements Serializable {
    private int id;
    private String nome;
    private Luogo luogo;
    private int fascia_prezzo;
    private boolean delivery;
    private boolean prenotazione_online;
    private int ristoratore;
    //private String[] tipiCucina;

    public Ristorante(int id, String nome, Luogo luogo, int fascia_prezzo, boolean delivery, boolean prenotazione_online, int ristoratore) {
        this.id = id;
        this.nome = nome;
        this.luogo = luogo;
        this.fascia_prezzo = fascia_prezzo;
        this.delivery = delivery;
        this.prenotazione_online = prenotazione_online;
        this.ristoratore = ristoratore;
    }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public Luogo getLuogo() { return luogo; }
    public void setLuogo(Luogo luogo) { this.luogo = luogo; }

    public int getFasciaPrezzo() { return fascia_prezzo; }
    public void setFasciaPrezzo(int fascia_prezzo) { this.fascia_prezzo = fascia_prezzo; }

    public boolean isDelivery() { return delivery; }
    public void setDelivery(boolean delivery) { this.delivery = delivery; }

    public boolean isPrenotazioneOnline() { return prenotazione_online; }
    public void setPrenotazioneOnline(boolean prenotazione_online) { this.prenotazione_online = prenotazione_online; }

    public int getRistoratore() { return ristoratore; }
    public void setRistoratore(int ristoratore) { this.ristoratore = ristoratore; }
}