package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDAO {
    public static Admin getAdminById(int adminId) {
        Admin admin = null;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseConnection.getConnection();
            String query = "SELECT * FROM Admins WHERE adminId = ?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, adminId);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int accountId = resultSet.getInt("accountId");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String name = resultSet.getString("name");
                admin = new Admin(adminId, accountId, username, password, name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(connection, statement, resultSet);
        }

        return admin;
    }

    public static void updateAdmin(Admin admin) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DatabaseConnection.getConnection();
            String query = "UPDATE Admins SET name = ?, username = ? WHERE adminId = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, admin.getName());
            statement.setString(2, admin.getUsername());
            statement.setInt(3, admin.getAdminId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(connection, statement, null);
        }
    }

    public static boolean changePassword(int adminId, String currentPassword, String newPassword) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseConnection.getConnection();
            String query = "SELECT password FROM Admins WHERE adminId = ?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, adminId);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String storedPassword = resultSet.getString("password");

                if (storedPassword.equals(currentPassword)) {
                    String updateQuery = "UPDATE Admins SET password = ? WHERE adminId = ?";
                    statement = connection.prepareStatement(updateQuery);
                    statement.setString(1, newPassword);
                    statement.setInt(2, adminId);
                    statement.executeUpdate();
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(connection, statement, resultSet);
        }

        return false;
    }

    private static void closeResources(Connection connection, PreparedStatement statement, ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
