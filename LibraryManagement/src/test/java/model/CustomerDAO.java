package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {
    private static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=LibraryDB";
    private static final String USER = "sa";
    private static final String PASS = "123456789";

    public static List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        String query = "SELECT CustomerID, Name, Age, PhoneNumber, AccountID, Username, Password FROM Customers";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("CustomerID");
                String name = rs.getString("Name");
                int age = rs.getInt("Age");
                String phoneNumber = rs.getString("PhoneNumber");
                int accountId = rs.getInt("AccountID");
                String username = rs.getString("Username");
                String password = rs.getString("Password");

                Customer customer = new Customer(id, name, age, phoneNumber, accountId, username, password);
                customers.add(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customers;
    }
    
    public static void deleteCustomer(int customerId) {
        String query = "DELETE FROM Customers WHERE CustomerID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, customerId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void updateCustomerInfo(int customerId, String newName, int newAge, String newPhoneNumber) {
        String query = "UPDATE Customers SET Name = ?, Age = ?, PhoneNumber = ? WHERE CustomerID = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, newName);
            stmt.setInt(2, newAge);
            stmt.setString(3, newPhoneNumber);
            stmt.setInt(4, customerId);
            
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static Customer getCustomerById(int customerId) {
        String query = "SELECT CustomerID, Name, Age, PhoneNumber, AccountID, Username, Password FROM Customers WHERE CustomerID = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, customerId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("CustomerID");
                String name = resultSet.getString("Name");
                int age = resultSet.getInt("Age");
                String phoneNumber = resultSet.getString("PhoneNumber");
                int accountId = resultSet.getInt("AccountID");
                String username = resultSet.getString("Username");
                String password = resultSet.getString("Password");

                return new Customer(id, name, age, phoneNumber, accountId, username, password);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void changePassword(int customerId, String newPassword) {
        String updateQuery = "UPDATE Customers SET Password = ? WHERE CustomerID = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(updateQuery)) {

            statement.setString(1, newPassword);
            statement.setInt(2, customerId);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}
