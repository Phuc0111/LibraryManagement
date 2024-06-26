package view;

import model.ButtonHoverListener;
import model.RegistrationDAO;
//import view.LoginView.ButtonHoverListener;
import view.LoginView.RegisterButtonListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import controller.LibraryClient;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;
import java.net.URL;
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

	private IconButton minimizeButton;
	private IconButton restoreDownButton;
	private IconButton closeButton;

	public RegistrationView() {
		setTitle("Regist");
		setSize(800, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setUndecorated(true);
		setShape(new RoundRectangle2D.Double(0, 0, 800, 400, 20, 20));
		setLocationRelativeTo(null);

		mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		mainPanel.setBackground(Color.WHITE);
		mainPanel.setOpaque(true);

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

		JLabel title = new JLabel("Registration", SwingConstants.CENTER);
		title.setFont(new Font("Arial", Font.BOLD, 24));
		JLabel nameLabel = new JLabel("Name:");
		JLabel ageLabel = new JLabel("Age:");
		JLabel phoneNumberLabel = new JLabel("Phone Number:");
		JLabel accountIdLabel = new JLabel("Account ID:");
		JLabel usernameLabel = new JLabel("Username:");
		JLabel passwordLabel = new JLabel("Password:");

		nameField = new JTextField();
		nameField.setPreferredSize(new Dimension(200, 30));
		nameField.setBorder(new RoundBorder(10, Color.LIGHT_GRAY));

		ageField = new JTextField();
		ageField.setPreferredSize(new Dimension(200, 30));
		ageField.setBorder(new RoundBorder(10, Color.LIGHT_GRAY));

		phoneNumberField = new JTextField();
		phoneNumberField.setPreferredSize(new Dimension(200, 30));
		phoneNumberField.setBorder(new RoundBorder(10, Color.LIGHT_GRAY));

		accountIdField = new JTextField();
		accountIdField.setPreferredSize(new Dimension(200, 30));
		accountIdField.setBorder(new RoundBorder(10, Color.LIGHT_GRAY));

		usernameField = new JTextField();
		usernameField.setPreferredSize(new Dimension(200, 30));
		usernameField.setBorder(new RoundBorder(10, Color.LIGHT_GRAY));

		passwordField = new JPasswordField();
		passwordField.setPreferredSize(new Dimension(200, 30));
		passwordField.setBorder(new RoundBorder(10, Color.LIGHT_GRAY));

		registerButton = new JButton("Register");
		registerButton.setPreferredSize(new Dimension(200, 30));
		registerButton.setBackground(Color.BLACK);
		registerButton.setForeground(Color.WHITE);
		registerButton.setBorder(new RoundBorder(10, Color.BLACK)); // Áp dụng RoundBorder
		registerButton.setBorderPainted(false); // Ẩn viền của nút
		registerButton.setFocusPainted(false); // Ẩn hiệu ứng khi focus
		registerButton.addMouseListener(new ButtonHoverListener(registerButton, Color.decode("#C66A5E"), Color.BLACK)); // Thay
																														// đổi
																														// màu
																														// nền
																														// khi
																														// hover
		registerButton.addActionListener(new RegisterButtonListener());

		p1 = new JPanel();
		p2 = new JPanel(new GridBagLayout());
		p3 = new JPanel();

		// Đặt opaque của các JPanel thành true để chúng vẽ nền màu trắng của mainPanel
		p1.setOpaque(true);
		p2.setOpaque(true);
		p3.setOpaque(true);

		p1.add(title);

		p3.add(registerButton);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		p2.add(title, gbc);

		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 1;
		p2.add(nameLabel, gbc);

		gbc.gridx = 1;
		gbc.gridy = 1;
		p2.add(nameField, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		p2.add(ageLabel, gbc);

		gbc.gridx = 1;
		gbc.gridy = 2;
		p2.add(ageField, gbc);

		gbc.gridx = 0;
		gbc.gridy = 3;
		p2.add(phoneNumberLabel, gbc);

		gbc.gridx = 1;
		gbc.gridy = 3;
		p2.add(phoneNumberField, gbc);

		gbc.gridx = 0;
		gbc.gridy = 4;
		p2.add(accountIdLabel, gbc);

		gbc.gridx = 1;
		gbc.gridy = 4;
		p2.add(accountIdField, gbc);

		gbc.gridx = 0;
		gbc.gridy = 5;
		p2.add(usernameLabel, gbc);

		gbc.gridx = 1;
		gbc.gridy = 5;
		p2.add(usernameField, gbc);

		gbc.gridx = 0;
		gbc.gridy = 6;
		p2.add(passwordLabel, gbc);

		gbc.gridx = 1;
		gbc.gridy = 6;
		p2.add(passwordField, gbc);

		gbc.gridx = 1;
		gbc.gridy = 7;
		gbc.gridwidth = 2;
//        gbc.anchor = GridBagConstraints.CENTER;
		p2.add(registerButton, gbc);

		imagePanel.setOpaque(false);

		mainPanel.add(imagePanel, BorderLayout.WEST);
//        mainPanel.add(p1, BorderLayout.NORTH);
		mainPanel.add(p2, BorderLayout.CENTER);
//        mainPanel.add(p3, BorderLayout.SOUTH);
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
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(RegistrationView.this, "Account ID phải là một số nguyên.",
						"Registration Failed", JOptionPane.ERROR_MESSAGE);
				return;
			}

			// Validate username
			if (!username.matches("^[A-Z0-9][A-Za-z0-9]*$")) {
				JOptionPane.showMessageDialog(RegistrationView.this,
						"Username phải bắt đầu bằng chữ cái in hoa hoặc số, và chỉ chứa chữ cái và số.",
						"Registration Failed", JOptionPane.ERROR_MESSAGE);
				return;
			}

			// Validate password
			if (!password.matches("^[A-Z0-9][A-Za-z0-9]*$")) {
				JOptionPane.showMessageDialog(RegistrationView.this,
						"Password phải bắt đầu bằng chữ cái in hoa hoặc số, và chỉ chứa chữ cái và số.",
						"Registration Failed", JOptionPane.ERROR_MESSAGE);
				return;
			}

			try {
				// Hash the password using SHA-256
				String hashedPassword = hashPassword(password);
				System.out.println("Hashed Password: " + hashedPassword);

				LibraryClient client = new LibraryClient("localhost", 12345);
				String response = client.registerUser(name, age, phoneNumber, accountId, username, hashedPassword);
				System.out.println("Server response: " + response);

				switch (response) {
				case "USERNAME_EXISTS":
					JOptionPane.showMessageDialog(RegistrationView.this, "Username đã tồn tại.", "Registration Failed",
							JOptionPane.ERROR_MESSAGE);
					break;
				case "ACCOUNTID_EXISTS":
					JOptionPane.showMessageDialog(RegistrationView.this, "Account ID đã tồn tại.",
							"Registration Failed", JOptionPane.ERROR_MESSAGE);
					break;
				case "REGISTER_SUCCESS":
					JOptionPane.showMessageDialog(RegistrationView.this, "Đăng ký thành công.", "Success",
							JOptionPane.INFORMATION_MESSAGE);
					LoginView();
					dispose(); // Close registration window after successful registration
					break;
				case "REGISTER_FAILED":
					JOptionPane.showMessageDialog(RegistrationView.this, "Đăng ký thất bại.", "Registration Failed",
							JOptionPane.ERROR_MESSAGE);
					break;
				case "DATABASE_ERROR":
					JOptionPane.showMessageDialog(RegistrationView.this, "Lỗi cơ sở dữ liệu.", "Registration Failed",
							JOptionPane.ERROR_MESSAGE);
					break;
				default:
					JOptionPane.showMessageDialog(RegistrationView.this, "Lỗi không xác định.", "Registration Failed",
							JOptionPane.ERROR_MESSAGE);
					break;
				}
			} catch (IOException | ClassNotFoundException ex) {
				((Throwable) ex).printStackTrace();
				JOptionPane.showMessageDialog(RegistrationView.this, "Lỗi kết nối server.", "Registration Failed",
						JOptionPane.ERROR_MESSAGE);
			} catch (NoSuchAlgorithmException ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(RegistrationView.this, "Lỗi mã hóa.", "Registration Failed",
						JOptionPane.ERROR_MESSAGE);
			}
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

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			RegistrationView registrationView = new RegistrationView();
			registrationView.setVisible(true);
		});
	}
}
