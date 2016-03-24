package ie.gmit.sw.ai.audio;

import java.io.File;
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
					AudioInputStream inputStream = AudioSystem
							.getAudioInputStream(file);
					clip.open(inputStream);
					if(loop == true){
						clip.loop(Clip.LOOP_CONTINUOUSLY);
					} else{
						clip.start();
					}
					
					
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
		}).start();
	}
	
	public static void playWin(){
		playSound("win", false);
	}
	
	public static void playMove(){
		playSound("move", false);
	}
	
	public static void playBGLoop(){
		playSound("POL-pocket-garden-short.wav", true);
	}
}
