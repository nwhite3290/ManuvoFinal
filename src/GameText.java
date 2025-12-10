
/*
 * GameText.java
 */

import java.awt.*;

/**
 * GameText class <br>
 * handles drawing of text overlays in the game screen, such as score, combo, and game over messages
 * - provides styling for game display
 *  - uses gold accent for scores and combos
 */
public class GameText {

	/**
	 * drawScoreHud(g,score,comboCount,comboScale) <br>
	 * draws the score and combo HUD at the top of the game screen
	 * - score is shown in gold
	 * - if (comboCount > 0) combo count shown with trailing 'x'
	 * - a scale factor is applied to the combo text briefly when combo increases
	 * @param g (Graphics) object passed in by the caller
	 * @param score (int) score passed in from the caller
	 * @param comboCount (int) combination count passed in from the caller
	 * @param comboScale (float) combination scale passed in from the caller
	 * @param accuracy (double) accuracy measurement passed in from the caller
	 * @param missed (int) missed tiles count passed in from the caller
	 */
	public void drawScoreHud(Graphics g, int score, int comboCount, float comboScale, double accuracy, int missed) {
		Graphics2D g2 = (Graphics2D) g;
		// draw "missed" and "accuracy" on top
		//g2.setFont(g2.getFont().deriveFont(Font.BOLD, 16f));
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		Color hudColor = new Color(230, 235, 245); // light text on dark bg
		int pad = 12;
		// use the actual height of the drawing area for bottom placement
		Rectangle clip = g2.getClipBounds();
		int bottomY = (clip != null ? clip.height : Config.HEIGHT) - 10;
		// top line: "missed", "accuracy"
		g2.setFont(Config.UI_FONT);
		g2.setColor(Color.RED.darker());
		//g2.setColor(hudColor);
		String top = String.format("Missed: " + missed + "  Acc: %.1f%%", accuracy);
		int tp = g2.getFontMetrics().stringWidth(top);
		g2.drawString(top, pad, 32);
		// middle line: draw "comboCount"
		g2.setColor(Config.ACCENT_COLOR_DARK);
		//g2.setColor(hudColor);
		g2.setFont(Config.COMBO_FONT);
		String center = "Max Combo: ";
		int cw = g2.getFontMetrics().stringWidth(center);
		g2.drawString(center, pad, 75);
		// score line: draw "score"
		g2.setColor(Config.ACCENT_COLOR_DARK);
		g2.setFont(Config.SCORE_FONT);
		//g2.setColor(hudColor);
		String bottom = "Score: " + score;
		g2.drawString(bottom, pad, 128);
		// bottom left: Controls hint
		String hint = "Keys: 1 2 3 4   Esc: Quit";
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 12f));
		g2.setColor(new Color(220, 220, 220));
		g2.drawString(hint, pad, Config.HEIGHT - 50);
        g2.setFont(ThemeLoader.SCORE_FONT);
        // top right: Big combo text
		// Combo display (only if comboCount > 0)
        if (comboCount > 0) {
            g2.setFont(Config.COMBO_FONT.deriveFont(48f * comboScale));
            g2.setColor(Color.RED);
	        //g2.setColor(hudColor);
            String comboText = comboCount + "x";
            // draw combo count on right side of screen inline with combo string
	        int rw = g2.getFontMetrics().stringWidth(comboText);
            g2.drawString(comboText, Config.WIDTH - rw - (pad * 2), 75);
        }
	}

	/**
	 * drawGameOver(g,score) <br>
	 * draws Game Over overlay
	 * @param g (Graphics) object passed in from the caller
	 * @param score (int) score passed in from the caller
	 */
	public void drawGameOver(Graphics g, int score) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		// Game Over text
		g2.setFont(new Font("SansSerif", Font.BOLD, 60));
		g2.setColor(Color.RED.darker());
		String overText = "GAME OVER";
		int textWidth = g2.getFontMetrics().stringWidth(overText);
		g2.drawString(overText, (600 - textWidth) / 2, 300);
		// Final score
		g2.setFont(new Font("SansSerif", Font.BOLD, 40));
		String scoreText = "Final Score: " + score;
		int scoreWidth = g2.getFontMetrics().stringWidth(scoreText);
		g2.drawString(scoreText, (600 - scoreWidth) / 2, 360);
		// Prompt to restart
		g2.setFont(new Font("SansSerif", Font.PLAIN, 24));
		g2.setColor(Color.WHITE);
		String prompt = "Press Enter to return";
		int promptWidth = g2.getFontMetrics().stringWidth(prompt);
		g2.drawString(prompt, (600 - promptWidth) / 2, 420);
	}

	/**
	 * drawPaused(g,score) <br>
	 * draw the Paused overlay
	 * @param g (Graphics) object passed in from the caller
	 * @param score (int) score passed in from caller
	 */
	public void drawPaused(Graphics g, int score) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		// Game Over text
		g2.setFont(new Font("SansSerif", Font.BOLD, 60));
		g2.setColor(Config.ACCENT_COLOR);
		//g2.setColor(Config.BACKGROUND_COLOR);
		String overText = "PAUSED";
		int textWidth = g2.getFontMetrics().stringWidth(overText);
		g2.drawString(overText, (600 - textWidth) / 2, 300);
		// Final score
		g2.setFont(new Font("SansSerif", Font.BOLD, 40));
		String scoreText = "Score: " + score;
		int scoreWidth = g2.getFontMetrics().stringWidth(scoreText);
		g2.drawString(scoreText, (600 - scoreWidth) / 2, 360);
		// Prompt to restart
		g2.setFont(new Font("SansSerif", Font.PLAIN, 24));
		g2.setColor(Color.WHITE);
		//g2.setColor(Config.BACKGROUND_COLOR);
		String prompt = "Press 'P' to continue";
		int promptWidth = g2.getFontMetrics().stringWidth(prompt);
		g2.drawString(prompt, (600 - promptWidth) / 2, 420);
	}

	/**
	 * drawWin(g,score) <br>
	 * draws the Winner overlay
	 * @param g (Graphics) object passed in from the caller
	 * @param score (int) score passed in from caller
	 */
	public void drawWin(Graphics g, int score) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		// Game Over text
		g2.setFont(new Font("SansSerif", Font.BOLD, 60));
		g2.setColor(Config.ACCENT_COLOR);
		String overText = "You Win!";
		int textWidth = g2.getFontMetrics().stringWidth(overText);
		g2.drawString(overText, (600 - textWidth) / 2, 300);
		// Final score
		g2.setFont(new Font("SansSerif", Font.BOLD, 40));
		String scoreText = "Final Score: " + score;
		int scoreWidth = g2.getFontMetrics().stringWidth(scoreText);
		g2.drawString(scoreText, (600 - scoreWidth) / 2, 360);
		// Prompt to restart
		g2.setFont(new Font("SansSerif", Font.PLAIN, 24));
		g2.setColor(Color.WHITE);
		String prompt = "Press Enter to return";
		int promptWidth = g2.getFontMetrics().stringWidth(prompt);
		g2.drawString(prompt, (600 - promptWidth) / 2, 420);
	}
}

