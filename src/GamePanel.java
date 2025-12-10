
/*
 * GamePanel.java
 */

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.AlphaComposite;
import java.awt.RenderingHints;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import static java.awt.event.KeyEvent.*;

/**
 * GamePanel class <br>
 * handles the main gameplay screen, including tile falling, input detection, score and combo display
 *  - uses existing game logic (timer and key events)
 *  - white glow tiles for combo < 25, gold glow tiles for combo >= 25
 *  - gold font for score, with combo count and a pulse animation on increment
 *  - floating musical note effects when combo is > 25
 * - extends JPanel
 * - implements KeyListener
 * - implements ActionListener
 */
@SuppressWarnings("FieldCanBeLocal")
public class GamePanel extends JPanel implements KeyListener, ActionListener {
	// Panel state
	private boolean play = false;
	private boolean over = false;
	private boolean finished = false;
	private boolean paused = false;
	private int fps = Config.FPS; // for spawn rate
	// Timing
	private final Timer timer;
	private final int speed;
	// Tile State
	private boolean[] tilesCheck = new boolean[4]; // active tile in lane
	private int[] tilesY = new int[4]; // tile Y positions
	private float comboPulse = 1.0f; // current scale for combo text pulse effect
	// Input
	private boolean[] keyPressedFlags = new boolean[4];
	// Foul indicator
	private boolean foul = false;
	private boolean flash = false; // flash effect (future use)
	private boolean hit = false;
	private float alpha = 1.0f;
	private int foulColumn = -1;
	private int foulY = 0;
	// Components
	private final Button buttonPainter = new Button();
	private final Tiles tilePainter = new Tiles();
	private final GameText gameText = new GameText();
	private final Sounder sounder = new Sounder();
	private final ScoreCalculate scorer = new ScoreCalculate();
	private final String difficultyLabel;
	private final int songIndex;
	private Audio gameMusic;
	private Random rng = new Random();

	/**
	 * NoteParticle class <br>
	 * local class
	 * - creates floating note objects for high combos
	 */
	private static class NoteParticle {
		int x;
		int y;
		int Width = 40;
		int Height = 40;
		float alpha;
		/**
		 * NoteParticles(x,y) <br>
		 * constructor
		 * - creates new floating note objects
		 * @param x (int) starting x coordinate for new objects
		 * @param y (int) starting y coordinate for new objects
		 */
		public NoteParticle(int x, int y, int width, int height) {
			this.x = x;
			this.y = y;
			this.Width = width;
			this.Height = height;
			this.alpha = 1.0f;
		}
	}

	/**
	 * Explosion class <br>
	 * local class
	 * - creates explosion effect for tiles or other game objects
	 */
	private static class Explosion {
		// local variables
		int x, y, r;
		int maxRadius;
		/**
		 * Explosion(x,y,r,max) <br>
		 * constructor
		 * - creates new explosion objects
		 * @param x (int) the x parameter for explosion
		 * @param y (int) the y parameter for explosion
		 * @param r (int) the initial radius of explosion
		 * @param max (int) the maximum radius of explosion
		 */
		public Explosion(int x, int y, int r, int max) {
			this.x = x;
			this.y = y;
			this.r = r;
			this.maxRadius = max;
		}
		/**
		 * update() <br>
		 * private update for explosions
		 * - updates the graphics effect for explosions
		 * @return boolean true if update is in progress
		 */
		public boolean update() {
			r++;
			return r >= maxRadius;
		}
		/**
		 * drawExplosion(g) <br>
		 * private method
		 * - draws explosion effect
		 * @param g graphics object passed in from caller
		 */
		public void drawExplosion(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			//g.setColor(new Color(255, 255, 255, 128));
			g.setColor(Config.ACCENT_COLOR);
			g2.drawOval((x - r), (y - r), 2 * r, 2 * r);
		}
	}

	// array list of explosions
	private final ArrayList<Explosion> explosions = new ArrayList<>();
	// array list of note particles
	private final ArrayList<NoteParticle> floatingNotes = new ArrayList<>();
	// allows return to main menu after game over
	private final Runnable returnToMenuCallback;

