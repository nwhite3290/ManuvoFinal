
/*
 * LoginPage.java
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * LoginPage class <br>
 * used to log into the system
 * - creates a login window
 * - uses hashmap to store the longin information
 * - displays the Welcome Window or gameScreen on successful login
 */
public class LoginPage extends JFrame implements ActionListener {
	Application application;
	JButton loginButton = new JButton("Login");
	JButton resetButton = new JButton("Reset");
	JTextField userIDField = new JTextField();
	JPasswordField userPasswordField = new JPasswordField();
	JLabel userIDLabel = new JLabel("User ID: ");
	JLabel userPasswordLabel = new JLabel("Password: ");
	JLabel messageLabel = new JLabel();
	// globally available copy of hashmap
	HashMap<String, String> loginInfo = new HashMap<String, String>();

	/**
	 * LoginPage(Hashmap) <br>
	 * uses the user information to login
	 * @param loginInfoOriginal (String,String) original user information from IDandPasswords
	 */
	public LoginPage(HashMap<String, String> loginInfoOriginal) {
		setSize(Config.WIDTH, Config.HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);
		setBackground(Config.BACKGROUND_COLOR);
		// copy of hashmap passed in from caller
		loginInfo = loginInfoOriginal;
		// user ID and password labels
		userIDLabel.setBounds(50, 200, 75, 25);
		userPasswordLabel.setBounds(50, 300, 75, 25);
		// user ID and password entry fields
		userIDField.setBounds(125, 200, 200, 25);
		userPasswordField.setBounds(125, 300, 200, 25);
		// login button
		loginButton.setBounds(125, 400, 100, 25);
		loginButton.setFocusable(false);
		loginButton.addActionListener(this);
		resetButton.setBounds(225, 400, 100, 25);
		resetButton.setFocusable(false);
		resetButton.addActionListener(this);
		// print message to screen
		messageLabel.setBounds(125, 500, 250, 35);
		messageLabel.setFont(new Font(null, Font.ITALIC, 25));
		// add all labels to the screen
		add(userIDLabel);
		add(userPasswordLabel);
		add(messageLabel);
		add(userIDField);
		add(userPasswordField);
		add(loginButton);
		add(resetButton);
		setSize(Config.WIDTH, Config.HEIGHT);
		setLayout(null);
		setVisible(true);
	}

	/**
	 * actionPerformed(e) <br>
	 * Invoked when an action occurs.
	 * @param e the event to be processed
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == resetButton) {
			userIDField.setText("");
			userPasswordField.setText("");
		}
		if (e.getSource() == loginButton) {
			String userID = userIDField.getText();
			String userPassword = String.valueOf(userPasswordField.getPassword());
			if(loginInfo.containsKey(userID)) {
				if (loginInfo.get(userID).equals(userPassword)) {
					messageLabel.setForeground(Color.GREEN);
					messageLabel.setText("Login successful!");
					dispose();
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							application = new Application();
						}
					});
					//WelcomePage welcomePage = new WelcomePage(userID);
				} else {
					messageLabel.setForeground(Color.RED);
					messageLabel.setText("Wrong password!");
				}
			} else {
				messageLabel.setForeground(Color.RED);
				messageLabel.setText("Username not found.");
			}
		}
	}
}
