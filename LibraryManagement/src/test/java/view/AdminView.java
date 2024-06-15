package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import model.Admin;
import model.AdminDAO;
import model.Book;
import model.BookDAO;
import model.Customer;
import model.CustomerDAO;
import model.BorrowInfoDAO;
import model.BorrowedBookInfo;

import java.awt.*;
import java.util.List;

public class AdminView extends JFrame {
    private JPanel sidebarPanel;
    private JPanel mainPanel;
    private JPanel personalInfoPanel;
    private JPanel manageBooksPanel;
    private JPanel manageBorrowersPanel;
    private JPanel manageCustomersPanel;

    private JLabel idLabel;
    private JLabel nameLabel;

    private JTextField idTextField;
    private JTextField nameTextField;

    private JTable bookTable;
    private JTable borrowerTable;
    private JTable customerTable;

    private JButton addBookButton;
    private JButton updateBookButton;
    private JButton deleteBookButton;
    private JButton personalInfoButton;
    private JButton manageBooksButton;
    private JButton manageBorrowersButton;
    private JButton manageCustomersButton;
    private JButton acceptReturnButton;
    private JButton rejectReturnButton;
    private JButton updateInfoButton;
    private JButton changePasswordButton;
    private JButton logoutButton;

    private Admin adminObject;

