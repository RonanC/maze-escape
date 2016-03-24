package ie.gmit.sw.ai;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.concurrent.*;

import javax.sound.sampled.*;
import javax.swing.*;

import ie.gmit.sw.ai.audio.*;

// game scene, drawing and movement
public class Board extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;

	// basic game info
	private int mazeDim;
	private int tileDim;
	private int relativeDim;
	private int frameRate;

	// objects
	private Timer timer; // needed for action performed
	private Map map;
	private Player player;

	// messages
	private String msgWin;
	private String msgStart;
	private Font fontGen;

	// walk
	// private char playerLookV;
	private char playerLookH;
	private int setWalk;
	private int walkDur;
	private int stepCount;

	// game vars
	private int setWin;
	private int winDur;
	private boolean haveWon;
	private boolean startDone;

	// additionals
	private boolean zoomedOut;

	// set game up
	public Board(int mazeDim, int tileDim) {
		this.mazeDim = mazeDim;
		this.tileDim = tileDim;
		relativeDim = mazeDim * tileDim;

		init();

		timer = new Timer(frameRate, this); // action performed every 25 Ms
		timer.start();
	}

	// initialization variables
	private void init() {
		// game init
		addKeyListener(new AcLis()); // get thing that listens for key press
		setFocusable(true); // adds the key listener to our frame
		frameRate = 1000 / 60; // 60 frames per second (every 16 Ms)
		map = new Map(mazeDim, tileDim);
		startDone = false;
		haveWon = false;
		setWin = 0; // how long you since you won
		this.setBackground(Color.DARK_GRAY);

		// player init
		player = new Player(tileDim);
		playerLookH = 'r';
		setWalk = 0;
		walkDur = 250; // quarter of a second
		stepCount = 0;
		winDur = 8000;

		// message init
		msgWin = "You found the teleportation potion!\n\n\n";
		msgWin += "*glug glug glug*\n\n";
		msgWin += "*POOF*\n\n\n";
		msgWin += "With a puff of smoke you vanish from the maze...";
		msgStart = "You are falsey imprisoned by the evil king\n";
		msgStart += "to endlessly wander the prison maze.\n\n";
		msgStart += "There are myths of a wizard who once walked these lonesome halls.\n\n";
		msgStart += "Legend says he left behind a potion which can free you of this place.\n\n";
		msgStart += "\n\nPress the 'Enter' key to begin your quest.\n\n";
		msgStart += "\n\n\n\nControls:\tWASD";
		fontGen = new Font("Serif", Font.BOLD, mazeDim * 2);

		// additionals
		zoomedOut = false;
		SoundEffects.playBGLoop();
	}

	// gets called a certain frames per second
	// check if won
	public void actionPerformed(ActionEvent e) {
		int timeElap = getTime() - setWin;

		// resets game
		if (haveWon && timeElap > winDur) { // n seconds of winning!
			System.out.println("Look for the magic potion.");
			haveWon = false;
			startDone = false;
			player.setTileX(1);
			player.setTileY(1);
			playerLookH = 'r';
		}

		repaint();
	}

	// paint map a certain frames per second
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if (!haveWon && startDone) {
			for (int y = 0; y < mazeDim; y++) {// col one
				for (int x = 0; x < mazeDim; x++) { // fill row
					String element = map.getMap(x, y);
					if (element.equals("f")) { // floor
						g.drawImage(map.getGround(), x * tileDim, y * tileDim, null);
					} else if (element.equals("w")) { // wall
						g.drawImage(map.getWall(), x * tileDim, y * tileDim, null);
					} else if (element.equals("g")) { // goal
						g.drawImage(map.getGoal(), x * tileDim, y * tileDim, null);
					}
				}
			}
		} else if (!startDone) {
			g.setColor(Color.CYAN);
			g.setFont(fontGen);
			drawString(g, msgStart, relativeDim / 2, relativeDim / 7);
		} else if (haveWon) {
			g.setColor(Color.CYAN);
			g.setFont(fontGen);
			drawString(g, msgWin, relativeDim / 2, relativeDim / 7);
		}

		int timeElap = getTime() - setWalk;
		if (timeElap < walkDur) {
			walk(g);
		} else if (haveWon) {
			win(g);
		} else if (playerLookH == 'l') {
			lookLeft(g);
		} else if (playerLookH == 'r') {
			lookRight(g);
		}
	}

	// draws multi-line string with correct spacing
	void drawString(Graphics g, String text, int x, int y) {
		for (String line : text.split("\n"))
			g.drawString(line, x - (g.getFontMetrics().stringWidth(line) / 2), y += g.getFontMetrics().getHeight());
	}

	// get time in millis
	public int getTime() {
		long delayNS = System.nanoTime();
		long durationInMs = TimeUnit.MILLISECONDS.convert(delayNS, TimeUnit.NANOSECONDS);
		return (int) durationInMs;
	}

	//// drawing player
	// walk draw
	public void walk(Graphics g) {
		if (playerLookH == 'l') {
			if (stepCount % 2 == 0) {
				g.drawImage(player.getPlayerWalk(), player.getTileX() * tileDim + tileDim, player.getTileY() * tileDim,
						-tileDim, tileDim, null);
			} else {
				g.drawImage(player.getPlayerWalk2(), player.getTileX() * tileDim + tileDim, player.getTileY() * tileDim,
						-tileDim, tileDim, null);
			}
		} else if (playerLookH == 'r') {
			if (stepCount % 2 == 0) {
				g.drawImage(player.getPlayerWalk(), player.getTileX() * tileDim, player.getTileY() * tileDim, tileDim,
						tileDim, null);
			} else {
				g.drawImage(player.getPlayerWalk2(), player.getTileX() * tileDim, player.getTileY() * tileDim, tileDim,
						tileDim, null);
			}
		}
	}

	// win draw
	public void win(Graphics g) {
		// System.out.printf("getTime: %d\t mod 1000: %d\n", getTime(),
		// getTime() % 1000);
		if (playerLookH == 'l') {
			if (getTime() % 1000 > 0 && getTime() % 1000 < 500) {
				g.drawImage(player.getPlayerWin(), player.getTileX() * tileDim + tileDim, player.getTileY() * tileDim,
						-tileDim, tileDim, null);
			} else {
				g.drawImage(player.getPlayer(), player.getTileX() * tileDim + tileDim, player.getTileY() * tileDim,
						-tileDim, tileDim, null);
			}
		} else if (playerLookH == 'r') {
			if (getTime() % 1000 > 0 && getTime() % 1000 < 500) {
				g.drawImage(player.getPlayerWin(), player.getTileX() * tileDim, player.getTileY() * tileDim, tileDim,
						tileDim, null);
			} else {
				g.drawImage(player.getPlayer(), player.getTileX() * tileDim, player.getTileY() * tileDim, tileDim,
						tileDim, null);
			}
		}
	}

	// left draw
	public void lookLeft(Graphics g) {
		g.drawImage(player.getPlayer(), player.getTileX() * tileDim + tileDim, player.getTileY() * tileDim, -tileDim,
				tileDim, null);
	}

	// right draw
	public void lookRight(Graphics g) {
		g.drawImage(player.getPlayer(), player.getTileX() * tileDim, player.getTileY() * tileDim, tileDim, tileDim,
				null);
	}

	// controls
	public class AcLis extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			int keycode = 0;

			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				startDone = true;
			}

			if (!haveWon && startDone) {
				keycode = e.getKeyCode();
			}

			if (keycode == KeyEvent.VK_W) {
				if (!map.getMap(player.getTileX(), player.getTileY() - 1).equals("w")) {
					moveCommon();
					// playerLookV = 'u';
					player.move(0, -1);
				}
			} else if (keycode == KeyEvent.VK_S) {
				if (!map.getMap(player.getTileX(), player.getTileY() + 1).equals("w")) {
					moveCommon();
					// playerLookV = 'd';
					player.move(0, 1);
				}
			} else if (keycode == KeyEvent.VK_A) {
				playerLookH = 'l';
				if (!map.getMap(player.getTileX() - 1, player.getTileY()).equals("w")) {
					moveCommon();
					player.move(-1, 0);
				}
			} else if (keycode == KeyEvent.VK_D) {
				playerLookH = 'r';
				if (!map.getMap(player.getTileX() + 1, player.getTileY()).equals("w")) {
					moveCommon();
					player.move(1, 0); // 1? tileDim
				}
			}
			if (!haveWon) {
				checkWin();
			}
		}

		// public void keyTyped(KeyEvent e) {
		// int keycode = e.getKeyCode();
		//
		// }
		//
		// public void keyReleased(KeyEvent e) {
		// int keycode = e.getKeyCode();
		//
		// }
	}

	// ran every valid keypress
	public void moveCommon() {
		stepCount++;
		setWalk = getTime();
		SoundEffects.playMove();
	}

	// ran every keypress
	public void checkWin() {
		if (map.getMap(player.getTileX(), player.getTileY()).equals("g")) {
			setWin = getTime();
			haveWon = true;
			SoundEffects.playWin();
		}
	}

	// // additional
	// zoom
	public void toggleZoom() {
		zoomedOut = !zoomedOut;
	}
}
