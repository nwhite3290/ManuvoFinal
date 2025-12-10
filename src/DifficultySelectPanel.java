
/*
 * DifficultySelectPanel.java
 */

import javax.swing.*;
import java.awt.Component;
import java.awt.event.*;
import java.util.*;

/**
 * DifficultySelectPanel class <br>
 * shows a list of difficulty options for the player to choose from
 * - appears after the main menu and before the song selection
 * - extends JPanel
 */
@SuppressWarnings("FieldCanBeLocal")
public class DifficultySelectPanel extends JPanel {
	public JButton[] difficultyButtons;
	public JButton exitButton = new JButton("EXIT");
	private int currentSelection = 0;

	/**
	 * DifficultySelectPanel() <br>
	 * constructor
	 * - creates a new DifficultySelectPanel Object
	 * default parameters:
	 * - box layout, background color by Config file
	 */
	public DifficultySelectPanel() {
		setSize(Config.WIDTH, Config.HEIGHT);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBackground(Config.BACKGROUND_COLOR);
		// difficulties array
		String[] difficulties = {
				"VERY EASY",
				"EASY",
				"MEDIUM",
				"HARD",
				"VERY HARD"

		};
		difficultyButtons = new JButton[difficulties.length];
		//add(Box.createVerticalGlue());
		add(Box.createVerticalStrut(75));
		for (int i = 0; i < difficulties.length; i++) {
			difficultyButtons[i] = ThemeLoader.createRoundButton(difficulties[i], 10);
			difficultyButtons[i].setActionCommand(difficulties[i]);
			difficultyButtons[i].setAlignmentX(Component.CENTER_ALIGNMENT);
			add(difficultyButtons[i]);
			add(Box.createVerticalStrut(40));
		}
		exitButton = ThemeLoader.createRoundButton("EXIT", 10);
		exitButton.setActionCommand("EXIT");
		exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(exitButton);
		setFocusable(true);
		add(Box.createVerticalStrut(5));
		add(Box.createVerticalGlue());
		setFocusable(true);
		updateFocus();
	}

	/**
	 * updateFocus()
	 * - updates the focus of the buttons
	 */
	private void updateFocus() {
		for (int i = 0; i < difficultyButtons.length; i++) {
			difficultyButtons[i].setFocusable(i == currentSelection);
		}
		difficultyButtons[currentSelection].requestFocusInWindow();
	}
}




