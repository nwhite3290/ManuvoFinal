
/*
 * Audio.java
 */

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.util.*;

/**
 * Audio class <br>
 * Handles the audio files
 * - starts and stops the music
 */
public class Audio {
	private boolean isPlaying = false;
	private int interval;
	int songLength;
	private Timer timer;
	String[] song = {
			"Music/1.wav", // Someone You Loved
			"Music/2.wav", // Memories
			"Music/3.wav", // Fur Elise
			"Music/4.wav", // Canon
			"Music/5.wav" // Moonlight Sonata
	};
	// preloaded audio track
	Clip clip;
	// audio sample
	AudioInputStream audioInputStream;

	/**
	 * Audio() <br>
	 * constructor for audio object
	 * - creates a new Audio music object
	 */
	public Audio() {
	}

	/**
	 * Audio(s) <br>
	 * chooses a song based on the number passed into the method
	 * - request songs (0 - 4) or change the number above when adding more music
	 * @param s (int) desired song number
	 */
	public Audio(int s) {
		timer = new Timer();
		// try to create a song clip
		try {
			// song file
			audioInputStream = AudioSystem.getAudioInputStream(new File(song[s]).getAbsoluteFile());
			// chosen piece of song to play
			clip = AudioSystem.getClip();
			// open the audioInputStream file as a clip
			clip.open(audioInputStream);
			// audio length for ending the game
			songLength = Integer.parseInt(clip.getMicrosecondLength() / 1000000 + "");
			// catch exception unable to read files
		} catch (Exception e) {
			// catch exception unable to read files
			System.out.println("Unable to load Audio file: " + e.getMessage());
		}
	}

	/**
	 * startAudio() <br>
	 * starts playing the audio clip
	 * - palys exactly 1 time before ending.
	 * - replace clip.loop(0) with,
	 * - clip.loop(Clip.LOOP_CONTINUOUSLY) to loop audio
	 */
	public void startAudio() {
		// start the music
		clip.start();
		isPlaying = true;
		//clip.loop(Clip.LOOP_CONTINUOUSLY);
		clip.loop(0);
		int delay = 1000;
		int period = 1000;
		// schedules remining audio time before returning isPlaying = false flag
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				int remaining = songLength + setInterval();
				// countdown timer output to stdout
				//System.out.println("Time remaining: " + remaining);
				if (remaining <= 0) {
					isPlaying = false;
				}
			}
		}, delay, period);
	}

	/**
	 * setInterval() <br>
	 * sets the countdown timer for 1 second ticks
	 * @return (int) interval = interval - 1
	 */
	private int setInterval() {
		if (interval == 1) {
			timer.cancel();
		}
		return --interval;
	}

	/**
	 * stopAudio() <br>
	 * stops playing the audio loop
	 */
	public void stopAudio() {
		// stop the music
		clip.stop();
	}

	/**
	 * isPlaying() <br>
	 * check if audio is playing flag
	 * @return (boolean) audio playing flag
	 */
	public boolean isPlaying() {
		return isPlaying;
	}
}
