package database;

import java.sql.*;

public class DbConnection {
    private static final String url = "jdbc:mysql://localhost:3306/proiect_pao";
    private static final String user = "root";
    private static final String pass = "RootPa55!";
    private static Connection connection;

    public static Connection getConnection() {
        if (DbConnection.connection != null) {
            connection = getDbConnection();
        }
        return DbConnection.connection;
    }

    public static Connection getDbConnection() {
        try{
            if (DbConnection.connection == null || DbConnection.connection.isClosed()){
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection newConnection = DriverManager.getConnection(url, user, pass);
                    DbConnection.connection = newConnection;
                } catch (ClassNotFoundException e) {
                    System.out.println("No JDBC driver available!");
                }
            }
        }
        catch (SQLException e) {
            System.out.println("No database available!");
        }

        return connection;
    }
}
