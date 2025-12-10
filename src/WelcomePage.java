
/*
 * WelcomePage.java
 */

import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * WelcomePage class <br>
 * creates a welcome page to display upon successful login
 */
public class WelcomePage {
	public float alpha = 1.0f;
	JFrame frame = new JFrame("Welcome Page");
	JLabel welcomeLabel = new JLabel("Welcome to Welcome Page");

	/**
	 * welcomePage(userID) <br>
	 * constructor for new welcome page
	 * @param userID (String) ID of currently logged user
	 */
	WelcomePage(String userID) {
		frame.setSize(Config.WIDTH, Config.HEIGHT);
		frame.setLayout(null);
		frame.setBackground(Config.BACKGROUND_COLOR);
		welcomeLabel.setBounds(100, 100, 200, 75);
		welcomeLabel.setFont(new Font(null, Font.PLAIN, 25));
		welcomeLabel.setText("Hello " + userID);
		frame.add(welcomeLabel);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(420,420);
		frame.setLayout(null);
		frame.setVisible(true);
	}
}
