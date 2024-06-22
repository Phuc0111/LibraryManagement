-- SQL Server setup script

USE LibraryDB;

CREATE TABLE Books (
    BookID INT PRIMARY KEY IDENTITY(1,1),
    Title NVARCHAR(100),
    Authors NVARCHAR(100),
    Quantity INT
);

CREATE TABLE Customers (
    CustomerID INT PRIMARY KEY IDENTITY(1,1),
    Name NVARCHAR(100),
    Age INT,
    PhoneNumber NVARCHAR(20),
    AccountID INT,
    Username NVARCHAR(50),
    Password NVARCHAR(50)
);

CREATE TABLE Admins (
    AdminID INT PRIMARY KEY IDENTITY(1,1),
    AccountID INT,
    Username NVARCHAR(50),
    Password NVARCHAR(50),
    Name NVARCHAR(100)
);

CREATE TABLE BorrowRecords (
    BorrowID INT PRIMARY KEY IDENTITY(1,1),
    CustomerID INT,
    BookID INT,
    BorrowDate DATE,
    FOREIGN KEY (CustomerID) REFERENCES Customers(CustomerID),
    FOREIGN KEY (BookID) REFERENCES Books(BookID)
);


-- Insert sample books
INSERT INTO Books (Title, Authors, Quantity) VALUES ('Java Programming', 'John Doe', 10);
INSERT INTO Books (Title, Authors, Quantity) VALUES ('Python Programming', 'Jane Smith', 5);

-- Insert sample customers
INSERT INTO Customers (Name, Age, PhoneNumber, AccountID, Username, Password) VALUES ('Alice', 25, '1234567890', 1, 'alice', 'password123');
INSERT INTO Customers (Name, Age, PhoneNumber, AccountID, Username, Password) VALUES ('Bob', 30, '0987654321', 2, 'bob', 'password456');

-- Insert sample admin
INSERT INTO Admins (AccountID, Username, Password, Name) VALUES (1, 'admin', 'adminpass', 'Administrator');

SELECT * FROM borrow_info;

CREATE TABLE borrow_info (
    CUSTOMER_ID INT,
	CUSTOMER_NAME VARCHAR(50),
    BORROW_DATE DATE,
    -- các cột khác của bảng borrow_info
    FOREIGN KEY (CUSTOMER_ID) REFERENCES customers(CustomerID)
);

-- Thêm các cột mới vào bảng hiện có
ALTER TABLE borrow_info
ADD BOOK_ID INT,
    TITLE VARCHAR(100),
    AUTHORS VARCHAR(100);

-- Cập nhật dữ liệu cho các cột mới
UPDATE borrow_info
SET BOOK_ID = NULL,
    TITLE = NULL,
    AUTHORS = NULL;

-- Xóa các cột không cần thiết
ALTER TABLE borrow_info
DROP COLUMN BORROW_STATUS;

ALTER TABLE borrow_info
ADD CONSTRAINT FK_borrow_info_Books
FOREIGN KEY (BOOK_ID) REFERENCES Books(BookID);

-- Xem cấu trúc của bảng borrow_info
EXEC sp_help 'borrow_info';

SELECT b.CUSTOMER_ID, c.Name, b.BORROW_DATE, b.BOOK_ID, b.TITLE, b.AUTHORS
FROM borrow_info b
JOIN Customers c ON b.CUSTOMER_ID = c.CustomerID;

ALTER TABLE Customers ALTER COLUMN Password VARCHAR(64);
