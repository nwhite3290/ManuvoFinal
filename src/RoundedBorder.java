
/*
 * RoundedBorder.java
 */

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.*;

/**
 * RoundedBorder class <br>
 * a custom Border implementation for drawing round-corner outlines
 * - implements Border for menu buttons
 */
class RoundedBorder implements Border {
	private final int radius;
	private final Color borderColor;

    /**
     * RoundedBorder(radius,color) <br>
     * constructor for RoundedBorder objects
     * @param radius (int) default radius value for new object corners
     * @param color (Color) user defined color for new object
     */
    public RoundedBorder(int radius, Color color) {
		this.radius = radius;
		this.borderColor = color;
	}

    /**
     * paintBorder(c,g,x,y,width,height) <br>
     * paints the border around the object
     * @param c (Component) the component for which this border is being painted
     * @param g (Graphics) the graphics object passed in from caller
     * @param x (int) the x position of the painted border
     * @param y (int) the y position of the painted border
     * @param width (int) the width of the painted border
     * @param height (int) the height of the painted border
     */
	@Override
	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		Graphics2D g2 = (Graphics2D) g.create();
		// Enable antialiasing for smooth corners
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		// If the button is in rollover (hover) state and opaque, it is filled with accent color
		// draw an outline with background color for contrast; Otherwise, draw outline in accent color
		Color outlineColor = borderColor;
		if (c instanceof JButton btn) {
			if (btn.getModel().isRollover() && btn.isOpaque()) {
				outlineColor = Config.BACKGROUND_COLOR;
			}
		}
		g2.setColor(outlineColor);
		g2.drawRoundRect(x + 2, y + 2, width - 4, height - 4, radius, radius);
		g2.dispose();
	}

    /**
     * getBorderInsets(c) <br>
     * gets the border inset
     * @param c (Component) the component for which this border inset value applies
     * @return (Inset) object requested by caller
     */
	@Override
    public Insets getBorderInsets(Component c) {
        return new Insets(radius, radius, radius, radius);
    }

    /**
     * isBorderOpaque() <br>
     * is the border opaque flag
     * @return (boolean) true if opaque, default == false
     */
    @Override
    public boolean isBorderOpaque() {
        return false;
    }
}


