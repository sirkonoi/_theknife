/**
 * Studente: Mattia Rotteri
 * Matricola: 762508
 * Sede: Varese
 */
package theknife.server.models;

import java.io.*;
import java.net.*;

/**
 * Classe Luogo.
 * Si occupa della gestione dei Luoghi.
 * Implementa l'interfaccia {@link Serializable}. 
 */
public class Luogo implements Serializable {
    private static final long serialVersionUID = 1L;
    
/**
     * L'id univoco del luogo.
     */
    private int id;
    
    /**
     * La stringa contenente l'indirizzo.
     */
    private String indirizzo;
    
    /**
     * La nazione.
     */
    private String nazione;
    
    /**
     * Il nome della città.
     */
    private String citta;
    
    /**
     * La longitudine del luogo.
     */
    private double longitudine;
    
    /**
     * La latitudine del luogo.
     */
    private double latitudine;

    /**
     * Costruttore per la creazione di un nuovo luogo.
     *
     * @param indirizzo La via e il numero civico.
     * @param citta La città di riferimento.
     * @param nazione La nazione.
     * @throws IOException
     */
    public Luogo(String indirizzo, String citta, String nazione) throws IOException {
        this.indirizzo = indirizzo;
        this.citta = citta;
        this.nazione = nazione;
        double[] tmp = getLatitudineLongitudine(indirizzo + ", " + citta + ", " + nazione);
        latitudine = tmp[0];
        longitudine = tmp[1];
    }
   /**
     * Costruttore per la creazione di un nuovo luogo.
     *
     * @param indirizzo La via e il numero civico.
     * @param citta La città di riferimento.
     * @param nazione La nazione.
     * @param latitudine La latitudine del luogo.
     * @param longitudine La longitudine del luogo.
     */
    public Luogo(String indirizzo, String citta, String nazione, double latitudine, double longitudine) {
        this.indirizzo = indirizzo;
        this.citta = citta;
        this.nazione = nazione;
        this.latitudine = latitudine;
        this.longitudine = longitudine;        
    }
  
    /**
     * Dato un indirizzo restituisce la sua latitudine e longitudine.
     *
     * @param indirizzo Indirizzo da localizzare.
     * @return Un array di double, dove l'indice 0 contiene la latitudine e l'indice 1 la longitudine, oppure null se la ricerca fallisce.
     * @throws IOException 
     */
    public static double[] getLatitudineLongitudine(String indirizzo) throws IOException {
        String urlString = "https://nominatim.openstreetmap.org/search?q=" + indirizzo.replace(" ", "+")
                + "&format=json&limit=1";
        URL url = URI.create(urlString).toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("User-Agent", "TheKnife/1.0");
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        
        String json = "";
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            json = json + inputLine;
        }
        in.close();

        int latIndex = json.indexOf("\"lat\":\"");
        int lonIndex = json.indexOf("\"lon\":\"");

        if (latIndex == -1 || lonIndex == -1) {
            return null;
        }

        double[] a = new double[2];
        String lat = json.substring(latIndex + 7, json.indexOf("\"", latIndex + 7));
        String lon = json.substring(lonIndex + 7, json.indexOf("\"", lonIndex + 7));

        a[0] = Double.parseDouble(lat);
        a[1] = Double.parseDouble(lon);
        
        return a; //a[0] è la latitudine e a[1] è longitudine
    }

    /**
     * Restituisce se un luogo esista o meno.
     *
     * @return true se il luogo esiste, false altrimenti.
     * @throws IOException
     */
    public boolean luogoExists() throws IOException {
        if (this.indirizzo == null || this.citta == null || this.nazione == null) {
            return false;
        }
        String indirizzo = this.indirizzo + ", " + this.citta + ", " + this.nazione;

        String urlString = "https://nominatim.openstreetmap.org/search?q=" + indirizzo.replace(" ", "+") + "&format=json&limit=1";
        URL url = URI.create(urlString).toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestProperty("User-Agent", "TheKnife/1.0");

        try (BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String line = rd.readLine();
            return line != null && !line.equals("[]");
        } catch (IOException e) {
            return false;
        }
    } 

    /**
     * Restituisce se un luogo esista o meno.
     *
     * @param indirizzo L'indirizzo da testare.
     * @return true se il luogo esiste, false altrimenti.
     * @throws IOException
     */
    public static boolean luogoExists(String indirizzo) throws IOException {
        if (indirizzo == null) { return false; }
        String urlString = "https://nominatim.openstreetmap.org/search?q=" + indirizzo.replace(" ", "+") + "&format=json&limit=1";
        URL url = URI.create(urlString).toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestProperty("User-Agent", "TheKnife/1.0");

        try (BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String line = rd.readLine();
            return line != null && !line.equals("[]");
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Restituisce l'id del luogo.
     *
     * @return Il valore id.
     */
    public int getId() { return id;}
    
    /**
     * Imposta l'id del luogo.
     *
     * @param id Il nuovo valore.
     */
    public void setId(int id) { this.id = id;}

    /**
     * Restituisce l'indirizzo.
     *
     * @return La stringa dell'indirizzo.
     */
    public String getIndirizzo() { return indirizzo; }
    
    /**
     * Aggiorna l'indirizzo.
     *
     * @param indirizzo Il nuovo indirizzo.
     */
    public void setIndirizzo(String indirizzo) { this.indirizzo = indirizzo; }

    /**
     * Restituisce il nome della nazione.
     *
     * @return La stringa della nazione.
     */
    public String getNazione() { return nazione; }
    
    /**
     * Aggiorna il nome della nazione.
     *
     * @param nazione Il nome della nuova nazione.
     */
    public void setNazione(String nazione) { this.nazione = nazione; }
    
    /**
     * Restituisce il nome della città.
     *
     * @return La stringa identificativa della città.
     */
    public String getCitta() { return citta; }
    
    /**
     * Aggiorna il nome della città.
     *
     * @param citta Il nome della nuova città.
     */
    public void setCitta(String citta) { this.citta = citta; }

    /**
     * Restituisce il valore della coordinata di latitudine.
     *
     * @return Il valore double della latitudine.
     */
    public double getLatitudine() { return latitudine; }
    
    /**
     * Imposta la coordinata di latitudine.
     *
     * @param latitudine Il valore double della nuova latitudine.
     */
    public void setLatitudine(double latitudine) { this.latitudine = latitudine; }

    /**
     * Restituisce il valore della coordinata di longitudine.
     *
     * @return Il valore double della longitudine.
     */
    public double getLongitudine() { return longitudine; }
    
    /**
     * Imposta la coordinata di longitudine.
     *
     * @param longitudine Il valore double della nuova longitudine.
     */
    public void setLongitudine(double longitudine) { this.longitudine = longitudine; } 
}
