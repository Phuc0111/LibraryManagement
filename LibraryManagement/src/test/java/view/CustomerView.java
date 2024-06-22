package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import controller.LibraryClient;
import model.*;

import java.awt.*;
import java.io.IOException;
import java.util.List;

public class CustomerView extends JFrame {
    private JTable bookTable;
    private JTable borrowedBookTable;
    private JPanel personalInfoPanel;
    private JPanel borrowBookPanel;
    private JPanel borrowedBooksPanel;
    private JButton borrowButton;
    private JButton returnButton;
    private JButton personalInfoButton;
    private JButton borrowBooksButton;
    private JButton borrowedBooksButton;
    private JPanel mainPanel;
    private LibraryClient client;
    private Customer customer;
    private JLabel idLabel;
    private JLabel nameLabel;
    private JLabel ageLabel;
    private JLabel phoneNumberLabel;
    private JLabel usernameLabel;
    private JTextField idTextField;
    private JTextField nameTextField;
    private JTextField ageTextField;
    private JTextField phoneNumberTextField;
    private JTextField usernameTextField;
    private JButton updateInfoButton;
    private JButton changePasswordButton;
    private JButton logoutButton;

    public CustomerView(Customer customer) {
        this.customer = customer;
        client = new LibraryClient("localhost", 12345);

        setTitle("Customer Panel");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Sidebar
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setBackground(Color.LIGHT_GRAY);
        sidebarPanel.setPreferredSize(new Dimension(200, getHeight()));
        sidebarPanel.setLayout(new GridLayout(3, 1));

        personalInfoButton = createSidebarButton("Thông tin cá nhân");
        borrowBooksButton = createSidebarButton("Mượn sách");
        borrowedBooksButton = createSidebarButton("Sách đã mượn");

        personalInfoButton.addActionListener(e -> showLayout(personalInfoPanel, personalInfoButton));
        borrowBooksButton.addActionListener(e -> {
            loadBooks();
            showLayout(borrowBookPanel, borrowBooksButton);
        });
        borrowedBooksButton.addActionListener(e -> {
            loadBorrowedBooks();
            showLayout(borrowedBooksPanel, borrowedBooksButton);
        });

        sidebarPanel.add(personalInfoButton);
        sidebarPanel.add(borrowBooksButton);
        sidebarPanel.add(borrowedBooksButton);

        // Main panel
        mainPanel = new JPanel(new BorderLayout());

        // Personal info panel
        personalInfoPanel = new JPanel(new BorderLayout());
        JPanel infoPanel = new JPanel(new GridBagLayout());
        JPanel buttonPanel_personal = new JPanel(new GridLayout(1, 3));

        idLabel = new JLabel("ID:");
        nameLabel = new JLabel("Tên:");
        ageLabel = new JLabel("Tuổi:");
        phoneNumberLabel = new JLabel("Số điện thoại:");
        usernameLabel = new JLabel("Tên người dùng:");
        idTextField = new JTextField(String.valueOf(customer.getId()));
        idTextField.setEditable(false);
        nameTextField = new JTextField(customer.getName());
        nameTextField.setEditable(false);
        ageTextField = new JTextField(String.valueOf(customer.getAge()));
        ageTextField.setEditable(false);
        phoneNumberTextField = new JTextField(customer.getPhoneNumber());
        phoneNumberTextField.setEditable(false);
        usernameTextField = new JTextField(customer.getUsername());
        usernameTextField.setEditable(false);
        updateInfoButton = new JButton("Cập nhật thông tin");
        changePasswordButton = new JButton("Thay đổi mật khẩu");
        logoutButton = new JButton("Đăng xuất");

        // Đặt kích thước cho các JTextField và JButton
        setComponentSize(idTextField, 200, 30);
        setComponentSize(nameTextField, 200, 30);
        setComponentSize(ageTextField, 200, 30);
        setComponentSize(phoneNumberTextField, 200, 30);
        setComponentSize(usernameTextField, 200, 30);
        setComponentSize(updateInfoButton, 200, 30);
        setComponentSize(changePasswordButton, 200, 30);
        setComponentSize(logoutButton, 200, 30);

        // Tạo margin cho các button
        setMarginForButton(updateInfoButton, 10);
        setMarginForButton(changePasswordButton, 10);
        setMarginForButton(logoutButton, 10);

        // Thêm các thành phần vào infoPanel sử dụng GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        infoPanel.add(idLabel, gbc);

        gbc.gridx = 1;
        infoPanel.add(idTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        infoPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        infoPanel.add(nameTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        infoPanel.add(ageLabel, gbc);

        gbc.gridx = 1;
        infoPanel.add(ageTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        infoPanel.add(phoneNumberLabel, gbc);

        gbc.gridx = 1;
        infoPanel.add(phoneNumberTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        infoPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        infoPanel.add(usernameTextField, gbc);

        // Thêm các button vào buttonPanel
        buttonPanel_personal.add(updateInfoButton);
        buttonPanel_personal.add(changePasswordButton);
        buttonPanel_personal.add(logoutButton);

        // Thêm infoPanel vào personalInfoPanel
        personalInfoPanel.add(new JLabel(), BorderLayout.NORTH);
        personalInfoPanel.add(infoPanel, BorderLayout.CENTER);
        personalInfoPanel.add(buttonPanel_personal, BorderLayout.SOUTH);

        // Borrow book panel
        borrowBookPanel = new JPanel(new BorderLayout());
        bookTable = new JTable();
        JScrollPane bookScrollPane = new JScrollPane(bookTable);
        borrowButton = new JButton("Mượn sách");

        borrowButton.addActionListener(e -> {
            try {
                borrowBook();
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(borrowButton);

        borrowBookPanel.add(bookScrollPane, BorderLayout.CENTER);
        borrowBookPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Borrowed books panel
        borrowedBooksPanel = new JPanel(new BorderLayout());
        borrowedBookTable = new JTable();
        JScrollPane borrowedBookScrollPane = new JScrollPane(borrowedBookTable);
        returnButton = new JButton("Trả sách");

        returnButton.addActionListener(e -> {
            try {
                returnBook();
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
            }
        });

        updateInfoButton.addActionListener(e -> {
            // Hiển thị giao diện cập nhật thông tin
            JPanel updatePanel = new JPanel(new GridBagLayout());
            JTextField newNameField = new JTextField(customer.getName(), 20);
            JTextField newAgeField = new JTextField(String.valueOf(customer.getAge()), 20);
            JTextField newPhoneNumberField = new JTextField(customer.getPhoneNumber(), 20);

            GridBagConstraints gbcUpdate = new GridBagConstraints();
            gbcUpdate.insets = new Insets(5, 5, 5, 5);
            gbcUpdate.gridx = 0;
            gbcUpdate.gridy = 0;
            updatePanel.add(new JLabel("Tên mới:"), gbcUpdate);

            gbcUpdate.gridx = 1;
            updatePanel.add(newNameField, gbcUpdate);

            gbcUpdate.gridx = 0;
            gbcUpdate.gridy = 1;
            updatePanel.add(new JLabel("Tuổi mới:"), gbcUpdate);

            gbcUpdate.gridx = 1;
            updatePanel.add(newAgeField, gbcUpdate);

            gbcUpdate.gridx = 0;
            gbcUpdate.gridy = 2;
            updatePanel.add(new JLabel("Số điện thoại mới:"), gbcUpdate);

            gbcUpdate.gridx = 1;
            updatePanel.add(newPhoneNumberField, gbcUpdate);

            int result = JOptionPane.showConfirmDialog(null, updatePanel, "Cập nhật thông tin", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                String newName = newNameField.getText().trim();
                String newAgeStr = newAgeField.getText().trim();
                String newPhoneNumber = newPhoneNumberField.getText().trim();

                if (newName.isEmpty() || newAgeStr.isEmpty() || newPhoneNumber.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin.", "Thiếu thông tin",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try {
                    int newAge = Integer.parseInt(newAgeStr);
                    updateCustomerInfo(customer.getId(), newName, newAge, newPhoneNumber);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Tuổi phải là một số nguyên.", "Lỗi định dạng",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        changePasswordButton.addActionListener(e -> {
            JPanel changePasswordPanel = new JPanel(new GridLayout(2, 2));
            JPasswordField currentPasswordField = new JPasswordField();
            JPasswordField newPasswordField = new JPasswordField();

            changePasswordPanel.add(new JLabel("Mật khẩu hiện tại:"));
            changePasswordPanel.add(currentPasswordField);
            changePasswordPanel.add(new JLabel("Mật khẩu mới:"));
            changePasswordPanel.add(newPasswordField);

            int result = JOptionPane.showConfirmDialog(null, changePasswordPanel, "Thay đổi mật khẩu", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                String currentPassword = new String(currentPasswordField.getPassword());
                String newPassword = new String(newPasswordField.getPassword());

                if (newPassword.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Mật khẩu mới không được để trống.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    String response = client.changePassword(customer.getId(), currentPassword, newPassword);
                    JOptionPane.showMessageDialog(this, response, "Kết quả thay đổi mật khẩu", JOptionPane.INFORMATION_MESSAGE);
                    // Cập nhật lại các dữ liệu khi thay đổi mật khẩu thành công
                    if ("Password changed successfully.".equals(response)) {
                        // Cập nhật lại dữ liệu ở personalInfoPanel
                        nameTextField.setText(customer.getName());
                        ageTextField.setText(String.valueOf(customer.getAge()));
                        phoneNumberTextField.setText(customer.getPhoneNumber());
                        // Load lại sách mượn nếu có thay đổi
                        loadBorrowedBooks();
                    }
                } catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Lỗi khi thay đổi mật khẩu: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        logoutButton.addActionListener(e -> {
            dispose(); // Đóng cửa sổ hiện tại
            new LoginView().setVisible(true); // Mở lại màn hình đăng nhập
        });

        JPanel returnButtonPanel = new JPanel();
        returnButtonPanel.add(returnButton);

        borrowedBooksPanel.add(borrowedBookScrollPane, BorderLayout.CENTER);
        borrowedBooksPanel.add(returnButtonPanel, BorderLayout.SOUTH);

        // Add components to main frame
        mainPanel.add(personalInfoPanel, BorderLayout.CENTER);

        // Default to personal info layout
        showLayout(personalInfoPanel, personalInfoButton);

        add(sidebarPanel, BorderLayout.WEST);
        add(mainPanel, BorderLayout.CENTER);
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

    private JButton createSidebarButton(String text) {
        JButton button = new JButton(text);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(false);
        button.setBackground(null);
        button.setPreferredSize(new Dimension(200, 50));
        return button;
    }

    private void showLayout(JPanel panel, JButton button) {
        mainPanel.removeAll();
        mainPanel.add(panel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();

        personalInfoButton.setBackground(null);
        borrowBooksButton.setBackground(null);
        borrowedBooksButton.setBackground(null);

        button.setBackground(Color.GRAY);
    }

    private void loadBooks() {
        try {
            List<Book> books = client.getAllBooks();
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("ID");
            model.addColumn("Tên");
            model.addColumn("Tác giả");
            model.addColumn("Số lượng");

            for (Book book : books) {
                model.addRow(new Object[]{book.getId(), book.getTitle(), book.getAuthors(), book.getQuantity()});
            }

            bookTable.setModel(model);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void loadBorrowedBooks() {
        try {
            List<BorrowedBookInfo> borrowedBooks = client.getBorrowedBooks(customer.getId());
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("ID sách");
            model.addColumn("Tên");
            model.addColumn("Ngày mượn");

            for (BorrowedBookInfo borrowedBook : borrowedBooks) {
                model.addRow(new Object[]{borrowedBook.getBookId(), borrowedBook.getTitle(), borrowedBook.getBorrowDate()});
            }

            borrowedBookTable.setModel(model);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void borrowBook() throws ClassNotFoundException {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một cuốn sách để mượn.", "Không có sách được chọn",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int bookId = (int) bookTable.getValueAt(selectedRow, 0);
        String title = (String) bookTable.getValueAt(selectedRow, 1);
        String authors = (String) bookTable.getValueAt(selectedRow, 2);

        try {
            String response = client.borrowBook(customer.getId(), bookId, title, authors);
            JOptionPane.showMessageDialog(this, response, "Kết quả mượn sách", JOptionPane.INFORMATION_MESSAGE);
            loadBooks();
            loadBorrowedBooks();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void returnBook() throws ClassNotFoundException {
        int selectedRow = borrowedBookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một cuốn sách để trả.", "Không có sách được chọn",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int bookId = (int) borrowedBookTable.getValueAt(selectedRow, 0);

        try {
            String response = client.returnBook(customer.getId(), bookId);
            JOptionPane.showMessageDialog(this, response, "Kết quả trả sách", JOptionPane.INFORMATION_MESSAGE);
            loadBooks();
            loadBorrowedBooks();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateCustomerInfo(int customerId, String newName, int newAge, String newPhoneNumber) {
        try {
            String response = client.updateCustomer(customerId, newName, newAge, newPhoneNumber);
            JOptionPane.showMessageDialog(this, response, "Kết quả cập nhật thông tin", JOptionPane.INFORMATION_MESSAGE);

            // Cập nhật thông tin hiển thị trên giao diện
            if (response.equals("Update successful.")) {
                nameTextField.setText(newName);
                ageTextField.setText(String.valueOf(newAge));
                phoneNumberTextField.setText(newPhoneNumber);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật thông tin: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}
