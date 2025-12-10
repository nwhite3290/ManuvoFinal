
/*
 * ScoreCalculate.java
 */

/**
 * ScoreCalculate class <br>
 * displays the players score during gameplay
 */
public class ScoreCalculate {
	public int score = 0;
	public int comboCount = 0;
	public int maxCombo = 0;
	public int hits = 0;
	public int misses = 0;
	public int wrongs = 0;
	public int add;
	public boolean goldMode = false; // gold tiles flag when (combo >= 25)
	public boolean whiteMode = false; // white tiles flag when (combo >= 10)

	/**
	 * score(place) <br>
	 * decides the congratulatory message based on the score
	 * @param place (int) location to display the message
	 * @return (String) message to display
	 */
	static String score(int place) {
		if(place >= 300) {
			return "Perfect!!";
		}
		else if (place < 299 && place >= 150) {
			return "Great!";
		}
		else {
			return "Ok";
		}
	}

	/**
	 * registerHit(quality) <br>
	 * called on successful hit
	 * - handles game scoring
	 * @param quality (int) hit location to window passed in by caller
	 */
	void registerHit(int quality) {
		// quality: 0 = perfect, 1 = good, 2 = late/early (still ok)
		add = switch (quality) {
			case 0 -> 300;
			case 1 -> 150;
			default -> 100;
		};
		score += add + (comboCount * add);
		hits++;
		comboCount++;
		if (comboCount > maxCombo) {
			maxCombo = comboCount;
		}
		// Update combo pulse animation (shrink back to normal)
		if (comboCount >= 10 && !whiteMode) {
			whiteMode = true;
		}
		if (comboCount >= 25 && !goldMode) {
			goldMode = true;
		}
	}

	/**
	 * registerMiss() <br>
	 * called on missed tile
	 */
	void registerMiss() {
		misses++;
		score -= 100;
		comboCount = 0;
		goldMode = false;
		whiteMode = false;
	}

	/**
	 * registerWrong() <br>
	 * called when wrong lane is hit
	 */
	void registerWrong() {
		misses++;
		wrongs++;
		score -= 100;
		comboCount = 0;
		goldMode = false;
		whiteMode = false;
	}

	/**
	 * accuracy() <br>
	 * calculates accuracy of game
	 * @return (double) accuracy as a percentage
	 */
	double accuracy() {
		int total = hits + misses + wrongs;
		if (total == 0) return 100.0;
		return (hits * 100.0) / total;
	}

	/**
	 * reset() <br>
	 * resets all values t0 zero
	 * - used when game is reset during play
	 */
	void reset() {
		score = 0;
		comboCount = 0;
		maxCombo = 0;
		hits = 0;
		misses = 0;
		wrongs = 0;
		goldMode = false;
		whiteMode = false;
	}
}


