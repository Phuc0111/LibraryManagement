-- SQL Server setup script

-- Tạo cơ sở dữ liệu
CREATE DATABASE LibraryDB;
GO

-- Sử dụng cơ sở dữ liệu
USE LibraryDB;
GO

-- Tạo bảng Books
CREATE TABLE Books (
    BookID INT PRIMARY KEY IDENTITY(1,1),
    Title NVARCHAR(100),
    Authors NVARCHAR(100),
    Quantity INT,
);

-- Tạo bảng Customers
CREATE TABLE Customers (
    CustomerID INT PRIMARY KEY IDENTITY(1,1),
    Name NVARCHAR(100),
    Age INT,
    PhoneNumber NVARCHAR(20),
    AccountID INT,
    Username NVARCHAR(50),
    Password NVARCHAR(64)  -- Đã cập nhật chiều dài cho mật khẩu
);

-- Tạo bảng Admins
CREATE TABLE Admins (
    AdminID INT PRIMARY KEY IDENTITY(1,1),
    AccountID INT,
    Username NVARCHAR(50),
    Password NVARCHAR(64),  -- Đã cập nhật chiều dài cho mật khẩu
    Name NVARCHAR(100)
);

-- Tạo bảng BorrowRecords
CREATE TABLE BorrowRecords (
    BorrowID INT PRIMARY KEY IDENTITY(1,1),
    CustomerID INT,
    BookID INT,
    BorrowDate DATE,
    FOREIGN KEY (CustomerID) REFERENCES Customers(CustomerID),
    FOREIGN KEY (BookID) REFERENCES Books(BookID)
);

-- Tạo bảng borrow_info với các cột yêu cầu
CREATE TABLE borrow_info (
    CUSTOMER_ID INT,
    CUSTOMER_NAME VARCHAR(50),
    BORROW_DATE DATE,
    BOOK_ID INT,
    TITLE VARCHAR(100),
    AUTHORS VARCHAR(100),
    FOREIGN KEY (CUSTOMER_ID) REFERENCES Customers(CustomerID),
    FOREIGN KEY (BOOK_ID) REFERENCES Books(BookID)
);

-- Xóa cột BORROW_STATUS
ALTER TABLE borrow_info
DROP COLUMN BORROW_STATUS;

-- Xem cấu trúc của bảng borrow_info
EXEC sp_help 'borrow_info';

-- Xem cấu trúc của các bảng
EXEC sp_help 'Books';
EXEC sp_help 'Customers';
EXEC sp_help 'Admins';
EXEC sp_help 'BorrowRecords';

-- Insert mẫu dữ liệu
-- Books
INSERT INTO Books (Title, Authors, Quantity)
VALUES ('Java Programming', 'John Doe', 10),
       ('Python Programming', 'Jane Smith', 5);

-- Customers
INSERT INTO Customers (Name, Age, PhoneNumber, AccountID, Username, Password)
VALUES ('Alice', 25, '1234567890', 1, 'alice', 'password123'),
       ('Bob', 30, '0987654321', 2, 'bob', 'password456');

-- Admins
INSERT INTO Admins (AccountID, Username, Password, Name)
VALUES (1, 'admin', 'adminpass', 'Administrator');

-- BorrowRecords (thêm dữ liệu theo yêu cầu, bạn có thể sử dụng các ID của Customers và Books từ bảng tương ứng)
-- Ví dụ:
-- INSERT INTO BorrowRecords (CustomerID, BookID, BorrowDate)
-- VALUES (1, 1, '2024-06-24');

-- Select dữ liệu từ borrow_info
SELECT b.CUSTOMER_ID, c.Name AS CUSTOMER_NAME, b.BORROW_DATE, b.BOOK_ID, b.TITLE, b.AUTHORS
FROM borrow_info b
JOIN Customers c ON b.CUSTOMER_ID = c.CustomerID;