	/**
	 * GamePanel(difficulty,songIndex) <br>
	 * constructor
	 * - sets up the game board with default parameters
	 * @param difficulty (String) difficulty passed in by caller
	 * @param songIndex (int) song index passed in by caller
	 */
	public GamePanel(String difficulty, int songIndex, Runnable returnToMenuCallback) {
		this.returnToMenuCallback = returnToMenuCallback;
		this.difficultyLabel = difficulty;
		this.songIndex = songIndex;
		// game board parameters
		setSize(Config.WIDTH, Config.HEIGHT);
		setLayout(null);
		setOpaque(true);
		setFocusable(true);
		addKeyListener(this);
		setBackground(Config.BACKGROUND_COLOR);
		// Set tile falling speed
		switch (difficulty) {
			case "VERY EASY": speed = 1; break;
			case "EASY":      speed = 2; fps = 120; break;
			case "MEDIUM":    speed = 3; fps = 120; break;
			case "HARD":      speed = 4; fps = 120; break;
			case "VERY HARD": speed = 5; fps = 120; break;
			default:          speed = Config.SPEED; // HARD
		}
		resetGameState();
		// Start music
		//gameMusic = new Audio(songIndex);
		//gameMusic.startAudio();
		// Start music (only if sound is enabled)
		if (Config.SOUND_ENABLED) {
			gameMusic = new Audio(songIndex);
			gameMusic.startAudio();
		} else {
			gameMusic = null;
		}

		timer = new Timer(1000 / fps, this);
		timer.start();
		play = true;
	}

	/**
	 * resetGameState()
	 * - reset game state variables for new play session
	 */
	private void resetGameState() {
		Arrays.fill(tilesCheck, false);
		Arrays.fill(tilesY, -9999);
		// Spawn 1 initial random tile to avoid empty screen
		spawnRandomTile();
		scorer.score = 0;
		scorer.comboCount = 0;
		comboPulse = 1.0f;
		scorer.hits = 0;
		scorer.misses = 0;
		scorer.wrongs = 0;
		scorer.goldMode = false;
		scorer.whiteMode = false;
		foul = false;
		over = false;
		finished = false;
		foulColumn = -1;
	}

	/**
	 * restartGame() <br>
	 * resets game during play
	 */
	private void restartGame() {
		resetGameState();
		// restart music
		if (gameMusic != null) {
			gameMusic.stopAudio();
			gameMusic = null;
		}
		if (Config.SOUND_ENABLED) {
			gameMusic = new Audio(songIndex);  // use the selected song
			gameMusic.startAudio();
		}
		play = true;
		over = false;
		paused = false;
		timer.start();
		requestFocusInWindow();
	}

	/**
	 * spawnRandomTile() <br>
	 * generates new tile in a random column at the top of the screen
	 */
	private void spawnRandomTile() {
		int col = rng.nextInt(4);
		// If lane already has a tile, pick a different lane
		int safeGuard = 0;
		while (tilesCheck[col] && safeGuard < 10) {
			col = rng.nextInt(4);
			safeGuard++;
		}
		tilesCheck[col] = true;
		tilesY[col] = -600;
	}

	/**
	 * paintComponent(g) <br>
	 * - paints the requested objects on the panel
	 * @param g (Graphics) object to paint
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		// Draw game background (dark navy) and lane separators
		setBackground(Config.BACKGROUND_COLOR);
		g2.setColor(Config.BACKGROUND_COLOR);
		g2.fillRect(0, 0, Config.WIDTH, Config.HEIGHT);
		// Lane separator lines with glow (using accent color translucent)
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		// draw 4 lanes
		g2.setStroke(new java.awt.BasicStroke(8f));
		g2.setColor(Config.LANE_COLOR);
		g2.drawLine((Config.WIDTH / 4), 0, (Config.WIDTH / 4), Config.HEIGHT);
		g2.drawLine((Config.WIDTH / 4) * 2, 0, (Config.WIDTH / 4) * 2, Config.HEIGHT);
		g2.drawLine((Config.WIDTH / 4) * 3, 0, (Config.WIDTH / 4) * 3, Config.HEIGHT);

		// draw hit line = 550, perfect hit window = 550 - 750
		g2.drawLine(0, Config.BOTTOM_BOUND, Config.WIDTH, Config.BOTTOM_BOUND);

		// draw hit zone
		g2.setColor(Config.HIT_ZONE_COLOR);
		g2.fillRect(0, Config.BOTTOM_BOUND, Config.WIDTH, Config.HEIGHT - Config.BOTTOM_BOUND);

		// Draw tiles (uses white or gold tile images depending on goldMode)
		tilePainter.drawTiles(g, tilesCheck, tilesY, play, scorer.goldMode, scorer.whiteMode);

		// Draw Explosion effects
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

		if (explosions != null) {
			for (Explosion ex : explosions) {
				// draw note with its current alpha
				g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
				ex.drawExplosion(g);
			}
			// reset composite
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
		}
		// Draw a red "missed" tile if a foul (miss) occurred
		if (foul) {
			tilePainter.drawFoul(g, foulColumn, foulY);
		}
		if (paused) {
			gameText.drawPaused(g, scorer.score);
		}
		// Draw the bottom control buttons (lane indicators)
		buttonPainter.gameButton(g, keyPressedFlags);
		// Draw score and combo HUD
		gameText.drawScoreHud(g, scorer.score, scorer.comboCount, comboPulse, scorer.accuracy(), scorer.misses);
		// If game over, overlay "Game Over" text and prompt
		if (over) {
			if (finished) {
				gameText.drawWin(g, scorer.score);
			} else {
				gameText.drawGameOver(g, scorer.score);
			}
		}

		try {
			// Draw floating note particles for high combos
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
			BufferedImage noteImg = AssetManager.getImage("note2");
			if (noteImg != null) {
				for (NoteParticle np : floatingNotes) {
					// draw note with its current alpha
					float alpha = np.alpha;
					g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
					if (np.x == 0) {
						noteImg = AssetManager.getImage("note0");
					} else if (np.x == 1) {
						noteImg = AssetManager.getImage("note1");
					} else if (np.x == 2) {
						noteImg = AssetManager.getImage("note2");
					} else {
						noteImg = AssetManager.getImage("note3");
					}
					g2.drawImage(noteImg, np.x - 10, np.y - 10, np.Width, np.Height, null);
				}
				// reset composite
				g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
			}
		} catch (Exception e) {
			System.err.println("File Not Found: " + e.getMessage());
		}
	}

	/**
	 * isWithinHitWindow(lane) <br>
	 * check if the tile in the given lane is within the hit window for a successful hit
	 * @param lane (int) index of the lane (0-3)
	 * @return true if tile is close enough to be considered a hit, false if a miss
	 */
	private boolean isWithinHitWindow(int lane) {
		int y = tilesY[lane];
		return y >= Config.HIT_MIN && y <= Config.HIT_MAX;
	}

