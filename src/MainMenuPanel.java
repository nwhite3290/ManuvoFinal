
/*
 * MainMenuPanel.java
 */

import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * MainMenuPanel class: UI <br>
 * represents the main menu screen of the game.
 *  - shows the game title and a buttons to access different panels
 *  - styled with dark theme and gold accent.
 *  - extends JPanel
 */
public class MainMenuPanel extends JPanel {
	public float alpha = 1.0f;
    public JButton playButton;  // "PLAY" button to start the game
	public JButton settingsButton;
	public JButton statisticsButton;
	public JButton loginButton;

    /**
     * MainMenuPanel() <br>
     * constructor
     * - creates a new MainMenuPanel object
     * default parameters:
     * - layout null
     * - background by ThemeLoader
     * - focused buttons
     */
    public MainMenuPanel() {
	    setSize(Config.WIDTH, Config.HEIGHT);
		setLayout(null);
	    setBackground(Config.BACKGROUND_COLOR);
		// create and style the "PLAY" button
        playButton = ThemeLoader.createRoundButton("PLAY", 40);
	    //playButton.addActionListener((ActionListener) this);
		playButton.setActionCommand("PLAY");
        playButton.setBounds(200, 200, 200, 60);
        add(playButton);
		setFocusable(true);
		// create and style the "SETTINGS" button
	    settingsButton = ThemeLoader.createRoundButton("SETTINGS", 40);
	    //settingsButton.addActionListener((ActionListener) this);
		settingsButton.setActionCommand("SETTINGS");
	    settingsButton.setBounds(175, 300, 250, 60);
	    add(settingsButton);
	    setFocusable(true);
		// create and style the "STATISTICS" button
	    statisticsButton = ThemeLoader.createRoundButton("STATISTICS", 40);
	    //statisticsButton.addActionListener((ActionListener) this);
		statisticsButton.setActionCommand("STATISTICS");
	    statisticsButton.setBounds(175, 400, 250, 60);
	    add(statisticsButton);
	    setFocusable(true);
		// create and style the "LOGIN" button
		loginButton = ThemeLoader.createRoundButton("LOGIN", 40);
	    //loginButton.addActionListener((ActionListener) this);
		loginButton.setActionCommand("LOGIN");
	    loginButton.setBounds(200, 500, 200, 60);
	    add(loginButton);
	    setFocusable(true);
		//setVisible(true);
    }

    /**
     * paintComponent(g) <br>
     * draws the game title
     * - Custom painting for the main menu
     * @param g (Graphics) the <code>Graphics</code> object passed in by the caller
     */
	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw background (if an image is provided in assets, otherwise just the solid color is used)
        // Example: g.drawImage(AssetManager.getImage("bg_main"), 0, 0, getWidth(), getHeight(), this);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.setFont(Config.TITLE_FONT);
		g2.setColor(Config.ACCENT_COLOR);
		// Draw game title text
        String title = "MANUVO";
        // Center the title horizontally at top
        int titleWidth = g2.getFontMetrics().stringWidth(title);
        g2.drawString(title,(Config.WIDTH - titleWidth)/2, 100);
    }

	/**
	 * setAlpha(alpha) <br>
	 * sets the alpha value
	 * - used for window remove/return effects
	 * @param alpha (float) value to set
	 */
	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}

	/**
	 * getAlpha() <br>
	 * gets the alpha value
	 * - used for window remove/return effects
	 * @return (float) alpha value
	 */
	public float getAlpha() {
		return alpha;
	}
}

