package view;

import model.RegistrationDAO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
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

            // Validate age
            if (age < 0) {
                JOptionPane.showMessageDialog(RegistrationView.this, "Age must be a non-negative number.",
                        "Registration Failed", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validate phone number
            Pattern phonePattern = Pattern.compile("(84|0[3|5|7|8|9])+([0-9]{8})\\b");
            Matcher matcher = phonePattern.matcher(phoneNumber);
            if (!matcher.matches()) {
                JOptionPane.showMessageDialog(RegistrationView.this, "Invalid phone number.",
                        "Registration Failed", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validate password
            if (!password.matches("^(?=.*[A-Z0-9])[A-Za-z0-9]+$")) {
                JOptionPane.showMessageDialog(RegistrationView.this, "Invalid password. Password must start with an uppercase letter or a number, and can only contain letters and numbers.",
                        "Registration Failed", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                // Check if username exists
                if (registrationDAO.isUsernameExist(username)) {
                    JOptionPane.showMessageDialog(RegistrationView.this, "Username already exists.",
                            "Registration Failed", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Register new user
                boolean isRegistered = registrationDAO.registerUser(name, age, phoneNumber, accountId, username, password);
                if (isRegistered) {
                    JOptionPane.showMessageDialog(RegistrationView.this, "Registration successful.",
                            "Success", JOptionPane.INFORMATION_MESSAGE);

                    dispose(); // Close registration window after successful registration
                } else {
                    JOptionPane.showMessageDialog(RegistrationView.this, "Registration failed.",
                            "Registration Failed", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(RegistrationView.this, "Database error.",
                        "Registration Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RegistrationView registrationView = new RegistrationView();
            registrationView.setVisible(true);
        });
    }
}