	/**
	 * actionPerformed(e) <br>
	 * checks for ActionEvents
	 * - inherited from ActionListener
	 * @param e (ActionEvent) the event to be processed
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (play && !over) {
			if (!gameMusic.isPlaying()) {
				finished = true;
				over = true;
				play = false;
			}
			// Move tiles
			for (int col = 0; col < 4; col++) {
				if (tilesCheck[col]) {
					tilesY[col] += speed;
				}
			}
			// missed tile detection
			for (int col = 0; col < 4; col++) {
				//if (tilesCheck[col] && tilesY[col] >= Config.BOTTOM_BOUND) {
				if (tilesCheck[col] && tilesY[col] >= Config.HEIGHT) {
					// Register miss
					scorer.registerMiss();
					sounder.playLane(col);
					foul = true;
					foulColumn = col;
					foulY = tilesY[col];
					// Clear lane
					tilesCheck[col] = false;
					if (scorer.misses >= 10) {
						over = true;
						play = false;
					} else {
						foul = false;
					}
					spawnRandomTile();
				}
			}
		}
		for (int i = 0; i < explosions.size(); i++) {
			boolean remove = explosions.get(i).update();
			if (remove) {
				explosions.remove(i);
				i--;
			}
		}
		Iterator<NoteParticle> it = floatingNotes.iterator();
		while (it.hasNext()) {
			NoteParticle np = it.next();
			if (scorer.goldMode)
			{
				np.Width += 2;
				np.Height += 4;
				np.y -= 3;          // move up
				np.alpha -= 0.01f;  // fade out
			} else {
				np.Width += 2;
				np.Height += 2;
				np.y -= 2;          // move up
				np.alpha -= 0.02f;  // fade out
			}
			if (np.alpha <= 0f) {
				it.remove();    // remove note when fully faded
			}
		}

		// Update combo pulse animation (shrink back to normal)
		if (comboPulse > 1.0f) {
			comboPulse -= 0.05f;
			if (comboPulse < 1.0f) {
				comboPulse = 1.0f;
			}
		}


		repaint();
		if (over && gameMusic != null) {
			gameMusic.stopAudio();
			gameMusic = null;
		}
	}

	/**
	 * keyPressed(e) <br>
	 * checks for key events
	 * - inherited from KeyListener
	 * @param e (KeyEvent) the event to be processed
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		// ----- ENTER returns to song menu when game is over -----
		if (over && code == VK_ENTER) {
			if (gameMusic != null) {
				gameMusic.stopAudio();
				gameMusic = null;
			}
			timer.stop();
			returnToMenuCallback.run();
			return;
		}
		// ----- ESC aborts current game WITHOUT saving stats -----
		if (!over && code == VK_ESCAPE) {
			play = false;
			// don't mark over = true; we are just aborting
			if (gameMusic != null) {
				gameMusic.stopAudio();
				gameMusic = null;
			}
			timer.stop();
			Application.abortGameFromEsc();
			return;
		}
		// Ignore normal input if not playing
		if (!play) { return; }
		// 1–4 lanes
		int lane = switch (code) {
			case VK_1 -> 0;
			case VK_2 -> 1;
			case VK_3 -> 2;
			case VK_4 -> 3;
			default -> -1;
		};
		if (lane == -1) return;
		// set keyPressedFlag for the lane
		keyPressedFlags[lane] = true;
		// HIT?
		if (tilesCheck[lane] && isWithinHitWindow(lane)) {
			// generate explosion effects for tile
			int xCenter = (lane * Config.TILE_WIDTH) + (Config.TILE_WIDTH / 2);
			explosions.add(new Explosion(xCenter, tilesY[lane] + (Config.TILE_HEIGHT / 2), 0, 75));
			// if combo reached, generate white tiles and a floating note at hit location
			if (scorer.whiteMode) {
				int noteXCenter = (lane * Config.TILE_WIDTH) + (Config.TILE_WIDTH / 2);
				floatingNotes.add(new NoteParticle(noteXCenter, Config.BOTTOM_BOUND, 40, 40));
			}
			// If high combo, generate gold tiles and a floating note effect at hit location
			if (scorer.goldMode) {
				int noteXCenter = (lane * Config.TILE_WIDTH) + (Config.TILE_WIDTH / 2);
				floatingNotes.add(new NoteParticle(noteXCenter, Config.BOTTOM_BOUND, 60, 60));
			}
			// register hit
			scorer.registerHit(lane);
			// trigger combo pulse effect
			comboPulse = 1.5f;
			// Clear tile
			tilesCheck[lane] = false;
			// spawn new random tile
			spawnRandomTile();
		} else {
			// register wrong button pressed
			scorer.registerWrong();
			// game over if missed >= 10
			if (scorer.misses >= 10) {
				foul = true;
				foulColumn = lane;
				foulY = tilesY[lane];
				over = true;
				play = false;
			}
		}
	}

	/**
	 * keyReleased(e) <br>
	 * invoked when a key has been released
	 * @param e (KeyEvent) the event to be processed
	 */
	@Override public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		if (code == KeyEvent.VK_1) keyPressedFlags[0] = false;
		if (code == KeyEvent.VK_2) keyPressedFlags[1] = false;
		if (code == KeyEvent.VK_3) keyPressedFlags[2] = false;
		if (code == KeyEvent.VK_4) keyPressedFlags[3] = false;
	}

	/**
	 * keyTyped (e) <br>
	 * @param e (KeyEvent) the event to be processed
	 */
	@Override public void keyTyped(KeyEvent e) {}

	/**
	 * log() <br>
	 * handles logging score to output file
	 */
	private void log() {
		Instant timestamp = Instant.now();
		LocalDateTime ldt = LocalDateTime.ofInstant(timestamp, ZoneId.systemDefault());

		// determine which user is logged in
		String userId = Session.getCurrentUserId();
		if (userId == null || userId.isEmpty()) {
			userId = "anonymous";
		}

		// per-user log file
		String filename = "output_" + userId + ".txt";
		try (PrintWriter output = new PrintWriter(new FileWriter(filename, true))) {
			output.println("--------------------------------");
			output.print("User: ");
			output.println(userId);
			output.print("Date/Time: ");
			output.printf("%s %d %d at %d:%d%n",
					ldt.getMonth(), ldt.getDayOfMonth(), ldt.getYear(),
					ldt.getHour(), ldt.getMinute());
			output.println("Score: " + scorer.score);
			output.println("Combo: " + scorer.comboCount);
			output.println("Max combo: " + scorer.maxCombo);
			output.println("Hits: " + scorer.hits);
			output.println("Misses: " + scorer.misses);
			output.println("Wrongs: " + scorer.wrongs);
			output.println("--------------------------------");
		} catch (IOException e) {
			System.err.println("Error writing stats for " + userId + ": " + e.getMessage());
		}
	}

	/**
	 * getScorer() <br>
	 * returns the active ScoreCalculate Object to the caller to log
	 * @return (ScoreCalculate) current score keeper
	 */
	public ScoreCalculate getScorer() {
		return scorer;
	}

	/**
	 * getDifficultyLabel() <br>
	 * returns the difficulty to caller to log
	 * @return (String) difficulty from current session
	 */
	public String getDifficultyLabel() {
		return difficultyLabel;
	}

	/**
	 * getSongIndex() <br>
	 * returns the song that was selected to caller to log
	 * @return (int) song index from current session
	 */
	public int getSongIndex() {
		return songIndex;
	}
}




