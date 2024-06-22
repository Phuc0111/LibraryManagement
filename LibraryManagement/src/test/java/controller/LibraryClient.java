package controller;

import java.io.*;
import java.net.*;
import java.util.List;
import model.*;

public class LibraryClient {
    private String hostname;
    private int port;
    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    public LibraryClient(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    private void connect() throws IOException {
        socket = new Socket(hostname, port);
        output = new ObjectOutputStream(socket.getOutputStream());
        input = new ObjectInputStream(socket.getInputStream());
    }

    public List<Book> getAllBooks() throws IOException, ClassNotFoundException {
        connect();
        try {
            output.writeObject("GET_ALL_BOOKS");
            output.flush();
            return (List<Book>) input.readObject();
        } finally {
            close();
        }
    }
    
    public String updateCustomer(int customerId, String newName, int newAge, String newPhoneNumber) throws IOException, ClassNotFoundException {
        connect();
        try {
            output.writeObject("UPDATE_CUSTOMER");
            output.flush();

            output.writeInt(customerId);
            output.writeObject(newName);
            output.writeInt(newAge);
            output.writeObject(newPhoneNumber);
            output.flush();

            return (String) input.readObject();
        } finally {
            close();
        }
    }


    public List<BorrowedBookInfo> getBorrowedBooks(int customerId) throws IOException, ClassNotFoundException {
        connect();
        try {
            output.writeObject("GET_BORROWED_BOOKS");
            output.writeInt(customerId);
            output.flush();
            return (List<BorrowedBookInfo>) input.readObject();
        } finally {
            close();
        }
    }

    public String borrowBook(int customerId, int bookId, String title, String authors) throws IOException, ClassNotFoundException {
        connect();
        try {
            output.writeObject("BORROW_BOOK");
            output.writeInt(customerId);
            output.writeInt(bookId);
            output.writeObject(title);
            output.writeObject(authors);
            output.flush();
            return (String) input.readObject();
        } finally {
            close();
        }
    }

    public String returnBook(int customerId, int bookId) throws IOException, ClassNotFoundException {
        connect();
        try {
            output.writeObject("RETURN_BOOK");
            output.writeInt(customerId);
            output.writeInt(bookId);
            output.flush();
            return (String) input.readObject();
        } finally {
            close();
        }
    }
    
    public String changePassword(int customerId, String currentPassword, String newPassword) throws IOException, ClassNotFoundException {
        connect(); // Ensure connection is established
        try {
            output.writeObject("CHANGE_PASS");
            output.writeInt(customerId);
            output.writeObject(currentPassword);
            output.writeObject(newPassword);
            output.flush();
            return (String) input.readObject();
        } finally {
            close(); // Close connection after operation is complete
        }
    }


    public void close() {
        try {
            if (input != null) input.close();
            if (output != null) output.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
