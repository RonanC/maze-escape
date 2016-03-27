package ie.gmit.sw.ai;

import javax.swing.*;

public class GameRunner {
	public static int MAZE_DIM;
	public static int TILE_DIM;
	public static int VIEW_DIM;
	public static int ZOOM_DIM;
	public static int SCREEN_DIM;
	public static boolean BG_ON;
	public static int ZOOM_MULT;
	private static int ZOOM_SCALE;
	private static boolean ZOOM_MOVE;
	private static int titleHeight;
	private static int infoBar;
	private static JFrame f;

	public static void main(String[] args) {
		new GameRunner();
	}

	public GameRunner() {
		init();
	}

	public static void reset() {
		f.dispose();
		init();
	}

	public static void init() {
		f = new JFrame();

		chooseMazeSize();
		chooseBGMusicOn();
		chooseZoomScale();
		chooseZoomMove();

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
		int playAgain = 1;
		try {
			playAgain = Integer.parseInt((String) JOptionPane.showInputDialog(f, "Play agan?\n0: no\n1: yes", null,
					JOptionPane.INFORMATION_MESSAGE, null, null, "1"));
		} catch (Exception e) {
			playAgain = 1;
		}

		if (ZOOM_SCALE < 0 || ZOOM_SCALE > 2) {
			ZOOM_SCALE = 0;
		}

		if (playAgain == 0) {
			System.exit(0);
		} else {
			reset();
		}

	}

	public static void chooseZoomScale() {
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
		int bg;
		try {
			bg = Integer.parseInt((String) JOptionPane.showInputDialog(f, "Music on?\n0: off\n1: on", null,
					JOptionPane.INFORMATION_MESSAGE, null, null, "0")); // TODO: change back to 1 when finishing
		} catch (Exception e) {
			bg = 1;
		}

		if (bg == 0) {
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
					JOptionPane.INFORMATION_MESSAGE, null, null, "100"));
		} catch (Exception e) {
			mazeSize = 100;
		}

		System.out.println("ans: " + mazeSize);

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
		int zoomMove = 1;
		boolean zoomMoveBool = true;
		try {
			zoomMove = Integer
					.parseInt((String) JOptionPane.showInputDialog(f, "Allow movement during zoom?\n0: false\n1: true",
							null, JOptionPane.INFORMATION_MESSAGE, null, null, "1"));
		} catch (Exception e) {
			zoomMove = 1;
		}

		System.out.println("zoomMove: " + zoomMove);

		if (zoomMove == 1) {
			zoomMoveBool = true;
		} else if (zoomMove == 0) {
			zoomMoveBool = false;
		} else {
			zoomMoveBool = true;
		}

		// size of maze
		ZOOM_MOVE = zoomMoveBool;
	}
}
