
/*
 * ThemeLoader.java
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

/**
 * ThemeLoader class <br>
 * configures the game's visual theme and loads all themed assets
 * - defines theme colors, fonts, and provides factory methods for styled components like buttons
 */
public class ThemeLoader {
	// theme colors
	public static final Color TEXT_COLOR_ALT = Config.TEXT_COLOR_ALT ;  // alternate text (dark navy for use on gold)
	public static final Color BACKGROUND_COLOR = Config.BACKGROUND_COLOR;  // dark navy background
	public static final Color ACCENT_COLOR = Config.ACCENT_COLOR;     // gold accent color
	public static final Color TEXT_COLOR = Config.TEXT_COLOR_PRIMARY;                 // primary text color (white for dark background)
	// theme fonts
	public static final Font SCORE_FONT = Config.SCORE_FONT;
	public static final Font COMBO_FONT = Config.COMBO_FONT;
	public static Font TITLE_FONT = Config.TITLE_FONT;
	public static Font UI_FONT = Config.UI_FONT;

	/**
	 * loadModernThemeAssets() <br>
	 * loads all image assets for the modern UI theme into the AssetManager
	 * - should be called once at game startup
	 */
	public static void loadModernThemeAssets() {
		try {
			// load background images (if any) for different screens
			AssetManager.loadImage("bg_main", "Images/backgrounds/4.png");
			AssetManager.loadImage("bg_difficulty", "Images/backgrounds/4.png");
			AssetManager.loadImage("bg_song", "Images/backgrounds/4.png");
			AssetManager.loadImage("bg_game", "Images/backgrounds/4.png");
			// load tile graphics for combos
			AssetManager.loadImage("tile_black", "Images/tiles/tile_blk_blu1.png");
			AssetManager.loadImage("tile_white", "Images/tiles/tile_gold_blu.png");
			AssetManager.loadImage("tile_gold", "Images/tiles/tile_gold_blu1.png");
			// load other UI graphics
			AssetManager.loadImage("note", "Images/tiles/blueNote4.png");
			AssetManager.loadImage("note0", "Images/tiles/blueNote0.png");
			AssetManager.loadImage("note1", "Images/tiles/blueNote1.png");
			AssetManager.loadImage("note2", "Images/tiles/blueNote2.png");
			AssetManager.loadImage("note3", "Images/tiles/blueNote3.png");
			// (additional assets like button icons can be loaded here as needed)
		} catch (Exception e) {
			System.err.println("File Not Found: ThemeLoader/loadModernThemeAssets()");
		}
	}

	/**
	 * createRoundButton(text,radius) <br>
	 * creates a JButton with theme styling (currently rounded and gold-accented)
	 *  - the button is given a rounded border and highlight effect on mouse hover
	 * @param text (String) button text label
	 * @param radius (int) corner radius for the rounded button border
	 * @return a styled JButton ready for use in the UI
	 */
	public static JButton createRoundButton(String text, int radius) {
		JButton button = new JButton(text);
		button.setFocusPainted(false);
		// no default rectangle fill, we will draw our own shape
		button.setContentAreaFilled(false);
		button.setForeground(Config.ACCENT_COLOR);
		button.setFont(Config.UI_FONT_ALT);
		button.setBorder(new RoundedBorder(radius, Config.ACCENT_COLOR));
		button.setRolloverEnabled(true);
		//hover effect: toggle text and background colors on mouse over
		button.addMouseListener(new java.awt.event.MouseAdapter() {

			/**
			 * mouseEntered(e) <br>
			 * - changes the color of button on mouse hover
			 * @param e (java.awt.event.MouseEvent) the event to be processed
			 */
			@Override
			public void mouseEntered(java.awt.event.MouseEvent e) {
				button.setForeground(Config.TEXT_COLOR_ALT); // text becomes dark (navy) on hover
				button.setOpaque(true);
				button.setBackground(Config.ACCENT_COLOR); // button background fills with gold on hover
			}

			/**
			 * mouseExited(e) <br>
			 * changes the color of button on mouse click
			 * @param e (java.awt.event.MouseEvent) the event to be processed
			 */
			@Override
			public void mouseExited(java.awt.event.MouseEvent e) {
				button.setForeground(Config.ACCENT_COLOR);             // text back to gold
				button.setOpaque(false);
				button.setBackground(new Color(0,0,0,0));       // transparent background (shows panel background)
			}
		});
		return button;
	}
}