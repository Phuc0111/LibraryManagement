package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=LibraryDB";
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "123456789";

    static {
        try {
            // Load the SQL Server JDBC driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            System.out.println("SQL Server JDBC Driver not found.");
            e.printStackTrace();
        }
    }

    public static Connection connect() {
        Connection con = null;
        try {
            // Establish the connection
            con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            if (con != null) {
                System.out.println("Connected to database!");
            }
        } catch (SQLException e) {
            System.out.println("Failed to connect to the database!");
            e.printStackTrace();
        }
        return con;
    }

    public static Connection getConnection() {
        return connect();
    }

    public static void main(String[] args) {
        connect();
    }
}
