
/*
 * StatsManager.java
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * StatsManager class <br>
 * handles the user stats
 */
public class StatsManager {

    // directory where all stats files live
    private static final String STATS_DIR = "saved_stats";

	/**
	 * ensureDir() <br>
	 * private class <br>
	 * verifies the existence of the stats directory
	 * - creates the directory if it is not available
	 * @return (FILE) stats directory "STATS_DIR"
	 */
    private static File ensureDir() {
        File dir = new File(STATS_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

	/**
	 * saveLastStats(stats) <br>
	 * save the latest game stats for a user
	 * - update lifetime aggregates
	 * - writes a per-user "last game" file
	 * @param stats (UserStats) stats to write passed in by caller
	 */
    public static void saveLastStats(UserStats stats) {
        if (stats == null || stats.userId == null || stats.userId.isEmpty()) {
            throw new IllegalArgumentException("stats.userId must not be null/empty");
        }
        ensureDir();
        File file = new File(STATS_DIR, "last_" + stats.userId + ".properties");
        Properties p = new Properties();
        p.setProperty("userId", stats.userId);
        p.setProperty("songIndex", Integer.toString(stats.songIndex));
        p.setProperty("difficulty", stats.difficulty != null ? stats.difficulty : "");
        p.setProperty("hits", Integer.toString(stats.hits));
        p.setProperty("misses", Integer.toString(stats.misses));
        p.setProperty("score", Integer.toString(stats.score));
        p.setProperty("timeText", stats.timeText != null ? stats.timeText : "");
        p.setProperty("accuracyPercent", Double.toString(stats.accuracyPercent));
        p.setProperty("maxCombo", Integer.toString(stats.maxCombo));
        p.setProperty("lastComboCount", Integer.toString(stats.lastComboCount));
        try (FileOutputStream out = new FileOutputStream(file)) {
            p.store(out, "Last stats for " + stats.userId);
        } catch (IOException e) {
			System.err.println("Error saving last stats for " + stats.userId + ", " +  e.getMessage());
        }
        // update lifetime aggregate & per-song best
        updateLifetime(stats);
    }

    // alias if you want a generic name
    public static void saveStats(UserStats stats) {
        saveLastStats(stats);
    }

	/**
	 * loadLastStats(userId) <br>
	 * load last-game stats for a user
	 * - returns null if no user exists
	 * @param userId (String) user ID passed in by caller
	 * @return (UserStats) stats for user
	 */
    public static UserStats loadLastStats(String userId) {
        if (userId == null || userId.isEmpty()) {
            return null;
        }
        ensureDir();
        File file = new File(STATS_DIR, "last_" + userId + ".properties");
        if (!file.exists()) {
            return null;
        }
        Properties p = new Properties();
        try (FileInputStream in = new FileInputStream(file)) {
            p.load(in);
        } catch (IOException e) {
			System.err.println("Error loading last stats for " + userId + ": " + e.getMessage());
            return null;
        }
        UserStats s = new UserStats();
        s.userId = p.getProperty("userId", userId);
        s.songIndex = parseInt(p.getProperty("songIndex"), 0);
        s.difficulty = p.getProperty("difficulty", "");
        s.hits = parseInt(p.getProperty("hits"), 0);
        s.misses = parseInt(p.getProperty("misses"), 0);
        s.errors = parseInt(p.getProperty("errors"), 0);
        s.score = parseInt(p.getProperty("score"), 0);
        s.timeText = p.getProperty("timeText", "");
        s.accuracyPercent = parseDouble(p.getProperty("accuracyPercent"), 0.0);
        s.maxCombo = parseInt(p.getProperty("maxCombo"), 0);
        s.lastComboCount = parseInt(p.getProperty("lastComboCount"), 0);
        return s;
    }

	/**
	 * loadStats(userId) <br>
	 * returns the last stats to the caller
	 * @param userId (String) user ID for desired stats
	 * @return (UserStats) last stats for desired user
	 */
    public static UserStats loadStats(String userId) {
        return loadLastStats(userId);
    }

	/**
	 * updateLifetime(stats) <br>
	 * internal method <br>
	 * update lifetime totals and per-song difficulty personal best
	 * @param stats (UserStats) stats to update
	 */
    private static void updateLifetime(UserStats stats) {
        ensureDir();
        File file = new File(STATS_DIR, "lifetime_" + stats.userId + ".properties");
        Properties p = new Properties();
        if (file.exists()) {
            try (FileInputStream in = new FileInputStream(file)) {
                p.load(in);
            } catch (IOException e) {
				System.err.println("Error loading last stats for " + stats.userId + ": " + e.getMessage());
            }
        }
        // Lifetime aggregates
        int totalGames  = parseInt(p.getProperty("lifetime.totalGames"), 0) + 1;
        int totalHits   = parseInt(p.getProperty("lifetime.totalHits"), 0) + stats.hits;
        int totalMisses = parseInt(p.getProperty("lifetime.totalMisses"), 0) + stats.misses;
        int totalErrors = parseInt(p.getProperty("lifetime.totalErrors"), 0) + stats.errors;
        int totalScore  = parseInt(p.getProperty("lifetime.totalScore"), 0) + stats.score;
        p.setProperty("lifetime.totalGames",  Integer.toString(totalGames));
        p.setProperty("lifetime.totalHits",   Integer.toString(totalHits));
        p.setProperty("lifetime.totalMisses", Integer.toString(totalMisses));
        p.setProperty("lifetime.totalErrors", Integer.toString(totalErrors));
        p.setProperty("lifetime.totalScore",  Integer.toString(totalScore));
        // per-song + difficulty "best" stats
        if (stats.difficulty != null && !stats.difficulty.isEmpty()) {
            String diffKey = encodeDifficulty(stats.difficulty);
            String prefix = "best." + stats.songIndex + "." + diffKey + ".";
            int prevBestScore = parseInt(p.getProperty(prefix + "score"), Integer.MIN_VALUE);
            double prevBestAcc = parseDouble(p.getProperty(prefix + "accuracyPercent"), -1.0);
            int prevBestCombo = parseInt(p.getProperty(prefix + "maxCombo"), Integer.MIN_VALUE);
            boolean improved = stats.score > prevBestScore;
            if (improved) {
                p.setProperty(prefix + "score", Integer.toString(stats.score));
                p.setProperty(prefix + "accuracyPercent", Double.toString(stats.accuracyPercent));
                p.setProperty(prefix + "maxCombo", Integer.toString(stats.maxCombo));
            } else if (stats.score == prevBestScore) {
                // tie-breakers on accuracy and combo
                if (stats.accuracyPercent > prevBestAcc ||
                        (stats.accuracyPercent == prevBestAcc && stats.maxCombo > prevBestCombo)) {
                    p.setProperty(prefix + "score", Integer.toString(stats.score));
                    p.setProperty(prefix + "accuracyPercent", Double.toString(stats.accuracyPercent));
                    p.setProperty(prefix + "maxCombo", Integer.toString(stats.maxCombo));
                }
            }
        }
        try (FileOutputStream out = new FileOutputStream(file)) {
            p.store(out, "Lifetime stats for " + stats.userId);
        } catch (IOException e) {
            System.err.println("Error saving last stats for " + stats.userId + ": " + e.getMessage());
        }
    }

	/**
	 * loadLifetimeSummary(userId,songIndex,difficulty) <br>
	 * returns lifetime totals including "best for this song & difficulty" to the caller
	 * @param userId (String) user id passed in from caller
	 * @param songIndex (int) song index passed in from caller
	 * @param difficulty (String) difficulty passed in from caller
	 * @return (LifetimeStats) summary of desired lifetime stats
	 */
    public static LifetimeStats loadLifetimeSummary(String userId, int songIndex, String difficulty) {
        if (userId == null || userId.isEmpty()) {
            return null;
        }
        ensureDir();
        File file = new File(STATS_DIR, "lifetime_" + userId + ".properties");
        if (!file.exists()) {
            return null;
        }
        Properties p = new Properties();
        try (FileInputStream in = new FileInputStream(file)) {
            p.load(in);
        } catch (IOException e) {
	        System.err.println("Error loading last stats for " + userId + ": " + e.getMessage());
            return null;
        }
        LifetimeStats s = new LifetimeStats();
        s.userId      = userId;
        s.totalGames  = parseInt(p.getProperty("lifetime.totalGames"), 0);
        s.totalHits   = parseInt(p.getProperty("lifetime.totalHits"), 0);
        s.totalMisses = parseInt(p.getProperty("lifetime.totalMisses"), 0);
        s.totalErrors = parseInt(p.getProperty("lifetime.totalErrors"), 0);
        s.totalScore  = parseInt(p.getProperty("lifetime.totalScore"), 0);
        int attempts = s.totalHits + s.totalMisses + s.totalErrors;
        s.lifetimeAccuracyPercent = attempts > 0 ? (100.0 * s.totalHits / attempts) : 0.0;
        if (songIndex >= 0 && difficulty != null && !difficulty.isEmpty()) {
            String diffKey = encodeDifficulty(difficulty);
            String prefix = "best." + songIndex + "." + diffKey + ".";
            s.bestScoreForCurrent    = parseInt(p.getProperty(prefix + "score"), 0);
            s.bestAccuracyForCurrent = parseDouble(p.getProperty(prefix + "accuracyPercent"), 0.0);
            s.bestMaxComboForCurrent = parseInt(p.getProperty(prefix + "maxCombo"), 0);
        }
        return s;
    }

	// --- helpers -------------------------------------------------------------

	/**
	 * parseInt(value,defaultValue) <br>
	 * parse integer value from a string
	 * @param value (String) value that contains the desired integer
	 * @param defaultValue (int) value to return to the caller
	 * @return (int) default value parsed from the String
	 */
    private static int parseInt(String value, int defaultValue) {
        if (value == null) return defaultValue;
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

	/**
	 * parseDouble(value,defaultValue) <br>
	 * parse double value from a string
	 * @param value (String) value that contains the desired double
	 * @param defaultValue (double) value to return to the caller
	 * @return (double) default value parsed from the String
	 */
    private static double parseDouble(String value, double defaultValue) {
        if (value == null) return defaultValue;
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

	/**
	 * encodeDifficulty(difficulty) <br>
	 * changes difficulty string to uppercase for use by the program
	 * @param difficulty (String) difficulty passed in by the caller
	 * @return (String) uppercase encoded difficulty
	 */
    private static String encodeDifficulty(String difficulty) {
        if (difficulty == null) {
            return "UNKNOWN";
        }
        return difficulty.trim().replaceAll("\\s+", "_").toUpperCase();
    }
}
