package theknife.server.models;

public class Ristorante {
    private int id;
    private String nome;
    private Luogo luogo;
    private int fascia_prezzo;
    private boolean delivery;
    private boolean prenotazione_online;
    private int ristoratore;

    public Ristorante(String nome, Luogo luogo, int fascia_prezzo, boolean delivery, boolean prenotazione_online, int ristoratore) {
        this.nome = nome;
        this.luogo = luogo;
        this.fascia_prezzo = fascia_prezzo;
        this.delivery = delivery;
        this.prenotazione_online = prenotazione_online;
        this.ristoratore = ristoratore;
    }

    public String getNome() { return nome; }
    public Luogo getLuogo() { return luogo; }
    public int getFasciaPrezzo() { return fascia_prezzo; }
    public boolean isDelivery() { return delivery; }
    public boolean isPrenotazioneOnline() { return prenotazione_online; }
    public int getRistoratore() { return ristoratore; }

    public void setNome(String nome) { this.nome = nome; }
    public void setLuogo(Luogo luogo) { this.luogo = luogo; }
    public void setFasciaPrezzo(int fascia_prezzo) { this.fascia_prezzo = fascia_prezzo; }
    public void setDelivery(boolean delivery) { this.delivery = delivery; }
    public void setPrenotazioneOnline(boolean prenotazione_online) { this.prenotazione_online = prenotazione_online; }
    public void setRistoratore(int ristoratore) { this.ristoratore = ristoratore; }
}