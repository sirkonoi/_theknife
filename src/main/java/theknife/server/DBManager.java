/**
 * Studente: Mattia Rotteri
 * Matricola: 762508
 * Sede: Varese
 */

package theknife.server;

import java.sql.*;

/**
 * Classe DBManager.
 * Gestisce la connessione con il database.
 */
public class DBManager {

    /**
     * L'oggetto di connessione JDBC.
     */
    Connection connection;

    /**
     * Costruttore predefinito. Usato solo per testing in localhost.
     */
    public DBManager() {
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/theknife", "postgres", "Rm37901e$");
            System.out.println("Server: Connessione con il DB riuscita.");
        } catch (SQLException e) {
            System.out.println("Server: Connessione con il DB fallita.");
            e.printStackTrace();
        }        
    }

    /**
     * Costruttore DBManager.
     * Stabilisce una connessione con il database
     * sulla base dei parametri specificati.
     *
     * @param url  La stringa contenente l'url (es. jdbc:postgresql://host:port/dbname).
     * @param user Username usato nel db.
     * @param psw  Password di accesso.
     */
    public DBManager(String url, String user, String psw) {
        try {
            connection = DriverManager.getConnection(url, user, psw);
            System.out.println("Server: Connessione con il DB riuscita.");
        } catch (SQLException e) {
            System.out.println("Server: Connessione con il DB fallita.");
            e.printStackTrace();
        }
    }

    /**
     * Costruisce una query.
     *
     * @param op Operazione (es. "viewAll", "insert").
     * @param values  Dati da inserire.
     * @param columns Colonne della tabella di destinazione.
     * @param table  Nome della tabella di destinazione.
     * @return La stringa della query SQL.
     */
    public String buildQuery(String op, Object[] values, String[] columns, String table) {
        String query = "";
        if(op.equals("viewAll")) {
            query = "SELECT * FROM " + table + ";";
        }

        else if(op.equals("insert")) {
            query = "INSERT INTO " + table + " (";
            for(int i=0; i<columns.length;i++) {
                if(i==columns.length-1) {
                    query+=columns[columns.length-1] + ") VALUES (";
                }
                else {
                    query+=columns[i] + ", ";
                }
            }
            for(int i=0; i<values.length; i++) {
                if(i==values.length-1) {
                    query+="?)";
                }
                else {
                    query+="?, ";
                }
            }
        }

        return query;
    }

    /**
     * Inserisce dei valori nel DB.
     *
     * @param values i valori.
     * @param columns Colonne della tabella destinataria.
     * @param table   La tabella destinataria dell'inserimento.
     * @throws SQLException In caso di fallimento.
     */
    public void insert(Object[] values,  String[] columns, String table) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(buildQuery("insert", values, columns, table));
        for(int i=0; i<values.length;i++) {
            statement.setObject(i+1, values[i]);
        }
        statement.executeUpdate();
    }
}