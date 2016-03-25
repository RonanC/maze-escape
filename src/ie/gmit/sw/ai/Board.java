package ie.gmit.sw.ai;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.concurrent.*;
import javax.swing.*;

import ie.gmit.sw.ai.audio.*;
import ie.gmit.sw.ai.chars.Enemy;
import ie.gmit.sw.ai.chars.EnemyBrain;
import ie.gmit.sw.ai.chars.Player;
import javafx.application.Application;

// the player is tied into this class, 
// as many player variables are checked against the game state
// it makes sense to keep the player in full focus
// however the enemies are in there own class (with own threads each)

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
	private Mapper map;
	private Player player;

	// messages
	private String msgWin;
	private String msgStart;
	private Font fontGen;

	// walk
	private int setWalk;
	private int walkDur;

	// game vars
	private int setWin;
	private int winDur;
	private boolean haveWon;
	private boolean startDone;

	// additionals
	private boolean zoomedOut;
	// private Graphics g;

	// refactored
	private PlayerDraw playerDraw;

	// enemy
	// private Enemy enemy;
	private ArrayList<Enemy> enemyList;
	private EnemyBrain enemyBrain;
	private boolean enemySpawned;

	// set game up
	public Board() {
		this.mazeDim = GameRunner.MAZE_DIM;
		this.tileDim = GameRunner.TILE_DIM;
		relativeDim = mazeDim * tileDim;

		init();

		timer = new Timer(frameRate, this); // action performed every 25 Ms
		timer.start();
	}
	
	public void messageInit(){
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
	}

	// initialization variables
	private void init() {
		// game init
		addKeyListener(new AcLis()); // get thing that listens for key press
		setFocusable(true); // adds the key listener to our frame
		frameRate = 1000 / 60; // 60 frames per second (every 16 Ms)
		map = new Mapper();
		startDone = false;
		haveWon = false;
		setWin = 0; // how long you since you won
		this.setBackground(Color.DARK_GRAY);

		// player init
		player = new Player(map);
		setWalk = 0;
		walkDur = 250; // quarter of a second
		winDur = 8000;

		// message init
		messageInit();

		// additionals
		zoomedOut = false;
		SoundEffects.playBGLoop();

		// refactored
		playerDraw = new PlayerDraw(player, tileDim, 'r', 0);

		// enemy
		enemyList = new ArrayList<Enemy>();
//		spawnEnemies(false);
		enemyBrain = new EnemyBrain(map, enemyList, player);
		enemySpawned = false;
		// old enemy
		// enemy = new Enemy(tileDim, 7, 7);
		// enemyBrain = new EnemyBrain(map, enemy, player);
	}
	
	public void spawnEnemies(boolean kill){
		if(kill){
			enemyList.clear();
		}

		for (int i = 0; i < 5; i++) {
			enemyList.add(new Enemy(map));
		}
		enemySpawned = true;
		enemyBrain.spawn();
	}


	// get time in millis
	public static int getTime() {
		long delayNS = System.nanoTime();
		long durationInMs = TimeUnit.MILLISECONDS.convert(delayNS, TimeUnit.NANOSECONDS);
		return (int) durationInMs;
	}
	
	// // additional
	public void fight() {
		System.out.println("Dual!");
		SoundEffects.playPlayerAttack();
	}

	public void checkFight() {
		for (Enemy enemyItem : enemyList) {
			if (player.getPos().equals(enemyItem.getPos())) {
				fight();
			}
		}

	}

	// zoom
	public void toggleZoom() {
		zoomedOut = !zoomedOut;
	}

	// KEY PRESS
	// controls
	public class AcLis extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			int keycode = 0;

			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				startDone = true;
				spawnEnemies(false);
			}

			if (!haveWon && startDone) {
				keycode = e.getKeyCode();
			}

			if (keycode == KeyEvent.VK_W) { // N
				if (!map.getPosElement(player.getTileX(), player.getTileY() - 1).equals("w")) {
					player.move(0, -1);
					moveCommon();
				}
			} else if (keycode == KeyEvent.VK_S) { // S
				if (!map.getPosElement(player.getTileX(), player.getTileY() + 1).equals("w")) {
					player.move(0, 1);
					moveCommon();
				}
			} else if (keycode == KeyEvent.VK_A) { // E
				playerDraw.setPlayerLookH('l');
				if (!map.getPosElement(player.getTileX() - 1, player.getTileY()).equals("w")) {
					player.move(-1, 0);
					moveCommon();
				}
			} else if (keycode == KeyEvent.VK_D) { // W
				playerDraw.setPlayerLookH('r');
				if (!map.getPosElement(player.getTileX() + 1, player.getTileY()).equals("w")) {
					player.move(1, 0); // 1? tileDim
					moveCommon();
				}
			}
			if (!haveWon) {
				checkTile();
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
		playerDraw.incStepCount();
		setWalk = getTime();
		SoundEffects.playMove();
		checkFight(); // checked when I or the enemy moves
	}

	// ran every keypress
	public void checkTile() {

		// flash items and do something
		if (map.getPosElement(player.getTileX(), player.getTileY()).equals("s")) { // sword
			System.out.println("Sweet sword!");
			SoundEffects.playFoundItem();
			map.setTileItem(player.getTileX(), player.getTileY(), 'f');
		} else if (map.getPosElement(player.getTileX(), player.getTileY()).equals("b")) { // bomb
			System.out.println("Brilliant bomb!");
			SoundEffects.playFoundItem();
			map.setTileItem(player.getTileX(), player.getTileY(), 'f');
		} else if (map.getPosElement(player.getTileX(), player.getTileY()).equals("h")) { // helper
			System.out.println("Happy helper!");
			SoundEffects.playFoundHelp();
		} else if (map.getPosElement(player.getTileX(), player.getTileY()).equals("g")) { // goal
			System.out.println("Perfect potion!");
			map.setTileItem(player.getTileX(), player.getTileY(), 'f');

			setWin = getTime();
			haveWon = true;
			SoundEffects.playGameOver();
			enemyList.clear();
		}
	}
	
	// // ACTION PERFORMED
	// gets called a certain frames per second
		// check if won
		public void actionPerformed(ActionEvent e) {
//			
//			if(startDone && !enemySpawned){
////				enemyBrain = new EnemyBrain(map, enemyList, player);
//				enemySpawned = true;
//			}
			
			int timeElap = getTime() - setWin;

			// resets game
			if (haveWon && timeElap > winDur) { // n seconds of winning!
				// SoundEffects.playWin();
				System.out.println("Look for the magic potion.");
				haveWon = false;
				startDone = false;
				player.setTileX(1);
				player.setTileY(1);
				playerDraw.setPlayerLookH('r');
				
				// enemy
				// TODO: change to random (let the brain reset)
				// for (Enemy enemyItem : enemyList) {
				// enemyItem.setPos(7, 7);
			
				enemySpawned = false;
				enemyBrain.resetAllPos();
				// }
				// enemy.setTileX(7);
				// enemy.setTileY(7);

				//
				map.reset();
			}

			repaint();
		}
	
	// // DRAWING
	// paint map a certain frames per second
		@Override
		public void paint(Graphics g) {
			super.paint(g);
			// this.g = g;

			if (!haveWon && startDone) {
				int animHelperDur = 2000;
				int animGoalDur = 1000;
				int animSwordDur = 3000;
				int animBombDur = 3000;

				for (int y = 0; y < mazeDim; y++) {// col one
					for (int x = 0; x < mazeDim; x++) { // fill row
						String element = map.getPosElement(x, y);

						// tiles
						if (element.equals("w")) { // wall
							g.drawImage(map.getWall(), x * tileDim, y * tileDim, null);
						} else if (element.equals("f")) { // floor
							// using default background color
							g.drawImage(map.getFloor(), x * tileDim, y * tileDim, null);
						} else {
							g.drawImage(map.getFloor(), x * tileDim, y * tileDim, null);

							// items
							// inner if is for flipping the image (basic animation)
							if (element.equals("g")) { // goal
								if (getTime() % animGoalDur * 2 > animGoalDur) {
									g.drawImage(map.getGoal(), x * tileDim, y * tileDim, null);
								} else {
									g.drawImage(map.getGoal(), (x + 1) * tileDim, y * tileDim, -tileDim, tileDim, null);
								}
							} else if (element.equals("h")) { // helper
								if (getTime() % animHelperDur * 2 > animHelperDur) {
									g.drawImage(map.getHelper(), x * tileDim, y * tileDim, null);
								} else {
									g.drawImage(map.getHelper(), (x + 1) * tileDim, y * tileDim, -tileDim, tileDim, null);
								}
							} else if (element.equals("s")) { // sword
								if (getTime() % animSwordDur * 2 > animSwordDur) {
									g.drawImage(map.getSword(), x * tileDim, y * tileDim, null);
								} else {
									g.drawImage(map.getSword(), (x + 1) * tileDim, y * tileDim, -tileDim, tileDim, null);
								}
							} else if (element.equals("b")) { // bomb
								if (getTime() % animBombDur * 2 > animBombDur) {
									g.drawImage(map.getBomb(), x * tileDim, y * tileDim, null);
								} else {
									g.drawImage(map.getBomb(), (x + 1) * tileDim, y * tileDim, -tileDim, tileDim, null);
								}
							}
						}

					}
				}
				// draw strings
			} else if (!startDone) {
				g.setColor(Color.CYAN);
				g.setFont(fontGen);
				drawString(g, msgStart, relativeDim / 2, relativeDim / 7);
			} else if (haveWon) {
				g.setColor(Color.CYAN);
				g.setFont(fontGen);
				drawString(g, msgWin, relativeDim / 2, relativeDim / 7);
			}

			// draw player
			// draw below items
			int timeElap = getTime() - setWalk;
			if (timeElap < walkDur) {
				playerDraw.walk(g);
			} else if (haveWon) {
				playerDraw.win(g);
			} else if (playerDraw.getPlayerLookH() == 'l') {
				playerDraw.lookLeft(g);
			} else if (playerDraw.getPlayerLookH() == 'r') {
				playerDraw.lookRight(g);
			}

			// draw enemy
			if (startDone) {
				int animEnemyDur = 1000;
				for (Enemy enemyItem : enemyList) {
					if (getTime() % animEnemyDur * 2 > animEnemyDur) {
						g.drawImage(enemyItem.getEnemy(), enemyItem.getTileX() * tileDim + tileDim,
								enemyItem.getTileY() * tileDim, -tileDim, tileDim, null);
					} else {
						g.drawImage(enemyItem.getEnemy2(), (enemyItem.getTileX() * tileDim + tileDim),
								enemyItem.getTileY() * tileDim, -tileDim, tileDim, null);
					}

				}
			}
		}

		// draws multi-line string with correct spacing
		void drawString(Graphics g, String text, int x, int y) {
			for (String line : text.split("\n"))
				g.drawString(line, x - (g.getFontMetrics().stringWidth(line) / 2), y += g.getFontMetrics().getHeight());
		}
}
