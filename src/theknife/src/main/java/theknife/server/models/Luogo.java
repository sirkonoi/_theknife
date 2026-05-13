package theknife.server.models;

import java.io.*;
import java.net.*;

public class Luogo implements Serializable {
    
    private int id;
    private String indirizzo;
    private String nazione;
    private String citta;
    private double longitudine;
    private double latitudine;

    public Luogo(String indirizzo, String citta, String nazione) throws IOException {
        this.indirizzo = indirizzo;
        this.citta = citta;
        this.nazione = nazione;
        double[] tmp = getLatitudineLongitudine(indirizzo + ", " + citta + ", " + nazione);
        latitudine = tmp[0];
        longitudine = tmp[1];
    }

    public Luogo(String indirizzo, String citta, String nazione, double latitudine, double longitudine) {
        this.indirizzo = indirizzo;
        this.citta = citta;
        this.nazione = nazione;
        this.latitudine = latitudine;
        this.longitudine = longitudine;        
    }
  
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

    public int getId() { return id;}
    public void setId(int id) { this.id = id;}

    public String getIndirizzo() { return indirizzo; }
    public void setIndirizzo(String indirizzo) { this.indirizzo = indirizzo; }

    public String getNazione() { return nazione; }
    public void setNazione(String nazione) { this.nazione = nazione; }
    
    public String getCitta() { return citta; }
    public void setCitta(String citta) { this.citta = citta; }

    public double getLatitudine() { return latitudine; }
    public void setLatitudine(double latitudine) { this.latitudine = latitudine; }

    public double getLongitudine() { return longitudine; }
    public void setLongitudine(double longitudine) { this.longitudine = longitudine; }    
}
