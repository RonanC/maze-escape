package ie.gmit.sw.ai.audio;

import java.io.File;
import java.net.URL;
import java.util.Random;

import javax.sound.sampled.*;

public class SoundEffects {
	public static synchronized void playSound(final String audioName, boolean loop) {
		String url = "resources/audio/" + audioName;
		File file = new File(url);

		new Thread(new Runnable() {
			// The wrapper thread is unnecessary, unless it blocks on the
			// Clip finishing; see comments.
			public void run() {
				try {
					Clip clip = AudioSystem.getClip();
					AudioInputStream inputStream = AudioSystem.getAudioInputStream(file);
					clip.open(inputStream);
					if (loop == true) {
						clip.loop(Clip.LOOP_CONTINUOUSLY);
					} else {
						clip.start();
					}

				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
		}).start();
	}

	public static void playWin() {
		playSound("win", false);
	}

	public static void playMove() {
		playSound("move", false);
	}

	public static void playFight() {
		String location = "fight/";

		Random random = new Random();
		int num = random.nextInt(2);

		switch (num) { // add more insults
		case 0:
			location += "come_on.wav";
			break;

		case 1:
			location += "come_on.wav";
			break;

		default:
			location += "come_on.wav";
			break;
		}

		playSound(location, false);
	}

	public static void playWonFight() {
		playSound("fight/terminated.wav", false);
	}

	public static void playLostFight() {
		playSound("fight/cry.wav", false); // ?
	}

	public static void playFoundItem() {
		playSound("items/excellent3.wav", false); // ?
	}

	public static void playFoundHelp() {
		playSound("items/lets_rock.wav", false); // ?
	}

	public static void playGameOver() {
		playSound("fight/game_over.wav", false); // ?
	}

	public static void playBGLoop() {
		playSound("POL-pocket-garden-short.wav", true);
	}
}
