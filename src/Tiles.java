
/*
 * Tiles.java
 */

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;

/**
 * Tiles class <br>
 * handles drawing of falling tiles on the game board.
 * - Tile objects are drawn as glowing rectangles using theme colors or as images for special effects
 */
public class Tiles {
    // dimensions for tiles
	int lane; // 0..3
	float y; // top y
	long spawnAtMs;
	// currently not used
	private int TILE_WIDTH = 150; // Config.TILE_WIDTH
	private int TILE_HEIGHT = 150; // Config.TILE_HEIGHT
	int eX;
	int eY;
	int radius = 0;
	int maxRadius = 30;
	float alpha = 1.0f;
	boolean active = true;
	boolean judged = false;

	/**
	 * Tiles() <br>
	 * default constructor for new tiles object
	 */
	public Tiles() {}

	/**
	 * Tiles(lane,y,spawnAtMs) <br>
	 * constructor for new Tiles object
	 * @param lane (int) lane to spawn tile
	 * @param y (float) y value of top left corner of tile
	 * @param spawnAtMs (long) spawn time in milliseconds
	 */
	public Tiles(int lane, float y, long spawnAtMs) {
		this.lane = lane;
		this.y = y;
		this.spawnAtMs = spawnAtMs;
	}

	/**
	 * bounds(laneX,laneW) <br>
	 * sets bounds for rectangle if no picture is available
	 * @param laneX (int) x value of lane to draw tile
	 * @param laneW (int) width value of new tile
	 * @return (Rectangle) object to place on game board
	 */
	Rectangle bounds(int laneX, int laneW) {
		return new Rectangle(laneX, Math.round(y), laneW, Config.TILE_HEIGHT);
	}

    /**
     * drawTiles(g,tilesCheck,tilesY,play,useGoldTiles,useWhiteTiles) <br>
     * Draws the falling tiles for each active column.
     * - Uses white glow tiles normally, and gold glow tiles when useGoldTiles is true (combo >= 25).
     * @param g (Graphics) object to draw
     * @param tilesCheck (boolean[]) boolean flags indicating which columns currently have tiles
     * @param tilesY (int[]) vertical positions of the tiles in each column
     * @param play (boolean)  true if the game is in play (has started)
     * @param useGoldTiles (boolean) whether to draw tiles in gold mode (activated by high combo)
     * @param useWhiteTiles (boolean) whether to draw tiles in white mode (activated by high combo)
     */
    public void drawTiles(Graphics g, boolean[] tilesCheck, int[] tilesY, boolean play, boolean useGoldTiles, boolean useWhiteTiles) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (!play) {
            // Game not started yet (this state might not be used in the new flow since game starts immediately)
	        //g2.setColor(Objects.requireNonNullElse(Config.ACCENT_COLOR_ALT, Config.ACCENT_COLOR).darker());
	        //g2.setColor(Config.BACKGROUND_COLOR);
            //g2.fillRoundRect(0, 350, TILE_WIDTH - 12, TILE_HEIGHT, 30, 30);
            //g2.setColor(Color.WHITE);
            //g2.setFont(new Font("SansSerif", Font.BOLD, 40));
            //g2.drawString("START", 25, 450);
            return;
        }
        try {
            // Choose tile image based on combo mode
	        BufferedImage blackTileImg = AssetManager.getImage("tile_black");
            BufferedImage whiteTileImg = AssetManager.getImage("tile_white");
			BufferedImage goldTileImg = AssetManager.getImage("tile_gold");
            for (int col = 0; col < 4; col++) {
                if (tilesCheck[col]) {
                    int x = col * Config.TILE_WIDTH;
                    int y = (int)tilesY[col];
                    if (useGoldTiles && goldTileImg != null) {
                        g2.drawImage(goldTileImg, x + 6, y, Config.TILE_WIDTH - 12, Config.TILE_HEIGHT, null);
                    } else if (useWhiteTiles && whiteTileImg != null ) {
                        g2.drawImage(whiteTileImg, x + 6, y, Config.TILE_WIDTH - 12, Config.TILE_HEIGHT, null);
                    } else if (!useGoldTiles && !useWhiteTiles && blackTileImg != null ) {
	                    g2.drawImage(blackTileImg, x + 6, y, Config.TILE_WIDTH - 12, Config.TILE_HEIGHT, null);
                    } else {
                        // Fallback: draw colored rectangle tiles if images not available
                        Color baseColor = useGoldTiles ? new Color(255, 215, 0) : Color.WHITE;
                        Color glowColor = useGoldTiles ? new Color(255, 215, 0, 100) : new Color(255, 255, 255, 100);
                        g2.setColor(baseColor);
                        g2.fillRoundRect(x + 6, y, Config.TILE_WIDTH - 12, Config.TILE_HEIGHT, 30, 30);
                        g2.setColor(glowColor);
                        g2.fillRoundRect(x + 6, y, Config.TILE_WIDTH - 12, Config.TILE_HEIGHT, 30, 30);
                    }
                }
            }
        }
        catch (Exception e) {
            System.err.println("File Not Found: " + e.getMessage());
        }
    }

    /**
     * drawFoul(g,foulCol,foulY) <br>
     * draws a red tile to indicate a missed note (foul) at the given column and position
     * @param g (Graphics) object to draw
     * @param foulCol (int) index of column where the miss occurred
     * @param foulY (int) vertical position of the missed tile
     */
    public void drawFoul(Graphics g, int foulCol, int foulY) {
        g.setColor(new Color(255, 0, 0, 180));
        g.fillRoundRect(foulCol * Config.TILE_WIDTH + 6, foulY, Config.TILE_WIDTH - 12, Config.TILE_HEIGHT, 30, 30);
    }

	/**
	 * drawFlash(g,flashCol,flashY) <br>
	 * flashes the lane to indicate (foul) at the given column and position
	 * - not currently used
	 * @param g (Graphics) object to draw
	 * @param flashCol (int) index of column where the miss occurred
	 * @param flashY (int) vertical position of the missed tile
	 */
	public void drawFlash(Graphics g, int flashCol, int flashY) {
		g.setColor(new Color(255, 255, 255, 180));
		g.fillRect(flashCol * Config.TILE_WIDTH + 6, flashY, Config.TILE_WIDTH - 12, Config.HEIGHT);
	}
}

