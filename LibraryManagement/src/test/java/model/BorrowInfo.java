package model;

import java.io.Serializable;
import java.util.Date;

public class BorrowInfo implements Serializable {
    private int customerId;
    private String customerName;
    private Date borrowDate;

    public BorrowInfo(int customerId, String customerName, Date borrowDate) {
        this.customerId = customerId;
        this.customerName = customerName;
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

    public Date getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(Date borrowDate) {
        this.borrowDate = borrowDate;
    }
}