    public AdminView(Admin adminObject) {
        this.adminObject = adminObject;

        setTitle("Admin Panel");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Sidebar
        sidebarPanel = new JPanel();
        sidebarPanel.setBackground(Color.LIGHT_GRAY);
        sidebarPanel.setPreferredSize(new Dimension(200, getHeight()));
        sidebarPanel.setLayout(new GridLayout(4, 1));

        personalInfoButton = createSidebarButton("Thông tin cá nhân");
        manageBooksButton = createSidebarButton("Quản lí sách");
        manageBorrowersButton = createSidebarButton("Quản lí người mượn");
        manageCustomersButton = createSidebarButton("Quản lí khách hàng");

        personalInfoButton.addActionListener(e -> {
            resetButtonBackgrounds();
            personalInfoButton.setBackground(Color.WHITE);
            showLayout(personalInfoPanel, "Thông tin cá nhân");
        });
        manageBooksButton.addActionListener(e -> {
            resetButtonBackgrounds();
            manageBooksButton.setBackground(Color.WHITE);
            showLayout(manageBooksPanel, "Quản lí sách");
        });
        manageBorrowersButton.addActionListener(e -> {
            resetButtonBackgrounds();
            manageBorrowersButton.setBackground(Color.WHITE);
            showLayout(manageBorrowersPanel, "Quản lí người mượn");
        });
        manageCustomersButton.addActionListener(e -> {
            resetButtonBackgrounds();
            manageCustomersButton.setBackground(Color.WHITE);
            showLayout(manageCustomersPanel, "Quản lí khách hàng");
        });

        sidebarPanel.add(personalInfoButton);
        sidebarPanel.add(manageBooksButton);
        sidebarPanel.add(manageBorrowersButton);
        sidebarPanel.add(manageCustomersButton);

        // Main panel
        mainPanel = new JPanel(new BorderLayout());

     // Personal info panel
        personalInfoPanel = new JPanel(new BorderLayout());
        JPanel infoPanel = new JPanel(new GridBagLayout());
        JPanel buttonPanel_personal = new JPanel(new GridLayout(1, 3));

        idLabel = new JLabel("ID: ");
        nameLabel = new JLabel("Tên:");
        idTextField = new JTextField(String.valueOf(adminObject.getAdminId()));
        idTextField.setEditable(false);
        nameTextField = new JTextField(adminObject.getName());
        nameTextField.setEditable(false);
        updateInfoButton = new JButton("Cập nhật thông tin");
        changePasswordButton = new JButton("Thay đổi mật khẩu");
        logoutButton = new JButton("Đăng xuất");

        // Đặt kích thước cho các JTextField và JButton
        setComponentSize(idTextField, 200, 60);
        setComponentSize(nameTextField, 200, 60);
        setComponentSize(updateInfoButton, 200, 60);
        setComponentSize(changePasswordButton, 200, 60);
        setComponentSize(logoutButton, 200, 60);

        // Tạo margin cho các button
        setMarginForButton(updateInfoButton, 20);
        setMarginForButton(changePasswordButton, 20);
        setMarginForButton(logoutButton, 20);

        // Thêm các thành phần vào infoPanel sử dụng GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5); // Đặt margin
        infoPanel.add(idLabel, gbc);

        gbc.gridx = 1;
        infoPanel.add(idTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        infoPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        infoPanel.add(nameTextField, gbc);

        // Thêm các button vào buttonPanel
//        buttonPanel_personal.add(updateInfoButton);
        buttonPanel_personal.add(changePasswordButton);
        buttonPanel_personal.add(logoutButton);

        // Thêm infoPanel vào personalInfoPanel
        personalInfoPanel.add(new JLabel(), BorderLayout.NORTH);
        personalInfoPanel.add(infoPanel, BorderLayout.CENTER);
        personalInfoPanel.add(buttonPanel_personal, BorderLayout.SOUTH);


        // Manage books panel
        manageBooksPanel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel();
        bookTable = new JTable();
        JScrollPane bookScrollPane = new JScrollPane(bookTable);
        addBookButton = new JButton("Thêm sách");
        updateBookButton = new JButton("Cập nhật sách");
        deleteBookButton = new JButton("Xóa sách");

        // Đặt kích thước cho các JButton
        setComponentSize(addBookButton, 150, 30);
        setComponentSize(updateBookButton, 150, 30);
        setComponentSize(deleteBookButton, 150, 30);

        buttonPanel.add(addBookButton);
        buttonPanel.add(updateBookButton);
        buttonPanel.add(deleteBookButton);
        manageBooksPanel.add(bookScrollPane, BorderLayout.CENTER);
        manageBooksPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Manage borrowers panel
        manageBorrowersPanel = new JPanel(new BorderLayout());
        borrowerTable = new JTable();
        JScrollPane borrowerScrollPane = new JScrollPane(borrowerTable);
        JPanel borrowerButtonPanel = new JPanel();
        acceptReturnButton = new JButton("Nhận sách");
        rejectReturnButton = new JButton("Từ chối cho mượn sách");

        // Đặt kích thước cho các JButton
        setComponentSize(acceptReturnButton, 150, 30);
        setComponentSize(rejectReturnButton, 150, 30);

//        borrowerButtonPanel.add(acceptReturnButton);
//        borrowerButtonPanel.add(rejectReturnButton);
        manageBorrowersPanel.add(borrowerScrollPane, BorderLayout.CENTER);
        manageBorrowersPanel.add(borrowerButtonPanel, BorderLayout.SOUTH);

        // Manage account customer
        manageCustomersPanel = new JPanel(new BorderLayout());
        JPanel customerButtonPanel = new JPanel();
        JButton addCustomerButton = new JButton("Thêm khách hàng");
        JButton updateCustomerButton = new JButton("Cập nhật khách hàng");
        JButton deleteCustomerButton = new JButton("Xóa khách hàng");

        // Đặt kích thước cho các JButton
        setComponentSize(addCustomerButton, 150, 30);
        setComponentSize(updateCustomerButton, 150, 30);
        setComponentSize(deleteCustomerButton, 150, 30);

        customerTable = new JTable();
        JScrollPane customerScrollPane = new JScrollPane(customerTable);
        manageCustomersPanel.add(customerScrollPane, BorderLayout.CENTER);

        // Thêm action listeners cho các nút để thực hiện các chức năng tương ứng
//        customerButtonPanel.add(addCustomerButton);
//        customerButtonPanel.add(updateCustomerButton);
        customerButtonPanel.add(deleteCustomerButton);
        manageCustomersPanel.add(customerButtonPanel, BorderLayout.SOUTH);

        // Thêm manageCustomersPanel vào mainPanel
        mainPanel.add(manageCustomersPanel, BorderLayout.CENTER);

        // Default to personal info layout
        showLayout(personalInfoPanel, "Thông tin cá nhân");
        personalInfoButton.setBackground(Color.WHITE);

        // Add components to main frame
        add(sidebarPanel, BorderLayout.WEST);
        add(mainPanel, BorderLayout.CENTER);

        addBookButton.addActionListener(e -> showAddBookDialog());
        updateBookButton.addActionListener(e -> showUpdateBookDialog());
        deleteBookButton.addActionListener(e -> deleteBook());

        acceptReturnButton.addActionListener(e -> acceptReturn());
        rejectReturnButton.addActionListener(e -> rejectReturn());
        
        deleteCustomerButton.addActionListener(e -> deleteCustomer());

        // Thêm action listener cho các button
        updateInfoButton.addActionListener(e -> updateInfo());
        changePasswordButton.addActionListener(e -> showChangePasswordDialog());
        logoutButton.addActionListener(e -> logout());

        // Load dữ liệu khách hàng
        loadCustomers();
        loadBooks();
        loadBorrowInfos();
    }

    private JButton createSidebarButton(String text) {
        JButton button = new JButton(text);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(false);
        button.setBackground(null);
        button.setPreferredSize(new Dimension(300, 400));
        return button;
    }

    private void resetButtonBackgrounds() {
        personalInfoButton.setBackground(null);
        manageBooksButton.setBackground(null);
        manageBorrowersButton.setBackground(null);
        manageCustomersButton.setBackground(null);
    }

    private void showLayout(JPanel panel, String title) {
        mainPanel.removeAll();
        JPanel layoutPanel = new JPanel(new BorderLayout());
        layoutPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        layoutPanel.add(new JLabel(title, JLabel.CENTER), BorderLayout.NORTH);
        layoutPanel.add(panel, BorderLayout.CENTER);
        mainPanel.add(layoutPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void loadCustomers() {
        List<Customer> customers = CustomerDAO.getAllCustomers();
        DefaultTableModel model = new DefaultTableModel(
                new Object[] { "Customer ID", "Name", "Age", "Phone Number", "Account ID", "Username" }, 0);
        for (Customer customer : customers) {
            model.addRow(new Object[] { customer.getId(), customer.getName(), customer.getAge(),
                    customer.getPhoneNumber(), customer.getAccountId(), customer.getUsername() });
        }
        customerTable.setModel(model);
    }

    private void loadBooks() {
        List<Book> books = BookDAO.getAllBooks();
        DefaultTableModel model = new DefaultTableModel(new Object[] { "ID", "Title", "Author", "Quantity" }, 0);
        for (Book book : books) {
            model.addRow(new Object[] { book.getId(), book.getTitle(), book.getAuthors(), book.getQuantity() });
        }
        bookTable.setModel(model);
    }

    private void loadBorrowInfos() {
        List<BorrowedBookInfo> borrowInfos = BorrowInfoDAO.getAllBorrowInfos();
        DefaultTableModel model = new DefaultTableModel(
                new Object[] { "Customer ID", "Customer Name", "Borrow Date", "Borrowed Book"}, 0);
        for (BorrowedBookInfo info : borrowInfos) {
            model.addRow(new Object[] { info.getCustomerId(), info.getCustomerName(), info.getBorrowDate(), info.getTitle() });
        }
        borrowerTable.setModel(model);
    }

    private void showAddBookDialog() {
        JDialog addDialog = new JDialog(this, "Thêm sách", true);
        addDialog.setSize(300, 200);
        addDialog.setLayout(new GridLayout(5, 2));

        JTextField idField = new JTextField();
        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();
        JTextField quantityField = new JTextField();
        JButton addButton = new JButton("Thêm");

        // Đặt kích thước cho các JTextField và JButton
        setComponentSize(idField, 200, 60);
        setComponentSize(titleField, 200, 60);
        setComponentSize(authorField, 200, 60);
        setComponentSize(quantityField, 200, 60);
        setComponentSize(addButton, 200, 60);

        addDialog.add(new JLabel("ID:"));
        addDialog.add(idField);
        addDialog.add(new JLabel("Title:"));
        addDialog.add(titleField);
        addDialog.add(new JLabel("Author:"));
        addDialog.add(authorField);
        addDialog.add(new JLabel("Quantity:"));
        addDialog.add(quantityField);
        addDialog.add(new JLabel());
        addDialog.add(addButton);

        addButton.addActionListener(e -> {
            int id = Integer.parseInt(idField.getText());
            String title = titleField.getText();
            String author = authorField.getText();
            int quantity = Integer.parseInt(quantityField.getText());

            Book newBook = new Book(id, title, author, quantity);
            BookDAO.addBook(newBook);
            loadBooks();
            addDialog.dispose();
        });

        addDialog.setVisible(true);
    }

    private void showUpdateBookDialog() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Chọn một sách để cập nhật", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int bookId = (int) bookTable.getValueAt(selectedRow, 0);
        String currentTitle = (String) bookTable.getValueAt(selectedRow, 1);
        String currentAuthor = (String) bookTable.getValueAt(selectedRow, 2);
        int currentQuantity = (int) bookTable.getValueAt(selectedRow, 3);

        JDialog updateDialog = new JDialog(this, "Cập nhật sách", true);
        updateDialog.setSize(300, 200);
        updateDialog.setLayout(new GridLayout(5, 2));

        JTextField idField = new JTextField(String.valueOf(bookId));
//        idField.setEnabled(false); // Disable ID field to prevent modification
        JTextField titleField = new JTextField(currentTitle);
        JTextField authorField = new JTextField(currentAuthor);
        JTextField quantityField = new JTextField(String.valueOf(currentQuantity));
        JButton updateButton = new JButton("Cập nhật");

        // Đặt kích thước cho các JTextField và JButton
        setComponentSize(idField, 200, 60);
        setComponentSize(titleField, 200, 60);
        setComponentSize(authorField, 200, 60);
        setComponentSize(quantityField, 200, 60);
        setComponentSize(updateButton, 200, 60);

        updateDialog.add(new JLabel("ID:"));
        updateDialog.add(idField);
        updateDialog.add(new JLabel("Title:"));
        updateDialog.add(titleField);
        updateDialog.add(new JLabel("Author:"));
        updateDialog.add(authorField);
        updateDialog.add(new JLabel("Quantity:"));
        updateDialog.add(quantityField);
        updateDialog.add(new JLabel());
        updateDialog.add(updateButton);

        updateButton.addActionListener(e -> {
            String title = titleField.getText();
            String author = authorField.getText();
            int quantity = Integer.parseInt(quantityField.getText());

            Book updatedBook = new Book(bookId, title, author, quantity);
            BookDAO.updateBook(updatedBook);
            loadBooks();
            updateDialog.dispose();
        });

        updateDialog.setVisible(true);
    }

    private void deleteBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Chọn một sách để xóa", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int bookId = (int) bookTable.getValueAt(selectedRow, 0);
        BookDAO.deleteBook(bookId);
        loadBooks();
    }

    private void acceptReturn() {
        // Implement the logic for accepting book return here
    }

    private void rejectReturn() {
        // Implement the logic for rejecting book return here
    }

    private void updateInfo() {
        String newName = nameTextField.getText();
        adminObject.setName(newName);
        AdminDAO.updateAdmin(adminObject); // Implement this method in AdminDAO
        JOptionPane.showMessageDialog(this, "Thông tin đã được cập nhật thành công.");
    }

    private void showChangePasswordDialog() {
        JDialog changePasswordDialog = new JDialog(this, "Thay đổi mật khẩu", true);
        changePasswordDialog.setSize(300, 150);
        changePasswordDialog.setLayout(new GridLayout(3, 2));

        JPasswordField currentPasswordField = new JPasswordField();
        JPasswordField newPasswordField = new JPasswordField();
        JButton changePasswordButton = new JButton("Thay đổi mật khẩu");

        // Đặt kích thước cho các JPasswordField và JButton
        setComponentSize(currentPasswordField, 200, 60);
        setComponentSize(newPasswordField, 200, 60);
        setComponentSize(changePasswordButton, 200, 60);

        changePasswordDialog.add(new JLabel("Mật khẩu hiện tại:"));
        changePasswordDialog.add(currentPasswordField);
        changePasswordDialog.add(new JLabel("Mật khẩu mới:"));
        changePasswordDialog.add(newPasswordField);
        changePasswordDialog.add(new JLabel());
        changePasswordDialog.add(changePasswordButton);

        changePasswordButton.addActionListener(e -> {
            String currentPassword = new String(currentPasswordField.getPassword());
            String newPassword = new String(newPasswordField.getPassword());

            if (AdminDAO.changePassword(adminObject.getAdminId(), currentPassword, newPassword)) {
                JOptionPane.showMessageDialog(this, "Mật khẩu đã được thay đổi thành công.");
                changePasswordDialog.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Mật khẩu hiện tại không đúng.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        changePasswordDialog.setVisible(true);
    }

    private void logout() {
        dispose();
        new LoginView().setVisible(true); // Implement and call the login view
    }

    private void setComponentSize(JComponent component, int width, int height) {
        Dimension size = new Dimension(width, height);
        component.setPreferredSize(size);
        component.setMinimumSize(size);
        component.setMaximumSize(size);
    }
    
    private void setMarginForButton(JButton button, int margin) {
        Insets insets = new Insets(margin, margin, margin, margin);
        button.setMargin(insets);
    }
    
    private void deleteCustomer() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Chọn một khách hàng để xóa", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int customerId = (int) customerTable.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa khách hàng này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
//            CustomerDAO.deleteCustomer(customerId);
            loadCustomers();
            JOptionPane.showMessageDialog(this, "Khách hàng đã được xóa thành công.");
        }
    }


    public static void main(String[] args) {
        Admin admin = AdminDAO.getAdminById(1); // Assuming you have a method to get an admin by ID
        SwingUtilities.invokeLater(() -> new AdminView(admin).setVisible(true));
    }
}
