package theknife.server;

import theknife.server.models.Password;

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
            boolean login = um.login("konoi", "ciao1234$");
            System.out.println("Login corretto: " + (login ? "OK" : "FALLITO"));

            // test login password sbagliata
            boolean loginFail = um.login("mrossi", "passwordsbagliata");
            System.out.println("Login password errata: " + (loginFail ? "OK" : "FALLITO"));

            // test login utente non esistente
            boolean loginNotFound = um.login("inesistente", "password123");
            System.out.println("Login utente inesistente: " + (loginNotFound ? "OK" : "FALLITO"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
