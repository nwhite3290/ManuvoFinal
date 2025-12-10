
/*
 * StatisticsPanel.java
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

/**
 * StatisticsPanel class <br>
 * shows game statistics for the user
 * - displays stats from the last run
 * - also shows lifetime statistics and best results for the current song/difficulty
 */
public class StatisticsPanel extends JPanel {
	public float alpha = 1.0f;
	// exit button
	JButton exitButton;
	JLabel userIDLabel = new JLabel("User ID: ");
	// Last game section
	JLabel lastHeader = new JLabel("Last Game");
	JLabel lastSongIDLabel = new JLabel("Song ID: ");
	JLabel lastDifficultyLabel = new JLabel("Difficulty: ");
	JLabel lastScoreLabel = new JLabel("Last Score: ");
	JLabel lastHitsLabel = new JLabel("Last Hits: ");
	JLabel lastMissesLabel = new JLabel("Last Misses: ");
	JLabel lastAccuracyLabel = new JLabel("Last Accuracy: ");
	JLabel lastMaxComboLabel = new JLabel("Last Max Combo: ");
	JLabel lastComboCountLabel = new JLabel("Last Combo Count: ");
	JLabel lastTimeLabel = new JLabel("Last Time: ");
	// Lifetime section
	JLabel lifetimeHeader = new JLabel("Lifetime");
	JLabel lifetimeGamesLabel = new JLabel("Total Games Played: ");
	JLabel lifetimeScoreLabel = new JLabel("Total Score: ");
	JLabel lifetimeHitsLabel = new JLabel("Total Hits: ");
	JLabel lifetimeMissesLabel = new JLabel("Total Misses: ");
	JLabel lifetimeAccuracyLabel = new JLabel("Average Accuracy: ");
	// Best per song/difficulty (for the same context as last game)
	JLabel bestHeader = new JLabel("Personal Best (this song & difficulty)");
	JLabel bestScoreLabel = new JLabel("Best Score: ");
	JLabel bestAccuracyLabel = new JLabel("Best Accuracy: ");
	JLabel bestMaxComboLabel = new JLabel("Best Max Combo: ");

	/**
	 * StatisticsPanel() <br>
	 * displays the values from the last user interaction
	 * - id, difficulty, hits, missed, errors, score, time, accuracy, combo count
	 */
	public StatisticsPanel(){
		setSize(Config.WIDTH, Config.HEIGHT);
		setLayout(null);
		setBackground(Config.BACKGROUND_COLOR);
		// local fonts and colors
		Font labelFont = new Font(null, Font.ITALIC, 16);
		Font headerFont = new Font(null, Font.BOLD, 18);
		Color labelColor = Color.WHITE;
		//local variables
		int x = 50;
		int y = 30;
		int dy = 25;
		// userID
		userIDLabel.setForeground(labelColor);
		userIDLabel.setFont(labelFont);
		userIDLabel.setBounds(x, y, 400, dy);
		add(userIDLabel);
		y += dy + 10;
		// last game header
		lastHeader.setForeground(labelColor);
		lastHeader.setFont(headerFont);
		lastHeader.setBounds(x, y, 400, dy);
		add(lastHeader);
		y += dy;
		// last labels
		JLabel[] lastLabels = {
				lastSongIDLabel,
				lastDifficultyLabel,
				lastScoreLabel,
				lastHitsLabel,
				lastMissesLabel,
				lastAccuracyLabel,
				lastMaxComboLabel,
				lastComboCountLabel,
				lastTimeLabel
		};
		for (JLabel lbl : lastLabels) {
			lbl.setForeground(labelColor);
			lbl.setFont(labelFont);
			lbl.setBounds(x, y, 500, dy);
			add(lbl);
			y += dy;
		}
		y += 10;
		// lifetime header
		lifetimeHeader.setForeground(labelColor);
		lifetimeHeader.setFont(headerFont);
		lifetimeHeader.setBounds(x, y, 400, dy);
		add(lifetimeHeader);
		y += dy;
		// lifetime labels
		JLabel[] lifeLabels = {
				lifetimeGamesLabel,
				lifetimeScoreLabel,
				lifetimeHitsLabel,
				lifetimeMissesLabel,
				lifetimeAccuracyLabel
		};
		for (JLabel lbl : lifeLabels) {
			lbl.setForeground(labelColor);
			lbl.setFont(labelFont);
			lbl.setBounds(x, y, 500, dy);
			add(lbl);
			y += dy;
		}
		y += 10;
		// best header
		bestHeader.setForeground(labelColor);
		bestHeader.setFont(headerFont);
		bestHeader.setBounds(x, y, 500, dy);
		add(bestHeader);
		y += dy;
		// best labels
		JLabel[] bestLabels = {
				bestScoreLabel,
				bestAccuracyLabel,
				bestMaxComboLabel
		};
		for (JLabel lbl : bestLabels) {
			lbl.setForeground(labelColor);
			lbl.setFont(labelFont);
			lbl.setBounds(x, y, 500, dy);
			add(lbl);
			y += dy;
		}
		// Exit button
		exitButton = ThemeLoader.createRoundButton("EXIT", 16);
		exitButton.setActionCommand("EXIT");
		exitButton.setBounds(x, y + 20, 200, 30);
		add(exitButton);
		setFocusable(true);
		// If a user is already logged in when this panel is constructed,
		// try to show their last and lifetime stats.
		String userId = Session.getCurrentUserId();
		if (userId != null) {
			UserStats last = StatsManager.loadStats(userId);
			if (last != null) {
				applyLastStats(last);
				LifetimeStats life = StatsManager.loadLifetimeSummary(
						userId, last.songIndex, last.difficulty);
				if (life != null) {
					updateLifetime(life);
				}
			} else {
				userIDLabel.setText("User ID: " + userId);
			}
		} else {
			userIDLabel.setText("User ID: (not logged in)");
		}
	}

