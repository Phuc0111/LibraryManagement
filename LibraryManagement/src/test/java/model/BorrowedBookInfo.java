package model;

import java.io.Serializable;
import java.util.Date;

public class BorrowedBookInfo implements Serializable {
    private int bookId;
    private String title;
    private String authors;
    private Date borrowDate;
    private int customerId;
    private String customerName;

    public BorrowedBookInfo(int customerId, String customerName, int bookId, String title, String authors, Date borrowDate) {
        this.bookId = bookId;
        this.title = title;
        this.authors = authors;
        this.borrowDate = borrowDate;
        this.customerId = customerId;
        this.customerName = customerName;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public Date getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(Date borrowDate) {
        this.borrowDate = borrowDate;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
