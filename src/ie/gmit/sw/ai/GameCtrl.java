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
	private int tileDim;
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
	private PlayerImgPainter playerImgPainter;

	// enemy
	// private Enemy enemy;
	private ArrayList<Enemy> enemyList;
	private EnemyBrain enemyBrain;
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

	// colors
	private Color brown;

	// bag stuff
	private int gameStartTime;

	// one key press at a time
	private boolean keyDown;

	// fight
	private FightCtrl fightCtrl;
	private int dieTime;
	private int dieDur;
	private boolean gameOverSeq;

	// set game up
	public GameCtrl() {
		this.tileDim = GameRunner.TILE_DIM;
		zoomDim = GameRunner.ZOOM_DIM;

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
		msgStart += "\nMap:\tM";
		msgStart += "\nReset:\tR";
		msgStart += "\nQuit:\tESC";

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
		playerImgPainter = new PlayerImgPainter(player, tileDim, 'r', 0);

		// fight
		fightCtrl = new FightCtrl(player);

		// enemy
		enemyList = new ArrayList<Enemy>();
		// spawnEnemies(false);
		enemyBrain = new EnemyBrain(maze, enemyList, player, imgCtrl, fightCtrl);
		enemyNum = GameRunner.MAZE_DIM / 2;

		// animations
		animHelperDur = 2000;
		animGoalDur = 1000;
		animSwordDur = 3000;
		animBombDur = 3000;

		// colors
		// brown = new Color(161,120,41);
		brown = new Color(99, 66, 19);

		// bag stuff
		gameStartTime = getTime();

		// key down
		keyDown = false;

		// fight
		dieDur = 3000;
		gameOverSeq = false;
	}

	// get time in millis
	public static int getTime() {
		long delayNS = System.nanoTime();
		long durationInMs = TimeUnit.MILLISECONDS.convert(delayNS, TimeUnit.NANOSECONDS);
		return (int) durationInMs;
	}

	// // additional
	public void startFight(Enemy enemy) {
		// fightCtrl.setFightStartTime(getTime());

		// TODO: lock enemies in place for 3 seconds
		// lock players in place
		// player.setInFight(true);
		// enemy.setInFight(true);

		// unlock
		// if (GameCtrl.getTime() - fightCtrl.getFightStartTime() >
		// fightCtrl.getFightDur()) {
		fightCtrl.fight(enemy);
		// }
	}

	public void checkFight() {
		for (Enemy enemy : enemyList) {
			if (player.getPos().equals(enemy.getPos())) {
				startFight(enemy);
			}
		}
	}

	// zoom
	public void toggleZoom() {
		if (startDone && !haveWon) {
			zoomedOut = !zoomedOut;
		}
	}

	// full reset
	public void fullReset() {

		// go to start screen
		startDone = false;

		// clear enemies
		enemyBrain.killAllEnemies();

		// config options
		// GameRunner.chooseBGMusicOn();
		// GameRunner.chooseMazeSize();
		// GameRunner.chooseZoomScale();
		// GameRunner.chooseZoomMove();
		//
		// // reset maze
		// maze.reset();
		//
		// // reset player
		// player.resetPos();
		// playerDraw.setPlayerLookH('r');

		GameRunner.reset();
	}

	// full reset
	public void choosePlayAgain() {

		// go to start screen
		startDone = false;

		// clear enemies
		enemyBrain.killAllEnemies();

		// play again
		GameRunner.choosePlayAgain();
	}

	// KEY PRESS
	// controls
	public class AcLis extends KeyAdapter {
		public void keyPressed(KeyEvent e) { // keyPressed
			if (!keyDown) {
				keyDown = true;
				int keycode = 0;

				if (e.getKeyCode() == KeyEvent.VK_ENTER && startDone == false) {
					startDone = true;
					enemyBrain.createEnemies(enemyNum);
					enemyBrain.spawn();
				}

				if (e.getKeyCode() == KeyEvent.VK_M) { // zoom into MAP
					toggleZoom();
				}

				if (e.getKeyCode() == KeyEvent.VK_R) { // reset
					fullReset();
				}

				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) { // reset
					System.exit(0);
				}

				// NB
				if (!haveWon && startDone && !player.isInFight()) {
					// if (!zoomedOut)// turn off to test maze
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
					playerImgPainter.setPlayerLookH('l');
					if (!maze.getPosElement(player.getTileX() - 1, player.getTileY()).equals("w")) {
						player.move(-1, 0);
						moveCommon();
					}
				} else if (keycode == KeyEvent.VK_D) { // W
					playerImgPainter.setPlayerLookH('r');
					if (!maze.getPosElement(player.getTileX() + 1, player.getTileY()).equals("w")) {
						player.move(1, 0); // 1? tileDim
						moveCommon();
					}
				}
				// if (!haveWon) {
				// checkTile();
				// }
				keyDown = false;
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
		playerImgPainter.incStepCount();
		setWalk = getTime();
		SoundEffects.playMove();
		checkFight(); // checked when I or the enemy moves

		checkTile();
	}

	// ran every keypress
	public void checkTile() {

		// flash items and do something
		if (maze.getPosElement(player.getTileX(), player.getTileY()).equals("s")) { // sword
			if (!player.getSwordStatus()) {
				player.setSwordStatus(true);
				maze.setTileItem(player.getTileX(), player.getTileY(), 'f');
				SoundEffects.playFoundItem();
			} else {// unneeded
				SoundEffects.playFoundItemNoPickup();
			}
			System.out.println("Sweet sword!");
		} else if (maze.getPosElement(player.getTileX(), player.getTileY()).equals("b")) { // bomb
			if (!player.getBombStatus()) {
				player.setBombStatus(true);
				maze.setTileItem(player.getTileX(), player.getTileY(), 'f');
				SoundEffects.playFoundItem();
			} else {// unneeded
				SoundEffects.playFoundItemNoPickup();
			}
			System.out.println("Brilliant bomb!");
		} else if (maze.getPosElement(player.getTileX(), player.getTileY()).equals("h")) { // helper
			System.out.println("Happy helper!");
			SoundEffects.playFoundHelp();
		} else if (maze.getPosElement(player.getTileX(), player.getTileY()).equals("g")) { // goal
			System.out.println("Perfect potion!");
			maze.setTileItem(player.getTileX(), player.getTileY(), 'f');

			setWin = getTime();
			haveWon = true;
			SoundEffects.playWin();
			enemyList.clear();
		}
	}

	// private void gameOver(){
	// // play first
	// if (getTime() - dieTime > 1000) {
	// SoundEffects.playPlayerDeath();
	// } else if (getTime() - dieTime > 2000) {
	// SoundEffects.playGameOver();
	// } else if (getTime() - dieTime > 3000) {
	// fullReset();
	// }
	// }

	// // ACTION PERFORMED
	// gets called a certain frames per second
	// check if won
	public void actionPerformed(ActionEvent e) {
		int timeElap = getTime() - setWin;

		// NB resets game
		if (haveWon && timeElap > winDur) { // n seconds of winning!
			// SoundEffects.playWin();
			System.out.println("Look for the magic potion.");
			haveWon = false;

			fullReset();
		}

		// TODO: check if alive/dead
		if (!player.getAlive()) {
			player.setAlive(true); // so this only gets called once
			dieTime = getTime(); // get time for game over sequence
			gameOverSeq = true; // then we trigger the game over sequence
			// fullReset();
			// SoundEffects.playGameOver();
			SoundEffects.playGameOver();
		}

		if (gameOverSeq) {
			if (getTime() - dieTime > 1000) {
				gameOverSeq = false;
				choosePlayAgain();
			}
		}

		// TODO: check if in fight
		if (fightCtrl.isFightInProgress()) {
			if (getTime() - fightCtrl.getFightStartTime() > fightCtrl.getFightDur()) {
				fightCtrl.fightOff();
			}
		}

		repaint();
	}

	// // DRAWING
	// paint map a certain frames per second
	@Override
	public void paint(Graphics g) {
		super.paint(g);

		// // BG color
		if (!startDone || haveWon) {
			// dialog view (dark gray)
			this.setBackground(Color.DARK_GRAY);
		} else if (zoomedOut) {
			// zoomed view (black)
			this.setBackground(Color.BLACK);
		} else {
			// normal view (brown)
			this.setBackground(brown);
			paintBagContents(g);
		}

		if (!zoomedOut) {
			if (!haveWon && startDone) {
				drawView(g);

				// draw strings
			} else if (!startDone) {
				g.setColor(Color.CYAN);
				g.setFont(fontGen);
				drawString(g, msgStart, GameRunner.SCREEN_DIM / 2, GameRunner.SCREEN_DIM / 30); // was

			} else if (haveWon) {
				g.setColor(Color.CYAN);
				g.setFont(fontGen);
				drawString(g, msgWin, GameRunner.SCREEN_DIM / 2, GameRunner.SCREEN_DIM / 30);
			}

			drawPlayer(g);

		} // not zoomed out
		else { // zoomed out
			drawViewZoomed(g);
		}
	}

	private void paintBagContents(Graphics g) {
		// // paint bag contents
		// paint sword (strength)
		if (player.getSwordStatus()) {
			paintBagItem(g, imgCtrl.getSword(), 3000, 1);
		}

		// paint bomb (search algorithm that destroys enemies/walls)
		if (player.getBombStatus()) {
			paintBagItem(g, imgCtrl.getBomb(), 4500, 2);
		}

		// paint step count (info, effects strength, makes you smarter as you
		// know the maze better)
		String bagStr = "Steps:\n" + player.getStepCount();
		paintStepCount(g, bagStr, 3000, 5);

		// paint health (goes down over time and from attacking enemies, need to
		// find food/potion/med kit to increase) [kind of like a timer]
		bagStr = "Health:\n" + player.getHealth();
		paintStepCount(g, bagStr, 3000, 4);

		// time elapsed.
		bagStr = "Time:\n" + getGameTime();
		paintStepCount(g, bagStr, 3000, 3);
	}

	public int getGameTime() {
		int gameTime = getTime() - gameStartTime;
		gameTime /= 1000;

		return gameTime;
	}

	private void paintStepCount(Graphics g, String bagStr, int animItemDur, int slot) {
		// paint sword (strength)
		int itemX = (GameRunner.TILE_DIM * slot) - (GameRunner.TILE_DIM / 2);
		int itemY = GameRunner.SCREEN_DIM;

		// minus half of object against it
		int fontMetrics = g.getFontMetrics(fontGen).getHeight();
		itemY -= fontMetrics; // font height

		itemY += GameRunner.TILE_DIM / 2;

		g.setFont(fontGen);

		switch (slot) {
		case 3: // time
			Color color = Color.BLACK;
			g.setColor(color);
			drawString(g, bagStr, itemX, itemY);
			break;

		case 4: // health
			color = Color.PINK;
			g.setColor(color);
			drawString(g, bagStr, itemX, itemY);
			break;

		case 5: // steps
			color = Color.LIGHT_GRAY;
			g.setColor(color);
			drawString(g, bagStr, itemX, itemY);
			break;

		default:
			break;
		}

	}

	private void paintBagItem(Graphics g, Image itemImg, int animItemDur, int slot) {
		int itemX = (GameRunner.TILE_DIM * slot) - (GameRunner.TILE_DIM / 2);
		int itemY = GameRunner.SCREEN_DIM;

		// minus half of object against it
		itemX -= itemImg.getWidth(null) / 2;
		itemY -= itemImg.getHeight(null) / 2;
		itemY += GameRunner.TILE_DIM / 2; // same as info bar

		if (getTime() % animItemDur * 2 > animItemDur) {
			g.drawImage(itemImg, itemX, itemY, null);
		} else {
			g.drawImage(itemImg, (itemX + GameRunner.TILE_DIM), itemY, -tileDim, tileDim, null);
		}
	}

	private void drawViewZoomed(Graphics g) {
		// draw only within this around player (and focus on player with offset)
		zoomedPosX = player.getTileX() - 1;
		zoomedPosY = player.getTileY() - 1;

		// 5 sqaured
		zoomedPosXmin = zoomedPosX - 2 * GameRunner.ZOOM_MULT;
		zoomedPosXmax = zoomedPosX + 3 * GameRunner.ZOOM_MULT;

		zoomedPosYmin = zoomedPosY - 2 * GameRunner.ZOOM_MULT;
		zoomedPosYmax = zoomedPosY + 3 * GameRunner.ZOOM_MULT;

		// remove index errors

		// need to add negative to positive
		// -1,9 becomes 0,10
		int offset;
		if (zoomedPosXmin < 0) {
			offset = zoomedPosXmin; // now 2
			offset *= -1;
			zoomedPosXmin = 0;
			zoomedPosXmax += offset;
		} else if (zoomedPosXmax > GameRunner.MAZE_DIM) {
			offset = zoomedPosXmax - GameRunner.MAZE_DIM;
			offset *= -1;
			zoomedPosXmax = GameRunner.MAZE_DIM;
			zoomedPosXmin += offset + 2; // +2 for outer walls
		}

		if (zoomedPosYmin < 0) {
			offset = zoomedPosYmin; // now 2
			offset *= -1;
			zoomedPosYmin = 0;
			zoomedPosYmax += offset;
		} else if (zoomedPosYmax > GameRunner.MAZE_DIM) {
			offset = zoomedPosYmax - GameRunner.MAZE_DIM;
			offset *= -1;
			zoomedPosYmax = GameRunner.MAZE_DIM;
			zoomedPosYmin += offset;
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
		} else if (String.format("%s,%s", x, y).equals(player.getPos())) {
			g.setColor(Color.ORANGE);
			g.fillRect(xCount * zoomDim, yCount * zoomDim, zoomDim, zoomDim);
		} else if (element.equals("g")) { // goal
			g.setColor(Color.YELLOW);
			g.fillRect(xCount * zoomDim, yCount * zoomDim, zoomDim, zoomDim);
		} else if (element.equals("b") || element.equals("s")) { // item
			g.setColor(Color.CYAN);
			g.fillRect(xCount * zoomDim, yCount * zoomDim, zoomDim, zoomDim);
		} else if (element.equals("h")) { // helper
			g.setColor(Color.GREEN);
			g.fillRect(xCount * zoomDim, yCount * zoomDim, zoomDim, zoomDim);
		} else { // floor
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(xCount * zoomDim, yCount * zoomDim, zoomDim, zoomDim);
		} // end of if

		// enemy
		for (Enemy enemy : enemyList) {
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
		if (player.isInFight()) {
			playerImgPainter.fight(g);
		} else if (timeElap < walkDur) {
			playerImgPainter.walk(g);
		} else if (haveWon) {
			playerImgPainter.win(g);
		} else if (playerImgPainter.getPlayerLookH() == 'l') {
			playerImgPainter.lookLeft(g);
		} else if (playerImgPainter.getPlayerLookH() == 'r') {
			playerImgPainter.lookRight(g);
		}
	}

	// draws multi-line string with correct spacing
	void drawString(Graphics g, String text, int x, int y) {
		for (String line : text.split("\n"))
			g.drawString(line, x - (g.getFontMetrics().stringWidth(line) / 2), y += g.getFontMetrics().getHeight());
	}
}
