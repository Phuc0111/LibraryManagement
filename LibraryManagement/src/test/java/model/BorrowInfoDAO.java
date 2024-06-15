package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BorrowInfoDAO {
    private static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=LibraryDB";
    private static final String USER = "sa";
    private static final String PASS = "123456789";

    public static List<BorrowedBookInfo> getAllBorrowInfos() {
        List<BorrowedBookInfo> borrowInfos = new ArrayList<>();
        String query = "SELECT b.CUSTOMER_ID, c.Name, b.BORROW_DATE, b.BOOK_ID, b.TITLE, b.AUTHORS " +
                "FROM borrow_info b " +
                "JOIN Customers c ON b.CUSTOMER_ID = c.CustomerID";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int customerId = rs.getInt("CUSTOMER_ID");
                String customerName = rs.getString("Name");
                Date borrowDate = rs.getDate("BORROW_DATE");
                int bookId = rs.getInt("BOOK_ID");
                String title = rs.getString("TITLE");
                String authors = rs.getString("AUTHORS");

                BorrowedBookInfo borrowInfo = new BorrowedBookInfo(customerId, customerName, bookId, title, authors, borrowDate);
                borrowInfos.add(borrowInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Query failed: " + query); // Thêm thông báo hiển thị câu truy vấn để kiểm tra lỗi
        }

        return borrowInfos;
    }


    public static void updateStatus(int customerId, String status) {
        String query = "UPDATE borrow_info SET BORROW_STATUS = ? WHERE CUSTOMER_ID = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, status);
            pstmt.setInt(2, customerId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
