package model;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BorrowedBookInfoDAO {
    public static List<BorrowedBookInfo> getAllBorrowedBooks() {
        List<BorrowedBookInfo> borrowedBooks = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT b.BookID AS BOOK_ID, b.Title, b.Authors, bi.BORROW_DATE, c.CustomerID AS CUSTOMER_ID, c.Name AS CUSTOMER_NAME " +
                             "FROM borrow_info bi " +
                             "JOIN Books b ON bi.BOOK_ID = b.BookID " +
                             "JOIN Customers c ON bi.CUSTOMER_ID = c.CustomerID");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int bookId = rs.getInt("BOOK_ID");
                String title = rs.getString("Title");
                String authors = rs.getString("Authors");
                Date borrowDate = rs.getDate("BORROW_DATE");
                int customerId = rs.getInt("CUSTOMER_ID");
                String customerName = rs.getString("CUSTOMER_NAME");
                borrowedBooks.add(new BorrowedBookInfo(customerId, customerName, bookId, title, authors, borrowDate));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return borrowedBooks;
    }
    
    public static void addBorrowedBook(int customerId, String customerName, int bookId, String title, String authors, Date borrowDate) {
        String query = "INSERT INTO borrow_info (CUSTOMER_ID, CUSTOMER_NAME, BOOK_ID, TITLE, AUTHORS, BORROW_DATE) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, customerId);
            stmt.setString(2, customerName);
            stmt.setInt(3, bookId);
            stmt.setString(4, title);
            stmt.setString(5, authors);
            stmt.setDate(6, new java.sql.Date(borrowDate.getTime()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removeBorrowedBook(int customerId, int bookId) {
        String query = "DELETE FROM borrow_info WHERE CUSTOMER_ID = ? AND BOOK_ID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, customerId);
            stmt.setInt(2, bookId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Add methods to insert, update, or delete borrowed book info as needed
}
