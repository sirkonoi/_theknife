package theknife.server;
import theknife.server.models.*;

public class Debug {
    public static void main(String[] args) {
        try {
            DBManager db = new DBManager();
            UsersManager um = new UsersManager(db);

            // test register
            Object[] values = {
                "Mario",        // nome
                "Rossi",        // cognome
                "konoi",        // username
                Password.encrypt("ciao1234$"), //pass
                null,           // data_nascita (facoltativa)
                "Via Cremona 15, Milano, Italia",       // domicilio
                "utente"        // ruolo
            };

            boolean registered = um.register(values);
            System.out.println("Registrazione: " + (registered ? "OK" : "Username già esistente"));

            // test register duplicato
            boolean duplicate = um.register(values);
            System.out.println("Registrazione duplicata: " + (duplicate ? "OK" : "Username già esistente"));

            // test login corretto
            Utente user = um.login("konoi", "ciao1234$");
            System.out.println("Login corretto: " + (user != null ? "OK" : "FALLITO"));
            System.out.println("Login corretto, ID: " + user.getId());


            // test login password sbagliata
            Utente loginFail = um.login("mrossi", "passwordsbagliata");
            System.out.println("Login password errata: " + (loginFail != null  ? "OK" : "FALLITO"));

            // test login utente non esistente
            Utente loginNotFound = um.login("inesistente", "password123");
            System.out.println("Login utente inesistente: " + (loginNotFound != null  ? "OK" : "FALLITO"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
