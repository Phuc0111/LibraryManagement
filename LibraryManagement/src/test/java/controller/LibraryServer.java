package controller;

import java.io.*;
import java.net.*;
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

	public static void addAdminClient(AdminWebSocketClient client) {
		adminClients.add(client);
	}

	public static void removeAdminClient(AdminWebSocketClient client) {
		adminClients.remove(client);
	}

	public static void notifyAdmins(String message) {
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
                    CustomerDAO.updateCustomerInfo(customerIdUpdate, newName, newAge, newPhoneNumber);
                    output.writeObject("Update successful.");
                    break;

                case "GET_ALL_BOOKS":
                    List<Book> books = BookDAO.getAllBooks();
                    output.writeObject(books);
                    break;

                case "GET_BORROWED_BOOKS":
                    List<BorrowedBookInfo> borrowedBooks = BorrowedBookInfoDAO.getAllBorrowedBooks();
                    output.writeObject(borrowedBooks);
                    break;

                case "BORROW_BOOK":
                    int customerId = input.readInt();
                    int bookId = input.readInt();
                    String title = (String) input.readObject();
                    String authors = (String) input.readObject();
                    BookDAO.updateBookQuantity(bookId, BookDAO.getBookById(bookId).getQuantity() - 1);
                    BorrowedBookInfoDAO.addBorrowedBook(customerId, "Customer Name", bookId, title, authors, new java.util.Date());
                    output.writeObject("SUCCESS");
                    LibraryServer.notifyAdmins("A book has been borrowed by customer ID: " + customerId);
                    break;

                case "RETURN_BOOK":
                    customerId = input.readInt();
                    bookId = input.readInt();
                    BookDAO.updateBookQuantity(bookId, BookDAO.getBookById(bookId).getQuantity() + 1);
                    BorrowedBookInfoDAO.removeBorrowedBook(customerId, bookId);
                    output.writeObject("SUCCESS");
                    LibraryServer.notifyAdmins("A book has been returned by customer ID: " + customerId);
                    break;

                case "CHANGE_PASS":
                    int customerIdChangePassword = input.readInt();
                    String currentPassword = (String) input.readObject();
                    String newPassword = (String) input.readObject();

                    Customer customer = CustomerDAO.getCustomerById(customerIdChangePassword);
                    if (customer != null && customer.getPassword().equals(currentPassword)) {
                        CustomerDAO.changePassword(customerIdChangePassword, newPassword);
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

