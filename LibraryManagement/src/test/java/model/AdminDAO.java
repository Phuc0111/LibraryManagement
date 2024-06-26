package model;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

                if (verifyPassword(currentPassword, storedPassword)) {
                    // Mã hóa mật khẩu mới trước khi cập nhật vào cơ sở dữ liệu
                    String hashedPassword = hashPassword(newPassword);

                    String updateQuery = "UPDATE Admins SET password = ? WHERE adminId = ?";
                    statement = connection.prepareStatement(updateQuery);
                    statement.setString(1, hashedPassword);
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

    private static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashInBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));

            // Convert byte array to a hex string
            StringBuilder sb = new StringBuilder();
            for (byte b : hashInBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static boolean verifyPassword(String inputPassword, String storedPassword) {
        String hashedInputPassword = hashPassword(inputPassword);
        return hashedInputPassword != null && hashedInputPassword.equals(storedPassword);
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
