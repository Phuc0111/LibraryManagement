package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Admin;
import model.ButtonHoverListener;
import model.Customer;
import model.DatabaseConnection;

public class LoginView extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private JPanel mainPanel;
    private JComboBox<String> roleComboBox;

    private IconButton minimizeButton;
    private IconButton restoreDownButton;
    private IconButton closeButton;

    public LoginView() {
        setTitle("Login");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        setShape(new RoundRectangle2D.Double(0, 0, 800, 400, 20, 20));
        setLocationRelativeTo(null);

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setOpaque(false);

        // Title panel với các nút minimize, restore down và close
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
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

        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // Left panel với hình ảnh
        JPanel imagePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                URL url = getClass().getResource("/ảnh background login.jpg");
                ImageIcon icon = new ImageIcon(url);
                Image img = icon.getImage();
                g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
            }
        };
        imagePanel.setPreferredSize(new Dimension(400, 400));
        imagePanel.setBackground(Color.WHITE);

        // Right panel với form đăng nhập
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Login", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        JLabel roleLabel = new JLabel("Role:");

        usernameField = new JTextField();
        usernameField.setPreferredSize(new Dimension(200, 30));
        usernameField.setBorder(new RoundBorder(10, Color.LIGHT_GRAY)); // Bo góc cho JTextField

        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(200, 30));
        passwordField.setBorder(new RoundBorder(10, Color.LIGHT_GRAY)); // Bo góc cho JPasswordField

        roleComboBox = new JComboBox<>(new String[]{"Admin", "User"});
        roleComboBox.setPreferredSize(new Dimension(200, 30));
        roleComboBox.setBorder(new RoundBorder(10, Color.LIGHT_GRAY)); // Bo góc cho JComboBox

        loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(200, 30));
        loginButton.setBackground(Color.BLACK);
        loginButton.setForeground(Color.WHITE);
        loginButton.setBorder(new RoundBorder(10, Color.BLACK)); // Áp dụng RoundBorder
        loginButton.setBorderPainted(false); // Ẩn viền của nút
        loginButton.setFocusPainted(false); // Ẩn hiệu ứng khi focus
        loginButton.addMouseListener(new ButtonHoverListener(loginButton, Color.decode("#C66A5E"), Color.BLACK)); // Thay đổi màu nền khi hover

        registerButton = new JButton("Register");
        registerButton.setPreferredSize(new Dimension(200, 30));
        registerButton.setBackground(Color.BLACK);
        registerButton.setForeground(Color.WHITE);
        registerButton.setBorder(new RoundBorder(10, Color.BLACK)); // Áp dụng RoundBorder
        registerButton.setBorderPainted(false); // Ẩn viền của nút
        registerButton.setFocusPainted(false); // Ẩn hiệu ứng khi focus
        registerButton.addMouseListener(new ButtonHoverListener(registerButton, Color.decode("#C66A5E"), Color.BLACK)); // Thay đổi màu nền khi hover

        loginButton.addActionListener(new LoginButtonListener());
        registerButton.addActionListener(new RegisterButtonListener());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        loginPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        loginPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        loginPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        loginPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        loginPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        loginPanel.add(roleLabel, gbc);

        gbc.gridx = 1;
        loginPanel.add(roleComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        loginPanel.add(loginButton, gbc);

        gbc.gridy = 5;
        loginPanel.add(registerButton, gbc);

        mainPanel.add(imagePanel, BorderLayout.WEST);
        mainPanel.add(loginPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    private IconButton createIconButton(String iconPath, String hoverIconPath, int width, int height) {
        ImageIcon icon = createResizedIcon(iconPath, width, height);
        ImageIcon hoverIcon = createResizedIcon(hoverIconPath, width, height);
        return new IconButton(icon, hoverIcon);
    }

    private ImageIcon createResizedIcon(String path, int width, int height) {
        ImageIcon icon = new ImageIcon(getClass().getResource(path));
        Image img = icon.getImage();
        Image resizedImage = img.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }

    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte b : encodedHash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private class LoginButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String role = (String) roleComboBox.getSelectedItem();

            try {
                String hashedPassword = hashPassword(password); // Mã hóa mật khẩu trước khi so sánh

                System.out.println("Username: " + username);
                System.out.println("Hashed Password: " + hashedPassword);
                System.out.println("Role: " + role);

                try (Connection connection = DatabaseConnection.getConnection()) {
                    String query;
                    if ("Admin".equals(role)) {
                        query = "SELECT * FROM Admins WHERE Username = ? AND Password = ?";
                    } else {
                        query = "SELECT * FROM Customers WHERE Username = ? AND Password = ?";
                    }

                    System.out.println("Query: " + query);

                    try (PreparedStatement statement = connection.prepareStatement(query)) {
                        statement.setString(1, username);
                        statement.setString(2, hashedPassword); // Sử dụng mật khẩu đã mã hóa

                        try (ResultSet resultSet = statement.executeQuery()) {
                            if (resultSet.next()) {
                                System.out.println("Login successful!");

                                if ("Admin".equals(role)) {
                                    int adminId = resultSet.getInt("AdminID");
                                    int accountId = resultSet.getInt("AccountID");
                                    String name = resultSet.getString("Name");
                                    String adminUsername = resultSet.getString("Username");
                                    String adminPassword = resultSet.getString("Password");

                                    Admin admin = new Admin(adminId, accountId, adminUsername, adminPassword, name);
                                    AdminView adminView = new AdminView(admin);
                                    adminView.setVisible(true);
                                } else {
                                    int customerId = resultSet.getInt("CustomerID");
                                    int accountId = resultSet.getInt("AccountID");
                                    String name = resultSet.getString("Name");
                                    int age = resultSet.getInt("Age");
                                    String phoneNumber = resultSet.getString("PhoneNumber");
                                    String customerUsername = resultSet.getString("Username");
                                    String customerPassword = resultSet.getString("Password");

                                    Customer customer = new Customer(customerId, name, age, phoneNumber, accountId,
                                            customerUsername, customerPassword);
                                    CustomerView customerView = new CustomerView(customer);
                                    customerView.setVisible(true);
                                }
                                dispose();
                            } else {
                                System.out.println("Invalid username or password.");
                                JOptionPane.showMessageDialog(LoginView.this, "Invalid username or password.",
                                        "Login Failed", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                }
            } catch (NoSuchAlgorithmException | SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(LoginView.this, "Database error.", "Login Failed",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    public class RegisterButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            RegistrationView registrationView = new RegistrationView();
            registrationView.setVisible(true);
            dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView();
            loginView.setVisible(true);
        });
    }
}
