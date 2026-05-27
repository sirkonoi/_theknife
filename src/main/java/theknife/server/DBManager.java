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

    public synchronized String buildQuery(String op, Object[] values, String[] columns, String table) {
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

    public synchronized void insert(Object[] values,  String[] columns, String table) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(buildQuery("insert", values, columns, table));
        for(int i=0; i<values.length;i++) {
            statement.setObject(i+1, values[i]);
        }
        statement.executeUpdate();
    }
}