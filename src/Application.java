
/*
 * Application.java
 */

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Application class (User UI) <br>
 * runs the main program
 * - sets the JFrame where the program panels are displayed
 * - initializes the panels and handles key presses
 * - Main -> MainMenuPanel -> DifficultySelectPanel -> SongSelectPanel -> GamePanel
 *  - SettingsPanel <-> StatisticsPanel <-> LoginPage
 * @version 1.0.1
 * @version Date: 12/07/2025, 11:28:44PM
 */
public class Application {
	// references to screens
	private static JFrame frame;
	private static MainMenuPanel mainMenuPanel;
	private static DifficultySelectPanel difficultySelectPanel;
	private static SongSelectPanel songSelectPanel;
	private static SettingsPanel settingsPanel;
	private static StatisticsPanel statisticsPanel;
	private static GamePanel gamePanel;

	// state tracking
	private static String selectedDifficulty;
	private static int selectedSongIndex;
	public static float alpha = 1.0f;

	// background music for menus
	private static Audio backgroundMusic;

	/**
	 * Application() <br>
	 * constructor <br>
	 * creates a new Application object and sets up the UI
	 * - handles user interaction
	 */
	Application() {
		// load theme assets
		ThemeLoader.loadModernThemeAssets();

		// initialize frame
		frame = new JFrame("Manuvo");
		frame.setSize(Config.WIDTH, Config.HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);

		// absolute positioning for custom transitions
		frame.setLayout(null);

		// initialize screens
		mainMenuPanel = new MainMenuPanel();
		difficultySelectPanel = new DifficultySelectPanel();
		songSelectPanel = new SongSelectPanel();
		settingsPanel = new SettingsPanel();
		statisticsPanel = new StatisticsPanel();
		//settingsPanel.refreshTheme();
		// GamePanel will be created when starting a game to include chosen difficulty/song

		// position screens (all fill the frame area)
		mainMenuPanel.setBounds(0, 0, Config.WIDTH, Config.HEIGHT);
		difficultySelectPanel.setBounds(0, 0, Config.WIDTH, Config.HEIGHT);
		songSelectPanel.setBounds(0, 0, Config.WIDTH, Config.HEIGHT);
		settingsPanel.setBounds(0, 0, Config.WIDTH, Config.HEIGHT);
		statisticsPanel.setBounds(0, 0, Config.WIDTH, Config.HEIGHT);

		// add screens to frame
		frame.add(mainMenuPanel);
		frame.add(difficultySelectPanel);
		frame.add(songSelectPanel);
		frame.add(settingsPanel);
		frame.add(statisticsPanel);

		// initially only main menu is visible
		difficultySelectPanel.setVisible(false);
		songSelectPanel.setVisible(false);
		settingsPanel.setVisible(false);
		statisticsPanel.setVisible(false);
		//loginPage.setVisible(false);

		// Start background menu music (looped) on launch
		backgroundMusic = new Audio(3); // index 0-4
		backgroundMusic.startAudio();

		// -------- Main menu buttons --------
		mainMenuPanel.playButton.addActionListener(e -> showDifficultySelect());
		mainMenuPanel.settingsButton.addActionListener(e -> showPanel(mainMenuPanel, settingsPanel));
		mainMenuPanel.statisticsButton.addActionListener(e -> showPanel(mainMenuPanel, statisticsPanel));
		// LOGIN BUTTON behaves as LOGOUT: clear session and go back to LoginPage
		if (mainMenuPanel.loginButton != null) {
			mainMenuPanel.loginButton.setText("LOGOUT");
			mainMenuPanel.loginButton.addActionListener(e -> logoutAndShowLogin());
		}

		// -------- Settings buttons --------
		// FONT button
		settingsPanel.fontButton.addActionListener(e -> {
			int fontSize = Config.TITLE_FONT.getSize();
			String name = Config.TITLE_FONT.getFontName();
			settingsPanel.messageLabel.setText("Font: " + name + ": Size: " + fontSize);
			settingsPanel.refreshTheme();
		});
		// BACKGROUND button - toggles theme 0/1 and updates Settings UI
		settingsPanel.backgroundColorButton.addActionListener(e -> {
			Config.THEME_INDEX = 1 - Config.THEME_INDEX;
			settingsPanel.refreshTheme();
		});
		// ACCENT button
		settingsPanel.accentColorButton.addActionListener(e -> {
			settingsPanel.messageLabel.setText(Config.ACCENT_COLOR.toString());
			settingsPanel.refreshTheme();
		});

		// SOUND button - toggles global sound flag ---
		settingsPanel.soundButton.addActionListener(e -> {
			Config.SOUND_ENABLED = !Config.SOUND_ENABLED;
			if (Config.SOUND_ENABLED) {
				settingsPanel.soundButton.setText("SOUND: ON");
				backgroundMusic.startAudio();
			} else {
				settingsPanel.soundButton.setText("SOUND: OFF");
				backgroundMusic.stopAudio();
			}
		});
		// DIFFICULTY button
		settingsPanel.difficultyButton.addActionListener(e -> {
			settingsPanel.messageLabel.setText(Config.getDifficulty());
			settingsPanel.refreshTheme();
		});

		// -------- Exit buttons on other panels --------
		settingsPanel.exitButton.addActionListener(e -> showPanel(settingsPanel, mainMenuPanel));
		difficultySelectPanel.exitButton.addActionListener(e -> showPanel(difficultySelectPanel, mainMenuPanel));
		songSelectPanel.exitButton.addActionListener(e -> showPanel(songSelectPanel, mainMenuPanel));
		statisticsPanel.exitButton.addActionListener(e -> showPanel(statisticsPanel, mainMenuPanel));

		// -------- Difficulty selection --------
		ActionListener difficultyListener = e -> {
			JButton source = (JButton) e.getSource();
			selectedDifficulty = source.getText(); // "VERY EASY", "EASY", ...
			showSongSelect();
		};
		for (JButton btn : difficultySelectPanel.difficultyButtons) {
			btn.addActionListener(difficultyListener);
		}

		// -------- Song selection --------
		ActionListener songListener = e -> {
			// action command is expected to be the song index ("0", "1", ...)
			selectedSongIndex = Integer.parseInt(e.getActionCommand());
			showGameScreen();
		};
		for (int i = 0; i < songSelectPanel.songButtons.length; i++) {
			// if SongSelectPanel already sets text to a number, this is redundant but harmless
			songSelectPanel.songButtons[i].setActionCommand(String.valueOf(i));
			songSelectPanel.songButtons[i].addActionListener(songListener);
		}
		// Show the window
		frame.setVisible(true);
	}

