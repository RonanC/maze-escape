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
import ie.gmit.sw.ai.chars.PlayerImgPainter;
import ie.gmit.sw.ai.img.ImgCtrl;
import javafx.application.Application;

// the player is tied into this class, 
// as many player variables are checked against the game state
// it makes sense to keep the player in full focus
// however the enemies are in there own class (with own threads each)

// game scene, drawing and movement
public class GameCtrl extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;

	// basic game info
	private int mazeDim;
	private int tileDim;
	private int relativeDim;
	private int frameRate;

	// objects
	private Timer timer; // needed for action performed
	private Maze maze;
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
	private PlayerImgPainter playerDraw;

	// enemy
	// private Enemy enemy;
	private ArrayList<Enemy> enemyList;
	private EnemyBrain enemyBrain;
	private boolean enemySpawned;
	private int enemyNum;

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

	// player pos Zoomed Out
	private int zoomedPosX;
	private int zoomedPosY;
	private int zoomedPosXmin;
	private int zoomedPosXmax;
	private int zoomedPosYmin;
	private int zoomedPosYmax;

	// zoomed out view
	private int zoomDim;

	// set game up
	public GameCtrl() {
		this.mazeDim = GameRunner.MAZE_DIM;
		this.tileDim = GameRunner.TILE_DIM;
		zoomDim = GameRunner.ZOOM_DIM;
		relativeDim = mazeDim * tileDim;

		init();

		timer = new Timer(frameRate, this); // action performed every 25 Ms
		timer.start();
	}

	public void messageInit() {
		msgWin = "You found the teleportation potion!\n\n\n";
		msgWin += "*glug glug glug*\n\n";
		msgWin += "*POOF*\n\n\n";
		msgWin += "\n\n\n\n\n\nWith a puff of smoke\nyou vanish from the maze...";

		msgStart = "You are falsey imprisoned by the evil king\n";
		msgStart += "to endlessly wander the prison maze.\n\n";
		msgStart += "There are myths of a wizard\nwho once walked these lonesome halls.\n\n";
		msgStart += "Legend says he left behind a potion\nwhich can free you of this place.\n\n";
		msgStart += "\n\n\nPress the 'Enter' key to begin your quest.\n\n";
		msgStart += "\n\nMovement:\tWASD";
		msgStart += "\nZoom:\tZ";

		int fontSize = 16;
		fontSize = GameRunner.TILE_DIM / 4; // doesn't scale when you change
											// maze dimensions
		fontGen = new Font("Serif", Font.BOLD, fontSize);
	}

	// initialization variables
	private void init() {
		// game init
		addKeyListener(new AcLis()); // get thing that listens for key press
		setFocusable(true); // adds the key listener to our frame
		frameRate = 1000 / 60; // 60 frames per second (every 16 Ms)
		maze = new Maze();
		imgCtrl = new ImgCtrl();
		startDone = false;
		haveWon = false;
		setWin = 0; // how long you since you won
		this.setBackground(Color.DARK_GRAY);

		// player init
		player = new Player(maze, imgCtrl);
		setWalk = 0;
		walkDur = 250; // quarter of a second
		winDur = 8000;

		// message init
		messageInit();

		// additionals
		zoomedOut = false;

		if (GameRunner.BG_ON) {
			SoundEffects.playBGLoop();
		}

		// refactored
		playerDraw = new PlayerImgPainter(player, tileDim, 'r', 0);

		// enemy
		enemyList = new ArrayList<Enemy>();
		// spawnEnemies(false);
		enemyBrain = new EnemyBrain(maze, enemyList, player);
		enemySpawned = false;
		enemyNum = GameRunner.MAZE_DIM / 2;

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

		for (int i = 0; i < enemyNum; i++) {
			enemyList.add(new Enemy(maze, imgCtrl));
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

			if (e.getKeyCode() == KeyEvent.VK_ENTER && startDone == false) {
				startDone = true;
				spawnEnemies(false);
			}

			if (e.getKeyCode() == KeyEvent.VK_Z) {
				toggleZoom();
			}

			if (!haveWon && startDone && !zoomedOut) {
				keycode = e.getKeyCode();
			}

			if (keycode == KeyEvent.VK_W) { // N
				if (!maze.getPosElement(player.getTileX(), player.getTileY() - 1).equals("w")) {
					player.move(0, -1);
					moveCommon();
				}
			} else if (keycode == KeyEvent.VK_S) { // S
				if (!maze.getPosElement(player.getTileX(), player.getTileY() + 1).equals("w")) {
					player.move(0, 1);
					moveCommon();
				}
			} else if (keycode == KeyEvent.VK_A) { // E
				playerDraw.setPlayerLookH('l');
				if (!maze.getPosElement(player.getTileX() - 1, player.getTileY()).equals("w")) {
					player.move(-1, 0);
					moveCommon();
				}
			} else if (keycode == KeyEvent.VK_D) { // W
				playerDraw.setPlayerLookH('r');
				if (!maze.getPosElement(player.getTileX() + 1, player.getTileY()).equals("w")) {
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
		if (maze.getPosElement(player.getTileX(), player.getTileY()).equals("s")) { // sword
			System.out.println("Sweet sword!");
			SoundEffects.playFoundItem();
			maze.setTileItem(player.getTileX(), player.getTileY(), 'f');
		} else if (maze.getPosElement(player.getTileX(), player.getTileY()).equals("b")) { // bomb
			System.out.println("Brilliant bomb!");
			SoundEffects.playFoundItem();
			maze.setTileItem(player.getTileX(), player.getTileY(), 'f');
		} else if (maze.getPosElement(player.getTileX(), player.getTileY()).equals("h")) { // helper
			System.out.println("Happy helper!");
			SoundEffects.playFoundHelp();
		} else if (maze.getPosElement(player.getTileX(), player.getTileY()).equals("g")) { // goal
			System.out.println("Perfect potion!");
			maze.setTileItem(player.getTileX(), player.getTileY(), 'f');

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

		// NB resets game
		if (haveWon && timeElap > winDur) { // n seconds of winning!
			// SoundEffects.playWin();
			System.out.println("Look for the magic potion.");
			haveWon = false;
			startDone = false;
			player.resetPos();
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
			maze.reset();
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
				drawView(g);

				// draw strings
			} else if (!startDone) {
				g.setColor(Color.CYAN);
				g.setFont(fontGen);
				drawString(g, msgStart, GameRunner.SCREEN_DIM / 2, GameRunner.SCREEN_DIM / 30); // was
																								// relative
																								// dim

				// drawString(g, msgStart, (GameRunner.VIEW_DIM *
				// GameRunner.TILE_DIM) / 2, (GameRunner.VIEW_DIM *
				// GameRunner.TILE_DIM) / 7); // was relative dim
			} else if (haveWon) {
				g.setColor(Color.CYAN);
				g.setFont(fontGen);
				drawString(g, msgWin, GameRunner.SCREEN_DIM / 2, GameRunner.SCREEN_DIM / 30);
				// drawString(g, msgWin, (GameRunner.VIEW_DIM *
				// GameRunner.TILE_DIM) / 2, (GameRunner.VIEW_DIM *
				// GameRunner.TILE_DIM) / 7);
			}

			drawPlayer(g);

			// drawEnemy(g);
		} // not zoomed out
		else { // zoomed out
			drawViewZoomed(g);
			// for (int y = 0; y < mazeDim; y++) {// col one
			// for (int x = 0; x < mazeDim; x++) { // fill row
			// String element = maze.getPosElement(x, y);
			//
			//// drawTilesZoomed(g, y, x, element);
			//// drawEnemiesZoomed(g, y, x);
			// drawViewZoomed(g);
			// }
			// }
		}
	}

	private void drawViewZoomed(Graphics g) {
		// draw only within this around player (and focus on player with offset)
		zoomedPosX = player.getTileX();
		zoomedPosY = player.getTileY();

		// 5 sqaured
		zoomedPosXmin = playerPosX - 2 * GameRunner.ZOOM_MULT;
		zoomedPosXmax = playerPosX + 3 * GameRunner.ZOOM_MULT;

		zoomedPosYmin = playerPosY - 2 * GameRunner.ZOOM_MULT;
		zoomedPosYmax = playerPosY + 3 * GameRunner.ZOOM_MULT;

		// remove index errors
		if (zoomedPosXmin < 0) {
			zoomedPosXmin = 0;
		} else if (playerPosXmax > GameRunner.MAZE_DIM) {
			zoomedPosXmax = GameRunner.MAZE_DIM;
		}

		if (zoomedPosYmin < 0) {
			zoomedPosYmin = 0;
		} else if (playerPosYmax > GameRunner.MAZE_DIM) {
			zoomedPosYmax = GameRunner.MAZE_DIM;
		}

		// draw 5 items
		int yCount = -1;
		// this will run 5 times
		for (int y = zoomedPosYmin; y < zoomedPosYmax; y++) {// col one
			yCount++;
			int xCount = -1;
			// this will run 5 times
			for (int x = zoomedPosXmin; x < zoomedPosXmax; x++) { // fill
				xCount++;
				// get element // row
				String element = maze.getPosElement(x, y); // gets element

				// now we need to draw it relative to the view window.
				// always have the center

				drawTilesZoomed(g, yCount, xCount, element, x, y);
			}
		}
	}

	private void drawTilesZoomed(Graphics g, int yCount, int xCount, String element, int x, int y) {
		// relative
		// tiles
		if (element.equals("w")) { // wall
			g.setColor(Color.DARK_GRAY);
			g.fillRect(xCount * zoomDim, yCount * zoomDim, zoomDim, zoomDim);
		} else if (element.equals("g")) { // goal
			g.setColor(Color.YELLOW);
			g.fillRect(xCount * zoomDim, yCount * zoomDim, zoomDim, zoomDim);
		} else if (String.format("%s,%s", x, y).equals(player.getPos())) {
			g.setColor(Color.ORANGE);
			g.fillRect(xCount * zoomDim, yCount * zoomDim, zoomDim, zoomDim);
		} else { // floor
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(xCount * zoomDim, yCount * zoomDim, zoomDim, zoomDim);
		} // end of if

		// enemy
		for (Enemy enemy : enemyList) {
			int enX = enemy.getTileX();
			int enY = enemy.getTileY();

			// checks if in view
			if (String.format("%s,%s", x, y).equals(enemy.getPos()))
				if (String.format("%s,%s", x, y).equals(enemy.getPos())) {
					g.setColor(Color.RED);
					g.fillRect(xCount * zoomDim, yCount * zoomDim, zoomDim, zoomDim);
				}

		}
	}

	private void drawView(Graphics g) {
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

		int yCount = -1;
		// this will run 5 times
		for (int y = playerPosYmin; y < playerPosYmax; y++) {// col one
			yCount++;
			int xCount = -1;
			// this will run 5 times
			for (int x = playerPosXmin; x < playerPosXmax; x++) { // fill
				xCount++;
				// get element // row
				String element = maze.getPosElement(x, y);

				// now we need to draw it relative to the view window.
				// always have the center

				drawTilesInView(g, yCount, xCount, element);
				drawEnemiesInView(g, yCount, xCount);
			}
		}
	}

	private void drawEnemiesInView(Graphics g, int yCount, int xCount) {
		// draw enemy if inside view
		int animEnemyDur = 1000;
		for (Enemy enemy : enemyList) {
			int enX = enemy.getTileX();
			int enY = enemy.getTileY();

			if (enX >= playerPosXmin && enX < playerPosXmax && enY >= playerPosYmin && enY < playerPosYmax) {
				drawEnemy(g, animEnemyDur, enemy);
			}
		}
	}

	private void drawTilesInView(Graphics g, int yCount, int xCount, String element) {
		// tiles
		if (element.equals("w")) { // wall
			g.drawImage(imgCtrl.getWall(), xCount * tileDim, yCount * tileDim, null);
		} else if (element.equals("f")) { // floor
			// using default background color
			g.drawImage(imgCtrl.getFloor(), xCount * tileDim, yCount * tileDim, null);
		} else {
			g.drawImage(imgCtrl.getFloor(), xCount * tileDim, yCount * tileDim, null);

			// items
			// inner if is for flipping the image (basic
			// animation)
			if (element.equals("g")) { // goal
				if (getTime() % animGoalDur * 2 > animGoalDur) {
					g.drawImage(imgCtrl.getGoal(), xCount * tileDim, yCount * tileDim, null);
				} else {
					g.drawImage(imgCtrl.getGoal(), (xCount + 1) * tileDim, yCount * tileDim, -tileDim, tileDim, null);
				}
			} else if (element.equals("h")) { // helper
				if (getTime() % animHelperDur * 2 > animHelperDur) {
					g.drawImage(imgCtrl.getHelper(), xCount * tileDim, yCount * tileDim, null);
				} else {
					g.drawImage(imgCtrl.getHelper(), (xCount + 1) * tileDim, yCount * tileDim, -tileDim, tileDim, null);
				}
			} else if (element.equals("s")) { // sword
				if (getTime() % animSwordDur * 2 > animSwordDur) {
					g.drawImage(imgCtrl.getSword(), xCount * tileDim, yCount * tileDim, null);
				} else {
					g.drawImage(imgCtrl.getSword(), (xCount + 1) * tileDim, yCount * tileDim, -tileDim, tileDim, null);
				}
			} else if (element.equals("b")) { // bomb
				if (getTime() % animBombDur * 2 > animBombDur) {
					g.drawImage(imgCtrl.getBomb(), xCount * tileDim, yCount * tileDim, null);
				} else {
					g.drawImage(imgCtrl.getBomb(), (xCount + 1) * tileDim, yCount * tileDim, -tileDim, tileDim, null);
				}
			}
		} // end of if
	}

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
			g.drawImage(enemy.getEnemy(), newEnX + tileDim, newEnY, -tileDim, tileDim, null);
		} else {
			g.drawImage(enemy.getEnemy2(), newEnX + tileDim, newEnY, -tileDim, tileDim, null);
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

	// draws multi-line string with correct spacing
	void drawString(Graphics g, String text, int x, int y) {
		for (String line : text.split("\n"))
			g.drawString(line, x - (g.getFontMetrics().stringWidth(line) / 2), y += g.getFontMetrics().getHeight());
	}
}
