package theknife.server;

import java.sql.*;

public class DBManager {

    Connection connection;

    public DBManager() {
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/theknife", "postgres", "Rm37901e$");
            System.out.println("Server: Connessione con il DB riuscita.");
        } catch (SQLException e) {
            System.out.println("Server: Connessione con il DB fallita.");
            e.printStackTrace();
        }        
    }

    public DBManager(String url, String user, String psw) {
        try {
            connection = DriverManager.getConnection(url, user, psw);
            System.out.println("Server: Connessione con il DB riuscita.");
        } catch (SQLException e) {
            System.out.println("Server: Connessione con il DB fallita.");
            e.printStackTrace();
        }
    }

    public String buildQuery(String op, String[] values, String table) {
        String query = "";
        int nValues = values.length;
        if(op.equals("viewAll")) {
            query = "SELECT * FROM " + table + ";";
        }

        else if(op.equals("insert")) {
            query = "INSERT INTO " + table + " VALUES (";
            for(int i=0; i<nValues; i++) {
                if(i==nValues-1) {
                    query+="?)";
                }
                else {
                    query+="?, ";
                }
            }
        }

        return query;
    }

}