/*
  __  __                          ______                                     
 |  \/  |                        |  ____|                                    
 | \  / |   __ _   ____   ___    | |__     ___    ___    __ _   _ __     ___ 
 | |\/| |  / _` | |_  /  / _ \   |  __|   / __|  / __|  / _` | | '_ \   / _ \
 | |  | | | (_| |  / /  |  __/   | |____  \__ \ | (__  | (_| | | |_) | |  __/
 |_|  |_|  \__,_| /___|  \___|   |______| |___/  \___|  \__,_| | .__/   \___|
                                                               | |           
                                                               |_|           
 */
package ie.gmit.sw.ai;

import javax.swing.*;

/**
 * Sets the game window and basic game attributes up.
 * 
 * @author Ronan
 */
public class GameRunner {
	public static int MAZE_DIM;
	public static int TILE_DIM;
	public static int VIEW_DIM;
	public static int ZOOM_DIM;
	public static int SCREEN_DIM;
	public static boolean BG_ON;
	public static boolean BG_KILL;
	public static int ZOOM_MULT;
	private static int ZOOM_SCALE;
	public static boolean ZOOM_MOVE;
	private static int titleHeight;
	private static int infoBar;
	private static JFrame f;

	public static int ENEMY_ALGO_NUM;
	
//	private static GameRunner gr = null;

	public static void main(String[] args) {
		new GameRunner();
	}

	public GameRunner() {
		init();
	}

	public static void gameOver() {
		chooseGameOver();
		
//		try {
//			gr.finalize();
//		} catch (Throwable e) {
//			e.printStackTrace();
//		}
//		gr = new GameRunner();
//		BG_KILL = true;
//		f.dispose();
//		f.removeAll();
//		f.getDefaultCloseOperation();
//		init();
	}

	public static void init() {
		BG_KILL = false;
		f = new JFrame();

		int choice = chooseUseDefaults();

		if (choice != 0) {
			chooseMazeSize();
			chooseZoomScale();
			chooseZoomMove();
			chooseEnemyAlgo();
			chooseBGMusicOn();
		} else {
			MAZE_DIM = 60;
			BG_ON = true;
			ZOOM_SCALE = 0;
			setZoomViews();
			ZOOM_MOVE = false;
			ENEMY_ALGO_NUM = 6;
		}
		
		
		TILE_DIM = 64 * 2; // scale of tiles
		VIEW_DIM = 5; // 5 * 5
		SCREEN_DIM = TILE_DIM * VIEW_DIM; // real co-ordinates
		infoBar = TILE_DIM;// plus an info bar
		titleHeight = 22;

		frameConfig(f);
		
		
	}

