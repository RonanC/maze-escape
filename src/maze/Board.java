package maze;

import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.TimeUnit;

import javax.swing.*;

public class Board extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;

	private int mazeDim;
	private int tileDim;
	private int frameRate = 1000 / 60; // 60 frames per second (every 16 Ms)

	private Timer timer; // needed for action performed
	private Map map;
	private Player player;

	private String message;
	private Font font;

	// walk
	// private char playerLookV;
	private char playerLookH;
	private int setWalk;
	private int walkDur;
	private int stepCount;
	private int setWin;
	private boolean haveWon;

	public Board(int mazeDim, int tileDim) {
		this.mazeDim = mazeDim;
		this.tileDim = tileDim;
		init();

		map = new Map(mazeDim, tileDim);
		// run this action performed every 25 milliseconds
		timer = new Timer(frameRate, this);
		timer.start();
	}

	private void init() {
		// playerLookV = 'x';
		playerLookH = 'r';
		setWalk = 0;
		walkDur = 250; // quarter of a second
		stepCount = 0;
		setWin = 0; // how long you since you won
		haveWon = false;
//		message = "Look for the magic potion.";
		message = "You found the teleportation potion!";
		font = new Font("Serif", Font.BOLD, mazeDim * 3);

		player = new Player(tileDim);
		addKeyListener(new AcLis()); // get thing that listens for key press
		setFocusable(true); // adds the key listener to our frame
	}

	public void actionPerformed(ActionEvent e) {
		int timeElap = getTime() - setWin;

		if (haveWon && timeElap > 5000) { // 5 seconds of winning!d
			System.out.println("Look for the magic potion.");
//			message = "Look for the magic potion.";
			haveWon = false;
			player.setTileX(1);
			player.setTileY(1);
			playerLookH = 'r';
		}

		repaint();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if (!haveWon) {
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
		} 
		else {
			g.setColor(Color.BLUE);
			g.setFont(font);
			int relativeDim = mazeDim * tileDim;
			g.drawString(message, (relativeDim / 6), (relativeDim / 5)); // mazeDim
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

	public int getTime() {
		long delayNS = System.nanoTime();
		long durationInMs = TimeUnit.MILLISECONDS.convert(delayNS, TimeUnit.NANOSECONDS);
		return (int) durationInMs;
	}

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

	public void lookLeft(Graphics g) {
		g.drawImage(player.getPlayer(), player.getTileX() * tileDim + tileDim, player.getTileY() * tileDim, -tileDim,
				tileDim, null);
	}

	public void lookRight(Graphics g) {
		g.drawImage(player.getPlayer(), player.getTileX() * tileDim, player.getTileY() * tileDim, tileDim, tileDim,
				null);
	}

	public void checkWin() {
		if (map.getMap(player.getTileX(), player.getTileY()).equals("g")) {
			System.out.println("You have found the teleportation potion!");
			message = "You found the teleportation potion!";
			setWin = getTime();
			haveWon = true;
		}
	}

	public void moveCommon() {
		stepCount++;
		setWalk = getTime();
	}

	public class AcLis extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			int keycode = 0;

			if (!haveWon) {
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
}
