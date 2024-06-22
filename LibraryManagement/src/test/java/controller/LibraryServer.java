package controller;

import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import model.*;

public class LibraryServer {
    private static CopyOnWriteArrayList<AdminWebSocketClient> adminClients = new CopyOnWriteArrayList<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Server is listening on port 12345");
            while (true) {
                Socket socket = serverSocket.accept();
                new ServerThread(socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void addAdminClient(AdminWebSocketClient client) {
        adminClients.add(client);
    }

    public static synchronized void removeAdminClient(AdminWebSocketClient client) {
        adminClients.remove(client);
    }

    public static synchronized void notifyAdmins(String message) {
        for (AdminWebSocketClient client : adminClients) {
            client.sendMessage(message);
        }
    }
}

class ServerThread extends Thread {
    private Socket socket;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try (
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream())
        ) {
            String command = (String) input.readObject();

            switch (command) {
                case "UPDATE_CUSTOMER":
                    int customerIdUpdate = input.readInt();
                    String newName = (String) input.readObject();
                    int newAge = input.readInt();
                    String newPhoneNumber = (String) input.readObject();
                    synchronized (CustomerDAO.class) {
                        CustomerDAO.updateCustomerInfo(customerIdUpdate, newName, newAge, newPhoneNumber);
                    }
                    output.writeObject("Update successful.");
                    break;

                case "GET_ALL_BOOKS":
                    List<Book> books;
                    synchronized (BookDAO.class) {
                        books = BookDAO.getAllBooks();
                    }
                    output.writeObject(books);
                    break;

                case "GET_BORROWED_BOOKS":
                    List<BorrowedBookInfo> borrowedBooks;
                    synchronized (BorrowedBookInfoDAO.class) {
                        borrowedBooks = BorrowedBookInfoDAO.getAllBorrowedBooks();
                    }
                    output.writeObject(borrowedBooks);
                    break;

                case "BORROW_BOOK":
                	int customerId = input.readInt();
                    int bookId = input.readInt();
                    String title = (String) input.readObject();
                    String authors = (String) input.readObject();

                    // Kiểm tra và cập nhật số lượng sách
                    synchronized (BookDAO.class) {
                        Book book = BookDAO.getBookById(bookId);
                        if (book != null && book.getQuantity() > 0) {
                            boolean updated = BookDAO.updateBookQuantity(bookId, book.getQuantity() - 1);
                            if (updated) {
                                // Thêm thông tin mượn sách vào borrow_info
                                Date borrowDate = new Date();
                                boolean added = BorrowedBookInfoDAO.addBorrowedBook(customerId, book.getId(), title, authors, borrowDate);
                                if (added) {
                                    output.writeObject("SUCCESS");
                                    LibraryServer.notifyAdmins("A book has been borrowed by customer ID: " + customerId);
                                } else {
                                    output.writeObject("Failed to add borrowed book info");
                                }
                            } else {
                                output.writeObject("Failed to update book quantity");
                            }
                        } else {
                            output.writeObject("Book not available");
                        }
                    }
                    break;

                case "RETURN_BOOK":
                    customerId = input.readInt();
                    bookId = input.readInt();
                    synchronized (BookDAO.class) {
                        BookDAO.updateBookQuantity(bookId, BookDAO.getBookById(bookId).getQuantity() + 1);
                        BorrowedBookInfoDAO.removeBorrowedBook(customerId, bookId);
                    }
                    output.writeObject("SUCCESS");
                    LibraryServer.notifyAdmins("A book has been returned by customer ID: " + customerId);
                    break;

                case "CHANGE_PASS":
                    int customerIdChangePassword = input.readInt();
                    String currentPassword = (String) input.readObject();
                    String newPassword = (String) input.readObject();

                    Customer customer;
                    synchronized (CustomerDAO.class) {
                        customer = CustomerDAO.getCustomerById(customerIdChangePassword);
                    }
                    if (customer != null && customer.getPassword().equals(currentPassword)) {
                        synchronized (CustomerDAO.class) {
                            CustomerDAO.changePassword(customerIdChangePassword, newPassword);
                        }
                        output.writeObject("Password changed successfully.");
                    } else {
                        output.writeObject("Current password is incorrect.");
                    }
                    break;

                default:
                    output.writeObject("UNKNOWN_COMMAND");
                    break;
            }
        } catch (IOException e) {
            if ("Socket closed".equals(e.getMessage())) {
                System.out.println("Socket was closed, likely due to a client disconnect.");
            } else {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