	private static void frameConfig(JFrame f) {
		f.setResizable(false);
		f.setTitle("Maze Escape");
		f.add(new GameCtrl());
		f.setSize(SCREEN_DIM, SCREEN_DIM + titleHeight + infoBar); // + infoBar
		f.setLocationRelativeTo(null);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void choosePlayAgain() {
		chooseGameOver();
//		BG_KILL = true;
//		int playAgain = 1;
//		try {
//			playAgain = Integer.parseInt((String) JOptionPane.showInputDialog(f, "Play again?\n0: no\n1: yes", null,
//					JOptionPane.INFORMATION_MESSAGE, null, null, "1"));
//		} catch (Exception e) {
//			playAgain = 1;
//		}
//
//		if (playAgain < 0 || playAgain > 1) {
//			playAgain = 1;
//		}
//
//		if (playAgain == 0) {
//			System.exit(0);
//		} else {
//			gameOver();
//		}

	}

	public static void chooseZoomScale() {
		ZOOM_SCALE = 0;
		try {
			ZOOM_SCALE = Integer
					.parseInt((String) JOptionPane.showInputDialog(f, "Zoom Scale?\n0: normal\n1: closer\n2: farther",
							null, JOptionPane.INFORMATION_MESSAGE, null, null, "0"));
		} catch (Exception e) {
			ZOOM_SCALE = 0;
		}

		if (ZOOM_SCALE < 0 || ZOOM_SCALE > 2) {
			ZOOM_SCALE = 0;
		}

		setZoomViews();
	}

	private static void setZoomViews() {
		// ZOOM MULT: view window
		// ZOOM DIM: tile size

		// ZOOM VIEW
		if (ZOOM_SCALE == 0) { // normal (8 tiles around)
			ZOOM_MULT = 4;
			ZOOM_DIM = 9 * ZOOM_MULT; // 36
		} else if (ZOOM_SCALE == 1) { // closer
			ZOOM_MULT = 2;
			ZOOM_DIM = 32 * ZOOM_MULT; // 64
		} else if (ZOOM_SCALE == 2) { // farther
			ZOOM_MULT = GameRunner.MAZE_DIM / 5; // show full map (no limit on
													// view window)
			ZOOM_DIM = 8; // tiles are 4x4 dim size
		} else {
			ZOOM_MULT = 4;
			ZOOM_DIM = 9 * ZOOM_MULT;
		}
	}

	public static void chooseBGMusicOn() {
		int bg = 1;
		bg = JOptionPane.showConfirmDialog(f, "Music on?");

		if (bg != 0) {
			BG_ON = false;
		} else {
			BG_ON = true;
			System.out.println("");
		}
	}

	public static void chooseMazeSize() {
		int mazeSize = 0;
		try {
			mazeSize = Integer.parseInt((String) JOptionPane.showInputDialog(f, "Maze Size?", null,
					JOptionPane.INFORMATION_MESSAGE, null, null, "60"));
		} catch (Exception e) {
			mazeSize = 100;
		}

		if (mazeSize < 20) {
			mazeSize = 20;
			System.out.println("min size is 20");
		} else if (mazeSize > 100) {
			mazeSize = 20;
			System.out.println("max size is 100");
		}

		// size of maze
		MAZE_DIM = mazeSize;
	}

	public static void chooseZoomMove() {
		int zoomMove = 0;
		boolean zoomMoveBool = true;
		zoomMove = JOptionPane.showConfirmDialog(f, "Allow movement during zoom?");

		System.out.println("zoomMove: " + zoomMove);

		if (zoomMove == 0) {
			zoomMoveBool = true;
		} else if (zoomMove == 0) {
			zoomMoveBool = false;
		} else {
			zoomMoveBool = false;
		}

		ZOOM_MOVE = zoomMoveBool;
	}

	public static void chooseEnemyAlgo() {
		String menuQuestion = "";
		menuQuestion += "Choose an enemy algorithm:\n";
		menuQuestion += "0: Random Walk\n";
		menuQuestion += "1: Brute Force (DFS)\n";
		menuQuestion += "2: Brute Force (BFS)\n";
		menuQuestion += "3: Recursive DFS\n";
		menuQuestion += "4: Depth Limited DFS\n";
		menuQuestion += "5: Iterative Deepening DFS\n";
		menuQuestion += "6: mixture of all algorithms\n";

		try {
			ENEMY_ALGO_NUM = Integer.parseInt((String) JOptionPane.showInputDialog(f, menuQuestion, null,
					JOptionPane.INFORMATION_MESSAGE, null, null, "6"));
		} catch (Exception e) {
			ENEMY_ALGO_NUM = 6;
		}

		if (ENEMY_ALGO_NUM < 0 || ENEMY_ALGO_NUM > 6) {
			ENEMY_ALGO_NUM = 6;
		}
	}

	public static int chooseUseDefaults() {
		String menuQuestion = "";
		menuQuestion += "Use default settings?:";
		int useDefaults = 1;
		
		useDefaults = JOptionPane.showConfirmDialog(f, menuQuestion);

		if (useDefaults < 0 || useDefaults > 1) {
			useDefaults = 1;
		}

		return useDefaults;
	}
	
	public static void chooseGameOver() {
		String menuQuestion = "";
		menuQuestion += "Thank you, come again.";
		
		JOptionPane.showConfirmDialog(f, menuQuestion); 
		System.exit(0);
	}
}
