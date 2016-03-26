package ie.gmit.sw.ai.audio;

import java.io.File;
import java.net.URL;
import java.util.Random;
import java.util.Timer;

import javax.sound.sampled.*;

import ie.gmit.sw.ai.GameCtrl;

public class SoundEffects {
	public static synchronized void playSound(final String audioName) {
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
					clip.start();

				} catch (Exception e) {
					System.out.println("url: " + url);
					System.err.println(e.getMessage());
				}
			}
		}).start();
	}

	public static synchronized void playBGLoop() {
		// int frameRate = 1000; // every second run timer
		// Timer timer = new Timer(frameRate, this);

		String url = "resources/audio/" + chooseBg();
		File file = new File(url);

		new Thread(new Runnable() {
			// The wrapper thread is unnecessary, unless it blocks on the
			// Clip finishing; see comments.
			public void run() {
				try {
					int count = 0;
					Clip clip = AudioSystem.getClip();

					AudioInputStream inputStream = AudioSystem.getAudioInputStream(file);
					clip.open(inputStream);

					// volume
					FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
					gainControl.setValue(-10.0f); // Reduce volume by 10
													// decibels.

					clip.loop(Clip.LOOP_CONTINUOUSLY);

					while (true) {
						// System.out.println(Board.getTime() % 100000);
						if (GameCtrl.getTime() % 100000 > 99950) { // song
																	// change
																	// every 100
																	// seconds
							String url = "resources/audio/" + chooseBg();
							System.out.println(url);
							File file = new File(url);
							inputStream = AudioSystem.getAudioInputStream(file);

							clip.close();
							clip.open(inputStream);
							clip.loop(Clip.LOOP_CONTINUOUSLY);
						}
					}

				} catch (Exception e) {
					System.out.print("Sound Effects: \t");
					System.out.print("url: " + url + "\t");
					System.err.println(e.getMessage());
				}
			}
		}).start();
	}

	public static void playWin() {
		playSound("win");
	}

	public static void playMove() {
		playSound("move");
	}

	public static void playPlayerAttack() {
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

		playSound(location);
	}

	public static void playWonFight() {
		playSound("fight/terminated.wav");
	}

	public static void playLostFight() {
		playSound("fight/cry.wav");
	}

	public static void playFoundItem() {
		playSound("items/excellent3.wav");
	}

	public static void playFoundHelp() {
		String location = "helper/";

		Random random = new Random();
		int num = random.nextInt(3);

		switch (num) { // add more insults
		case 0:
			location += "bugs_answer.wav";
			break;

		case 1:
			location += "bugs_you_again.wav";
			break;

		case 2:
			location += "daffy_evil_laugh.wav";
			break;

		default:
			location += "bugs_answer.wav";
			break;
		}
		try {
			playSound(location);
		} catch (Exception e) {
			System.out.println("unable to load: " + location);
		}
		
	}

	public static void playGameOver() {
		playSound("items/Brave5.wav");
	}

	public static void playIntro() {
		// playSound("pacman_intro.wav");
	}

	public static void playEnemyAttack() {
		playSound("fight/Slime-SoundBible.com-803762203.wav");
	}

	public static String chooseBg() {
		String location = "bg/";

		Random random = new Random();
		int num = random.nextInt(4);

		switch (num) { // add more insults
		case 0:
			location += "POL-brave-worm-short.wav";
			break;

		case 1:
			location += "POL-floating-town-short.wav";
			break;

		case 2:
			location += "POL-pocket-garden-short.wav";
			break;

		case 3:
			location += "POL-pyramid-sands-short.wav";
			break;

		default:
			location += "POL-brave-worm-short.wav";
			break;
		}

		return location;
	}
}
