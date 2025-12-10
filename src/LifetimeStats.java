
/*
 * LifetimeStats.java
 */

/**
 * LifetimeStats class <br>
 * aggregates the statistics over all games for a user
 * - plus "best" performance for a particular song/difficulty.
 */
public class LifetimeStats {
	public String userId;
	// lifetime totals over all games (all songs / difficulties)
	public int totalGames;
	public int totalHits;
	public int totalMisses;
	public int totalErrors;
	public int totalScore;
	public double lifetimeAccuracyPercent;
	// best performance for the currently queried song/difficulty
	public int bestScoreForCurrent;
	public double bestAccuracyForCurrent;
	public int bestMaxComboForCurrent;
}
