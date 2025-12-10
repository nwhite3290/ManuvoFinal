
/*
 * Sounder.java
 */

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Synthesizer;
import javax.swing.*;

/**
 * Sounder class <br>
 * adds synthesized sound whenever desired
 * - added to missed tiles and erroneous key presses
 */
public class Sounder {
	private Synthesizer synth;
	private MidiChannel ch;
	private final int[] laneNotes = {60, 64, 67, 72}; // C4, E4, G4, C5

	/**
	 * Sounder() <br>
	 * constructor
	 * - creates a new sounder object for gameplay sounds
	 */
	Sounder() {
		try {
			synth = MidiSystem.getSynthesizer();
			synth.open();
			MidiChannel[] channels = synth.getChannels();
			if (channels != null && channels.length > 0) {
				ch = channels[0];
				ch.programChange(0); // default piano
			}
		} catch (Exception ignored) {
			System.err.println("Error opening sound channel: " + ignored.getMessage());
		}
	}

	/**
	 * playLane(lane) <br>
	 * uses the lane to determine what sound to play
	 * @param lane (int) current lane passed in by caller
	 */
	void playLane(int lane) {
		if (ch == null) return;
		lane = Math.max(0, Math.min(lane, laneNotes.length - 1));
		int note = laneNotes[lane];
		// short blip
		ch.noteOn(note, 100);
		// schedule noteOff
		new Timer(120, e -> ch.noteOff(note)).start();
	}

	/**
	 * close() <br>
	 * closes the sounder object if left open
	 */
	void close() {
		if (synth != null && synth.isOpen()) synth.close();
	}
}
