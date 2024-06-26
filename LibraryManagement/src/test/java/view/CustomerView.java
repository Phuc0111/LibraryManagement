package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import controller.LibraryClient;
import model.*;
//import view.LoginView.ButtonHoverListener;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class CustomerView extends JFrame {
    private JTable bookTable;
    private JTable borrowedBookTable;
    private JPanel personalInfoPanel;
    private JPanel borrowBookPanel;
    private JPanel borrowedBooksPanel;
    private JButton borrowButton;
    private JButton returnButton;
    
    private JButton MainButton;
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
    
    private IconButton minimizeButton;
    private IconButton restoreDownButton;
    private IconButton closeButton;

    public CustomerView(Customer customer) {
        this.customer = customer;
        client = new LibraryClient("localhost", 12345);

        setTitle("Customer Panel");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Sidebar
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setBackground(Color.WHITE);
        sidebarPanel.setPreferredSize(new Dimension(200, getHeight()));
        sidebarPanel.setLayout(new GridLayout(4, 1));
        
        MainButton = createSidebarButton("Library BP", new ImageIcon(getClass().getResource("/logo-library.png")), 30, 30);
        personalInfoButton = createSidebarButton("Thông tin cá nhân",  new ImageIcon(getClass().getResource("/User.png")), 30, 30);
        borrowBooksButton = createSidebarButton("Mượn sách",  new ImageIcon(getClass().getResource("/Shopping Bag.png")), 30, 30);
        borrowedBooksButton = createSidebarButton("Sách đã mượn",  new ImageIcon(getClass().getResource("/Book.png")), 30, 30);

        personalInfoButton.addActionListener(e -> showLayout(personalInfoPanel, personalInfoButton));
        borrowBooksButton.addActionListener(e -> {
            loadBooks();
            showLayout(borrowBookPanel, borrowBooksButton);
        });
        borrowedBooksButton.addActionListener(e -> {
            loadBorrowedBooks();
            showLayout(borrowedBooksPanel, borrowedBooksButton);
        });
        MainButton.setEnabled(false);
        sidebarPanel.add(MainButton); // Thêm nút chính vào sidebar
        sidebarPanel.add(personalInfoButton);
        sidebarPanel.add(borrowBooksButton);
        sidebarPanel.add(borrowedBooksButton);

        // Main panel
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setOpaque(false);
        setUndecorated(true); // Tắt viền cửa sổ mặc định
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));


        // Title panel với các nút minimize, restore down và close
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        titlePanel.setOpaque(false); // Đảm bảo tính trong suốt
        titlePanel.setBackground(new Color(255, 255, 255, 0));
        titlePanel.setPreferredSize(new Dimension(800, 20));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));

        int buttonWidth = 15; // Kích thước của nút minimize, restore down, close
        int buttonHeight = 15;

        minimizeButton = createIconButton("/minimize.png", "/minimize_hover.png", buttonWidth, buttonHeight);
        minimizeButton.addActionListener(e -> setExtendedState(JFrame.ICONIFIED));
        titlePanel.add(minimizeButton);

        restoreDownButton = createIconButton("/restore_down.png", "/restore_down_hover.png", buttonWidth, buttonHeight);
        restoreDownButton.addActionListener(e -> {
            if ((getExtendedState() & JFrame.MAXIMIZED_BOTH) == 0) {
                setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
            } else {
                setExtendedState(JFrame.NORMAL);
            }
        });
        titlePanel.add(restoreDownButton);

        closeButton = createIconButton("/close.png", "/close_hover.png", buttonWidth, buttonHeight);
        closeButton.addActionListener(e -> System.exit(0));
        titlePanel.add(closeButton);

        add(titlePanel, BorderLayout.NORTH);
        

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

     // Phương thức xử lý sự kiện cho button cập nhật
        updateInfoButton.addActionListener(e -> showUpdateDialog());

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

                if (!isValidPassword(newPassword)) {
                    JOptionPane.showMessageDialog(this, "Mật khẩu mới phải bắt đầu bằng chữ cái in hoa hoặc số và không chứa ký tự đặc biệt.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    // Mã hóa mật khẩu hiện tại và mật khẩu mới
                    String hashedCurrentPassword = hashPassword(currentPassword);
                    String hashedNewPassword = hashPassword(newPassword);

                    // Gửi yêu cầu thay đổi mật khẩu về server
                    String response = client.changePassword(customer.getId(), hashedCurrentPassword, hashedNewPassword);
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
                } catch (IOException | NoSuchAlgorithmException | ClassNotFoundException ex) {
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
        
        //Custome Button
        CustomeButton(updateInfoButton, 200, 30,Color.BLACK ,Color.WHITE);
        CustomeButton(changePasswordButton, 200, 30,Color.BLACK ,Color.WHITE);
        CustomeButton(logoutButton, 200, 30,Color.BLACK ,Color.WHITE);
        CustomeButton(returnButton, 200, 30,Color.BLACK ,Color.WHITE);
        CustomeButton(borrowButton, 200, 30,Color.BLACK ,Color.WHITE);

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

    private JButton createSidebarButton(String text, ImageIcon icon, int desiredWidth, int desiredHeight) {
        JButton button = new JButton();
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(false);
        button.setBackground(null);
        button.setPreferredSize(new Dimension(200, 30));
        
        ImageIcon resizedIcon = resizeImageIcon(icon, desiredWidth, desiredHeight);
        
        // Tạo một label để chứa icon và text
        JLabel label = new JLabel(text, resizedIcon, JLabel.CENTER);
        label.setHorizontalTextPosition(JLabel.RIGHT); // Đặt vị trí của text
        
        // Đặt label làm nội dung của button
        button.add(label);
        
        return button;
    }
    
    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(password.getBytes());
        byte[] byteData = md.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : byteData) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    
    public void CustomeButton (JButton button, int width, int height, Color colorBgc, Color Foreground) {
    	button.setPreferredSize(new Dimension(width, height));
    	button.setBackground(colorBgc);
    	button.setForeground(Foreground);
    	button.setBorder(new RoundBorder(10, Color.BLACK)); // Áp dụng RoundBorder
    	button.setBorderPainted(false); // Ẩn viền của nút
    	button.setFocusPainted(false); // Ẩn hiệu ứng khi focus
    	button.addMouseListener(new ButtonHoverListener(button, Color.decode("#C66A5E"), Color.BLACK));
    }


    private void showLayout(JPanel panel, JButton button) {
        mainPanel.removeAll();
        mainPanel.add(panel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();

        personalInfoButton.setBackground(null);
        borrowBooksButton.setBackground(null);
        borrowedBooksButton.setBackground(null);

        button.setBackground(Color.WHITE);
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
    
    private IconButton createIconButton(String iconPath, String hoverIconPath, int width, int height) {
        ImageIcon icon = createResizedIcon(iconPath, width, height);
        ImageIcon hoverIcon = createResizedIcon(hoverIconPath, width, height);
        return new IconButton(icon, hoverIcon);
    }
    
 // Hàm tạo thẻ với hình ảnh ở bên trái và tiêu đề ở bên phải
    private JPanel createCard(String imagePath, String title, int imageWidth, int imageHeight) {
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BorderLayout());

        // Panel bên trái để chứa hình ảnh
        JPanel leftPanel = new JPanel();
        leftPanel.setPreferredSize(new Dimension(imageWidth, imageHeight)); // Kích thước của hình ảnh
        leftPanel.setBackground(Color.WHITE); // Màu nền của panel chứa hình ảnh

        // Tạo và thêm hình ảnh vào leftPanel
        ImageIcon imgIcon = createResizedIcon(imagePath, imageWidth, imageHeight);
        JLabel imgLabel = new JLabel(imgIcon);
        leftPanel.add(imgLabel);

        // Panel bên phải để chứa tiêu đề
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());

        // Tạo và thêm tiêu đề vào rightPanel
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16)); // Font chữ cho tiêu đề
        titleLabel.setHorizontalAlignment(JLabel.CENTER); // Căn giữa tiêu đề
        rightPanel.add(titleLabel, BorderLayout.CENTER);

        // Thêm leftPanel và rightPanel vào cardPanel
        cardPanel.add(leftPanel, BorderLayout.WEST);
        cardPanel.add(rightPanel, BorderLayout.CENTER);

        return cardPanel;
    }

    private ImageIcon createResizedIcon(String path, int width, int height) {
        ImageIcon icon = new ImageIcon(getClass().getResource(path));
        Image img = icon.getImage();
        Image resizedImage = img.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }
    
    private ImageIcon resizeImageIcon(ImageIcon icon, int width, int height) {
        Image img = icon.getImage();
        Image resizedImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }

 // Phương thức hiển thị giao diện cập nhật và xử lý ngoại lệ
    private void showUpdateDialog() {
        // Tạo JDialog
        JDialog updateDialog = new JDialog(this, "Cập nhật thông tin", true); // true để làm cho JDialog modal

        // Panel chứa các thành phần nhập liệu
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

        // Nút cập nhật
        JButton updateButton = new JButton("Cập nhật");
        updateButton.addActionListener(e -> {
            String newName = newNameField.getText().trim();
            String newAgeStr = newAgeField.getText().trim();
            String newPhoneNumber = newPhoneNumberField.getText().trim();

            if (newName.isEmpty() || newAgeStr.isEmpty() || newPhoneNumber.isEmpty()) {
                JOptionPane.showMessageDialog(updateDialog, "Vui lòng điền đầy đủ thông tin.", "Thiếu thông tin", JOptionPane.ERROR_MESSAGE);
                return; // Không tiếp tục nếu thiếu thông tin
            }

            try {
                int newAge = Integer.parseInt(newAgeStr);
                updateCustomerInfo(customer.getId(), newName, newAge, newPhoneNumber);
                // Không cần đóng JDialog ở đây, để người dùng quyết định khi nào đóng
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(updateDialog, "Tuổi phải là một số nguyên.", "Lỗi định dạng", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Panel chứa nút cập nhật
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(updateButton);

        // Thêm panel nhập liệu và panel nút vào JDialog
        updateDialog.setLayout(new BorderLayout());
        updateDialog.add(updatePanel, BorderLayout.CENTER);
        updateDialog.add(buttonPanel, BorderLayout.SOUTH);

        // Thiết lập kích thước và hiển thị JDialog
        updateDialog.setSize(400, 200);
        updateDialog.setLocationRelativeTo(this); // Hiển thị JDialog ở giữa cửa sổ chính
        updateDialog.setVisible(true);
    }

    // Phương thức cập nhật thông tin khách hàng
    private void updateCustomerInfo(int customerId, String newName, int newAge, String newPhoneNumber) {
        // Kiểm tra điều kiện nhập liệu và xử lý lỗi nếu có
        if (!isValidName(newName)) {
            JOptionPane.showMessageDialog(this, "Tên không được chứa ký tự đặc biệt hoặc số.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return; // Không tiếp tục xử lý nếu dữ liệu không hợp lệ
        }

        if (!isValidAge(newAge)) {
            JOptionPane.showMessageDialog(this, "Tuổi phải là một số nguyên dương.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return; // Không tiếp tục xử lý nếu dữ liệu không hợp lệ
        }

        if (!isValidPhoneNumber(newPhoneNumber)) {
            JOptionPane.showMessageDialog(this, "Số điện thoại không hợp lệ.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return; // Không tiếp tục xử lý nếu dữ liệu không hợp lệ
        }

        try {
            // Gọi phương thức cập nhật thông tin khách hàng ở đây
            String response = client.updateCustomer(customerId, newName, newAge, newPhoneNumber);
            JOptionPane.showMessageDialog(this, response, "Kết quả cập nhật thông tin", JOptionPane.INFORMATION_MESSAGE);

            // Cập nhật thông tin hiển thị trên giao diện nếu cập nhật thành công
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

    
    private boolean isValidName(String name) {
        // Kiểm tra tên không chứa ký tự đặc biệt hoặc số
        return name.matches("[a-zA-Z\\s]+");
    }

    private boolean isValidAge(int age) {
        // Kiểm tra tuổi là số nguyên dương
        return age > 0;
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        // Kiểm tra số điện thoại hợp lệ
        // Có 10 chữ số và bắt buộc có số 0 đầu hoặc "+084"
        return phoneNumber.matches("(0|\\+084)\\d{9}");
    }
    private boolean isValidPassword(String password) {
    	// Kiểm tra mật khẩu bắt đầu bằng chữ cái in hoa hoặc số và không chứa ký tự đặc biệt
    	return password.matches("^[A-Z0-9][A-Za-z0-9]*$");
    }
   
}
