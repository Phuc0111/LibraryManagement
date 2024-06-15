package view;

import javax.swing.*;

import model.Admin;
import model.Customer;
import model.DatabaseConnection;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginView extends JFrame {
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JButton loginButton;
	private JPanel mainPanel;
	private JComboBox<String> roleComboBox;
	private JButton registerButton;
	private JPanel p1, p2, p3;

	public LoginView() {
		 setTitle("Login");
	        setSize(300, 200);
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	        mainPanel = new JPanel(new BorderLayout());

	        JLabel title = new JLabel("Login", SwingConstants.CENTER);
	        JLabel usernameLabel = new JLabel("Username:");
	        JLabel passwordLabel = new JLabel("Password:");
	        JLabel roleLabel = new JLabel("Role:");

	        usernameField = new JTextField();
	        usernameField.setPreferredSize(new Dimension(100, 20));
	        passwordField = new JPasswordField();
	        passwordField.setPreferredSize(new Dimension(100, 20));

	        // Initialize the role combo box with some example roles
	        roleComboBox = new JComboBox<>(new String[]{"Admin", "User"});
	        roleComboBox.setPreferredSize(new Dimension(100, 20));

	        loginButton = new JButton("Login");
	        registerButton = new JButton("Register");

	        loginButton.addActionListener(new LoginButtonListener());
	        registerButton.addActionListener(new RegisterButtonListener());

	        p1 = new JPanel();
	        p2 = new JPanel(new GridBagLayout());
	        p3 = new JPanel();

	        p1.add(title);

	        p3.add(loginButton);
	        p3.add(registerButton);

	        GridBagConstraints gbc = new GridBagConstraints();
	        gbc.insets = new Insets(5, 5, 5, 5);

	        gbc.gridx = 0;
	        gbc.gridy = 0;
	        p2.add(usernameLabel, gbc);

	        gbc.gridx = 1;
	        gbc.gridy = 0;
	        p2.add(usernameField, gbc);

	        gbc.gridx = 0;
	        gbc.gridy = 1;
	        p2.add(passwordLabel, gbc);

	        gbc.gridx = 1;
	        gbc.gridy = 1;
	        p2.add(passwordField, gbc);

	        gbc.gridx = 0;
	        gbc.gridy = 2;
	        p2.add(roleLabel, gbc);

	        gbc.gridx = 1;
	        gbc.gridy = 2;
	        p2.add(roleComboBox, gbc);

	        mainPanel.add(p1, BorderLayout.NORTH);
	        mainPanel.add(p2, BorderLayout.CENTER);
	        mainPanel.add(p3, BorderLayout.SOUTH);
	        add(mainPanel);

	}

	private class LoginButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String username = usernameField.getText();
			String password = new String(passwordField.getPassword());
			String role = (String) roleComboBox.getSelectedItem();

			try (Connection connection = DatabaseConnection.getConnection()) {
				String query;
				if ("Admin".equals(role)) {
					query = "SELECT * FROM Admins WHERE Username = ? AND Password = ?";
				} else {
					query = "SELECT * FROM Customers WHERE Username = ? AND Password = ?";
				}

				try (PreparedStatement statement = connection.prepareStatement(query)) {
					statement.setString(1, username);
					statement.setString(2, password);

					try (ResultSet resultSet = statement.executeQuery()) {
						if (resultSet.next()) {
							if ("Admin".equals(role)) {
								// Lấy thông tin Admin
								int adminId = resultSet.getInt("AdminID");
								int accountId = resultSet.getInt("AccountID");
								String name = resultSet.getString("Name");
								String adminUsername = resultSet.getString("Username");
								String adminPassword = resultSet.getString("Password");

								Admin admin = new Admin(adminId, accountId, adminUsername, adminPassword, name);
								AdminView adminView = new AdminView(admin);
								adminView.setVisible(true);
							} else {
								// Lấy thông tin Customer
								int customerId = resultSet.getInt("CustomerID");
								int accountId = resultSet.getInt("AccountID");
								String name = resultSet.getString("Name");
								int age = resultSet.getInt("Age");
								String phoneNumber = resultSet.getString("PhoneNumber");
								String customerUsername = resultSet.getString("Username");
								String customerPassword = resultSet.getString("Password");

								Customer customer = new Customer(customerId,name, age, phoneNumber, accountId,
										customerUsername, customerPassword);
								CustomerView customerView = new CustomerView(customer);
								customerView.setVisible(true);
							}
							dispose();
						} else {
							JOptionPane.showMessageDialog(LoginView.this, "Invalid username or password.",
									"Login Failed", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(LoginView.this, "Database error.", "Login Failed",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	private class RegisterButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Mở trang đăng ký
            RegistrationView registrationView = new RegistrationView();
            registrationView.setVisible(true);
            dispose();  // Đóng trang login
        }
    }

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			LoginView loginView = new LoginView();
			loginView.setVisible(true);
		});
	}
}
