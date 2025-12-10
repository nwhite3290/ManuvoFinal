
/*
 * SongSelectPanel.java
 */

import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.JButton;
import java.awt.Component;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * SongSelectPanel class <br>
 * displays the list of songs available to play after selecting difficulty.
 * - Each song is represented by a button
 *  - (possible to include song name and optionally an icon).
 */
public class SongSelectPanel extends JPanel {
	// new collection of JButtons for song selection
	public JButton[] songButtons;
	// create exit button
	public JButton exitButton = new JButton("EXIT");
    // label to highlight song on keyboard hover (currently N/A)
	public JLabel songLabel = new JLabel();
	// currently selected song
	private int currentSelection = 0;

    /**
     * SongSelectPanel() <br>
     * constructor
     * - creates new SongSelectPanel object
     * default parameters:
     * - box layout
     * - ThemeLoader class managed background
     */
	public SongSelectPanel() {
		setSize(Config.WIDTH, Config.HEIGHT);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBackground(Config.BACKGROUND_COLOR);
		// collection of songs available
		String[] songs = {
				"Someone You Loved", // Audio song[0]
				"Memories", // Audio song[1]
				"Fur Elise", // Audio song[2]
				"Canon", // Audio song[3]
				"Moonlight Sonata" // Audio song[4]
		};
		songButtons = new JButton[songs.length];
		//add(Box.createVerticalGlue());
		add(Box.createVerticalStrut(75));
		for (int i = 0; i < songs.length; i++) {
			songButtons[i] = ThemeLoader.createRoundButton(songs[i], 10);
			songButtons[i].setActionCommand(String.valueOf(i));
			songButtons[i].setAlignmentX(Component.CENTER_ALIGNMENT);
			add(songButtons[i]);
			add(Box.createVerticalStrut(40));
		}
		exitButton = ThemeLoader.createRoundButton("EXIT", 10);
		exitButton.setActionCommand("EXIT");
		exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(exitButton);
		setFocusable(true);
		add(Box.createVerticalStrut(10));
		add(Box.createVerticalGlue());
		setFocusable(true);
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_UP) {
					currentSelection = (currentSelection + songButtons.length + 1) % songButtons.length;
					updateFocus();
				} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					currentSelection = (currentSelection + songButtons.length - 1) % songButtons.length;
					updateFocus();
				} else if (e.getKeyCode() == KeyEvent.VK_1) {
					currentSelection = 0;
					updateFocus();
					repaint();
				}else if (e.getKeyCode() == KeyEvent.VK_2) {
					currentSelection = 1;
					updateFocus();
					repaint();
				}else if (e.getKeyCode() == KeyEvent.VK_3) {
					currentSelection = 2;
					updateFocus();
					repaint();
				}else if (e.getKeyCode() == KeyEvent.VK_4) {
					currentSelection = 3;
					updateFocus();
					repaint();
				}else if (e.getKeyCode() == KeyEvent.VK_5) {
					currentSelection = 4;
					updateFocus();
					repaint();
				} else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					songButtons[currentSelection].doClick();
				}
			}
		});
	}

    /**
     * updateFocus() <br>
     * updates focus on panel buttons
     */
    private void updateFocus() {
		for (int i = 0; i < songButtons.length; i++) {
			songButtons[i].setFocusable(i == currentSelection);
		}
		songButtons[currentSelection].requestFocusInWindow();
	}
}

