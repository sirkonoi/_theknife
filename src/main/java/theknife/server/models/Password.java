/**
 * Studente: Mattia Rotteri
 * Matricola: 762508
 * Sede: Varese
 */
package theknife.server.models;

import java.io.Serializable;

/**
 * La classe Password rappresenta una password cifrata usando una semplice cifratura
 * basata sulla lunghezza della stringa.
 * Implementa {@link Serializable}
 */
public class Password implements Serializable {
    private static final long serialVersionUID = 1L;   
     
    /**
     * Il valore criptato.
     */
    private String encrypted;

     /**
     * Costruttore. Data una password in chiaro, la cifra e la memorizza.
     *
     * @param value Password in chiaro da cifrare.
     */
    public Password(String value) {
        this.encrypted = encrypt(value);
    }

    /**
     * Applica la cifratura a una stringa, traslando ogni carattere di un numero di posizioni pari alla lunghezza della stringa.
     *
     * @param clearValue La stringa da cifrare.
     * @return La stringa corrispondente alla password cifrata.
     */
    public static String encrypt(String clearValue) {
        int key = clearValue.length();
        String encrypted = "";

        for (int i = 0; i < clearValue.length(); i++) {
            char enChar = (char) (clearValue.charAt(i) + key);
            encrypted += enChar;
        }

        return encrypted;
    }

    /**
     * Decifra una stringa cifrata, traslando ogni carattere di un numero di posizioni pari alla lunghezza della stringa.
     *
     * @param encryptedValue La stringa cifrata da decifrare.
     * @return La stringa in chiaro.
     */
    public static String decrypt(String encryptedValue) {
        int key = encryptedValue.length();
        String decrypted = "";

        for (int i = 0; i < encryptedValue.length(); i++) {
            char enChar = (char) (encryptedValue.charAt(i) - key);
            decrypted += enChar;
        }

        return decrypted;
    }

    /**
     * Restituisce la stringa cifrata.
     *
     * @return La password cifrata come stringa.
     */ 
    @Override
    public String toString() {
        return encrypted;
    }
}