	/**
	 * applyLastStats(stats) <br>
	 * set labels to show last user stats
	 * @param stats (UserStats) last user stats
	 */
	private void applyLastStats(UserStats stats) {
		userIDLabel.setText("User ID: " + stats.userId);
		lastSongIDLabel.setText("Song ID: " + stats.songIndex);
		lastDifficultyLabel.setText("Difficulty: " + stats.difficulty);
		lastScoreLabel.setText("Last Score: " + stats.score);
		lastHitsLabel.setText("Last Hits: " + stats.hits);
		lastMissesLabel.setText("Last Misses: " + stats.misses);
		lastAccuracyLabel.setText(String.format("Last Accuracy: %.1f%%", stats.accuracyPercent));
		lastMaxComboLabel.setText("Last Max Combo: " + stats.maxCombo);
		lastComboCountLabel.setText("Last Combo Count: " + stats.lastComboCount);
		lastTimeLabel.setText("Last Time: " + stats.timeText);
	}

	/**
	 * updateFromStats(allStats) <br>
	 * updates the statistics panel with the current stats
	 * - Called from Application when a game finishes to refresh the panel.
	 * @param userId (String) user ID
	 * @param songIndex (int) song index
	 * @param difficultyText (int) difficulty
	 * @param hitsVal (int) hits
	 * @param missesVal (int) misses
	 * @param scoreVal (int) score
	 * @param timeText (String) time
	 * @param accuracyPercent (double) accuracy
	 * @param maxComboVal (int) max Combo
	 * @param lastComboVal (int) last combo count
	 */
	public void updateFromStats(
			String userId,
			int songIndex,
			String difficultyText,
			int hitsVal,
			int missesVal,
			int scoreVal,
			String timeText,
			double accuracyPercent,
			int maxComboVal,
			int lastComboVal) {
		if (userId == null || userId.isEmpty()) {
			userId = "Guest";
		}
		// set labels
		userIDLabel.setText("User ID: " + userId);
		lastSongIDLabel.setText("Song ID: " + songIndex);
		lastDifficultyLabel.setText("Difficulty: " + difficultyText);
		lastScoreLabel.setText("Last Score: " + scoreVal);
		lastHitsLabel.setText("Last Hits: " + hitsVal);
		lastMissesLabel.setText("Last Misses: " + missesVal);
		lastAccuracyLabel.setText(String.format("Last Accuracy: %.1f%%", accuracyPercent));
		lastMaxComboLabel.setText("Last Max Combo: " + maxComboVal);
		lastComboCountLabel.setText("Last Combo Count: " + lastComboVal);
		lastTimeLabel.setText("Last Time: " + timeText);
		LifetimeStats life = StatsManager.loadLifetimeSummary(userId, songIndex, difficultyText);
		if (life != null) {
			updateLifetime(life);
		}
	}

	/**
	 * updateLifetime(s) <br>
	 * updates lifetime stats
	 * @param s (LifetimeStats) stats to log
	 */
	public void updateLifetime(LifetimeStats s) {
		lifetimeGamesLabel.setText("Total Games Played: " + s.totalGames);
		lifetimeScoreLabel.setText("Total Score: " + s.totalScore);
		lifetimeHitsLabel.setText("Total Hits: " + s.totalHits);
		lifetimeMissesLabel.setText("Total Misses: " + s.totalMisses);
		lifetimeAccuracyLabel.setText(
				String.format("Average Accuracy: %.1f%%", s.lifetimeAccuracyPercent));
		bestScoreLabel.setText("Best Score: " + s.bestScoreForCurrent);
		bestAccuracyLabel.setText(
				String.format("Best Accuracy: %.1f%%", s.bestAccuracyForCurrent));
		bestMaxComboLabel.setText("Best Max Combo: " + s.bestMaxComboForCurrent);
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