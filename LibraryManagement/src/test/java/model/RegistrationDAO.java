package model;

import model.DatabaseConnection;
import java.sql.*;

public class RegistrationDAO {
    
    public boolean isUsernameExist(String username) throws SQLException {
        String query = "SELECT * FROM Customers WHERE Username = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    public boolean registerUser(String name, int age, String phoneNumber, int accountId, String username, String password) throws SQLException {
        String query = "INSERT INTO Customers (Name, Age, PhoneNumber, AccountID, Username, Password) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            statement.setInt(2, age);
            statement.setString(3, phoneNumber);
            statement.setInt(4, accountId);
            statement.setString(5, username);
            statement.setString(6, password);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        }
    }
}
