
/*
 * Button.java
 */

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.util.*;

/**
 * Button class <br>
 * provides custom drawing for in-game control buttons
 * - (the round lane indicators at the bottom)
 * - uses the theme accent color for the button highlights and provides feedback when keys are pressed
 */
public class Button {
	/**
	 * gameButton(g, keyPressed) <br>
	 * draws the four lane control buttons
	 * - (circles with numbers 1-4 at the bottom of the game screen)
	 * - each button lights up when its corresponding key is pressed
	 * @param g (Graphics) context passed in from caller
	 * @param keyPressed (boolean) array of booleans indicating which keys (1-4) are currently pressed
	 */
	public void gameButton(Graphics g, boolean[] keyPressed) {
		// Coordinates and size for the circles
		int[] xPositions = {40, 190, 340, 490};
		int y = 625;
		int size = 60;
		g.setFont(new Font("SansSerif", Font.BOLD, 34));
		for (int i = 0; i < 4; i++) {
			// Outer circle (dark background)
			g.setColor(new Color(20, 20, 20));
			g.fillOval(xPositions[i], y, size, size);
			// Inner circle (accent color or highlight if pressed)
			if (keyPressed[i]) {
				g.setColor(Color.WHITE);  // flash white when pressed
			} else {
				g.setColor(Config.ACCENT_COLOR);
			}
			g.fillOval(xPositions[i] + 4, y + 4, size - 8, size - 8);
			// Label number
			g.setColor(Color.BLACK);
			g.drawString(String.valueOf(i + 1), xPositions[i] + 20, y + 44);
		}
	}
}



