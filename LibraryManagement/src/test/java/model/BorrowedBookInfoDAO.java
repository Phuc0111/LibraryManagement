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
    
    public static boolean addBorrowedBook(int customerId, int bookId, String title, String authors, Date borrowDate) {
        String query = "INSERT INTO borrow_info (CUSTOMER_ID, BOOK_ID, TITLE, AUTHORS, BORROW_DATE) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, customerId);
            stmt.setInt(2, bookId);
            stmt.setString(3, title);
            stmt.setString(4, authors);
            stmt.setDate(5, new java.sql.Date(borrowDate.getTime()));
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0; // Trả về true nếu có ít nhất một dòng được thêm vào
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Trả về false nếu có lỗi xảy ra
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
