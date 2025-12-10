
/*
 * Config.java
 */

import java.awt.*;

/**
 * Config class <br>
 * stores the configuration items used by the program
 * - generic class for commonly used configuration items
 */
public class Config {
	// sound
	static boolean SOUND_ENABLED = true;
	// board
	static final int WIDTH = 600;
	static final int HEIGHT = 780;
	// lanes
	static final int LANE_GAP = 4;
	static final int SIDE_PADDING = 12;
	static final int LANES = 4;
	static final int FPS = 120;
	// music / rhythm
	static final int BPM = 60; // for rhythmic spawn variants
	static final boolean RHYTHMIC_SPAWN = true;
	// timing and Speed
	static final int DELAY = 15; // base timer delay (ms)
	static final int SPEED = 4;
	// hit window for scoring (not for foul)
	static final int HIT_LINE_Y = 650; // y position of the hit line
	static final int HIT_WINDOW = 200; // window for scoring: HIT_LINE +/- 50
	// hit window for fouls
	static final int HIT_MIN = -150; // hits accepted starting immediately
	static final int HIT_MAX = 900; // hits accepted until tiles fall below screen
	// tile
	static final int TILE_WIDTH = 150;
	static final int TILE_HEIGHT = 150;
	static final int TILE_START_X = SIDE_PADDING; // lane 0 starts 4px from left
	// tile Management
	static final int BOTTOM_BOUND = 550; // perfect hit spot, buttons under here
	// Theme index (0 = default, 1 = alt)
	public static int THEME_INDEX = 0;

	// default theme colors
	static Color BACKGROUND_COLOR = new Color(10, 10, 30); // dark navy background
	static Color ACCENT_COLOR = new Color(255, 215, 0); // gold accent color
	static final Color ACCENT_COLOR_DARK = new Color(155, 115, 0, 255); // shaded ACCENT_COLOR
	static final Color LANE_COLOR = new Color(255, 215, 215, 60); // shaded ACCENT_COLOR
	static final Color HIT_ZONE_COLOR = new Color(30, 30, 30, 200); // default hit zone color
	static final Color TEXT_COLOR = new Color(255, 215, 0, 255); // call TEXT_COLOR_ALT.darker() for shaded

	// alternate theme colors
	static Color BACKGROUND_COLOR_ALT = new Color(20, 15, 50); // dark navy background;
	static Color ACCENT_COLOR_ALT = new Color(155, 115, 0, 255); // shaded ACCENT_COLOR
	static Color HIT_ZONE_COLOR_ALT = new Color(30, 30, 30, 200); // default hit zone color

	// other colors
	static Color TEXT_COLOR_ALT = new Color(10, 10, 30); // alternate text (dark navy for use on gold)
	static Color TEXT_COLOR_PRIMARY = Color.WHITE; // primary text color (white for dark background)

	// default theme fonts
	static final Font SCORE_FONT = new Font("SansSerif", Font.BOLD, 48);
	static final Font COMBO_FONT = new Font("SansSerif", Font.BOLD, 32);
	static Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 52);
	static final Font UI_FONT = new Font("SansSerif", Font.BOLD, 28);

	// alternate theme fonts
	static Font TITLE_FONT_ALT = new Font("SansSerif", Font.BOLD, 52);
	static Font UI_FONT_ALT = new Font("SansSerif", Font.BOLD, 28);

	// settings
	static int SIZE;
	static int DIFFICULTY = 2;
	static int DIFFICULTY_ALT = 2;

	/**
	 * getGameBackgroundColor() <br>
	 * returns the background color based on theme
	 * @return (Color) requested by the caller
	 */
	public static Color getGameBackgroundColor() {
		return switch (THEME_INDEX) {
			case 1 -> BACKGROUND_COLOR_ALT; //THEME_INDEX=1, navy blue
			case 2 -> HIT_ZONE_COLOR; //THEME_INDEX=2, (dusty gold)
			default -> BACKGROUND_COLOR; //THEME_INDEX=0, dark navy blue
		};
	}


	/**
	 * getFontSize() <br>
	 * returns the alternate title font size
	 * @return TITLE_FONT_ALT
	 */
	static Font getFontSize() {
		return TITLE_FONT_ALT;
	}

	/**
	 * setFontSize(s) <br>
	 * allows the user to set the TITLE_FONT_ALT size
	 * - sets the alternate tile font size
	 * @param size (int) desired size of the font
	 */
	static void setFontSize(int size) {
		TITLE_FONT_ALT = new Font("SansSerif", Font.BOLD, size);
	}

	/**
	 * getBackgroundColor() <br>
	 * returns the alternate background color
	 * @return BACKGROUND_COLOR_ALT
	 */
	static Color getBackgroundColor() {
		return BACKGROUND_COLOR_ALT;
	}

	/**
	 * setBackgroundColor(Color) <br>
	 * allows the user to set the alternate background color
	 * @param Color BACKGROUND_COLOR_ALT
	 */
	static void setBackgroundColor(Color Color) {
		BACKGROUND_COLOR_ALT = Color;
	}

	/**
	 * getHitZoneColor() <br>
	 * returns the default hit zone color
	 * @return (Color) HIT_ZONE_COLOR
	 */
	static Color getHitZoneColor() {
		return HIT_ZONE_COLOR_ALT;
	}

	/**
	 * getAccentColor() <br>
	 * returns the alternate accent color
	 * @return ACCENT_COLOR or ALT
	 */
	static Color getAccentColor() {
		return ACCENT_COLOR_ALT;
	}

	/**
	 * setAccentColor(Color) <br>
	 * allows the user to set the alternate accent color
	 * @param Color color to set as the accent color
	 */
	static void setAccentColor(Color Color) {
		ACCENT_COLOR_ALT = Color;
	}

	/**
	 * getDifficulty() <br>
	 * returns the set difficulty to the user
	 * - Very Easy, Easy, Medium, Hard, Very Hard
	 * - default is MEDIUM
	 * @return DIFFICULTY_ALT setting
	 */
	static String getDifficulty() {
		String[] difficulty = {"VERY EASY", "EASY", "MEDIUM", "HARD", "VERY HARD"};
		String d = "";
		if (DIFFICULTY_ALT == -1) {
			DIFFICULTY_ALT = DIFFICULTY;
		}
		if (DIFFICULTY_ALT == 0) {
			d = difficulty[0];
		}
		if (DIFFICULTY_ALT == 1) {
			d = difficulty[1];
		}
		if (DIFFICULTY_ALT == 2) {
			d = difficulty[2];
		}
		if (DIFFICULTY_ALT == 3) {
			d = difficulty[3];
		}
		if (DIFFICULTY_ALT == 4) {
			d = difficulty[4];
		}
		return d;
	}

	/**
	 * setDifficulty(String) <br>
	 * allows the user to set the alternate difficulty
	 * @param difficulty string to set as the difficulty
	 */
	static void setDifficulty(String difficulty) {
		switch (difficulty) {
			case "VERY EASY" -> DIFFICULTY_ALT = 0;
			case "EASY" -> DIFFICULTY_ALT = 1;
			case "MEDIUM" -> DIFFICULTY_ALT = 2;
			case "HARD" -> DIFFICULTY_ALT = 3;
			case "VERY HARD" -> DIFFICULTY_ALT = 4;
			default -> DIFFICULTY_ALT = DIFFICULTY;
		}
	}
}