	/**
	 * showDifficultySelect() <br>
	 * shows the difficulty select screen
	 * - transition from Main Menu to Difficulty Select Panel
	 */
	private static void showDifficultySelect() {
		// Prepare Difficulty screen
		difficultySelectPanel.setVisible(true);
		difficultySelectPanel.requestFocusInWindow();
		// Fade out main menu panel
		Timer fadeTimer = new Timer(15, null);
		fadeTimer.addActionListener(new ActionListener() {
			float alpha = 1.0f;
			/**
			 * actionPerformed(e) <br>
			 * handles actions as defined in the body
			 * @param e (ActionEvent) the event to be processed
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				alpha -= 0.05f;
				if (alpha <= 0f) {
					alpha = 0f;
					// End fade: hide Main Menu and stop timer
					mainMenuPanel.setVisible(false);
					mainMenuPanel.setAlpha(1.0f); // reset alpha for potential future use
					fadeTimer.stop();
				}
				mainMenuPanel.setAlpha(alpha);
			}
		});
		fadeTimer.start();
	}

	/**
	 * showSongSelect() <br>
	 * shows the song select screen
	 *  - transition from DifficultySelectPanel to SongSelectPanel
	 */
	private static void showSongSelect() {
		songSelectPanel.setVisible(true);
		songSelectPanel.setLocation(Config.WIDTH, 0);  // start song panel to the right of frame
		// Slide difficulty panel out to left, song panel in from right
		Timer slideTimer = new Timer(5, null);
		slideTimer.addActionListener(new ActionListener() {
			final int dx = 20; // slide step
			/**
			 * actionPerformed(e) <br>
			 * handles actions as defined in the body
			 * @param e (ActionEvent) the event to be processed
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				int songX = songSelectPanel.getLocation().x;
				int diffX = difficultySelectPanel.getLocation().x;
				// Move panels towards target
				songX -= dx;
				diffX -= dx;
				if (songX <= 0) {
					// Finalize positions
					songX = 0;
					diffX = -(Config.WIDTH);
					// Stop timer when slide is complete
					slideTimer.stop();
					difficultySelectPanel.setVisible(false);
				}
				songSelectPanel.setLocation(songX, 0);
				difficultySelectPanel.setLocation(diffX, 0);
			}
		});
		slideTimer.start();
	}

	/**
	 * showPanel(currentPanel,nextPanel) <br>
	 * Helper function
	 * - helps to transition between arbitrary panels with a fade-out
	 * @param currentPanel (JPanel) panel currently in focus
	 * @param nextPanel (JPanel) panel to show next
	 */
	public static void showPanel (JPanel currentPanel, JPanel nextPanel) {
		// set next panel visible
		nextPanel.setVisible(true);
		// prepare next panel
		nextPanel.requestFocusInWindow();
		// fade out current panel
		Timer fadeInTimer = new Timer(15, null);
		fadeInTimer.addActionListener(new ActionListener() {
			float alpha = 1.0f;
			/**
			 * actionPerformed(ActionEvent e)
			 * - handles actions as defined in the body
			 * @param e the event to be processed
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				alpha -= 0.05f;
				if (alpha <= 0f) {
					alpha = 0f;
					// end fade: hide Main Menu and stop timer
					currentPanel.setVisible(false);
					fadeInTimer.stop();
				}
			}
		});
		fadeInTimer.start();
	}

	/**
	 * showGameScreen() <br>
	 * creates a new GamePanel for the chosen difficulty/song
	 * - hides menu screens and shows the game
	 * - when the game ends and the player presses ENTER, the callback
	 *   will update statistics and return to the main menu.
	 */
	private static void showGameScreen() {
		System.out.println("Song: " + selectedSongIndex + ", Difficulty: " + selectedDifficulty);
		// stop the background music
		if (backgroundMusic != null) {
			backgroundMusic.stopAudio();
			backgroundMusic = null;
		}
		// create gamePanel with selected difficulty and song
		gamePanel = new GamePanel(selectedDifficulty, selectedSongIndex, () -> {
			// restart callback runs when the player presses ENTER after GAME OVER
			ScoreCalculate sc = gamePanel.getScorer();
			// determine logged-in user
			String userId = Session.getCurrentUserId();
			if (userId == null || userId.isEmpty()) {
				userId = "Guest";
			}
			// build snapshot for this run
			UserStats stats = new UserStats();
			stats.userId = userId;
			stats.songIndex = gamePanel.getSongIndex();
			stats.difficulty = gamePanel.getDifficultyLabel();
			stats.hits = sc.hits;
			stats.misses = sc.misses;
			stats.errors = sc.wrongs;
			stats.score = sc.score;
			stats.timeText = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
			stats.accuracyPercent = sc.accuracy();
			stats.maxCombo = sc.maxCombo;
			stats.lastComboCount = sc.comboCount;
			// persist to local storage (last game + lifetime + per-song best)
			StatsManager.saveLastStats(stats);
			// update the statistics panel for this user and context
			statisticsPanel.updateFromStats(
					stats.userId,
					stats.songIndex,
					stats.difficulty,
					stats.hits,
					stats.misses,
					stats.score,
					stats.timeText,
					stats.accuracyPercent,
					stats.maxCombo,
					stats.lastComboCount
			);
			// hide and remove game panel
			gamePanel.setVisible(false);
			frame.remove(gamePanel);
			// return to main menu
			mainMenuPanel.setVisible(true);
			mainMenuPanel.requestFocusInWindow();
			// reset positions
			mainMenuPanel.setLocation(0, 0);
			difficultySelectPanel.setLocation(0, 0);
			songSelectPanel.setLocation(0, 0);
			// revalidate and repaint all panels
			frame.revalidate();
			frame.repaint();
		});
		gamePanel.setBounds(0, 0, Config.WIDTH, Config.HEIGHT);
		frame.add(gamePanel);
		// hide the song selector
		songSelectPanel.setVisible(false);
		// request gamePanel focus and set visible
		gamePanel.requestFocusInWindow();
		gamePanel.setVisible(true);
	}

	/**
	 * logoutAndShowLogin() <br>
	 * clears the current user session
	 * - closes the Application frame
	 * - opens a new LoginPage window
	 */
	private static void logoutAndShowLogin() {
		// clear the session
		Session.setCurrentUserId(null);
		// Close the current application window
		if (frame != null) {
			frame.dispose();
		}
		// re-open the login screen
		SwingUtilities.invokeLater(() -> {
			IDandPasswords ids = new IDandPasswords();
			new LoginPage(ids.getLoginInfo());
		});
	}

	/**
	 * abortGameFromEsc() <br>
	 * aborts game when the ESCAPE button is pressed
	 */
	public static void abortGameFromEsc() {
		// if a game panel exists, remove it
		if (gamePanel != null) {
			gamePanel.setVisible(false);
			frame.remove(gamePanel);
			gamePanel = null;
		}
		// show main menu again
		if (mainMenuPanel != null) {
			mainMenuPanel.setVisible(true);
			mainMenuPanel.requestFocusInWindow();
			mainMenuPanel.setLocation(0, 0);
		}
		// reset other panels’ positions
		if (difficultySelectPanel != null) {
			difficultySelectPanel.setLocation(0, 0);
		}
		if (songSelectPanel != null) {
			songSelectPanel.setLocation(0, 0);
		}
		frame.revalidate();
		frame.repaint();
	}
}



