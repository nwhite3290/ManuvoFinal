

/*
 * SettingsPanel.java
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * SettingsPanel class <br>
 * allows player to set selected options for the game
 * - appears when settings button is pressed in the main menu
 * - extends JPanel
 */
@SuppressWarnings("FieldCanBeLocal")
public class SettingsPanel extends JPanel {
	// current song selection
	private int currentSelection = 0;
	// used for fade transitions
	private float alpha = 1.0f;
	// JButton Array List updateFocus() iteration
	ArrayList<JButton> bit = new ArrayList<>();

	// create JButtons for Panel
	public JButton fontButton;
	public JButton backgroundColorButton;
	public JButton accentColorButton;
	public JButton soundButton;
	public JButton difficultyButton;
	public JButton exitButton;
	// message label and corresponding test field
	public JLabel messageLabel = new JLabel();
	public JTextField messageField = new JTextField();
	// theme label
	private JLabel themeLabel = new JLabel();

	/**
	 * SettingsPanel() <br>
	 * constructor
	 * - creates new SettingsPanel object
	 * default parameters:
	 * - size, background color, accent color, font, difficulty,
	 */
	public SettingsPanel() {
		setSize(Config.WIDTH, Config.HEIGHT);
		setLayout(null);
		setBackground(Config.BACKGROUND_COLOR);
		// font size button
		fontButton = ThemeLoader.createRoundButton("FONT", 20);
		fontButton.setActionCommand("FONT");
		fontButton.setBounds(225, 140, 150, 40);
		bit.add(fontButton);
		add(fontButton);
		//setFocusable(true);
		// background color button
		backgroundColorButton = ThemeLoader.createRoundButton("BACKGROUND", 20);
		backgroundColorButton.setActionCommand("BACKGROUND");
		backgroundColorButton.setBounds(175, 215, 250, 40);
		bit.add(backgroundColorButton);
		add(backgroundColorButton);
		//setFocusable(true);
		// theme label under the background button
		themeLabel.setForeground(Color.WHITE);
		themeLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
		themeLabel.setBounds(185, 255, 200, 25);
		add(themeLabel);
		// accent color button
		accentColorButton = ThemeLoader.createRoundButton("ACCENT", 20);
		accentColorButton.setActionCommand("ACCENT");
		accentColorButton.setBounds(200, 290, 200, 40);
		bit.add(accentColorButton);
		add(accentColorButton);
		//setFocusable(true);
		// Sound toggle button
		soundButton = ThemeLoader.createRoundButton("SOUND: ON", 20);
		soundButton.setActionCommand("SOUND");
		soundButton.setBounds(175, 365, 250, 40);
		bit.add(soundButton);
		add(soundButton);
		//setFocusable(true);
		// difficulty button
		difficultyButton = ThemeLoader.createRoundButton("DIFFICULTY", 20);
		difficultyButton.setActionCommand("DIFFICULTY");
		difficultyButton.setBounds(175, 440, 250, 40);
		bit.add(difficultyButton);
		add(difficultyButton);
		//setFocusable(true);
		// exit button
		exitButton = ThemeLoader.createRoundButton("EXIT", 20);
		exitButton.setActionCommand("EXIT");
		exitButton.setBounds(225, 525, 150, 40);
		bit.add(exitButton);
		add(exitButton);
		// message label under the exit button
		messageLabel.setForeground(Color.WHITE);
		messageLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
		messageLabel.setBounds(175, 600, 300, 25);
		add(messageLabel);
		// message field under the messageLabel button
		messageField.setBackground(Config.BACKGROUND_COLOR);
		messageField.setForeground(Color.WHITE);
		messageField.setFont(new Font("SansSerif", Font.PLAIN, 16));
		messageField.setBounds(175, 650, 300, 25);
		//add(messageField);
		//setFocusable(true);
		// update focus
		updateFocus();
		refreshTheme();
	}

	/**
	 * updateFocus() <br>
	 * updates focus on the buttons
	 */
	private void updateFocus() {
		for (JButton b : bit) {
			b.setFocusable(true);
			currentSelection = bit.indexOf(b);
			b.requestFocusInWindow();
			System.out.println("Button is focusable: " + b.isFocusable());
			System.out.println("Current selection: " + currentSelection);
		}
	}

	/**
	 * setTitleFontSize(size) <br>
	 * allows the user to set the TITLE_FONT size
	 * - sets the tile font size
	 * @param size (int) desired size of the font
	 */
	static void setTitleFontSize(int size) {
		Config.TITLE_FONT = new Font("SansSerif", Font.BOLD, size);
	}

	/**
	 * setTitleFontName(name) <br>
	 * allows the user to set the TITLE_FONT name
	 * - sets the tile font name
	 * @param name (String) desired name of the font
	 */
	static void setTitleFontName(String name) {
		Config.TITLE_FONT = new Font(Config.TITLE_FONT.getFontName(), Font.BOLD, Config.TITLE_FONT.getSize());
	}

	/**
	 * getBackgroundColor() <br>
	 * returns the background color name
	 * @return (String) BACKGROUND_COLOR
	 */
	static String getBackgroundColor() {
		return Config.BACKGROUND_COLOR.toString();
	}

	/**
	 * setBackgroundColor(Color) <br>
	 * allows the user to set the background color
	 * @param Color BACKGROUND_COLOR
	 */
	static void setBackgroundColor(Color Color) {
		Config.BACKGROUND_COLOR = Color;
	}

	/**
	 * getHitZoneColor() <br>
	 * returns the default hit zone color name
	 * @return (String) HIT_ZONE_COLOR
	 */
	static String getHitZoneColor() {
		return Config.HIT_ZONE_COLOR.toString();
	}

	/**
	 * setAccentColor(Color) <br>
	 * allows the user to set the accent color
	 * @param Color color to set as the accent color
	 */
	static void setAccentColor(Color Color) {
		Config.ACCENT_COLOR = Color;
	}

	/**
	 * setDifficulty(String) <br>
	 * allows the user to set the alternate difficulty
	 * @param difficulty string to set as the difficulty
	 */
	static void setDifficulty(String difficulty) {
		switch (difficulty) {
			case "VERY EASY" -> Config.DIFFICULTY = 0;
			case "EASY" -> Config.DIFFICULTY = 1;
			case "MEDIUM" -> Config.DIFFICULTY = 2;
			case "HARD" -> Config.DIFFICULTY = 3;
			case "VERY HARD" -> Config.DIFFICULTY = 4;
			default -> Config.DIFFICULTY = 2;
		}

	}

	/**
	 * setAlpha(float alpha) <br>
	 * sets the alpha value
	 * - used for fade transitions
	 * @param alpha float value to set
	 */
	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}

	/**
	 * getAlpha() <br>
	 * gets the alpha value
	 * - used for fade transitions
	 * @return float alpha value
	 */
	public float getAlpha() {
		return alpha;
	}

	/**
	 * refreshTheme() <br>
	 * change the theme
	 * - updates SettingsPanel look when theme changes
	 */
	public void refreshTheme() {
		Color bg = Config.getGameBackgroundColor();
		setBackground(bg);
		String text = (Config.THEME_INDEX == 0)
				? "Theme: 1 (Default)"
				: "Theme: 2 (Alt)";
		themeLabel.setText(text);
		repaint();
	}
}




