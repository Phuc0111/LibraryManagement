package view;

import model.RegistrationDAO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationView extends JFrame {
    private JTextField nameField;
    private JTextField ageField;
    private JTextField phoneNumberField;
    private JTextField accountIdField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton registerButton;
    private JPanel mainPanel;
    private JPanel p1;
    private JPanel p2;
    private JPanel p3;
    private RegistrationDAO registrationDAO;

    public RegistrationView() {
        registrationDAO = new RegistrationDAO();
        setTitle("Registration");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainPanel = new JPanel(new BorderLayout());

        JLabel title = new JLabel("Registration", SwingConstants.CENTER);
        JLabel nameLabel = new JLabel("Name:");
        JLabel ageLabel = new JLabel("Age:");
        JLabel phoneNumberLabel = new JLabel("Phone Number:");
        JLabel accountIdLabel = new JLabel("Account ID:");
        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");

        nameField = new JTextField();
        nameField.setPreferredSize(new Dimension(100, 20));
        ageField = new JTextField();
        ageField.setPreferredSize(new Dimension(100, 20));
        phoneNumberField = new JTextField();
        phoneNumberField.setPreferredSize(new Dimension(100, 20));
        accountIdField = new JTextField();
        accountIdField.setPreferredSize(new Dimension(100, 20));
        usernameField = new JTextField();
        usernameField.setPreferredSize(new Dimension(100, 20));
        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(100, 20));

        registerButton = new JButton("Register");
        registerButton.addActionListener(new RegisterButtonListener());

        p1 = new JPanel();
        p2 = new JPanel(new GridBagLayout());
        p3 = new JPanel();

        p1.add(title);

        p3.add(registerButton);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        p2.add(nameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        p2.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        p2.add(ageLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        p2.add(ageField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        p2.add(phoneNumberLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        p2.add(phoneNumberField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        p2.add(accountIdLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        p2.add(accountIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        p2.add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        p2.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        p2.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        p2.add(passwordField, gbc);

        mainPanel.add(p1, BorderLayout.NORTH);
        mainPanel.add(p2, BorderLayout.CENTER);
        mainPanel.add(p3, BorderLayout.SOUTH);
        add(mainPanel);
    }

    private class RegisterButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = nameField.getText();
            int age = Integer.parseInt(ageField.getText());
            String phoneNumber = phoneNumberField.getText();
            int accountId = Integer.parseInt(accountIdField.getText());
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            // Validate name
            if (!name.matches("[a-zA-Z ]+")) {
                JOptionPane.showMessageDialog(RegistrationView.this, "Tên không được chứa số hoặc ký tự đặc biệt.",
                        "Registration Failed", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validate age
            try {
                age = Integer.parseInt(ageField.getText());
                if (age < 0) {
                    JOptionPane.showMessageDialog(RegistrationView.this, "Tuổi phải là số không âm.",
                            "Registration Failed", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(RegistrationView.this, "Tuổi phải là một số nguyên.",
                        "Registration Failed", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validate phone number
            Pattern phonePattern = Pattern.compile("^0[0-9]{9}$");
            Matcher matcher = phonePattern.matcher(phoneNumber);
            if (!matcher.matches()) {
                JOptionPane.showMessageDialog(RegistrationView.this, "Số điện thoại không hợp lệ.",
                        "Registration Failed", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validate accountId
            try {
                accountId = Integer.parseInt(accountIdField.getText());
                if (registrationDAO.isAccountIdExist(accountId)) {
                    JOptionPane.showMessageDialog(RegistrationView.this, "Account ID đã tồn tại.",
                            "Registration Failed", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(RegistrationView.this, "Account ID phải là một số nguyên.",
                        "Registration Failed", JOptionPane.ERROR_MESSAGE);
                return;
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(RegistrationView.this, "Lỗi cơ sở dữ liệu.",
                        "Registration Failed", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validate username
            if (!username.matches("^[A-Z0-9][A-Za-z0-9]*$")) {
                JOptionPane.showMessageDialog(RegistrationView.this, "Username phải bắt đầu bằng chữ cái in hoa hoặc số, và chỉ chứa chữ cái và số.",
                        "Registration Failed", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validate password
            if (!password.matches("^[A-Z0-9][A-Za-z0-9]*$")) {
                JOptionPane.showMessageDialog(RegistrationView.this, "Password phải bắt đầu bằng chữ cái in hoa hoặc số, và chỉ chứa chữ cái và số.",
                        "Registration Failed", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                // Check if username exists
                if (registrationDAO.isUsernameExist(username)) {
                    JOptionPane.showMessageDialog(RegistrationView.this, "Username đã tồn tại.",
                            "Registration Failed", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Hash the password using SHA-256
                String hashedPassword = hashPassword(password);
                System.out.println("Hashed Password: " + hashedPassword);

                // Register new user with hashed password
                boolean isRegistered = registrationDAO.registerUser(name, age, phoneNumber, accountId, username, hashedPassword);
                if (isRegistered) {
                    JOptionPane.showMessageDialog(RegistrationView.this, "Đăng ký thành công.",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    LoginView();
                    dispose(); // Close registration window after successful registration
                } else {
                    JOptionPane.showMessageDialog(RegistrationView.this, "Đăng ký thất bại.",
                            "Registration Failed", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(RegistrationView.this, "Lỗi cơ sở dữ liệu.",
                        "Registration Failed", JOptionPane.ERROR_MESSAGE);
            } catch (NoSuchAlgorithmException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(RegistrationView.this, "Lỗi mã hóa.",
                        "Registration Failed", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void LoginView() {
            // TODO Auto-generated method stub
            LoginView a = new LoginView();
            a.setVisible(true);
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
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RegistrationView registrationView = new RegistrationView();
            registrationView.setVisible(true);
        });
    }
}
