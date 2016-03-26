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
import ie.gmit.sw.ai.img.ImgCtrl;
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
	private Maze map;
	private ImgCtrl imgCtrl;
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

	// animations
	private int animHelperDur;
	private int animGoalDur;
	private int animSwordDur;
	private int animBombDur;

	// player pos
	private int playerPosX;
	private int playerPosY;
	private int playerPosXmin;
	private int playerPosXmax;
	private int playerPosYmin;
	private int playerPosYmax;

	// set game up
	public Board() {
		this.mazeDim = GameRunner.MAZE_DIM;
		this.tileDim = GameRunner.TILE_DIM;
		relativeDim = mazeDim * tileDim;

		init();

		timer = new Timer(frameRate, this); // action performed every 25 Ms
		timer.start();
	}

	public void messageInit() {
		msgWin = "You found the teleportation potion!\n\n\n";
		msgWin += "*glug glug glug*\n\n";
		msgWin += "*POOF*\n\n\n";
		msgWin += "With a puff of smoke you vanish from the maze...";
		msgStart = "You are falsey imprisoned by the evil king\n";
		msgStart += "to endlessly wander the prison maze.\n\n";
		msgStart += "There are myths of a wizard who once walked these lonesome halls.\n\n";
		msgStart += "Legend says he left behind a potion which can free you of this place.\n\n";
		msgStart += "\n\nPress the 'Enter' key to begin your quest.\n\n";
		msgStart += "\n\n\n\nMovement:\tWASD";
		msgStart += "\n\nZoom:\tZ";
		int fontSize = (int) (GameRunner.TILE_DIM / 1.5); // works at 64 tile
															// size (and all
															// maze sizes)
		fontSize *= GameRunner.TILE_DIM / 2;

		fontGen = new Font("Serif", Font.BOLD, fontSize);
	}

	// initialization variables
	private void init() {
		// game init
		addKeyListener(new AcLis()); // get thing that listens for key press
		setFocusable(true); // adds the key listener to our frame
		frameRate = 1000 / 60; // 60 frames per second (every 16 Ms)
		map = new Maze();
		imgCtrl = new ImgCtrl();
		startDone = false;
		haveWon = false;
		setWin = 0; // how long you since you won
		this.setBackground(Color.DARK_GRAY);

		// player init
		player = new Player(map, imgCtrl);
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
		// spawnEnemies(false);
		enemyBrain = new EnemyBrain(map, enemyList, player);
		enemySpawned = false;
		// old enemy
		// enemy = new Enemy(tileDim, 7, 7);
		// enemyBrain = new EnemyBrain(map, enemy, player);

		// animations
		animHelperDur = 2000;
		animGoalDur = 1000;
		animSwordDur = 3000;
		animBombDur = 3000;
	}

	public void spawnEnemies(boolean kill) {
		if (kill) {
			enemyList.clear();
		}

		for (int i = 0; i < 5; i++) {
			enemyList.add(new Enemy(map, imgCtrl));
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

			if (e.getKeyCode() == KeyEvent.VK_Z) {
				toggleZoom();
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
		// if(startDone && !enemySpawned){
		//// enemyBrain = new EnemyBrain(map, enemyList, player);
		// enemySpawned = true;
		// }

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

		if (!zoomedOut) {
			if (!haveWon && startDone) {

				// this draws all
				// we only want to draw the tiles 2 either side of our player
				// for (int y = 0; y < mazeDim; y++) {// col one
				// for (int x = 0; x < mazeDim; x++) { // fill row
				// String element = map.getPosElement(x, y);
				//
				// drawTiles(g, y, x, element);
				// }
				// }

				// this draw around player
				playerPosX = player.getTileX();
				playerPosY = player.getTileY();

				playerPosXmin = playerPosX - 2;
				playerPosXmax = playerPosX + 3;

				playerPosYmin = playerPosY - 2;
				playerPosYmax = playerPosY + 3;

				if (playerPosXmin < 0) {
					playerPosXmin = 0;
				} else if (playerPosXmax > GameRunner.MAZE_DIM) {
					playerPosXmax = GameRunner.MAZE_DIM;
				}

				if (playerPosYmin < 0) {
					playerPosYmin = 0;
				} else if (playerPosYmax > GameRunner.MAZE_DIM) {
					playerPosYmax = GameRunner.MAZE_DIM;
				}

//				System.out.print("playerPosX: " + playerPosX + "\t");
//				System.out.println("playerPosY: " + playerPosY);
//
//				System.out.print("playerPosXmin: " + playerPosXmin + "\t");
//				System.out.println("playerPosYmin: " + playerPosYmin);
//
//				System.out.print("playerPosXmax: " + playerPosXmax + "\t");
//				System.out.println("playerPosYmax: " + playerPosYmax);
				
				int yCount = -1;
				// this will run 5 times
				for (int y = playerPosYmin; y < playerPosYmax; y++) {// col one
					yCount++;
					int xCount = -1;
					// this will run 5 times
					for (int x = playerPosXmin; x < playerPosXmax; x++) { // fill
						xCount++;
						// get element													// row
						String element = map.getPosElement(x, y);

						// now we need to draw it relative to the view window.
						// always have the center
						
						drawTiles(g, yCount, xCount, element);

						drawEnemiesInView(g);

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

			drawPlayer(g);

			// drawEnemy(g);
		} // not zoomed out
		else { // zoomed out
			for (int y = 0; y < mazeDim; y++) {// col one
				for (int x = 0; x < mazeDim; x++) { // fill row
					String element = map.getPosElement(x, y);

					int zoomDim = GameRunner.ZOOM_DIM;
					
					// tiles
					if (element.equals("w")) { // wall
						g.setColor(Color.DARK_GRAY);
						g.fillRect(x * zoomDim, y * zoomDim, zoomDim, zoomDim);
					} else if (element.equals("g")) { // goal
						g.setColor(Color.YELLOW);
						g.fillRect(x * zoomDim, y * zoomDim, zoomDim, zoomDim);
					} else if (String.format("%s,%s", x, y).equals(player.getPos())) { // player
						g.setColor(Color.ORANGE);
						g.fillRect(x * zoomDim, y * zoomDim, zoomDim, zoomDim);
					} else { // floor (with items or whatever)
						// System.out.print(String.format("%s, %s\t", x, y));
						// System.out.println(player.getPos());
						g.setColor(Color.LIGHT_GRAY);
						g.fillRect(x * zoomDim, y * zoomDim, zoomDim, zoomDim);
					}
				}
			}
		}
	}

	private void drawEnemiesInView(Graphics g) {
		// draw enemy if inside view
		int animEnemyDur = 1000;
		for (Enemy enemy : enemyList) {
			int enX = enemy.getTileX();
			int enY = enemy.getTileY();

			if (enX > playerPosXmin && enX < playerPosXmax && enY > playerPosYmin && enY < playerPosYmax) {
				drawEnemy(g, animEnemyDur, enemy);
			}
		}
	}

//	private void drawEnemy(Graphics g, int animEnemyDur, Enemy enemy) {
//		if (getTime() % animEnemyDur * 2 > animEnemyDur) {
//			g.drawImage(enemy.getEnemy(), enemy.getTileX() * tileDim + tileDim, enemy.getTileY() * tileDim, -tileDim,
//					tileDim, null);
//		} else {
//			g.drawImage(enemy.getEnemy2(), (enemy.getTileX() * tileDim + tileDim), enemy.getTileY() * tileDim, -tileDim,
//					tileDim, null);
//		}
//	}
	
	// relative to the player
	// we know this guy is visible to the player
	// if we take his x away from the players we will have the offset
	private void drawEnemy(Graphics g, int animEnemyDur, Enemy enemy) {
		int playerX = player.getTileX();
		int playerY = player.getTileY();
		int enemyX = enemy.getTileX();
		int enemyY = enemy.getTileY();
		
		// if player at 8 and spider at 10 then we want spider at +2
		// if player at 8 and spider at 6 then we want spider at -2
		// therefore spider - player = offset
		
		int playerZoomed = 2; // center (out of 5, with index 0)
		
		int offsetX = enemyX - playerX;
		int offsetY = enemyY - playerY;
		
		int newEnX = playerZoomed + offsetX;
		int newEnY = playerZoomed + offsetY;
		
		newEnX *= tileDim; // resize
		newEnY *= tileDim;
		
		if (getTime() % animEnemyDur * 2 > animEnemyDur) {
			g.drawImage(enemy.getEnemy(), newEnX + tileDim, newEnY, -tileDim,
					tileDim, null);
		} else {
			g.drawImage(enemy.getEnemy2(), newEnX + tileDim, newEnY, -tileDim,
					tileDim, null);
		}
	}

	private void drawPlayer(Graphics g) {
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
	}

	private void drawTiles(Graphics g, int y, int x, String element) {
		// tiles
		if (element.equals("w")) { // wall
			g.drawImage(imgCtrl.getWall(), x * tileDim, y * tileDim, null);
		} else if (element.equals("f")) { // floor
			// using default background color
			g.drawImage(imgCtrl.getFloor(), x * tileDim, y * tileDim, null);
		} else {
			g.drawImage(imgCtrl.getFloor(), x * tileDim, y * tileDim, null);

			// items
			// inner if is for flipping the image (basic
			// animation)
			if (element.equals("g")) { // goal
				if (getTime() % animGoalDur * 2 > animGoalDur) {
					g.drawImage(imgCtrl.getGoal(), x * tileDim, y * tileDim, null);
				} else {
					g.drawImage(imgCtrl.getGoal(), (x + 1) * tileDim, y * tileDim, -tileDim, tileDim, null);
				}
			} else if (element.equals("h")) { // helper
				if (getTime() % animHelperDur * 2 > animHelperDur) {
					g.drawImage(imgCtrl.getHelper(), x * tileDim, y * tileDim, null);
				} else {
					g.drawImage(imgCtrl.getHelper(), (x + 1) * tileDim, y * tileDim, -tileDim, tileDim, null);
				}
			} else if (element.equals("s")) { // sword
				if (getTime() % animSwordDur * 2 > animSwordDur) {
					g.drawImage(imgCtrl.getSword(), x * tileDim, y * tileDim, null);
				} else {
					g.drawImage(imgCtrl.getSword(), (x + 1) * tileDim, y * tileDim, -tileDim, tileDim, null);
				}
			} else if (element.equals("b")) { // bomb
				if (getTime() % animBombDur * 2 > animBombDur) {
					g.drawImage(imgCtrl.getBomb(), x * tileDim, y * tileDim, null);
				} else {
					g.drawImage(imgCtrl.getBomb(), (x + 1) * tileDim, y * tileDim, -tileDim, tileDim, null);
				}
			}
		} // end of if
	}

	// draws multi-line string with correct spacing
	void drawString(Graphics g, String text, int x, int y) {
		for (String line : text.split("\n"))
			g.drawString(line, x - (g.getFontMetrics().stringWidth(line) / 2), y += g.getFontMetrics().getHeight());
	}
}
